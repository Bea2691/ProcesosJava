package ejercicioTCPyUDP;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class serverTCP {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public serverTCP(int puerto) throws IOException {
        serverSocket = new ServerSocket();
        InetSocketAddress addr = new InetSocketAddress("localhost", puerto);
        serverSocket.bind(addr);
    }

    public void start() throws IOException {
        System.out.println("(Servidor) Esperando conexiones...");
        socket = serverSocket.accept();
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true); 
        System.out.println("(Servidor) Conexión establecida...");
    }

    public void stop() throws IOException {
        System.out.println("(Servidor) Cerrando conexiones...");
        reader.close();
        writer.close();
        socket.close();
        serverSocket.close();
        System.out.println("(Servidor) Conexión cerrada...");
    }

    public String comprobarEdad(int edad) {
        if (edad >= 18) {
            return "Eres mayor de edad";
        } else {
            return "Eres menor de edad";
        }
    }

    public static void main(String[] args) {
        try {
            serverTCP servidor = new serverTCP(49171);
            servidor.start();

            
            String edadStr = servidor.reader.readLine();
            int edad = Integer.parseInt(edadStr); 
            String mensaje = servidor.comprobarEdad(edad);

            System.out.println("Edad del cliente: " + edad);
            System.out.println("Pasando la info al cliente: " + mensaje);

            
            servidor.writer.println(mensaje);

            servidor.stop();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NumberFormatException nfe) {
            System.err.println("Error: Formato de edad no válido");
        }
    }
}
