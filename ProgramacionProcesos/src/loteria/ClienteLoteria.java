package loteria;


import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteLoteria {
    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVIDOR, PUERTO);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Introduce tu número de boleto: ");
            int boleto = scanner.nextInt();
            salida.println(boleto);

            String respuesta;
            while ((respuesta = entrada.readLine()) != null) {
                System.out.println(respuesta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
