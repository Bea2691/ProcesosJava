 package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.hibernate.Session;
import org.hibernate.Transaction;

import PetnovaHN.Facturas;
import PetnovaHN.HibernateUtil;
import PetnovaHN.Personas;

import java.util.List;
import veterinarioDAO.ConsultaAltaCliente;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class P_Facturas extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
    private DefaultTableModel tableModel;
    private V_Historial vhis;

    public P_Facturas() {
        setLayout(new BorderLayout());

        //Aquí se crea la tabla con los servicios pendientes de pago
        String[] columnNames = {"Consulta", "Fecha", "Cliente", "Mascota", "Nchip", "Servicios", "Imprimir"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Integer.class;
                    case 1:
                        return Date.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    case 4:
                        return Integer.class;
                    case 5:
                        return String.class;
                    case 6:
                        return ImageIcon.class;
                    default:
                        return Object.class;
                }
            }

          
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 6; 
            } //La col 6 no es editable pues la usamos para hacer doble click y generar el pdf
        };

        table = new JTable(tableModel);

        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        //Resto de componentes del panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setPreferredSize(new Dimension(309, 50)); 
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        searchPanel.add(new JLabel("Buscar DNI:"));

        
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);

        
        JButton btnLupa = new JButton("");
        btnLupa.setIcon(escalarIcono("/iconosXS/lupaXS.png", 17, 17));
        searchPanel.add(btnLupa);

        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setPreferredSize(new Dimension(309, 60)); 
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        JButton btnHistorial = new JButton("");
        btnHistorial.setVerticalAlignment(SwingConstants.TOP);
        btnHistorial.setIcon(escalarIcono("/iconosXS/historialXS.png",20,20));
        bottomPanel.add(btnHistorial);

        JLabel lblInfo = new JLabel("Histórico");
        bottomPanel.add(lblInfo);

        
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(309, 50)); 
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        int totalWidth = 800; 
        //Tamaño personalizado
        table.getColumnModel().getColumn(0).setPreferredWidth(totalWidth / 12);
        table.getColumnModel().getColumn(1).setPreferredWidth(totalWidth / 10);
        table.getColumnModel().getColumn(2).setPreferredWidth(totalWidth / 6); 
        table.getColumnModel().getColumn(3).setPreferredWidth(totalWidth / 10);
        table.getColumnModel().getColumn(4).setPreferredWidth(totalWidth / 8); 
        table.getColumnModel().getColumn(5).setPreferredWidth(totalWidth / 2); 
        table.getColumnModel().getColumn(6).setPreferredWidth(totalWidth / 10);

        
        add(searchPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH); 
        add(tablePanel, BorderLayout.CENTER);
        
        bottomPanel.add(btnHistorial);
        //Listener para abrir la ventana de historial de facturas
        btnHistorial.addActionListener(e -> {
            V_Historial historialVentana = new V_Historial(); 
            historialVentana.setVisible(true); 
        });
        //Listener para buscar los servicios no facturados
        btnLupa.addActionListener(e -> {
            String dni = searchField.getText().trim();

            
            if (!dni.isEmpty()) {
                System.out.println("Buscando consultas para DNI: " + dni); 
                
                
                List<Object[]> datos = ConsultaAltaCliente.obtenerConsultasPorDni(dni);

                if (datos != null && !datos.isEmpty()) {  
                    System.out.println("Datos obtenidos: " + datos.size() + " consultas."); 

                    
                    tableModel.setRowCount(0); 

                    
                    for (Object[] row : datos) {
                        Integer idConsulta = (Integer) row[0]; 
                        Date fechaConsulta = (Date) row[1];     
                        String cliente = (String) row[2];      
                        String nombreMascota = (String) row[3]; 
                        Integer nchip = (Integer) row[4];        
                        String serviciosConcat = (String) row[5]; 

                        
                        ImageIcon imprimirIcon = escalarIcono("/iconosXS/imprimirXS.png", 20, 20);

                        
                        tableModel.addRow(new Object[]{idConsulta, fechaConsulta, cliente, nombreMascota, nchip, serviciosConcat, imprimirIcon});
                    }
                } else {
                   
                    System.out.println("No se encontraron datos para el DNI: " + dni); 
                    JOptionPane.showMessageDialog(null, "No se encontraron consultas sin facturar para el DNI ingresado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } else {
               
                System.out.println("DNI vacío"); 
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un DNI.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        

        //Listener para hacer doble click en la tabla y generar factura
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (column == 6 && e.getClickCount() == 2) { 
                    
                    int idConsulta = (Integer) table.getValueAt(row, 0); 

                   
                    String dniCliente = searchField.getText();  

                    
                    int idFactura = crearFactura(dniCliente);  //Creamos una factura antes de insertarle datos porque necesitamos el id de factura para el jaspersoft

                    
                    System.out.println("idFactura generado: " + idFactura);  

                   
                    jaspersoftMetodos.Principal.generarFactura(idFactura, idConsulta);  //Una vez tenemos el id, ya se genera el pdf

                    
                    String rutaPDF = "Informes/InformeFactura.pdf";
                    actualizarFacturaConPDF(idFactura, rutaPDF);
                    JOptionPane.showMessageDialog(null, "Factura generada con éxito.", "Aviso", JOptionPane.INFORMATION_MESSAGE);

                   
                    ConsultaAltaCliente.marcarServiciosComoFacturados(idConsulta); //Cambiamos el boolean facturado a true

                    
                    List<Object[]> datosActualizados = ConsultaAltaCliente.obtenerConsultasPorDni(dniCliente); //se actualiza la tabla
                    tableModel.setRowCount(0); 

                    for (Object[] fila : datosActualizados) {
                        Integer idConsulta2 = (Integer) fila[0]; 
                        Date fechaConsulta = (Date) fila[1];  
                        String cliente = (String) fila[2]; 
                        String nombreMascota = (String) fila[3]; 
                        Integer nchip = (Integer) fila[4]; 
                        String serviciosConcat = (String) fila[5]; 

                       
                        ImageIcon imprimirIcon = escalarIcono("/iconosXS/imprimirXS.png", 20, 20);

                       
                        tableModel.addRow(new Object[]{idConsulta2, fechaConsulta, cliente, nombreMascota, nchip, serviciosConcat, imprimirIcon});
                    }
                }
            }
        });


    }
    
    public static void actualizarFacturaConPDF(int idFactura, String rutaPDF) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            File pdfFile = new File(rutaPDF);
            FileInputStream fis = new FileInputStream(pdfFile);
            byte[] pdfData = new byte[(int) pdfFile.length()];
            fis.read(pdfData);
            fis.close();

            transaction = session.beginTransaction();

           
            Facturas factura = session.get(Facturas.class, idFactura);
            if (factura != null) {
                factura.setArchivopdf(pdfData); 
                session.update(factura);
                transaction.commit();
                System.out.println("Factura actualizada con el PDF correctamente.");
            } else {
                System.out.println("No se encontró la factura con ID: " + idFactura);
            }
        } catch (IOException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    public static int crearFactura(String dniCliente) {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        
        Personas persona = (Personas) session.createQuery("FROM Personas WHERE dni = :dni")
                                             .setParameter("dni", dniCliente)
                                             .uniqueResult();

       
        if (persona == null) {
            throw new IllegalArgumentException("No se encontró una persona con el DNI: " + dniCliente);
        }

        
        Facturas factura = new Facturas();
        factura.setPersonas(persona); 

       
        java.sql.Date fechaFactura = new java.sql.Date(new Date().getTime());
        factura.setFechafactura(fechaFactura);  

        
        session.save(factura);
        transaction.commit();
        session.close();

        
        System.out.println("Factura creada con id: " + factura.getIdfactura());  

       
        return factura.getIdfactura();
    }



    private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(ruta));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }
}
