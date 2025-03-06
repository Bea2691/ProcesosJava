package simulacroProductos;
import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorProductos {
    private static final int PUERTO = 5000;
    private static final String ARCHIVO_PRODUCTOS = "src/simulacroProductos/productos.txt";
    private static final String ARCHIVO_COMPRAS = "src/simulacroProductos/compras.txt";
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("🟢 Servidor esperando conexiones en el puerto " + PUERTO + "...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("📶 Cliente conectado: " + socket.getInetAddress());

                new Thread(new ManejadorCliente(socket)).start(); //Explicacion para Navazo
                						//Cuando la conexion esta en un bucle y cada conexion esta en un hilo, es multicliente
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ManejadorCliente implements Runnable { //Hilo para manejar los clientes
        private Socket socket;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                // Recibir el nombre del cliente
                String nombreCliente = input.readLine();
                System.out.println("👤 Cliente identificado como: " + nombreCliente);

                boolean continuar = true;
                while (continuar) {
                    // Leer opción del menú
                    String opcion = input.readLine();

                    switch (opcion) {
                        case "1":
                            output.println(obtenerListaProductos());
                            break;

                        case "2":
                            String producto = input.readLine();
                            int cantidad = Integer.parseInt(input.readLine());
                            String mensajeCompra = procesarCompra(nombreCliente, producto, cantidad);
                            output.println(mensajeCompra);
                            break;

                        case "3":
                            output.println("👋 ¡Gracias por su visita, " + nombreCliente + "!");
                            continuar = false;
                            break;

                        default:
                            output.println("⚠️ Opción inválida, intente de nuevo.");
                            break;
                    }
                }
                socket.close();
                System.out.println("🔴 Cliente desconectado.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String obtenerListaProductos() {
            StringBuilder lista = new StringBuilder("📦 Productos disponibles:\n");
            try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PRODUCTOS))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    lista.append(linea).append("\n");
                }
            } catch (IOException e) {
                return "⚠️ Error al leer los productos.";
            }
            return lista.toString(); //Devuelve toda la lista
        }


        private synchronized String procesarCompra(String nombreCliente, String producto, int cantidadComprada) {
            List<String> productosActualizados = new ArrayList<>();
            boolean encontrado = false;
            StringBuilder mensaje = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PRODUCTOS))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(","); // Separar por coma (nombre, precio, cantidad)
                    if (partes.length == 3) {
                        String nombreProducto = partes[0].trim();
                        double precio = Double.parseDouble(partes[1].trim());
                        int cantidadDisponible = Integer.parseInt(partes[2].trim());

                        // Depuración: Ver qué producto y cantidad leemos
                        System.out.println("Leyendo producto: " + nombreProducto + " | Cantidad disponible: " + cantidadDisponible);

                        if (nombreProducto.equalsIgnoreCase(producto)) {
                            encontrado = true;
                            // Verificar si hay suficiente stock
                            if (cantidadComprada <= cantidadDisponible) {
                                // Descontar la cantidad comprada
                                cantidadDisponible -= cantidadComprada;
                                mensaje.append("✅ Compra realizada: " + cantidadComprada + "x " + nombreProducto + "\n");
                              
                                guardarEnArchivoCompras(nombreCliente, nombreProducto, cantidadComprada); // Guardar en el archivo de compras
                            } else {
                                return "❌ No hay suficiente stock. Solo quedan " + cantidadDisponible + " unidades.";
                            }
                        }
                        // Guardar los productos actualizados (con nueva cantidad)
                        productosActualizados.add(nombreProducto + ", " + precio + ", " + cantidadDisponible);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                return "⚠️ Error al procesar la compra.";
            }

            // Si no encontramos el producto
            if (!encontrado) {
                return "❌ Producto no encontrado.";
            }

            // Actualizar el archivo con las nuevas cantidades
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_PRODUCTOS))) {
                for (String p : productosActualizados) {
                    bw.write(p);
                    bw.newLine();
                }
            } catch (IOException e) {
                return "⚠️ Error al actualizar el archivo.";
            }

            return mensaje.toString();
        }
        private void guardarEnArchivoCompras(String nombreCliente, String producto, int cantidad) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_COMPRAS, true))) {  // "true" para añadir al archivo sin sobrescribir
                String lineaCompra = nombreCliente + ", " + producto + ", " + cantidad;
                bw.write(lineaCompra);
                bw.newLine();
            } catch (IOException e) {
                System.out.println("⚠️ Error al guardar la compra en el archivo.");
            }
        }



       

    }
}