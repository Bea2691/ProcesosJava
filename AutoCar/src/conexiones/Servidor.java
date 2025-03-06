package conexiones;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Servidor {
    private static final int PUERTO = 12345; 
    private static final String VEHICULOS_ARCHIVO = "src/vehiculos.log"; //Archivo con la info de los coches
    private static final List<Vehiculo> VEHICULOS = new ArrayList<>(); //Lista de vehiculos
    private static final Map<Integer, Vehiculo> RESERVAS = new HashMap<>(); //HasMap con el vehiculo y el cliente
    private static final String LOG_FILE = "ActividadServidor.log"; //Archivo donde se guarda la actividad
    private static final int TIEMPO_ENTRE_ESTADOS_DE_ALQUILER = 5000; //Tiempo entre estados (reservado, limpiando, etc)
    private static final int TIEMPO_VOLVER_DISPONIBLE = 10000; //Tiempo para volver a etar para alquilar
    private static int reservaId = 1; //Id que se le asigna al cliente para identificarlo junto a su reserva

    public static void main(String[] args) throws IOException { 
        loadVehicles();
        ServerSocket serverSocket = new ServerSocket(PUERTO); //Inicio de sockets
        log("Servidor iniciado en el puerto " + PUERTO);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }

    private static void loadVehicles() throws IOException { //Metodo para leer la info los coches y usarla en los JOptionPane de info
        BufferedReader reader = new BufferedReader(new FileReader(VEHICULOS_ARCHIVO));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] details = line.split("\\|"); //En es achivo estan separados los detalles por -> | <- 
            if (details.length == 5) {
                String marca = details[0];
                String modelo = details[1];
                String tipo = details[2];
                String autonomia = details[3];
                String precio = details[4];
                VEHICULOS.add(new Vehiculo(marca, modelo, tipo, autonomia, precio));
            }
        }
        reader.close();
    }

    private static void log(String message) { //Método para guardar la informacion que queramos en el archivo añadiendo la hora
        try (FileWriter fw = new FileWriter(LOG_FILE, true); //escribe lo que queramos, usando el metodo, precedido por la hora
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            out.println(timestamp + " " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable { //Clase para manejar la comunicacion con el cliente es un hilo
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String connectionMsg = in.readLine();
                String[] connectionParts = connectionMsg.split(" ");
                String userName = connectionParts[1];
                log("Conexión establecida con " + userName); //Coge el nombre del cliente del login para establecer la conexion

                out.println("COCHES DISPONIBLES:"); //Este bucle se carga en un combobox donde salen los nombres de los vehiculos disponibles
                log("Enviando listado de coches disponibles a " + userName); //Aqui guarda simplemente que le esta mostrando la lista de coches disponibles
                for (int i = 0; i < VEHICULOS.size(); i++) {
                    out.println((i + 1) + ". " + VEHICULOS.get(i).getName());
                }

                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    if (clientMessage.startsWith("INFO ")) { //Si pulsa en el boton de info de cada panel de los coches
                        int selectedCarIndex = Integer.parseInt(clientMessage.split(" ")[1]);
                        Vehiculo selectedCar = VEHICULOS.get(selectedCarIndex);
                        out.println("INFO " + selectedCar.getFullDetails()); //Obtiene los detalles del objeto vehiculo que se leen del archivo
                        log("Enviando detalles del coche a " + userName); //Se guarda el registro de que el cliente esta mirando la info de los coches, osea que le ha dado al boton de info
                    } else {
                        int selectedCarIndex = Integer.parseInt(clientMessage) - 1;
                        Vehiculo selectedCar = VEHICULOS.remove(selectedCarIndex); //Cuando selecciona uno se elimina del combobox de todos los vehiculos
                        int id = reservaId++;
                        RESERVAS.put(id, selectedCar);
                        log("Reserva confirmada: " + id + " para " + userName); //Se guarda la reserva con un numero y el cliente

                        out.println("RESERVA CONFIRMADA. ID: " + id);

                        String[] statuses = {"REGISTRANDO", "LIMPIANDO", "ENVIANDO", "ENTREGADO"}; //Estados por los que pasa la reserva
                        for (String status : statuses) {
                            Thread.sleep((int) (Math.random() * TIEMPO_ENTRE_ESTADOS_DE_ALQUILER)); //De la variable fija de timepo max, se calcula de manera random el tiempo de cada estado
                            out.println(status);
                            log("Reserva " + id + ": " + status); //Se va escribiendo el estado de la reserva
                        }

                        log("Reserva " + id + ", vehículo entregado a " + userName + " modelo: " + selectedCar.getName()); //Cuando finalizan los estados se imprime la id, el cliente y el coche adquirido


                        // Esperar antes de que el vehículo vuelva a estar disponible
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                            	VEHICULOS.add(selectedCar); //Cuando termina el tiempo falso de posesión del coche se le vuelve a añadir a la lista y se guarda en el archivo.
                                log("Vehículo " + selectedCar.getName() + " disponible nuevamente");
                            }
                        }, TIEMPO_VOLVER_DISPONIBLE);
                    }
                }

                clientSocket.close(); //se cierra la conexion con el cliente y se guarda su desconexion en el archivo
                log("Conexión terminada con " + userName);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Vehiculo { //clase de vehiculo donde se estrae la info del archivo y se guarda en variables
        private String marca;
        private String modelo;
        private String tipo;
        private String autonomia;
        private String precio;

        public Vehiculo(String marca, String modelo, String tipo, String autonomia, String precio) {
            this.marca = marca;
            this.modelo = modelo;
            this.tipo = tipo;
            this.autonomia = autonomia;
            this.precio = precio;
        }

        public String getName() {
            return marca + " " + modelo;
        }

        public String getDetails() {
            return "Marca: " + marca + "\nModelo: " + modelo + "\nTipo: " + tipo + "\nAutonomía: " + autonomia + "\nPrecio alquiler: " + precio;
        }

        public String getFullDetails() {
            return "Marca: " + marca + "\nModelo: " + modelo + "\nTipo: " + tipo + "\nAutonomía: " + autonomia + "\nPrecio alquiler: " + precio + "\n---";
        }
    }
}
