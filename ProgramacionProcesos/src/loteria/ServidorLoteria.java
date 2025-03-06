package loteria;


import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
 
public class ServidorLoteria {
    private static final int PUERTO = 5000;
    private static final String ARCHIVO_LOTERIA = "src/loteria/loteria.txt";
    private static Map<Integer, String> premios = new HashMap<>();
    
    public static void main(String[] args) {
        cargarNumerosGanadores();

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor de lotería esperando conexiones...");

            while (true) {
                try (Socket cliente = servidor.accept();
                     BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                     PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)) {

                    int boleto = Integer.parseInt(entrada.readLine());
                    System.out.println("Cliente consultando boleto: " + boleto);

                    if (premios.containsKey(boleto)) {
                        salida.println("¡Felicidades! Has ganado: " + premios.get(boleto));
                    } else {
                        salida.println("No has ganado. Los números ganadores son:");
                        for (Map.Entry<Integer, String> entry : premios.entrySet()) {
                            salida.println(entry.getKey() + " - " + entry.getValue());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error con un cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cargarNumerosGanadores() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_LOTERIA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(" ");
                int numero = Integer.parseInt(partes[0]);
                String premio = partes[1];
                premios.put(numero, premio);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de lotería.");
        }
    }
}
