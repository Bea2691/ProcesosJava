package interfaces;

import javax.swing.*;

import conexiones.Cliente;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.*;

public class AlquilerFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private Cliente client;
    private JComboBox<String> vehicleComboBox;
    private JButton alquilerBoton;
    private JPanel customPanel;
    private JLabel lblImagen1, lblNombre1, lblImagen2, lblNombre2, lblImagen3, lblNombre3;
    private JButton btnInfo1, btnInfo2, btnInfo3, btnInfo4, btnInfo5, btnInfo6;
    private JLabel lblImagen4, lblNombre4, lblImagen5, lblNombre5, lblImagen6, lblNombre6;
    private JPanel statusPanel;
    private static final String VEHICULOS_ARCHIVO = "src/vehiculos.log";  //Necesitamos el archivo para la info de ls coches

    public AlquilerFrame(Cliente client, String userName) { //Frame con el nombre del cliente, el combobox con los coches y un panel inferior para los estados de alquiler
        this.client = client;
        setTitle("Alquiler coches AutoCar");
        setSize(861, 629);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Usuario: " + userName));

        vehicleComboBox = new JComboBox<>(); //Combobox de los coches
        alquilerBoton = new JButton("Alquilar"); //Boton de alquilar
        alquilerBoton.setEnabled(false);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new FlowLayout());
        comboBoxPanel.add(new JLabel("Coches disponibles:"));
        comboBoxPanel.add(vehicleComboBox);
        comboBoxPanel.add(alquilerBoton);
        centerPanel.add(comboBoxPanel, BorderLayout.NORTH);

        customPanel = new JPanel();
        customPanel.setLayout(new GridLayout(3, 2, 10, 10));
        centerPanel.add(customPanel, BorderLayout.CENTER);
        crearPanelesCoches(); //Aqui esta el panel con los coches, sus imagenes y boton para la info

        //Panel de estados de alquiler
        statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        JScrollPane statusScrollPane = new JScrollPane(statusPanel);
        statusScrollPane.setPreferredSize(new Dimension(799, 150));

       JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerPanel, statusScrollPane);
       splitPane.setDividerLocation(400);
       splitPane.setBackground(Color.LIGHT_GRAY);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        alquilerBoton.addActionListener(new ActionListener() { //Boton de alquiler, listener, si selecciona un coche el cliente envia mensaje
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = vehicleComboBox.getSelectedIndex();
                if (selectedIndex >= 0) {
                    client.sendMessage(String.valueOf(selectedIndex + 1));
                    alquilerBoton.setEnabled(false); //Deshabilitar el botón después de alquilar el coche para que no siga alquilando
                }
            }
        });
        
        //Listener para cada boton de informacion de cada coche
        btnInfo1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarInfoCoche(1);
            }
        });
        btnInfo2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarInfoCoche(2);
            }
        });
        btnInfo3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarInfoCoche(3);
            }
        });
        btnInfo4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarInfoCoche(4);
            }
        });
        btnInfo5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarInfoCoche(5);
            }
        });
        btnInfo6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarInfoCoche(6);
            }
        });

        new Thread(new ClientTask()).start();
    }

    private String obtenerInformacionCoche(int indice) { //Aqui es donde lee el archivo  
        StringBuilder info = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(VEHICULOS_ARCHIVO))) {
            String line;
            int currentIndex = 0;
            while ((line = reader.readLine()) != null) {
                if (currentIndex == indice) {
                    info.append(line); // Mostrar detalles del coche
                    break;
                }
                currentIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info.toString();
    }

    private void mostrarInfoCoche(int indice) { //y aqui carga la info en los JOPtionPane.showmessagedialog
        String info = obtenerInformacionCoche(indice - 1);  
        if (info.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Información no disponible.");
        } else {
            String[] partes = info.split("\\|");
            StringBuilder infoConSaltos = new StringBuilder();
            for (String parte : partes) {
                infoConSaltos.append(parte).append("\n"); //Esto es para que cada parte separada con |, la ponga en un salto de linea
            }
            JOptionPane.showMessageDialog(this, infoConSaltos.toString());
        }
    }
    //Aqui estan los 6 paneles por coche con su imagen y nombre y boton de info
    private void crearPanelesCoches() {
        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        lblImagen1 = new JLabel(new ImageIcon(AlquilerFrame.class.getResource("/imagenes/tesla.jpg")));
        lblImagen1.setBounds(10, 0, 202, 115);
        panel1.add(lblImagen1);
        lblNombre1 = new JLabel("CyberTruck");
        lblNombre1.setBounds(245, 44, 91, 20);
        lblNombre1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel1.add(lblNombre1);
        btnInfo1 = new JButton("Info");
        btnInfo1.setBounds(245, 81, 99, 23);
        panel1.add(btnInfo1);
        customPanel.add(panel1);

        JPanel panel2 = new JPanel();
        panel2.setLayout(null);
        lblImagen2 = new JLabel(new ImageIcon(AlquilerFrame.class.getResource("/imagenes/lambo.jpg")));
        lblImagen2.setBounds(0, 0, 202, 115);
        panel2.add(lblImagen2);
        lblNombre2 = new JLabel("Lamborghini Terzo");
        lblNombre2.setBounds(246, 38, 141, 20);
        lblNombre2.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel2.add(lblNombre2);
        btnInfo2 = new JButton("Info");
        btnInfo2.setBounds(246, 81, 99, 23);
        panel2.add(btnInfo2);
        customPanel.add(panel2);

        JPanel panel3 = new JPanel();
        panel3.setLayout(null);
        lblImagen3 = new JLabel(new ImageIcon(AlquilerFrame.class.getResource("/imagenes/bmw.jpg")));
        lblImagen3.setBounds(10, 0, 202, 115);
        panel3.add(lblImagen3);
        lblNombre3 = new JLabel("BMW Vision");
        lblNombre3.setBounds(245, 38, 97, 20);
        lblNombre3.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel3.add(lblNombre3);
        btnInfo3 = new JButton("Info");
        btnInfo3.setBounds(245, 81, 97, 23);
        panel3.add(btnInfo3);
        customPanel.add(panel3);

        JPanel panel4 = new JPanel();
        panel4.setLayout(null);
        lblImagen4 = new JLabel(new ImageIcon(AlquilerFrame.class.getResource("/imagenes/audi.jpg")));
        lblImagen4.setBounds(0, 0, 202, 115);
        panel4.add(lblImagen4);
        lblNombre4 = new JLabel("Audi AI:TRAIL");
        lblNombre4.setBounds(247, 37, 147, 20);
        lblNombre4.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel4.add(lblNombre4);
        btnInfo4 = new JButton("Info");
        btnInfo4.setBounds(247, 81, 99, 23);
        panel4.add(btnInfo4);
        customPanel.add(panel4);

        JPanel panel5 = new JPanel();
        panel5.setLayout(null);
        lblImagen5 = new JLabel(new ImageIcon(AlquilerFrame.class.getResource("/imagenes/mercedes.jpg")));
        lblImagen5.setBounds(10, 0, 202, 115);
        panel5.add(lblImagen5);
        lblNombre5 = new JLabel("Mercedes Vision");
        lblNombre5.setBounds(245, 41, 123, 20);
        lblNombre5.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel5.add(lblNombre5);
        btnInfo5 = new JButton("Info");
        btnInfo5.setBounds(245, 81, 97, 23);
        panel5.add(btnInfo5);
        customPanel.add(panel5);

        JPanel panel6 = new JPanel();
        panel6.setLayout(null);
        lblImagen6 = new JLabel(new ImageIcon(AlquilerFrame.class.getResource("/imagenes/porsche.jpg")));
        lblImagen6.setBounds(0, 0, 202, 115);
        panel6.add(lblImagen6);
        lblNombre6 = new JLabel("Porsche Taycan");
        lblNombre6.setBounds(243, 37, 173, 20);
        lblNombre6.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel6.add(lblNombre6);
        btnInfo6 = new JButton("Info");
        btnInfo6.setBounds(243, 81, 99, 23);
        panel6.add(btnInfo6);
        customPanel.add(panel6);
    }

    
    //Este el el hilo que controla los estados de alquiler
    private class ClientTask implements Runnable {
        @Override
        public void run() {
            boolean completed = false; //Boolean para avisar cuando haya terminado el proceso de alquiler
            try {
                String response;
                while ((response = client.readMessage()) != null) {
                    if (response.startsWith("COCHES DISPONIBLES:")) { 
                        vehicleComboBox.removeAllItems();
                    } else if (response.matches("\\d+\\. .*")) {
                        vehicleComboBox.addItem(response.split(" ", 2)[1]);
                        alquilerBoton.setEnabled(true);
                    } else if (response.startsWith("RESERVA CONFIRMADA")) { //Comprueba que comienda con reserva confirmada
                        actualizarEstado(response); //Esto va comprobando con el cliente->servidor y vuelta que coincide los estados del coche
                    } else if (response.matches("REGISTRANDO|LIMPIANDO|ENVIANDO|ENTREGADO")) {
                        actualizarEstado(response);
                        if (response.equals("ENTREGADO")) {
                            completed = true; //Termina el proceso de alquiler/reserva de coche
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    client.close(); //Cerramos la conexion con el cliente
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (completed) { //Si ha ido bien el proceso de reserva
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(AlquilerFrame.this, "Gracias por utilizar AutoCar! 😊 "); //Cuando termina, muestra mensaje, cierra la ventana y nos devuelve al login.
                        new LoginFrame().setVisible(true);
                        AlquilerFrame.this.dispose();
                    });
                }
            }
        }

        private void actualizarEstado(String estado) { //esto es solo para que vaya cambiando de color de verde, es un hilo falso par simular el cambio
            SwingUtilities.invokeLater(() -> {
                // Crear un nuevo JLabel para cada estado
                JLabel estadoLabel = new JLabel(estado);
                estadoLabel.setForeground(Color.BLACK);  // Inicialmente en negro
                statusPanel.add(estadoLabel);
                statusPanel.revalidate();
                statusPanel.repaint();

                // Cambiar a verde solo cuando sea un estado completado
                if (estado.equals("RESERVA CONFIRMADA") || estado.equals("ENTREGADO") ||
                    estado.equals("REGISTRANDO") || estado.equals("LIMPIANDO") || estado.equals("ENVIANDO")) {
                    // Aseguramos que se cambia a verde uno por uno
                    new Thread(new ChangeColorTask(estadoLabel)).start();
                }
            });
        }

        //Clase que cambia el color de la etiqueta a verde, es un hilo
        private class ChangeColorTask implements Runnable {
            private JLabel estadoLabel;

            public ChangeColorTask(JLabel estadoLabel) {
                this.estadoLabel = estadoLabel;
            }

            @Override
            public void run() {
                try {
                    
                    Thread.sleep(500); //Este hilo simula el 0.5 seg para que cambie de negro a verde el texto del proceso de alquiler, es algo visual un poco inutil.
                    estadoLabel.setForeground(Color.GREEN);
                    statusPanel.revalidate();
                    statusPanel.repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
