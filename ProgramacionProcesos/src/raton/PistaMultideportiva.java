package raton;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class PistaMultideportiva extends Thread {

    private int id;
    private static final int COSTE_POR_JUGADOR = 5;

    private static Semaphore[] jugadoresPorPista = new Semaphore[4];
    private static int dinero = 0;

    public PistaMultideportiva(int id) {
        this.id = id;
    }

    public static int getDinero() {
        return dinero;
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            try {
               
                if (jugadoresPorPista[i].tryAcquire()) {
                    System.out.println("Jugador " + id + " entra a la pista " + (i + 1));

                   
                    Thread.sleep(1000);

                    System.out.println("Jugador " + id + " sale de la pista " + (i + 1));

                    
                    dinero += COSTE_POR_JUGADOR;

                  
                    jugadoresPorPista[i].release();
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

       
        for (int i = 0; i < 4; i++) {
            jugadoresPorPista[i] = new Semaphore(4); 
        }

        System.out.print("¿Cuántos clientes hay hoy? ");
        int numClientes = sc.nextInt();

        for (int i = 1; i <= numClientes; i++) {
            new PistaMultideportiva(i).start();
        }

        
        try {
            Thread.sleep(2000);  
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total recaudado: " + getDinero() + " €");
        sc.close();
    }
}
