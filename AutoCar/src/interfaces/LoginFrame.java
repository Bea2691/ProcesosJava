package interfaces;

import javax.swing.*;
import com.formdev.flatlaf.FlatDarculaLaf;
import conexiones.Cliente;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField userNameField;
    private JButton loginButton;
    private Cliente client;
    private String userName;

    public LoginFrame() {
    	setResizable(false); //Eseta es la ventana para que logee el cliente.
        setTitle("AutoCar");
        setSize(400, 400); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 394, 370);
        
        
        ImageIcon appIcon = new ImageIcon("src/imagenes/logoico.png");  //Icono del programa
        setIconImage(appIcon.getImage());  
      
        
        
        ImageIcon logo = new ImageIcon("src/imagenes/logo3.png"); //Imagen fondo logo de AutoCar
        panel.setLayout(null);
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setBounds(0, -1, 394, 253);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(imageLabel); 

       //Panel donde esta el texfield del nombre y el boton para entrar
        JPanel formPanel = new JPanel();
        formPanel.setBounds(0, -1, 394, 347);
        formPanel.setLayout(null);
        JLabel label = new JLabel("Usuario:");
        label.setBounds(0, 265, 394, 33);
        formPanel.add(label);
        userNameField = new JTextField();
        userNameField.setBounds(0, 290, 199, 33);
        formPanel.add(userNameField);
        panel.add(formPanel);

        // Botón de login
        loginButton = new JButton("Login");
        loginButton.setBounds(207, 290, 176, 33);
        formPanel.add(loginButton);
        
                loginButton.addActionListener(new ActionListener() { //Cuando pulsa el boton se abre la ventana de alquiler, si no hay nombre sale un MesageDialog.
                    public void actionPerformed(ActionEvent e) {
                        userName = userNameField.getText().trim().toUpperCase(getLocale());
                        if (!userName.isEmpty()) {
                            try {
                                client = new Cliente();
                                client.sendMessage("CONECTANDO " + userName);
                                new AlquilerFrame(client, userName).setVisible(true);
                                dispose();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(LoginFrame.this, "Por favor, introduce un nombre de usuario.");
                        }
                    }
                });

        // Centrado en pantalla
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
