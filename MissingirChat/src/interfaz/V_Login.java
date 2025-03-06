package interfaz;

import javax.swing.*;
import conexion.Cliente;
import java.awt.*;

public class V_Login extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn;

    @SuppressWarnings("unused")
	public V_Login(Cliente cliente) {
        setIconImage(Toolkit.getDefaultToolkit().getImage(V_Login.class.getResource("/imagenes/logoInicio.png")));
        setTitle("Login");
        setSize(399, 626);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setBounds(70, 232, 80, 25);
        getContentPane().add(userLabel);

        userField = new JTextField(15);
        userField.setBounds(70, 269, 235, 25);
        getContentPane().add(userField);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setBounds(73, 306, 80, 25);
        getContentPane().add(passLabel);

        passField = new JPasswordField(15);
        passField.setBounds(73, 337, 232, 25);
        getContentPane().add(passField);

        loginBtn = new JButton("Ingresar");
        loginBtn.setBounds(123, 406, 120, 30);
        getContentPane().add(loginBtn);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(V_Login.class.getResource("/imagenes/logoInicio.png")));
        lblNewLabel.setBounds(93, 33, 193, 187);
        getContentPane().add(lblNewLabel);

        JButton btnRegistro = new JButton("Regístrate");
        btnRegistro.setBounds(212, 490, 93, 22);
        getContentPane().add(btnRegistro);

        JLabel lblNewLabel_1 = new JLabel("¿Nuevo en Missingir?");
        lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNewLabel_1.setBounds(56, 484, 149, 31);
        getContentPane().add(lblNewLabel_1);

        //Listener que usa el metodo intentologeo para verificar si el usuario esta en la BD e iniciar sesion
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            final boolean[] loginSuccessful = {false};

            cliente.intentoLogeo(username, password);

            if (loginSuccessful[0]) {
                dispose();
            }
        });
        //Listener para abrir la ventana de registro
        btnRegistro.addActionListener(e -> {
            new V_Registro().setVisible(true); 
            
        });

        setLocationRelativeTo(null);
        setVisible(true); 
    }

    
}
