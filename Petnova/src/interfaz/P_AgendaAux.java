package interfaz;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import PetnovaHN.Citas;
import PetnovaHN.HibernateUtil;
import PetnovaHN.Mascotas;
import PetnovaHN.Personas;
import veterinarioDAO.ConsultaAltaCliente;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.Locale;

public class P_AgendaAux extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
    private DefaultTableModel tableModel;
    private JLabel headerLabel;
    private Calendar calendar;
    private boolean isDarkMode = false; 

    public P_AgendaAux() {
        setLayout(new BorderLayout());

        calendar = Calendar.getInstance(); 

        //Componentes del panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerLabel = new JLabel();
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateHeaderLabel();
        headerPanel.setPreferredSize(new Dimension(309, 50)); 
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        
        JButton anteriorSemana = new JButton("Semana anterior");
        JButton siguienteSemana = new JButton("Semana siguiente");
        //Listener de los botones para moverse por las semanas
        anteriorSemana.addActionListener(e -> cambiarSemana(-1));
        siguienteSemana.addActionListener(e -> cambiarSemana(1));

        JPanel navigationPanel = new JPanel();
        navigationPanel.add(siguienteSemana);

        headerPanel.add(navigationPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JPanel navigationPanel_1 = new JPanel();
        headerPanel.add(navigationPanel_1, BorderLayout.WEST);
        navigationPanel_1.add(anteriorSemana);

        
        String[] columnasNombres = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        
        
        String[][] data = new String[28][8];

        
        String[] horas = {
            "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", 
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", 
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", 
            "20:00", "20:30", "21:00", "21:30"
        };

        for (int i = 0; i < horas.length; i++) {
            data[i][0] = horas[i];
        }

        
        tableModel = new DefaultTableModel(data, columnasNombres);
        table = new JTable(tableModel);

        
        table.setDefaultRenderer(Object.class, createTableCellRenderer());

        
        table.setDefaultEditor(Object.class, null); 

        //Listener para al hacer doble click abrir el JOption Pane y añadir la cita
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();

                    if (row >= 0 && column > 0) {
                        Object valorCelda = tableModel.getValueAt(row, column);
                        
                        
                        if (valorCelda != null && !valorCelda.toString().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(table, "Ya hay una cita asignada a esa hora.\nPulse Supr para eliminar", "Aviso", JOptionPane.WARNING_MESSAGE);
                            return; 
                        }

                        String hora = (String) tableModel.getValueAt(row, 0);
                        String dia = columnasNombres[column];
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
                        calendar.add(Calendar.DAY_OF_YEAR, column - 1); 
                        String fecha = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                                + " " + calendar.get(Calendar.DAY_OF_MONTH) + " "
                                + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                                + " " + calendar.get(Calendar.YEAR);
                        //Se le pasa la localizacion de fila y columna, dia hora y fecha a la ventana de añadir
                        abrirVentanaEmergente(row, column, dia, hora, fecha);
                    }
                }
            }
        });


        
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(table.getPreferredSize().width, table.getPreferredSize().height)); 
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        //Se llama al metodo para que aparezcan las citas cargadas
        cargarCitas();
        //Listener para eliminar citas al pulsar la tecla Supr
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) { 
                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();

                    if (row >= 0 && column > 0) { 
                        String contenidoCelda = (String) tableModel.getValueAt(row, column);

                        if (contenidoCelda != null && !contenidoCelda.trim().isEmpty()) {
                         
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            calendar.add(Calendar.DAY_OF_YEAR, column - 1); 

                            int hora = 8 + (row / 2); 
                            int minutos = (row % 2 == 0) ? 0 : 30; 
                            Time horaCita = Time.valueOf(hora + ":" + minutos + ":00");
                            java.util.Date fechaCitaUtil = calendar.getTime(); 
                            java.sql.Date fechaCita = new java.sql.Date(fechaCitaUtil.getTime()); 

                            
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            try {
                               
                                CriteriaBuilder cb = session.getCriteriaBuilder();
                               
                                CriteriaQuery<Citas> cq = cb.createQuery(Citas.class);
                               
                                Root<Citas> root = cq.from(Citas.class);

                               
                                cq.select(root).where(
                                        cb.equal(root.get("fechacita"), fechaCita),
                                        cb.equal(root.get("horacita"), horaCita)
                                );

                               
                                Citas citaAEliminar = session.createQuery(cq).uniqueResult(); 
                                //JOptionPane para asegurarnos de que quiere eliminar esa cita
                                if (citaAEliminar != null) {
                                    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar esta cita?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                                    if (confirmacion == JOptionPane.YES_OPTION) {
                                       
                                        ConsultaAltaCliente.eliminarCitaDeBaseDeDatos(citaAEliminar);
                                        tableModel.setValueAt("", row, column); 
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace(); 
                            } finally {
                                session.close(); 
                            }
                        }
                    }
                }
            }
        });
    }
    //Diseño estetico de la tabla para que cada fila tenga un color
    private DefaultTableCellRenderer createTableCellRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) { //para que no se aplique a lo seleccionado porque sino no se ve el texto en modo claro
                    if (isDarkMode) {
                        comp.setBackground(row % 2 == 0 ? new Color(96, 96, 96) : Color.DARK_GRAY);
                    } else {
                        comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                    }
                }

                return comp;
            }
        };
    }


    public void toggleDarkMode(boolean isDark) {
        this.isDarkMode = isDark;
        updateTableColors();
    }

    private void updateTableColors() {
        table.setDefaultRenderer(Object.class, createTableCellRenderer());
        table.repaint();
    }

    
    private void cambiarSemana(int weeks) {
        calendar.add(Calendar.WEEK_OF_YEAR, weeks);
        updateHeaderLabel();
        cargarCitas();
    }

   
    private void updateHeaderLabel() {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String monthYear = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) 
                + " " + calendar.get(Calendar.YEAR);
        headerLabel.setText("Semana de " + calendar.get(Calendar.DAY_OF_MONTH) + " de " + monthYear);
    }
    private void limpiarTabla() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 1; j < tableModel.getColumnCount(); j++) { 
                tableModel.setValueAt("", i, j);
            }
        }
    }
    //Metodo para que se abra el panel tipo JDialog
    private void abrirVentanaEmergente(int row, int column, String dia, String hora, String fecha) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Detalles de la cita", true);
        dialog.setSize(400, 300); 
        dialog.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        //Componentes
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; 
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String fechaNumerica = String.format("%04d-%02d-%02d", year, month, day);

        
        JLabel label = new JLabel("Día: " + dia + " | Fecha: " + fechaNumerica + " | Hora: " + hora);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        dialog.getContentPane().add(label, gbc);

        
        JTextField textField = new JTextField("Buscar cliente y sus mascotas");
        JButton buscarButton = new JButton("Buscar",escalarIcono("/iconosXS/lupaXS.png", 17, 17));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.8;
        dialog.getContentPane().add(textField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        dialog.getContentPane().add(buscarButton, gbc);

        
        JComboBox<String> comboBox = new JComboBox<>();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        dialog.getContentPane().add(comboBox, gbc);

       
        JLabel motivoLabel = new JLabel("Motivo de la cita:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.getContentPane().add(motivoLabel, gbc);

        JTextArea motivoTextArea = new JTextArea(4, 20);
        JScrollPane motivoScrollPane = new JScrollPane(motivoTextArea);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        dialog.getContentPane().add(motivoScrollPane, gbc);

        //Listener para buscar al cliente por dni al pulsar el boton de buscar
        buscarButton.addActionListener(e -> {
            String dniBuscado = textField.getText().trim();
            if (dniBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, introduce un DNI para buscar.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ConsultaAltaCliente consulta = new ConsultaAltaCliente(); //Usamos este metodo para que nos de Persona + Mascota
            List<String> resultados = consulta.buscarPersonaYMascotaPorDni(dniBuscado);
            comboBox.removeAllItems(); 
            if (!resultados.isEmpty()) {
                for (String resultado : resultados) {
                    comboBox.addItem(resultado);
                }
            } else {
                comboBox.addItem("No se encontraron resultados");
            }
        });
        
        JButton guardarButton = new JButton("Guardar");
        JButton cancelarButton = new JButton("Cancelar");
        //Listener del boton guardar cita
        guardarButton.addActionListener(e -> {
           
            String seleccionado = (String) comboBox.getSelectedItem();
          
            if (seleccionado == null || seleccionado.equals("No se encontraron resultados")) {
                JOptionPane.showMessageDialog(dialog, "Por favor, selecciona un resultado válido antes de guardar.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
          
            String motivoCita = motivoTextArea.getText().trim();
            if (motivoCita.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, introduce el motivo de la cita.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
           
            String[] partes = seleccionado.split(" "); 
            String nchipString = partes[partes.length - 1]; 
            
            Integer nchip = Integer.parseInt(nchipString);
            
            tableModel.setValueAt(seleccionado, row, column);
           
            Date fechaCita = Date.valueOf(fechaNumerica); 
            Time horaCita = Time.valueOf(hora + ":00");   
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            Mascotas mascota = session.get(Mascotas.class, nchip);
            session.close();
            if (mascota == null) {
                JOptionPane.showMessageDialog(dialog, "No se encontró la mascota con el N° de chip: " + nchip, "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Citas nuevaCita = new Citas(mascota, fechaCita, horaCita, motivoCita);
           
            ConsultaAltaCliente.guardarCitaEnBaseDeDatos(nuevaCita); //Se guarda la cita en la BD
           
            dialog.dispose();
        });

        
       
        guardarButton.setIcon(escalarIcono("/iconosXS/guardarXS.png", 24, 24));
       
        cancelarButton.setIcon(escalarIcono("/iconosXS/xXXS.png", 24, 24));

       

        cancelarButton.addActionListener(e -> dialog.dispose());

        
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(5, 10, 5, 10);
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.weightx = 1.0;

        buttonGbc.gridx = 0;
        buttonPanel.add(guardarButton, buttonGbc);

        buttonGbc.gridx = 1;
        buttonPanel.add(cancelarButton, buttonGbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        dialog.getContentPane().add(buttonPanel, gbc);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    //Metodo para cargar las citas
    private void cargarCitas() {
    	limpiarTabla();
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Citas> citas = session.createQuery("FROM Citas", Citas.class).list();
        
        
        for (Citas cita : citas) {
            Hibernate.initialize(cita.getMascotas());
            Hibernate.initialize(cita.getMascotas().getPersonas()); 
        }

        session.close();

        for (Citas cita : citas) {
            Mascotas mascota = cita.getMascotas();
            Personas persona = mascota.getPersonas(); 
            String nombreCliente = persona.getNombre();

           
            Date fecha = cita.getFechacita();
            Time hora = cita.getHoracita(); 

           
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);

           
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            if (!mismaSemana(calendar, cal)) {
                continue; 
            }

            int diaSemana = cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 1; 
            if (diaSemana < 0 || diaSemana > 6) {
                continue; 
            }

            int row = (hora.toLocalTime().getHour() - 8) * 2 + (hora.toLocalTime().getMinute() == 0 ? 0 : 1);
            if (row < 0 || row >= tableModel.getRowCount()) {
                continue; 
            }

           
            tableModel.setValueAt(nombreCliente + " - " + mascota.getNombremascota() + " " + mascota.getNchip(), row, diaSemana);
        }
    }

    //Metodo para que coincida  y que se ajuste la fecha con lo reflejado en la tabla
    private boolean mismaSemana(Calendar reference, Calendar target) {
        int yearRef = reference.get(Calendar.YEAR);
        int weekRef = reference.get(Calendar.WEEK_OF_YEAR);

        int yearTarget = target.get(Calendar.YEAR);
        int weekTarget = target.get(Calendar.WEEK_OF_YEAR);

        return yearRef == yearTarget && weekRef == weekTarget;
    }

    private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
    	ImageIcon iconoOriginal = new ImageIcon(P_AltaCliente.class.getResource(ruta));
    	Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
    	return new ImageIcon(imagenEscalada);
	}

    
}
