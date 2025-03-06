package interfaz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class V_Registro extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldUsuario;
    private JPasswordField textFieldPass;
    private JPasswordField textFieldPass2;
    private Connection dbConnection;

    @SuppressWarnings("unused")
	public V_Registro() {
        setTitle("Registro");
        setIconImage(Toolkit.getDefaultToolkit().getImage(V_Registro.class.getResource("/imagenes/logoInicio.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textFieldPass = new JPasswordField();
        textFieldPass.setBounds(207, 83, 149, 22);
        contentPane.add(textFieldPass);
        textFieldPass.setColumns(10);

        textFieldUsuario = new JTextField();
        textFieldUsuario.setBounds(207, 37, 149, 22);
        contentPane.add(textFieldUsuario);
        textFieldUsuario.setColumns(10);

        textFieldPass2 = new JPasswordField();
        textFieldPass2.setBounds(207, 130, 149, 22);
        contentPane.add(textFieldPass2);
        textFieldPass2.setColumns(10);

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnEnviar.setBounds(163, 179, 93, 22);
        contentPane.add(btnEnviar);

        //Intentar conectarse directamente a la base de datos
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "");
            System.out.println("Conexión a la base de datos establecida en V_Registro.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al establecer la conexión a la base de datos en V_Registro.");
        }

        btnEnviar.addActionListener(e -> {
            String usuario = textFieldUsuario.getText();
            String pass1 = new String(textFieldPass.getPassword());
            String pass2 = new String(textFieldPass2.getPassword());

            if (usuario.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                //Verifica si la conexión a la base de datos está establecida
                if (dbConnection == null) {
                    System.err.println("Error: La conexión a la base de datos no está establecida en V_Registro.");
                    JOptionPane.showMessageDialog(null, "Error en la conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //Verifica si el nombre de usuario ya existe
                if (!nombreUsuarioExiste(usuario)) {
                    // Intenta insertar el nuevo usuario
                    if (insertarUsuario(usuario, pass1)) {
                        JOptionPane.showMessageDialog(null, "Registro exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); //Cerrar la ventana si el registro es exitoso
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lblNewLabel = new JLabel("Nombre de usuario:");
        lblNewLabel.setBounds(77, 39, 149, 16);
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Contraseña:");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNewLabel_1.setBounds(120, 85, 87, 16);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Repita contraseña:");
        lblNewLabel_1_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNewLabel_1_1.setForeground(Color.WHITE);
        lblNewLabel_1_1.setBounds(87, 132, 149, 16);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_2 = new JLabel("");
        lblNewLabel_2.setBounds(0, -165, 1072, 1106);
        lblNewLabel_2.setIcon(new ImageIcon(V_Registro.class.getResource("/imagenes/pngwing.com.png")));
        contentPane.add(lblNewLabel_2);

        setLocationRelativeTo(null);
        setVisible(true); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(V_Registro::new);
    }

    public boolean insertarUsuario(String username, String password) {
        if (dbConnection == null) {
            System.err.println("La conexión a la base de datos no está establecida.");
            return false;
        }
        try {
            String query = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
            PreparedStatement statement = dbConnection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean nombreUsuarioExiste(String username) {
        try {
            String query = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
            PreparedStatement statement = dbConnection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
