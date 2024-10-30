package raton;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class EstacionTrenes extends Thread {

    private int id; 
    private String tipoTren; 
    private static int contadorPasajeros = 0; 
    private static int contadorCarga = 0; 
    private static Semaphore estacion = new Semaphore(4); //Solo entran 4 max a la estación cada vez
    private static Semaphore[] vias = new Semaphore[4]; //Hay 4 vías
    private static Random random = new Random(); 

    public EstacionTrenes(int id) {
        this.id = id;
        this.tipoTren = random.nextBoolean() ? "Pasajeros" : "Carga"; //Que sea aleatorio si entra de un tipo u otro
    }

    @Override
    public void run() {
        try {
            
            estacion.acquire(); 
            System.out.println("Tren de " + tipoTren + " número " + id + ", ha entrado en la estación");

            
            int via = random.nextInt(vias.length); //Se elige de manera random la via
            vias[via].acquire(); 
            System.out.println("Tren de " + tipoTren + " número " + id + ", ha entrado en la vía " + (via));
            Thread.sleep(1000); 

            //Aumentamos el contador según el tipo de tren que ha elegido el random
            if (tipoTren.equals("Pasajeros")) {
                contadorPasajeros++;
            } else {
                contadorCarga++;
            }

            System.out.println("Tren de " + tipoTren + " número " + id + ", ha salido de la vía " + (via));
            vias[via].release(); 
            
            System.out.println("Tren de " + tipoTren + " número " + id + ", ha salido de la estación");
            estacion.release(); 
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
       // System.out.print("¿Cuántos trenes van a entrar hoy a la estación? "); 
       // int totalTrenes = sc.nextInt();
        
       //No se si se quiere el total asi que lo dejo por si acaso y lo pongo para que sea un numero alatorio de trenes
        
        int totalTrenes = random.nextInt(11) + 10; 
        System.out.println("Número total de trenes que entrarán hoy a la estación: " + totalTrenes);
        
        //Comienza el semaforo de las vias
        for (int i = 0; i < vias.length; i++) {
            vias[i] = new Semaphore(1); //Aquí se pone cuantos trenes permite la via a la vez
        }
       
        EstacionTrenes[] trenes = new EstacionTrenes[totalTrenes];
        
        //Comienzan los trenes
        for (int i = 0; i < totalTrenes; i++) {
            trenes[i] = new EstacionTrenes(i);
            trenes[i].start();
        }

        //Esto porque sino no me cuenta bien los trenes, OJO, aquí pasa algo raro, el Thread.sleep lo tengo que poner exactamente
        //en 1000 sino no me salen las cuentas al final, no se qué pasa la verdad, solo esta bien sincronizado con 1000
        //o directamente quitar la simulación de tiempo, sin ella el contador va genial
        for (EstacionTrenes tren : trenes) {
            try {
                tren.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Mostrar total de trenes de cada tipo
        System.out.println("Total trenes de pasajeros: " + contadorPasajeros);
        System.out.println("Total trenes de carga: " + contadorCarga);
        sc.close();
    }
}

