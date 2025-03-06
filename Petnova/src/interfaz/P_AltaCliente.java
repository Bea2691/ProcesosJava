package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import PetnovaHN.Direccion;
import PetnovaHN.Personas;
import veterinarioDAO.ConsultaAltaCliente;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class P_AltaCliente extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JComboBox<String> cbProvincia;
	private JComboBox<String> cbPais;
	private JTextField tfDni;

	private DefaultTableModel tableModel;

	public P_AltaCliente() {
    	setLayout(new BorderLayout());
    	

    	//Componentes del panel
    	JPanel formPanel = new JPanel();
    	formPanel.setLayout(null); 
    	formPanel.setPreferredSize(new Dimension(300, 0));

    	JTextField tfBuscar = new JTextField();
    	tfBuscar.setBounds(21, 113, 217, 25);

    	JButton btnLupa = new JButton("", escalarIcono("/iconosXS/lupaXS.png", 22, 22));
    	btnLupa.setBounds(246, 110, 30, 30);

    	JLabel lblBsqueda = new JLabel("Búsqueda:");
    	lblBsqueda.setBounds(21, 79, 120, 25);
    	JLabel lblNombre = new JLabel("Nombre:");
    	lblNombre.setBounds(21, 153, 120, 25);
    	JTextField tfNombre = new JTextField();
    	tfNombre.setBounds(132, 153, 150, 25);

    	JLabel lblApellidos = new JLabel("Apellidos:");
    	lblApellidos.setBounds(21, 188, 120, 25);
    	JTextField tfApellidos = new JTextField();
    	tfApellidos.setBounds(132, 188, 150, 25);

    	JLabel lblDni = new JLabel("DNI:");
    	lblDni.setBounds(21, 223, 120, 25);
    	tfDni = new JTextField();
    	tfDni.setBounds(132, 223, 150, 25);

    	JLabel lblTelefono = new JLabel("Teléfono:");
    	lblTelefono.setBounds(21, 258, 120, 25);
    	JTextField tfTelefono = new JTextField();
    	tfTelefono.setBounds(132, 258, 150, 25);

    	JLabel lblEmail = new JLabel("Email:");
    	lblEmail.setBounds(21, 293, 120, 25);
    	JTextField tfCorreo = new JTextField();
    	tfCorreo.setBounds(132, 293, 150, 25);

    	JLabel lblDireccion = new JLabel("Dirección:");
    	lblDireccion.setBounds(21, 328, 120, 25);
    	JTextField tfDireccion = new JTextField();
    	tfDireccion.setBounds(132, 328, 150, 25);

    	JLabel lblProvincia = new JLabel("Provincia:");
    	lblProvincia.setBounds(21, 363, 120, 25);
    	cbProvincia = new JComboBox<>();
    	cbProvincia.setBounds(132, 363, 150, 25);

    	JLabel lblPais = new JLabel("País:");
    	lblPais.setBounds(21, 398, 120, 25);
    	cbPais = new JComboBox<>();
    	cbPais.setBounds(132, 398, 150, 25);

    	JButton btnGuardar = new JButton("Guardar");
    	btnGuardar.setBounds(21, 443, 120, 30);
    	btnGuardar.setIcon(escalarIcono("/iconosXS/guardarXS.png", 24, 24));
    	JButton btnCancelar = new JButton("Cancelar");
    	btnCancelar.setIcon(escalarIcono("/iconosXS/xXXS.png", 24, 24));
    	btnCancelar.setBounds(162, 443, 120, 30);

    	formPanel.add(tfBuscar);
    	formPanel.add(btnLupa);
    	formPanel.add(lblBsqueda);
    	formPanel.add(lblNombre);
    	formPanel.add(tfNombre);
    	formPanel.add(lblApellidos);
    	formPanel.add(tfApellidos);
    	formPanel.add(lblDni);
    	formPanel.add(tfDni);
    	formPanel.add(lblTelefono);
    	formPanel.add(tfTelefono);
    	formPanel.add(lblEmail);
    	formPanel.add(tfCorreo);
    	formPanel.add(lblDireccion);
    	formPanel.add(tfDireccion);
    	formPanel.add(lblProvincia);
    	formPanel.add(cbProvincia);
    	formPanel.add(lblPais);
    	formPanel.add(cbPais);
    	formPanel.add(btnGuardar);
    	formPanel.add(btnCancelar);
    	
    	//Listener para limpiar el panel
    	btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	tfNombre.setText("");
        	    tfApellidos.setText("");
        	    tfTelefono.setText("");
        	    tfDireccion.setText("");
        	    tfCorreo.setText("");
        	    tfDni.setText("");
        	    tfBuscar.setText("");
        	    cbPais.setSelectedIndex(-1); 
        	    cbProvincia.setSelectedIndex(-1);

        	    tableModel.setRowCount(0);
            	
            }
        });

    	//Tabla donde se cargan las mascotas
    	JPanel tablePanel = new JPanel();
    	tablePanel.setLayout(new BorderLayout());

    	String[] columnNames = { "Nombre Mascota", "Tipo Mascota", "Raza Mascota", "Nº Chip", "Sexo" };
    	tableModel = new DefaultTableModel(new Object[0][5], columnNames);
    	JTable table = new JTable(tableModel) {
    	    @Override
    	    public boolean isCellEditable(int row, int column) {
    	        return false;
    	    }
    	};
    	//PopupMenu para editar la mascota de la tabla
    	JPopupMenu popupMenu = new JPopupMenu();
    	JMenuItem editItem = new JMenuItem("Editar");
    	popupMenu.add(editItem);

    	//Listener del popup para editar mascota que abre la ventana Editar y le pasa valores de la fila seleccionada
    	editItem.addActionListener(e -> {
    	    int selectedRow = table.getSelectedRow();
    	    String dniPersona = tfDni.getText().trim(); 
    	    if (selectedRow != -1) {
    	        String nchip = table.getValueAt(selectedRow, 3).toString();
    	        V_EditarMascota ventanaEditarMascota = new V_EditarMascota(nchip, dniPersona, this);
    	        ventanaEditarMascota.setVisible(true);
    	    }
    	});

    	//Listener de la tabla
    	table.addMouseListener(new java.awt.event.MouseAdapter() {
    	    @Override
    	    public void mousePressed(java.awt.event.MouseEvent e) {
    	        if (e.isPopupTrigger()) {
    	            showPopup(e);
    	        }
    	    }

    	    @Override
    	    public void mouseReleased(java.awt.event.MouseEvent e) {
    	        if (e.isPopupTrigger()) {
    	            showPopup(e);
    	        }
    	    }

    	    private void showPopup(java.awt.event.MouseEvent e) {
    	        int row = table.rowAtPoint(e.getPoint());
    	        if (row != -1) {
    	            table.setRowSelectionInterval(row, row);
    	            popupMenu.show(e.getComponent(), e.getX(), e.getY());
    	        }
    	    }
    	});
    	
    	//Scroll de la tabla
    	JScrollPane scrollPane = new JScrollPane(table);
    	scrollPane.setPreferredSize(new Dimension(0, 250)); 
    	tablePanel.add(scrollPane, BorderLayout.CENTER);

    	
    	JPanel btnPanel = new JPanel();
    	btnPanel.setPreferredSize(new Dimension(0, 60)); 
    	
    	JButton btnAnadirMascota = new JButton("Añadir Mascota");
    	btnAnadirMascota.addActionListener(e-> abrirVentanaAltaMascota());
    	btnAnadirMascota.setIcon(escalarIcono("/iconosXS/perroXS.png", 24, 24)); 
    	
    	
    	//Layout para el btn de añadir mascota
    	tablePanel.add(btnPanel, BorderLayout.SOUTH);
    	GroupLayout gl_btnPanel = new GroupLayout(btnPanel);
    	gl_btnPanel.setHorizontalGroup(gl_btnPanel.createParallelGroup(Alignment.LEADING)
            	.addGroup(gl_btnPanel.createSequentialGroup().addComponent(btnAnadirMascota)
                    	.addPreferredGap(ComponentPlacement.RELATED, 283, Short.MAX_VALUE)));
                    	
    	gl_btnPanel.setVerticalGroup(gl_btnPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_btnPanel
            	.createSequentialGroup().addGap(5)
            	.addGroup(gl_btnPanel.createParallelGroup(Alignment.BASELINE)
                    	.addComponent(btnAnadirMascota, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))));
                    
    	
    	btnPanel.setLayout(gl_btnPanel);
    	
    	JPanel containerPanel = new JPanel(new BorderLayout());
    	containerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); 
    	containerPanel.add(tablePanel, BorderLayout.CENTER);

    	
    	add(formPanel, BorderLayout.WEST); 

    	JLabel lblNewLabel = new JLabel("");
    	lblNewLabel.setIcon(escalarIcono("/iconosM/personaAdd.png", 64, 64)); 
    	lblNewLabel.setBounds(121, 6, 64, 61);
    	formPanel.add(lblNewLabel);
    	add(containerPanel, BorderLayout.CENTER); 
    	
    	//Se llama al metodo cargar paises para que salgan en el formulario de cliente al arrancar el panel
    	ConsultaAltaCliente.cargarPaises(cbPais);
    	//Listener para el boton buscar cliente
    	btnLupa.addActionListener(e -> {
    	    String dniBuscado = tfBuscar.getText().trim(); 
    	    if (dniBuscado.isEmpty()) {
    	        JOptionPane.showMessageDialog(null, "Por favor, ingresa un DNI.");
    	        return;
    	    }

    	    //Usa el metodo para cargar los datos de la persona en el formulario
    	    ConsultaAltaCliente consulta = new ConsultaAltaCliente();
    	    Personas persona = consulta.buscarPersonaPorDni(dniBuscado);
    	    
    	    
    	    if (persona != null) {
    	        tfNombre.setText(persona.getNombre());
    	        tfApellidos.setText(persona.getApellido());
    	        tfDni.setText(persona.getDni());
    	        tfTelefono.setText(String.valueOf(persona.getTelefono()));
    	        tfCorreo.setText(persona.getEmail());

    	        
    	        if (persona.getDireccion() != null) {
    	            tfDireccion.setText(persona.getDireccion().getCalle());
    	        }

    	        
    	        if (persona.getDireccion() != null) {
    	            Direccion direccion = persona.getDireccion();
    	            if (direccion.getProvincias() != null) {
    	                cbProvincia.setSelectedItem(direccion.getProvincias().getNombreprovincia());
    	            }
    	            if (direccion.getProvincias() != null && direccion.getProvincias().getPaises() != null) {
    	                cbPais.setSelectedItem(direccion.getProvincias().getPaises().getNombrepais());
    	            }
    	        }

    	        //Si tiene mascotas se cargan en la tabla
    	        cargarMascotas(dniBuscado);
    	    } else {
    	        JOptionPane.showMessageDialog(null, "No se encontró una persona con ese DNI.");
    	    }
    	    consulta.closeSessionFactory();
    	});
    	
    	//listener para guardar una persona tipo cliente al darle a guardar
    	btnGuardar.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        
    	        String dni = tfDni.getText().trim();
    	        String nombre = tfNombre.getText().trim();
    	        String apellido = tfApellidos.getText().trim();
    	        String telefono = tfTelefono.getText().trim();
    	        String email = tfCorreo.getText().trim();
    	        String calle = tfDireccion.getText().trim();
    	        String nombreProvincia = (String) cbProvincia.getSelectedItem();
    	        String nombrePais = (String) cbPais.getSelectedItem();

    	        //Se usa el metodo guardar cliente con lo que obtenemos del formulario
    	        ConsultaAltaCliente consulta = new ConsultaAltaCliente();
    	        consulta.guardarCliente(dni, nombre, apellido, telefono, email, calle, nombreProvincia, nombrePais);
    	        consulta.closeSessionFactory();
    	    }
    	});
    	
    	
    	//Combobox de los paises
    	cbPais.addActionListener(e -> {
    	    String paisSeleccionado = (String) cbPais.getSelectedItem();
    	    if (paisSeleccionado != null && !paisSeleccionado.isEmpty()) {
    	    	//Según el país seleccionado se cargan las provincias que le corresponen, actualmente solo esta España
    	        ConsultaAltaCliente.cargarProvinciasPorPais(paisSeleccionado, cbProvincia); 
    	    }
    	});
	}
	

	//Metodo para crear la tabla con las mascotas
	public void cargarMascotas(String dniPersona) {
	    
	    tableModel.setRowCount(0);

	    
	    ConsultaAltaCliente consulta = new ConsultaAltaCliente();
	    List<Object[]> mascotas = consulta.buscarMascotasPorDni(dniPersona); 

	    if (mascotas != null && !mascotas.isEmpty()) {
	        for (Object[] mascota : mascotas) {
	            
	            Object[] fila = {
	                mascota[0],
	                mascota[1],
	                mascota[2],
	                mascota[3],
	                mascota[4] 
	            };
	            tableModel.addRow(fila); 
	        }
	    } else {
	        JOptionPane.showMessageDialog(null, "No se encontraron mascotas para este DNI.");
	    }
	}
	//Método para escalar el icono y ajustarlo al btn al gusto
	private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
    	ImageIcon iconoOriginal = new ImageIcon(P_AltaCliente.class.getResource(ruta));
    	Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
    	return new ImageIcon(imagenEscalada);
	}

	//Aqui le pasamos el dni de la persona a la ventana de Alta Mascota, lo necesitamos para hacer el insert
	private void abrirVentanaAltaMascota() { 
        String dniPersona = tfDni.getText().trim(); 
        if (!dniPersona.isEmpty()) {
            V_AltaMascota ventanaAltaMascota = new V_AltaMascota(dniPersona, this);  
            ventanaAltaMascota.setVisible(true); 
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa un DNI.");
        }
    }
	
	

}

