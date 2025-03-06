
package examenBeatriz2ev;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class cliente {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 49171;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("(Cliente) Conectado al servidor");

            System.out.print("Introduzca su nombre: ");
            String nombre = scanner.nextLine();
            printWriter.println(nombre);

            String opcion;
            do {
                mostrarMenu();
                opcion = scanner.nextLine();
                printWriter.println(opcion);

                switch (opcion) {
                    case "1":
                        System.out.print("Introduzca la nota: ");
                        int nota = scanner.nextInt();
                        scanner.nextLine(); 
                        printWriter.println(nota);
                        System.out.println(bufferedReader.readLine());
                        break;
                    case "2":
                        System.out.print("Introduzca la nueva nota: ");
                        int nota2 = scanner.nextInt();
                        scanner.nextLine(); 
                        printWriter.println(nota2);
                        System.out.println(bufferedReader.readLine());
                        break;
                    case "3":
                        System.out.println("Nota almacenada: " + bufferedReader.readLine());
                        break;
                    case "4":
                        System.out.println("Nota eliminada: " + bufferedReader.readLine());
                        break;
                    case "5":
                        System.out.println("(Cliente) Cerrando conexión...");
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            } while (!opcion.equals("5"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menú ---");
        System.out.println("1. Insertar nota");
        System.out.println("2. Modificar nota");
        System.out.println("3. Consultar nota");
        System.out.println("4. Eliminar nota");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }
}
