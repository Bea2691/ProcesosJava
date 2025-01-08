package ejercicioNavideño;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class cliente {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 49171;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             DataInputStream dataInputStream = new DataInputStream(inputStream);
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("(Cliente) Conectado al servidor");

            System.out.print("Introduzca su nombre: ");
            String nombre = scanner.nextLine();
            dataOutputStream.writeUTF(nombre);

            String opcion;
            do {
                mostrarMenu();
                opcion = scanner.nextLine();
                dataOutputStream.writeUTF(opcion);

                switch (opcion) {
                    case "1":
                        System.out.print("Introduzca un número: ");
                        int numero = scanner.nextInt();
                        scanner.nextLine(); //buffer de siempre
                        dataOutputStream.writeInt(numero);
                        System.out.println(dataInputStream.readUTF());
                        break;
                    case "2":
                        System.out.println("Total de números almacenados: " + dataInputStream.readInt());
                        break;
                    case "3":
                        System.out.println("Números almacenados:\n" + dataInputStream.readUTF());
                        break;
                    case "4":
                        System.out.println("Total de números almacenados por usted: " + dataInputStream.readInt());
                        break;
                    case "5":
                        recibirArchivo(dataInputStream); 
                        break;
                    case "0":
                        System.out.println("(Cliente) Cerrando conexión...");
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            } while (!opcion.equals("0"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menú ---");
        System.out.println("1. Almacenar un número");
        System.out.println("2. Contar números almacenados");
        System.out.println("3. Listar números almacenados");
        System.out.println("4. Contar números por cliente");
        System.out.println("5. Recibir archivo de números");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void recibirArchivo(DataInputStream dataInputStream) throws IOException {
        String linea;
        System.out.println("\n--- Números recibidos ---");
        
        //Leer todas las líneas HASTA "EOF"
        while (!(linea = dataInputStream.readUTF()).equals("EOF")) {
            System.out.println(linea);
        }
        
        //Después de "recibir" el archivo, que esta almacenado en el directorio a parte de en pantalla con el read
        System.out.println("Archivo recibido correctamente.");
    }
}
