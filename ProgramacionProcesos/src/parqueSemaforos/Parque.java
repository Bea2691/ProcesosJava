package parqueSemaforos;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Parque extends Thread {

    private int id; // ID del visitante
    private static int contadorVisitantesTorno1 = 0;
    private static int contadorVisitantesTorno2 = 0;
    private static int contadorVisitantesTorno3 = 0;
    private static Semaphore torno1 = new Semaphore(1); // Solo 1 visitante a la vez por torno
    private static Semaphore torno2 = new Semaphore(1);
    private static Semaphore torno3 = new Semaphore(1);
    private static Random random = new Random();

    public Parque(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            boolean paso = false;
            // Intentamos adquirir un torno con tryAcquire (no bloqueante)
            while (!paso) {
                if (torno1.tryAcquire()) {  // Intentamos primero el torno 1
                    System.out.println("Visitante " + id + " entrando por el torno 1");
                    // Simulamos el paso por el torno
                    Thread.sleep(random.nextInt(1000));
                    contadorVisitantesTorno1++;
                    System.out.println("Visitante " + id + " ha pasado por el torno 1");
                    torno1.release(); // Liberamos el torno
                    paso = true;
                } else if (torno2.tryAcquire()) {  // Intentamos luego el torno 2
                    System.out.println("Visitante " + id + " entrando por el torno 2");
                    // Simulamos el paso por el torno
                    Thread.sleep(random.nextInt(1000));
                    contadorVisitantesTorno2++;
                    System.out.println("Visitante " + id + " ha pasado por el torno 2");
                    torno2.release(); // Liberamos el torno
                    paso = true;
                } else if (torno3.tryAcquire()) {  // Intentamos el torno 3
                    System.out.println("Visitante " + id + " entrando por el torno 3");
                    // Simulamos el paso por el torno
                    Thread.sleep(random.nextInt(1000));
                    contadorVisitantesTorno3++;
                    System.out.println("Visitante " + id + " ha pasado por el torno 3");
                    torno3.release(); // Liberamos el torno
                    paso = true;
                } else {
                    // Si no puede pasar por ningún torno, espera un poco y vuelve a intentarlo
                    Thread.sleep(500); // Retardo para evitar un ciclo continuo sin descanso
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        // Pedir el número de visitantes
        System.out.print("¿Cuántos visitantes van a entrar al parque? ");
        int totalVisitantes = sc.nextInt();

        // Crear y lanzar los hilos para cada visitante
        Parque[] visitantes = new Parque[totalVisitantes];
        for (int i = 0; i < totalVisitantes; i++) {
            visitantes[i] = new Parque(i);
            visitantes[i].start();
        }

        // Esperar a que todos los visitantes terminen de pasar por los tornos
        for (Parque visitante : visitantes) {
            try {
                visitante.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Mostrar los resultados
        System.out.println("\nVisitantes por torno:");
        System.out.println("Torno 1: " + contadorVisitantesTorno1 + " visitantes");
        System.out.println("Torno 2: " + contadorVisitantesTorno2 + " visitantes");
        System.out.println("Torno 3: " + contadorVisitantesTorno3 + " visitantes");

        sc.close();
    }
}
