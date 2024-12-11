package ejercicioTCPyUDP;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Scanner;



public class clienteTCP {
    private String serverIP;
    private int serverPort;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public clienteTCP(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void start() throws IOException {
        System.out.println("(Cliente) Estableciendo conexión...");
        socket = new Socket();
        InetSocketAddress addr = new InetSocketAddress(serverIP, serverPort);
        socket.connect(addr);
        writer = new PrintWriter(socket.getOutputStream(), true); // Autoflush activado
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("(Cliente) Conexión establecida...");
    }

    public void stop() throws IOException {
        System.out.println("(Cliente) Cerrando conexiones...");
        reader.close();
        writer.close();
        socket.close();
        System.out.println("(Cliente) Conexiones cerradas...");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        clienteTCP cliente = new clienteTCP("localhost", 49171);

        try {
            cliente.start();
            System.out.println("Introduzca su edad: ");
            int edad = sc.nextInt();
            
            cliente.writer.println(edad);

            String respuesta = cliente.reader.readLine();

            System.out.println("Mensaje del servidor: " + respuesta);
            cliente.stop();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}