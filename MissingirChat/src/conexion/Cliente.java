package conexion;

import javax.swing.*;
import com.formdev.flatlaf.FlatDarculaLaf;
import interfaz.V_Chat;
import interfaz.V_Login;
import interfaz.V_Menu;
import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente {
    private String username;
    private PrintWriter out;
    private BufferedReader in;
    private V_Menu mainWindow;
    private Map<String, V_Chat> openChats = new HashMap<>();

    public static void main(String[] args) {
        //Configura el look and feel y lanza la aplicación
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new Cliente().iniciar());
    }

    public void iniciar() {
        //Muestra la ventana de inicio de sesión
        V_Login loginWindow = new V_Login(this);
        loginWindow.setVisible(true);
    }

    @SuppressWarnings("resource")
	public void intentoLogeo(String username, String password) {
        //Intenta iniciar sesión con el servidor
        try {
            Socket socket = new Socket("Localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.username = username;
            out.println(username);
            out.println(password);

            String response = in.readLine();
            if ("AUTH_SUCCESS".equals(response)) {
                // Si la autenticación es exitosa, muestra la ventana principal y empieza a escuchar mensajes
                mainWindow = new V_Menu(this);
                mainWindow.setVisible(true);
                new Thread(this::escuchaMensajes).start();
            } else {
                // Muestra un mensaje de error si la autenticación falla
                JOptionPane.showMessageDialog(null, "Credenciales incorrectas");
            }
        } catch (IOException e) {
            // Muestra un mensaje de error si hay un problema de conexión
            JOptionPane.showMessageDialog(null, "Error de conexión");
        }
    }

    private void escuchaMensajes() {
        //Escucha mensajes entrantes del servidor
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/users ")) {
                    // Actualiza la lista de usuarios conectados
                    String[] users = message.substring(7).split(",");
                    mainWindow.actualizarListaUsuarios(users);
                } else if (message.startsWith("/msg ")) {
                    // Muestra un mensaje entrante en la ventana de chat correspondiente
                    String[] parts = message.split(" ", 3);
                    String sender = parts[1];
                    String content = parts[2];
                    mostrarMensaje(sender, content);
                } else if (message.startsWith("/history ")) {
                    // Carga el historial de chat
                    cargarHistorialChats(message.substring(9));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarHistorialChats(String history) {
        //Carga el historial de chats de un contacto
        String[] parts = history.split("\\|");
        String contact = parts[0];
        StringBuilder mensajes = new StringBuilder();

        for (int i = 1; i < parts.length; i++) {
            String[] messageParts = parts[i].split(":", 2);
            if (messageParts.length == 2) {
                String sender = messageParts[0];
                String content = messageParts[1];
                mensajes.append(sender).append(": ").append(content).append("\n");
            }
        }

        SwingUtilities.invokeLater(() -> {
            if (!openChats.containsKey(contact)) {
                abrirChat(contact);
            }
            openChats.get(contact).appendHistory(mensajes.toString());
        });
    }

    public void abrirChat(String contact) {
        //Abre una ventana de chat con un contacto
        if (openChats.containsKey(contact) && !openChats.get(contact).isVisible()) {
            openChats.remove(contact);
        }

        if (!openChats.containsKey(contact)) {
            V_Chat chatWindow = new V_Chat(this, contact);
            openChats.put(contact, chatWindow);
            chatWindow.setVisible(true);
            out.println("/history " + contact);
        }
    }

    public void enviarMensaje(String contact, String message) {
        // Envía un mensaje a un contacto
        out.println("/chat " + contact + " " + message);
    }

    public void mostrarMensaje(String sender, String content) {
        // Muestra un mensaje entrante en la ventana de chat correspondiente
        SwingUtilities.invokeLater(() -> {
            if (!openChats.containsKey(sender)) {
                abrirChat(sender);
            }
            openChats.get(sender).appendMessage(sender, content);
        });
    }

    public void eliminarVentanaChat(String contact) {
        //Elimina una ventana de chat
        openChats.remove(contact);
    }

    public String getUsername() {
        return username;
    }
}
