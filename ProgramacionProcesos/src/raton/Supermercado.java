package raton;

import java.util.Random;

public class Supermercado implements Runnable {
    private String nombreCaja;
    private int tiempoCierre;
    private static int[] recaudacion = new int[5]; // Array para almacenar la recaudación de cada caja
        

    // Constructor
    public Supermercado(String nombreCaja, int tiempoCierre) {
        this.nombreCaja = nombreCaja;
        this.tiempoCierre = tiempoCierre;
    }

    @Override
    public void run() {
        Random rand = new Random();
        int totalCaja = 0;

        try {
            System.out.printf("La caja %s ha comenzado a operar%n", nombreCaja);
            
            for (int i = 0; i < tiempoCierre; i++) {
                
                int ingreso = rand.nextInt(99) + 1; 
                totalCaja += ingreso;
                Thread.sleep(1000); 
                System.out.printf("Caja %s ha recaudado: %d euros%n", nombreCaja, ingreso);
            }
            recaudacion[Integer.parseInt(nombreCaja.split(" ")[1]) - 1] = totalCaja; 
            System.out.printf("La caja %s ha cerrado con un total de: %d euros%n", nombreCaja, totalCaja);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Definición de las cajas y sus tiempos de cierre
        Supermercado caja1 = new Supermercado("Caja 1", 2);
        Supermercado caja2 = new Supermercado("Caja 2", 3);
        Supermercado caja3 = new Supermercado("Caja 3", 6);
        Supermercado caja4 = new Supermercado("Caja 4", 4);
        Supermercado caja5 = new Supermercado("Caja 5", 6);

        // Crear y arrancar los hilos para cada caja
        Thread threadCaja1 = new Thread(caja1);
        Thread threadCaja2 = new Thread(caja2);
        Thread threadCaja3 = new Thread(caja3);
        Thread threadCaja4 = new Thread(caja4);
        Thread threadCaja5 = new Thread(caja5);

        threadCaja1.start();
        threadCaja2.start();
        threadCaja3.start();
        threadCaja4.start();
        threadCaja5.start();

        // Esperar a que todas las cajas terminen
        try {
            threadCaja1.join();
            threadCaja2.join();
            threadCaja3.join();
            threadCaja4.join();
            threadCaja5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Calcular y mostrar la recaudación total
        int totalRecaudado = 0;
        for (int i = 0; i < recaudacion.length; i++) {
            totalRecaudado += recaudacion[i];
        }

        System.out.printf("Recaudación total de todas las cajas: %d euros%n", totalRecaudado);
    }
}
