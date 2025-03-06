package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import veterinarioDAO.ConsultaAltaCliente;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.border.TitledBorder;
import java.util.Date;

public class V_Historial extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldCliente;
    private JTable tableFacturas;
    private DefaultTableModel tableModel;

    public V_Historial() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(V_Historial.class.getResource("/imagenes/logoPETNOVApng.png")));
        setTitle("Historial de Facturas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 801, 536);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        setLocationRelativeTo(null);
        setResizable(false);

        //Componentes del panel
        JPanel panelFacturitas = new JPanel();
        panelFacturitas.setBounds(30, 71, 735, 401);
        panelFacturitas.setLayout(null);
        contentPane.add(panelFacturitas);

        TitledBorder borderDatosMascota = BorderFactory.createTitledBorder("Facturas encontradas:");
        borderDatosMascota.setTitleJustification(TitledBorder.RIGHT);
        borderDatosMascota.setTitlePosition(TitledBorder.CENTER);
        panelFacturitas.setBorder(borderDatosMascota);

        JLabel lblDni = new JLabel("Dni del Cliente:");
        lblDni.setBounds(38, 26, 89, 18);
        contentPane.add(lblDni);
        lblDni.setFont(new Font("Tahoma", Font.PLAIN, 12));

        textFieldCliente = new JTextField();
        textFieldCliente.setBounds(134, 23, 175, 25);
        contentPane.add(textFieldCliente);

        JButton buscarButton = new JButton(escalarIcono("/iconosXS/lupaXS.png", 17, 17));
        buscarButton.setBounds(320, 23, 25, 25);
        contentPane.add(buscarButton);

        //Diseño de la tabla de facturas
        String[] columnNames = {"ID Factura", "Fecha", "Archivo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableFacturas = new JTable(tableModel);
        tableFacturas.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(tableFacturas);
        scrollPane.setBounds(20, 30, 695, 350);
        panelFacturitas.add(scrollPane);

        //Listener para buscar y cargar las facturas en la tabla
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dni = textFieldCliente.getText().trim();
                if (!dni.isEmpty()) {
                    cargarFacturasEnTabla(dni);
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese un DNI válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Listener en la tabla para hacer doble click y que se imprima
        tableFacturas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableFacturas.rowAtPoint(e.getPoint());
                int col = tableFacturas.columnAtPoint(e.getPoint());

                
                if (e.getClickCount() == 2 && col == 2 && row != -1) {
                    Integer idFactura = (Integer) tableModel.getValueAt(row, 0);
                    ConsultaAltaCliente.abrirPDFDesdeBD(idFactura);
                }
            }
        });
    }

    //método para cargar las facturas por dni
    private void cargarFacturasEnTabla(String dniPersona) {
        List<Object[]> facturas = ConsultaAltaCliente.buscarFacturasPorDni(dniPersona);
        tableModel.setRowCount(0); 

        for (Object[] factura : facturas) {
            Integer idFactura = (Integer) factura[0];
            Date fechaFactura = (Date) factura[1];
            String nombreArchivo = "Factura_" + idFactura + ".pdf"; 

            tableModel.addRow(new Object[]{idFactura, fechaFactura, nombreArchivo});
        }
    }

   

    private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
        ImageIcon iconoOriginal = new ImageIcon(V_Historial.class.getResource(ruta));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }
}
