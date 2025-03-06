package simulacroProductos;

import java.io.*;
import java.net.*;

public class ClienteProductos {
    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVIDOR, PUERTO);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //input entrada
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);						//output salida, cambiar
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            // Solicitar nombre del cliente antes de continuar
            System.out.print("👤 Ingrese su nombre: ");
            String nombreCliente = teclado.readLine();
            output.println(nombreCliente);

            boolean continuar = true;
            while (continuar) {
                // Mostrar menú
                System.out.println("\n📌 MENÚ:");
                System.out.println("1️- Ver productos");
                System.out.println("2️- Comprar producto");
                System.out.println("3️- Salir");
                System.out.print("🛒 Elija una opción: ");
                
                String opcion = teclado.readLine();
                output.println(opcion);

                switch (opcion) {
	                case "1":
	                    String linea;
	                    while ((linea = input.readLine()) != null && !linea.isEmpty()) {
	                        System.out.println(linea);
	                    }
	                    break;


                    case "2":
                        System.out.print("✏️ Ingrese el nombre del producto: ");
                        output.println(teclado.readLine());

                        System.out.print("🔢 Ingrese la cantidad a comprar: ");
                        output.println(teclado.readLine());

                        System.out.println(input.readLine()); // Respuesta de compra
                        break;

                    case "3":
                        System.out.println(input.readLine()); // Mensaje de despedida
                        continuar = false;
                        break;

                    default:
                        System.out.println("❌ Opción inválida, intente de nuevo.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
