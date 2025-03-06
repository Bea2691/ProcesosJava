
package examenBeatriz2ev;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class servidor {
    private static final int PUERTO = 49171;
    private static final String ARCHIVO_NUMEROS = "src/examenBeatriz2ev/numeros.txt";

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
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                String nombreCliente = reader.readLine();
                System.out.println("(Servidor) Atendiendo a: " + nombreCliente);

                boolean continuar = true;
                String opcion;
                while ((opcion = reader.readLine()) != null) {
                    switch (opcion) {
                        case "1":
                            int nota = Integer.parseInt(reader.readLine());
                            almacenarNota(nombreCliente, nota);
                            writer.println("Nota almacenada " + nota);
                            break;
                        case "2":
                            int nota2 = Integer.parseInt(reader.readLine());
                            writer.println(modificarNota(nombreCliente, nota2));
                            break;
                        

						case "3":
						    writer.println(obtenerNota(nombreCliente));
						    break;

                        case "4":
                            String nombreEliminar = reader.readLine();
                            boolean eliminado = eliminarNota(nombreEliminar);
                            if (eliminado) {
                                writer.println("Nota eliminada");
                            } else {
                                writer.println("Nota no encontrada");
                            }
                            break;
                        case "5":
                            continuar = false;
                            break;
                        default:
                            writer.println("Opción no válida");
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

     

			private String obtenerNota(String cliente) {
			    StringBuilder lista = new StringBuilder("Nota Almacenada: ");
			    try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_NUMEROS))) {
			        String linea;
			        while ((linea = br.readLine()) != null) {
			            String[] partes = linea.split(",");
			            if (partes.length == 2) {
			                String nombreCliente = partes[0].trim();
			                if (nombreCliente.equalsIgnoreCase(cliente)) {
			                    lista.append(linea).append("\n");
			                }
			            }
			        }
			    } catch (IOException e) {
			        return "⚠️ Error al leer la nota.";
			    }
			    return lista.toString();
			}


        private synchronized String modificarNota(String cliente, int nota2) throws IOException {
            List<String> notaActualizada = new ArrayList<>();
            boolean encontrado = false;
            StringBuilder mensaje = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_NUMEROS))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length == 2) {
                        String nombreCliente = partes[0].trim();
                        int nota = (int) Double.parseDouble(partes[1].trim());

                        if (nombreCliente.equalsIgnoreCase(cliente)) {
                            encontrado = true;
                            notaActualizada.add(nombreCliente + "," + nota2);
                            mensaje.append("✅ Nota de: ").append(cliente).append(" cambiada a ").append(nota2).append("\n");
                        } else {
                            notaActualizada.add(linea);
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                return "⚠️ Error al procesar la operación.";
            }

            if (!encontrado) {
                return "❌ Cliente no encontrado.";
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_NUMEROS))) {
                for (String p : notaActualizada) {
                    bw.write(p);
                    bw.newLine();
                }
            } catch (IOException e) {
                return "⚠️ Error al actualizar el archivo.";
            }

            return mensaje.toString();
        }

   /*     private synchronized void almacenarNota(String cliente, int numero) throws IOException {
            try (FileWriter fw = new FileWriter(ARCHIVO_NUMEROS, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(cliente + "," + numero);
                bw.newLine();
            }
        }
*/
			
			private synchronized void almacenarNota(String cliente, int numero) throws IOException {
			    File archivo = new File(ARCHIVO_NUMEROS);
			    if (!archivo.exists()) {
			        archivo.getParentFile().mkdirs();
			        archivo.createNewFile();
			    }
			
			    boolean clienteExiste = false;
			
			    try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_NUMEROS))) {
			        String linea;
			        while ((linea = br.readLine()) != null) {
			            String[] partes = linea.split(",");
			            if (partes.length == 2) {
			                String nombreCliente = partes[0].trim();
			                if (nombreCliente.equalsIgnoreCase(cliente)) {
			                    clienteExiste = true;
			                    break;
			                }
			            }
			        }
			    }
			
			    if (clienteExiste) {
			        System.out.println("El cliente ya existe. Solo se puede modificar o eliminar la nota.");
			    } else {
			        try (FileWriter fw = new FileWriter(ARCHIVO_NUMEROS, true);
			             BufferedWriter bw = new BufferedWriter(fw)) {
			            bw.write(cliente + "," + numero);
			            bw.newLine();
			        }
			    }
			}



				private synchronized boolean eliminarNota(String cliente) throws IOException {
				    List<String> notasActualizadas = new ArrayList<>();
				    boolean encontrado = false;
				
				    try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_NUMEROS))) {
				        String linea;
				        while ((linea = br.readLine()) != null) {
				            String[] partes = linea.split(",");
				            if (partes.length == 2) {
				                String nombreCliente = partes[0].trim();
				                if (!nombreCliente.equalsIgnoreCase(cliente)) {
				                    notasActualizadas.add(linea);
				                } else {
				                    encontrado = true;
				            }
				         }
				      }
				    }
				
				    if (encontrado) {
				        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_NUMEROS))) {
				            for (String nota : notasActualizadas) {
				                bw.write(nota);
				                bw.newLine();
				            }
				        }
				    }
				
				    return encontrado;
				}

    }
}
