package raton;



import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class SemaforoSuperTryAcquireArraysCajas extends Thread {

    private static Semaphore supermercado = new Semaphore(5); // Permite que solo 5 personas entren al supermercado a la vez
    private static Semaphore[] cajas = new Semaphore[5];// Semáforos para las 5 cajas registradoras
    private int identificador; // Número de cliente
    private static Scanner sc = new Scanner(System.in);

    public SemaforoSuperTryAcquireArraysCajas(int identificador) {
        this.identificador = identificador;
    }

    @Override
    public void run() {
        try {
            // El cliente intenta entrar al supermercado
            supermercado.acquire();
            System.out.println("El cliente " + this.identificador + " entra al supermercado.");
            Thread.sleep((long) (Math.random() * 2000) + 100); // Simular tiempo de compra

            // Buscar una caja disponible
            boolean atendido = false;
            for (int i = 0; i < cajas.length && !atendido; i++) {
            	 if (cajas[i].tryAcquire()) { // Intenta adquirir una caja
                    System.out.println("El cliente " + this.identificador + " está siendo atendido en la caja " + (i + 1));
                    Thread.sleep((long) (Math.random() * 2000) + 100); // Simular tiempo de atención en caja
                    System.out.println("El cliente " + this.identificador + " ha sido atendido en la caja " + (i + 1));
                    cajas[i].release(); 
                    atendido = true;
                }
            }

            // El cliente sale del supermercado
            supermercado.release();
            System.out.println("El cliente " + this.identificador + " sale del supermercado.");
        } catch (InterruptedException e) {
            System.out.println("El cliente " + this.identificador + " fue interrumpido.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Inicializar los semáforos de las cajas
        for (int i = 0; i < cajas.length; i++) {
            cajas[i] = new Semaphore(1); 
        }

        System.out.println("¿Cuántos clientes han entrado hoy al supermercado?");
        int totalClientes = sc.nextInt();

        // Crear y lanzar los hilos para cada cliente
        for (int i = 1; i <= totalClientes; i++) {
            new SemaforoSuperTryAcquireArraysCajas(i).start();
        }
    }
}
