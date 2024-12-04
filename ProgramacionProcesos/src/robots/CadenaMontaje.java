package robots;

import java.util.concurrent.Semaphore;

public class CadenaMontaje {
    private static final int N = 5; // Capacidad máxima de la cadena
    private final String[] cadena = new String[N]; // Representación de la cadena de montaje
    private int indexInsertar = 0; // Índice para insertar productos

    private final Semaphore espacioDisponible = new Semaphore(N); // Espacios libres en la cadena
    private final Semaphore[] productosDisponibles = {
        new Semaphore(0), // Productos tipo A
        new Semaphore(0), // Productos tipo B
        new Semaphore(0)  // Productos tipo C
    };
    private final Semaphore accesoCadena = new Semaphore(1); // Exclusión mutua para acceder a la cadena

    private int totalProductosEmpaquetados = 0; // Contador total de productos empaquetados
    private final Semaphore accesoTotal = new Semaphore(1); // Exclusión mutua para el contador total

    // Método para colocar un producto en la cadena
    public void colocarProducto(String producto) throws InterruptedException {
        espacioDisponible.acquire(); 
        accesoCadena.acquire(); 
        cadena[indexInsertar] = producto;
        
        // Actualiza el índice de inserción de manera explícita
        indexInsertar++;
        if (indexInsertar == N) {
            indexInsertar = 0;
        }

        System.out.println("Colocado " + producto);
        accesoCadena.release(); // Libera acceso a la cadena

        if (producto.equals("ProductoA")) {
            productosDisponibles[0].release();
        } else if (producto.equals("ProductoB")) {
            productosDisponibles[1].release();
        } else if (producto.equals("ProductoC")) {
            productosDisponibles[2].release();
        }
    }

    // Método para empaquetar un producto de un tipo específico
    public void empaquetarProducto(String tipo, String robot) throws InterruptedException {
        int tipoIndex = obtenerIndiceTipo(tipo);
        productosDisponibles[tipoIndex].acquire(); // Espera un producto del tipo correspondiente
        accesoCadena.acquire(); // Acceso exclusivo a la cadena
        for (int i = 0; i < N; i++) { // Busca el producto del tipo correspondiente
            if (cadena[i] != null && cadena[i].equals(tipo)) {
                cadena[i] = null; // Vacía la posición
                System.out.println(robot + " empaquetó " + tipo);
                break;
            }
        }
        accesoCadena.release(); // Libera acceso a la cadena
        espacioDisponible.release(); // Libera un espacio en la cadena
        actualizarTotalEmpaquetados();
    }

    // Método auxiliar para obtener el índice correspondiente a un tipo de producto
    private int obtenerIndiceTipo(String tipo) {
        if (tipo.equals("ProductoA")) {
            return 0;
        } else if (tipo.equals("ProductoB")) {
            return 1;
        } else if (tipo.equals("ProductoC")) {
            return 2;
        }
        throw new IllegalArgumentException("Tipo de producto desconocido: " + tipo);
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
        private final CadenaMontaje cadenaMontaje;

        public Colocador(CadenaMontaje cadenaMontaje) {
            this.cadenaMontaje = cadenaMontaje;
        }

        @Override
        public void run() {
            while (true) { // Bucle infinito
                try {
                    String producto = generarProductoAleatorio();
                    cadenaMontaje.colocarProducto(producto);
                    Thread.sleep(500); // Simula el tiempo de colocación
                } catch (Exception e) {
                    // Ignorar excepciones inesperadas
                }
            }
        }

        private String generarProductoAleatorio() {
            int random = (int) (Math.random() * 3);
            if (random == 0) return "ProductoA";
            if (random == 1) return "ProductoB";
            return "ProductoC";
        }
    }

    // Hilo del empaquetador
    static class Empaquetador extends Thread {
        private final CadenaMontaje cadenaMontaje;
        private final String tipo;
        private final String nombre;

        public Empaquetador(CadenaMontaje cadenaMontaje, String tipo, String nombre) {
            this.cadenaMontaje = cadenaMontaje;
            this.tipo = tipo;
            this.nombre = nombre;
        }

        @Override
        public void run() {
            while (true) { // Bucle infinito
                try {
                    cadenaMontaje.empaquetarProducto(tipo, nombre);
                    Thread.sleep(700); // Simula el tiempo de empaquetado
                } catch (Exception e) {
                    // Ignorar excepciones inesperadas
                }
            }
        }
    }

    public static void main(String[] args) {
        CadenaMontaje cadenaMontaje = new CadenaMontaje();

        // Inicia el colocador
        new Colocador(cadenaMontaje).start();

        // Inicia los empaquetadores
        new Empaquetador(cadenaMontaje, "ProductoA", "EmpaquetadorA").start();
        new Empaquetador(cadenaMontaje, "ProductoB", "EmpaquetadorB").start();
        new Empaquetador(cadenaMontaje, "ProductoC", "EmpaquetadorC").start();
    }
}