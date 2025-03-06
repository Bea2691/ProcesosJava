package interfaz;

import javax.swing.*;
import org.hibernate.Session;
import PetnovaHN.HibernateUtil;
import PetnovaHN.Razamascota;
import PetnovaHN.Tipomascota;
import veterinarioDAO.ConsultaAltaCliente;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.TitledBorder;



public class V_AltaMascota extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfNombreMascota;
    private JTextField tfChip;
    private String dniPersona;
    private JComboBox<String> comboBoxTipoMascota;
    private JComboBox<String> comboBoxRazaMascota;

    public V_AltaMascota(String dniPersona,  P_AltaCliente pAltaCliente) {
        this.dniPersona = dniPersona;
        

        setIconImage(Toolkit.getDefaultToolkit().getImage(V_AltaMascota.class.getResource("/imagenes/logoPETNOVApng.png")));
        setTitle("Añadir Mascota");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 801, 536);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        setResizable(false);
        setLocationRelativeTo(null);

        //Componentes del panel
        JPanel panelDatosMascota = new JPanel();
        panelDatosMascota.setBounds(20, 20, 735, 452);
        panelDatosMascota.setLayout(null);
        contentPane.add(panelDatosMascota);

        TitledBorder borderDatosMascota = BorderFactory.createTitledBorder("Datos de la Mascota");
        borderDatosMascota.setTitleJustification(TitledBorder.RIGHT);
        borderDatosMascota.setTitlePosition(TitledBorder.CENTER);
        panelDatosMascota.setBorder(borderDatosMascota);

        JLabel lblNombre = new JLabel("Nombre Mascota:");
        lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblNombre.setBounds(377, 62, 120, 25);
        panelDatosMascota.add(lblNombre);

        tfNombreMascota = new JTextField();
        tfNombreMascota.setBounds(479, 62, 222, 25);
        panelDatosMascota.add(tfNombreMascota);

        JLabel lblNumChip = new JLabel("Número de chip:");
        lblNumChip.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblNumChip.setBounds(20, 63, 120, 25);
        panelDatosMascota.add(lblNumChip);

        tfChip = new JTextField();
        tfChip.setBounds(115, 62, 222, 25);
        panelDatosMascota.add(tfChip);

        JLabel lblTipoMascota = new JLabel("Tipo Mascota:");
        lblTipoMascota.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblTipoMascota.setBounds(20, 187, 120, 25);
        panelDatosMascota.add(lblTipoMascota);

        comboBoxTipoMascota = new JComboBox<>();
        comboBoxTipoMascota.setBounds(115, 186, 222, 25);
        panelDatosMascota.add(comboBoxTipoMascota);

        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblSexo.setBounds(377, 187, 36, 25);
        panelDatosMascota.add(lblSexo);

        JRadioButton rbMacho = new JRadioButton("Macho");
        rbMacho.setFont(new Font("Tahoma", Font.PLAIN, 12));
        rbMacho.setBounds(417, 186, 80, 25);
        panelDatosMascota.add(rbMacho);

        JRadioButton rbHembra = new JRadioButton("Hembra");
        rbHembra.setFont(new Font("Tahoma", Font.PLAIN, 12));
        rbHembra.setBounds(479, 186, 80, 25);
        panelDatosMascota.add(rbHembra);

        ButtonGroup grupoSexo = new ButtonGroup();
        grupoSexo.add(rbMacho);
        grupoSexo.add(rbHembra);

        JLabel lblFechaNac = new JLabel("Fecha Nacimiento:");
        lblFechaNac.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblFechaNac.setBounds(377, 122, 120, 25);
        panelDatosMascota.add(lblFechaNac);

        JSpinner spinnerFechaNacimiento = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerFechaNacimiento, "yyyy/MM/dd");
        spinnerFechaNacimiento.setEditor(dateEditor);
        spinnerFechaNacimiento.setBounds(493, 122, 208, 25);
        panelDatosMascota.add(spinnerFechaNacimiento);

        JLabel lblObservaciones = new JLabel("Observaciones:");
        lblObservaciones.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblObservaciones.setBounds(20, 250, 120, 25);
        panelDatosMascota.add(lblObservaciones);

        JTextArea taObservaciones = new JTextArea();
        taObservaciones.setBounds(115, 251, 586, 127);
        panelDatosMascota.add(taObservaciones);
        
        //Listener en la ventana para que cuando vuelva al panel de alta se cargue la mascota añadida
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                pAltaCliente.cargarMascotas(dniPersona); 
            }
        });

        JButton btnGuardar = new JButton("Guardar", redimensionarIcono("/iconosXS/guardarXS.png", 22, 22));
        btnGuardar.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnGuardar.setBounds(115, 390, 120, 30);
        panelDatosMascota.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar", redimensionarIcono("/iconosXS/xXXS.png", 22, 22));
        btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnCancelar.setBounds(581, 390, 120, 30);
        btnCancelar.addActionListener(e -> dispose());
        panelDatosMascota.add(btnCancelar);

        JLabel lblRazaMascota = new JLabel("Raza Mascota:");
        lblRazaMascota.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblRazaMascota.setBounds(20, 123, 120, 25);
        panelDatosMascota.add(lblRazaMascota);

        comboBoxRazaMascota = new JComboBox<>();
        comboBoxRazaMascota.setBounds(115, 122, 222, 25);
        panelDatosMascota.add(comboBoxRazaMascota);
        
        //Cargamos los tipos de mascotas al abrir la ventana en el combobox
        ConsultaAltaCliente.cargarTiposMascota(comboBoxTipoMascota);
        //Listener para segun el tipo de mascosa se cargue en el otro combobox las razas correspondientes
        comboBoxTipoMascota.addActionListener(e -> {
            String tipoSeleccionado = (String) comboBoxTipoMascota.getSelectedItem();
            if (tipoSeleccionado != null) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try {
                    Tipomascota tipo = session.createQuery("FROM Tipomascota WHERE nombretipo = :tipoNombre", Tipomascota.class)
                            .setParameter("tipoNombre", tipoSeleccionado)
                            .uniqueResult();
                    if (tipo != null) {
                        ConsultaAltaCliente.cargarRazasPorTipo(comboBoxRazaMascota,tipo.getIdtipo());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    session.close();
                }
            }
        });
        //Listener para guardar la mascota
        btnGuardar.addActionListener(e -> {
            try {
                String nombremascota = tfNombreMascota.getText().trim();
                String numChipStr = tfChip.getText().trim();
                int numChip = Integer.parseInt(numChipStr);
                String sexo = rbMacho.isSelected() ? "Macho" : rbHembra.isSelected() ? "Hembra" : null;

              
                java.util.Date fechaNacimientoUtil = (java.util.Date) spinnerFechaNacimiento.getValue();

                if (fechaNacimientoUtil == null) {
                    JOptionPane.showMessageDialog(null, "Por favor, selecciona una fecha de nacimiento válida.");
                    return;
                }

                java.sql.Date fechaNacimientoSql = new java.sql.Date(fechaNacimientoUtil.getTime());

                String observaciones = taObservaciones.getText().trim();
                String nombretipo = (String) comboBoxTipoMascota.getSelectedItem();
                String nombreraza = (String) comboBoxRazaMascota.getSelectedItem();

               
                if (dniPersona.isEmpty() || nombremascota.isEmpty() || sexo == null || fechaNacimientoSql == null ||
                    nombretipo == null || nombreraza == null || numChip <= 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
                    return;
                }

                //Se usa el metodo para saber si ese chip ya existe, es PK
                ConsultaAltaCliente consultaAltaCliente = new ConsultaAltaCliente();
                if (consultaAltaCliente.isChipRegistrado(numChip)) {
                    JOptionPane.showMessageDialog(null, "Ese número de Chip pertenece a una mascota ya existente.");
                    return; 
                }

               
                Integer idRazaAux = null;
                Session session = HibernateUtil.getSessionFactory().openSession();
                try {
                    Razamascota raza = (Razamascota) session.createQuery("FROM Razamascota WHERE nombreraza = :nombreRaza")
                                                            .setParameter("nombreRaza", nombreraza)
                                                            .uniqueResult();
                    if (raza != null) {
                        idRazaAux = raza.getIdraza();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró la raza seleccionada.");
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al buscar la raza seleccionada.");
                    return;
                } finally {
                    session.close();
                }

                //Método para guardar mascota
                consultaAltaCliente.guardarMascota(numChip, nombremascota, fechaNacimientoSql, sexo, idRazaAux, observaciones, dniPersona);

                JOptionPane.showMessageDialog(null, "Mascota guardada exitosamente.");

                
                tfNombreMascota.setText("");
                tfChip.setText("");
                spinnerFechaNacimiento.setValue(new java.util.Date()); 
                taObservaciones.setText("");
                grupoSexo.clearSelection();
                comboBoxTipoMascota.setSelectedIndex(0);
                comboBoxRazaMascota.setSelectedIndex(0);

                
                SwingUtilities.invokeLater(() -> {
                    dispose();
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ocurrió un error al guardar la mascota.");
            }
        });
    }
  





    private ImageIcon redimensionarIcono(String ruta, int ancho, int alto) {
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(ruta));
        Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenRedimensionada);
    }
}
