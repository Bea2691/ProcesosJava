package ejercicioNavideño;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class servidor {
    private static final int PUERTO = 49171;
    private static final String ARCHIVO_NUMEROS = "numeros.txt"; //Archivo donde se guardarán nombre:numero
    private static final String ARCHIVO_SOLO_NUMEROS = "solonumeros.txt"; //Archivo donde se guardarán solo los números, para el case 5

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("(Servidor) Escuchando en el puerto " + PUERTO);
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("(Servidor) Cliente conectado: " + clienteSocket.getInetAddress());
                new ManejadorCliente(clienteSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ManejadorCliente extends Thread {
        private Socket socket;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream();
                 DataInputStream dataInputStream = new DataInputStream(inputStream);
                 DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {

                String nombreCliente = dataInputStream.readUTF();
                System.out.println("(Servidor) Atendiendo a: " + nombreCliente);

                String opcion;
                while ((opcion = dataInputStream.readUTF()) != null) {
                    switch (opcion) {
                        case "1":
                            int numero = dataInputStream.readInt();
                            almacenarNumero(nombreCliente, numero);
                            dataOutputStream.writeUTF("Número almacenado");
                            break;
                        case "2":
                            dataOutputStream.writeInt(contarNumeros());
                            break;
                        case "3":
                            dataOutputStream.writeUTF(listarNumeros());
                            break;
                        case "4":
                            dataOutputStream.writeInt(contarNumerosPorCliente(nombreCliente));
                            break;
                        case "5":
                            generarArchivoSoloNumeros(dataOutputStream);
                            break;
                        default:
                            dataOutputStream.writeUTF("Opción no válida");
                            break;
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private synchronized void almacenarNumero(String cliente, int numero) throws IOException {
            try (FileWriter fw = new FileWriter(ARCHIVO_NUMEROS, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(cliente + ":" + numero);
                bw.newLine();
            }
        }

        private synchronized int contarNumeros() {
            int contador = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_NUMEROS))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    contador++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contador;
        }

        private synchronized String listarNumeros() {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_NUMEROS))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");  //Salto de linea
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        private synchronized int contarNumerosPorCliente(String cliente) {
            int count = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_NUMEROS))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith(cliente + ":")) {  //Comprobar si la línea corresponde al cliente, el startwith lo usamos en AndroidStudio Navazo
                        count++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return count;
        }

        private synchronized void generarArchivoSoloNumeros(DataOutputStream dataOutputStream) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_NUMEROS));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_SOLO_NUMEROS))) {
                String line;
                while ((line = br.readLine()) != null) {
                 
                    String[] partes = line.split(":");
                    if (partes.length > 1) {
                        bw.write(partes[1]);  //Escribir solo el número y no el nombre del cliente
                        bw.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Para Enviar archivo al cliente, crea un archivo que se almacena el el directorio del servidor
            try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_SOLO_NUMEROS))) {
                String line;
                while ((line = br.readLine()) != null) {
                    dataOutputStream.writeUTF(line);  //Enviar cada línea del archivo
                }
            }

            
            dataOutputStream.writeUTF("EOF"); //Esto es End of file

            //Mensaje para el cliente
            dataOutputStream.writeUTF("Archivo generado correctamente en el directorio");
        }
    }
}