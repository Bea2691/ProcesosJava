package interfaz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class VentanaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel mainPanel;
	private boolean isDarkTheme = false; 
	private P_AgendaAux agendaPanel; 

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(new FlatLightLaf());
				VentanaPrincipal frame = new VentanaPrincipal(); 
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaPrincipal() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaPrincipal.class.getResource("/imagenes/logoPETNOVApng.png")));
		setTitle("PetNova");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1099, 730);
		setLocationRelativeTo(null);

		contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);

		
		JPanel topBar = panelBarraSuperior();
		contentPane.add(topBar, BorderLayout.NORTH);

		
		JPanel panelLateral = panelBarraLateral();
		contentPane.add(panelLateral, BorderLayout.WEST);

	
		mainPanel = new JPanel(new CardLayout());
		contentPane.add(mainPanel, BorderLayout.CENTER);

		
		PrincipalPanel fondoPanel = new PrincipalPanel();
		agendaPanel = new P_AgendaAux(); 
		P_Facturas facturasPanel = new P_Facturas();
		P_AltaCliente altaPanel = new P_AltaCliente();

		
		mainPanel.add(fondoPanel, "Principal");
		mainPanel.add(agendaPanel, "Agenda");
		mainPanel.add(facturasPanel, "Facturas");
		mainPanel.add(altaPanel, "Alta");

		
	}

	private JPanel panelBarraSuperior() {
		JPanel barraSuperior = new JPanel(null);
		barraSuperior.setPreferredSize(new Dimension(0, 50));

		
		JButton btnAgente = new JButton(new ImageIcon(VentanaPrincipal.class.getResource("/iconosXS/agenteXS.png")));
		btnAgente.setFocusPainted(false);
		btnAgente.setContentAreaFilled(false);
		btnAgente.setBorderPainted(false);
		barraSuperior.add(btnAgente);

		
		JLabel lblIconoPet = new JLabel(new ImageIcon(getClass().getResource("/imagenes/BannerXXXS.png")));
		lblIconoPet.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblIconoPet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cambiarPanel("Principal");
			}
		});
		barraSuperior.add(lblIconoPet);

		
		JLabel lblTitulo = new JLabel("Aux. Veterinario", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		barraSuperior.add(lblTitulo);

		barraSuperior.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evt) {
				int width = barraSuperior.getWidth();
				int height = barraSuperior.getHeight();

				lblIconoPet.setBounds(-20, 0, 220, height);
				lblTitulo.setBounds(220, 0, width - 280, height);
				btnAgente.setBounds(width - 60, 0, 50, height);
			}
		});

		return barraSuperior;
	}

	private JPanel panelBarraLateral() {
		JPanel panelLateral = new JPanel(null);
		panelLateral.setPreferredSize(new Dimension(200, 0));

		
		int margenIzquierdo = 10;

		
		JButton btnFicha = new JButton("  Alta");
		btnFicha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnFicha.setIcon(escalarIcono("/iconosM/perrito.png", 30, 30));
		btnFicha.setHorizontalAlignment(SwingConstants.LEFT);
		btnFicha.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnFicha.setBounds(margenIzquierdo, 0, 190, 55);
		btnFicha.addActionListener(e -> cambiarPanel("Alta"));

		
		JButton btnVademecum = new JButton("  Facturas");
		btnVademecum.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnVademecum.setIcon(escalarIcono("/iconosM/libro.png", 30, 30));
		btnVademecum.setHorizontalAlignment(SwingConstants.LEFT);
		btnVademecum.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnVademecum.setBounds(margenIzquierdo, 67, 190, 55);
		btnVademecum.addActionListener(e -> cambiarPanel("Facturas"));

		
		JButton btnAgenda = new JButton("  Agenda/Citas");
		btnAgenda.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAgenda.setIcon(escalarIcono("/iconosM/agenda.png", 30, 30));
		btnAgenda.setHorizontalAlignment(SwingConstants.LEFT);
		btnAgenda.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnAgenda.setBounds(margenIzquierdo, 134, 190, 55);
		btnAgenda.addActionListener(e -> cambiarPanel("Agenda"));

		panelLateral.add(btnFicha);
		panelLateral.add(btnVademecum);
		panelLateral.add(btnAgenda);

		
		JButton btnTema = new JButton("");
		btnTema.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/iconosXS/temaXS.png")));
		btnTema.setBounds(10, 584, 51, 51);
		btnTema.addActionListener(e -> cambiarTema());
		panelLateral.add(btnTema);

		panelLateral.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evt) {
				int width = panelLateral.getWidth();
				int height = panelLateral.getHeight();

				btnTema.setBounds(10, height - 70, 51, 51);
			}
		});

		return panelLateral;
	}

	
	private void cambiarTema() {
		try {
			if (isDarkTheme) {
				UIManager.setLookAndFeel(new FlatLightLaf());
			} else {
				UIManager.setLookAndFeel(new FlatDarculaLaf());
			}
			SwingUtilities.updateComponentTreeUI(this);
			isDarkTheme = !isDarkTheme;
			agendaPanel.toggleDarkMode(isDarkTheme); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
		ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(ruta));
		Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
		return new ImageIcon(imagenEscalada);
	}

	private void cambiarPanel(String nombrePanel) {
		CardLayout cl = (CardLayout) mainPanel.getLayout();
		cl.show(mainPanel, nombrePanel);
	}
}
