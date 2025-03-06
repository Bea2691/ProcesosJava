package conexion;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class Servidor {
    private static final int PORT = 12345;
    private static ServerSocket serverSocket;
    private static Map<String, ClientHandler> clientes = new HashMap<>();
    private static Connection dbConnection;

    public static void main(String[] args) {
    	
    	
            try {
                // Esta es la conexión con la base de datos
                System.out.println("Intentando conectar con la base de datos...");
                dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ejemplo", "root", "");
                System.out.println("Conexión con la base de datos establecida.");

                // Aquí está la conexión con sockets
                serverSocket = new ServerSocket(PORT);
                System.out.println("Servidor iniciado en puerto " + PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new ClientHandler(clientSocket, dbConnection).start();
                }
            } catch (SQLException e) {
                System.err.println("Error al establecer la conexión con la base de datos.");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}
      

    // Clase para manejar cada cliente
    static class ClientHandler extends Thread {
        private Socket socket;
        private Connection dbConnection;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket, Connection dbConnection) {
            this.socket = socket;
            this.dbConnection = dbConnection;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                //Autenticación
                username = in.readLine();
                String password = in.readLine();

                if (autenticarUsuario(username, password)) {
                    out.println("AUTH_SUCCESS");
                    actualizarEstadoUsuario(true);
                    clientes.put(username, this); //Añadir a la lista de clientes conectados
                    enviarListaUsuarios(); //Enviar lista actualizada
                    manejarMensajes();
                } else {
                    out.println("AUTH_FAIL");
                    socket.close();
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (username != null) {
                    	clientes.remove(username); // Eliminar de la lista de clientes conectados
                        actualizarEstadoUsuario(false);
                        enviarListaUsuarios(); // Enviar lista actualizada
                    }
                    socket.close();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //Metodo para atentificar si el usuario en la base de datos para hacer el login
        private boolean autenticarUsuario(String user, String pass) throws SQLException {
            String query = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            try (PreparedStatement pst = dbConnection.prepareStatement(query)) {
                pst.setString(1, user);
                pst.setString(2, pass);
                return pst.executeQuery().next();
            }
        }

        //Actualizar el estado en línea del usuario, si el cliente ha iniciado sesion aparece en la interfaz V_Menu
        private void actualizarEstadoUsuario(boolean online) throws SQLException {
            String query = "UPDATE usuarios SET online = ? WHERE username = ?";
            try (PreparedStatement pst = dbConnection.prepareStatement(query)) {
                pst.setBoolean(1, online);
                pst.setString(2, username);
                pst.executeUpdate();
            }
        }

        //Manejar mensajes entrantes del cliente, lee los mensajes, el emisor y el contenido
        private void manejarMensajes() throws IOException, SQLException {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/chat ")) {
                    String[] parts = message.split(" ", 3);
                    String receiver = parts[1];
                    String content = parts[2];
                    enviarMensaje(receiver, content);
                } else if (message.startsWith("/history ")) {
                    String contact = message.substring(9); // Obtener el contacto
                    String history = obtenerHistorialChat(username, contact); // Obtener historial
                    out.println(history); //Enviar historial al cliente
                }
            }
        }

        //Método para obtener historial de chat entre dos usuarios para cargarlo en la interfaz del chat del usuario
        private String obtenerHistorialChat(String user1, String user2) throws SQLException {
            StringBuilder history = new StringBuilder("/history " + user2 + "|");
            String query = "SELECT u.username AS sender, m.contenido AS message " + "FROM mensajes m "
                    + "JOIN usuarios u ON m.sender_id = u.id "
                    + "WHERE m.conversacion_id = (SELECT id FROM conversaciones "
                    + "WHERE (user1_id = (SELECT id FROM usuarios WHERE username = ?) "
                    + "AND user2_id = (SELECT id FROM usuarios WHERE username = ?)) "
                    + "OR (user1_id = (SELECT id FROM usuarios WHERE username = ?) "
                    + "AND user2_id = (SELECT id FROM usuarios WHERE username = ?))) " + "ORDER BY m.fecha ASC";

            try (PreparedStatement pst = dbConnection.prepareStatement(query)) {
                pst.setString(1, user1);
                pst.setString(2, user2);
                pst.setString(3, user2);
                pst.setString(4, user1);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String sender = rs.getString("sender");
                    String message = rs.getString("message");
                    history.append(sender).append(":").append(message).append("|");
                }
            }

            return history.toString();
        }

        //Método para enviar mensaje a un receptor
        private void enviarMensaje(String receiver, String content) throws SQLException {
            // Guardar en base de datos
            int conversacionId = obtenerIdConversacion(username, receiver); //Se va insertando los mensajes en la BD
            String query = "INSERT INTO mensajes (conversacion_id, sender_id, contenido) VALUES (?, (SELECT id FROM usuarios WHERE username = ?), ?)";
            try (PreparedStatement pst = dbConnection.prepareStatement(query)) {
                pst.setInt(1, conversacionId);
                pst.setString(2, username);
                pst.setString(3, content);
                pst.executeUpdate();
            }

            // Enviar al receptor
            if (clientes.containsKey(receiver)) {
            	clientes.get(receiver).out.println("/msg " + username + " " + content);
            }
        }

        //Método para crear una conversación única entre dos usuarios
        private int obtenerIdConversacion(String user1, String user2) throws SQLException {
            String query = "SELECT id FROM conversaciones WHERE (user1_id = (SELECT id FROM usuarios WHERE username = ?) AND user2_id = (SELECT id FROM usuarios WHERE username = ?)) OR (user1_id = (SELECT id FROM usuarios WHERE username = ?) AND user2_id = (SELECT id FROM usuarios WHERE username = ?))";
            try (PreparedStatement pst = dbConnection.prepareStatement(query)) {
                pst.setString(1, user1);
                pst.setString(2, user2);
                pst.setString(3, user2);
                pst.setString(4, user1);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    //Si la convensarcion no existe crea una nueva conversación
                    String insertQuery = "INSERT INTO conversaciones (user1_id, user2_id) VALUES ((SELECT id FROM usuarios WHERE username = ?), (SELECT id FROM usuarios WHERE username = ?))";
                    try (PreparedStatement insertPst = dbConnection.prepareStatement(insertQuery,
                            Statement.RETURN_GENERATED_KEYS)) {
                        insertPst.setString(1, user1);
                        insertPst.setString(2, user2);
                        insertPst.executeUpdate();
                        ResultSet generatedKeys = insertPst.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }
            return -1;
        }

        //Para enviar la lista de usuarios conectados a todos los clientes
        private void enviarListaUsuarios() {
            StringBuilder userList = new StringBuilder("/users ");
            for (String user : clientes.keySet()) {
                userList.append(user).append(",");
            }
            for (ClientHandler client : clientes.values()) {
                client.out.println(userList.toString());
            }
        }
    }

    
   

    
}
