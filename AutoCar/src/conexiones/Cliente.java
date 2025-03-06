package conexiones;

import java.io.*;
import java.net.*;

public class Cliente {
    private static final String SERVER_DIR = "localhost"; //variables para conecgar a una direccion y puerto
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Cliente() throws IOException {
        this.socket = new Socket(SERVER_DIR, SERVER_PORT);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    //Envio de mensajes
    public void sendMessage(String message) {
        out.println(message);
    }
    //Lectura de mensajes
    public String readMessage() throws IOException {
        return in.readLine();
    }
    //Cierre de la conexion 
    public void close() throws IOException {
        socket.close();
    }
}
