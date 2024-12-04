package robots;

import java.util.concurrent.Semaphore;

public class RobotsEjercicio {
    private static final int N = 5; 
    private final int[] cadena = new int[N]; 
    private int indexInsertar = 0; // Índice para insertar productos

    private final Semaphore espacioDisponible = new Semaphore(N); 
    private final Semaphore[] productosDisponibles = {
        new Semaphore(0), 
        new Semaphore(0), 
        new Semaphore(0)  
    };
    private final Semaphore accesoCadena = new Semaphore(1); //Para acceder a la cadena

    private int totalProductosEmpaquetados = 0; //Contador de productos empaquetados
    private final Semaphore accesoTotal = new Semaphore(1); //Exclusión mutua para el contador total

    // Método para colocar un producto en la cadena
    public void colocarProducto(int producto) throws InterruptedException {
        espacioDisponible.acquire(); // Espera un espacio disponible
        accesoCadena.acquire(); // Acceso exclusivo a la cadena
        cadena[indexInsertar] = producto;
        indexInsertar = (indexInsertar + 1) % N; // Índice circular para insertar productos
        System.out.println("Colocado producto tipo " + producto);
        accesoCadena.release(); // Libera acceso a la cadena
        productosDisponibles[producto - 1].release(); // Señala que hay un producto del tipo correspondiente
    }

    // Método para empaquetar un producto de un tipo específico
    public void empaquetarProducto(int tipo) throws InterruptedException {
        productosDisponibles[tipo - 1].acquire(); // Espera un producto del tipo correspondiente
        accesoCadena.acquire(); // Acceso exclusivo a la cadena
        for (int i = 0; i < N; i++) { // Busca el producto del tipo correspondiente
            if (cadena[i] == tipo) {
                cadena[i] = 0; // Vacía la posición
                System.out.println("Empaquetado producto tipo " + tipo);
                break;
            }
        }
        accesoCadena.release(); // Libera acceso a la cadena
        espacioDisponible.release(); // Libera un espacio en la cadena
        actualizarTotalEmpaquetados();
    }

    // Método para actualizar el contador total de productos empaquetados
    private void actualizarTotalEmpaquetados() throws InterruptedException {
        accesoTotal.acquire(); // Acceso exclusivo al contador total
        totalProductosEmpaquetados++;
        System.out.println("Total productos empaquetados: " + totalProductosEmpaquetados);
        accesoTotal.release(); // Libera acceso al contador total
    }

    // Hilo del colocador
    static class Colocador extends Thread {
        private final RobotsEjercicio cadenaMontaje;

        public Colocador(RobotsEjercicio cadenaMontaje) {
            this.cadenaMontaje = cadenaMontaje;
        }

        @Override
        public void run() {
            while (true) { // Bucle infinito
                try {
                    int producto = (int) (Math.random() * 3) + 1; // Genera un producto aleatorio entre 1 y 3
                    cadenaMontaje.colocarProducto(producto);
                    Thread.sleep(500); // Simula el tiempo de colocación
                } catch (Exception e) {
                    // Ignorar excepciones inesperadas
                }
            }
        }
    }

    // Hilo del empaquetador
    static class Empaquetador extends Thread {
        private final RobotsEjercicio cadenaMontaje;
        private final int tipo;

        public Empaquetador(RobotsEjercicio cadenaMontaje, int tipo) {
            this.cadenaMontaje = cadenaMontaje;
            this.tipo = tipo;
        }

        @Override
        public void run() {
            while (true) { // Bucle infinito
                try {
                    cadenaMontaje.empaquetarProducto(tipo);
                    Thread.sleep(700); // Simula el tiempo de empaquetado
                } catch (Exception e) {
                    // Ignorar excepciones inesperadas
                }
            }
        }
    }

    public static void main(String[] args) {
    	RobotsEjercicio cadenaMontaje = new RobotsEjercicio();

        // Inicia el colocador
        new Colocador(cadenaMontaje).start();

        // Inicia los empaquetadores
        for (int i = 1; i <= 3; i++) {
            new Empaquetador(cadenaMontaje, i).start();
        }
    }
}
