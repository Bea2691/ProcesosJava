 package parqueSemaforos;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Parque extends Thread {

	
    private int id; 
    //private static Semaphore parque = new Semaphore(25); semaphore por si quisiera limitar el aforo
    private static int contadorVisitantesTorno1 = 0;
    private static int contadorVisitantesTorno2 = 0;
    private static int contadorVisitantesTorno3 = 0;
    
    private static Semaphore torno1 = new Semaphore(1); 
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
           
            while (!paso) {
            	//Aqui iria el semaphore del parque
                if (torno1.tryAcquire()) { 
                    System.out.println("Visitante " + id + " entrando por el torno 1");
                    
                    Thread.sleep(random.nextInt(1000));
                    contadorVisitantesTorno1++;
                    System.out.println("Visitante " + id + " ha pasado por el torno 1");
                    torno1.release(); 
                    paso = true;
                } else if (torno2.tryAcquire()) {  
                    System.out.println("Visitante " + id + " entrando por el torno 2");
                    Thread.sleep(random.nextInt(1000));
                    contadorVisitantesTorno2++;
                    System.out.println("Visitante " + id + " ha pasado por el torno 2");
                    torno2.release(); 
                    paso = true;
                } else if (torno3.tryAcquire()) {  
                    System.out.println("Visitante " + id + " entrando por el torno 3");
                   
                    Thread.sleep(random.nextInt(1000));
                    contadorVisitantesTorno3++;
                    System.out.println("Visitante " + id + " ha pasado por el torno 3");
                    torno3.release(); 
                    paso = true;
                } else {
                  
                    Thread.sleep(500); 
                }
                //Aquí el release del semaphore parque
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

       
        System.out.print("¿Cuántos visitantes van a entrar al parque? ");
        int totalVisitantes = sc.nextInt();

        
        Parque[] visitantes = new Parque[totalVisitantes];
        for (int i = 0; i < totalVisitantes; i++) {
            visitantes[i] = new Parque(i);
            visitantes[i].start();
        }

        
        for (Parque visitante : visitantes) {
            try {
                visitante.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

       
        System.out.println("\nVisitantes por torno:");
        System.out.println("Torno 1: " + contadorVisitantesTorno1 + " visitantes");
        System.out.println("Torno 2: " + contadorVisitantesTorno2 + " visitantes");
        System.out.println("Torno 3: " + contadorVisitantesTorno3 + " visitantes");

    
    }
}
