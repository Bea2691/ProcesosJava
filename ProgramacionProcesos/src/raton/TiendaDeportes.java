package raton;

import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class TiendaDeportes extends Thread {


	
	protected static int caja1 = 0, caja2 = 0, caja3 = 0;
	private static Random random = new Random();
    
	private int id;
	private static Semaphore tienda = new Semaphore(10);  // Máximo de 10 personas
	private static Semaphore c1 = new Semaphore(1);
	private static Semaphore c2 = new Semaphore(1);
	private static Semaphore c3 = new Semaphore(1);
	private static Scanner sc = new Scanner(System.in);


	// Constructor que asigna un ID al cliente
	public TiendaDeportes(int id) {
    	this.id = id;
	}


	
	public void run(){
    	int cajaAsignada = 0;
    	try {
        	
        	tienda.acquire();
        	System.out.println("El cliente " + this.id + " ha entrado en la tienda de deportes.");
        	Thread.sleep(3000);  // Tiempo de compra del cliente


        	// Asignar una caja al azar
        	cajaAsignada = random.nextInt(3) + 1;
        	switch(cajaAsignada) {
            	case 1:
                	c1.acquire();
                	 caja1 += random.nextInt(100) + 1;
                	System.out.println("El cliente " + this.id + " está pagando en la Caja 1.");
                	c1.release();
                	break;
            	case 2:
                	c2.acquire();
                	caja2 += random.nextInt(100) + 1;
                	System.out.println("El cliente " + this.id + " está pagando en la Caja 2.");
                	c2.release();
                	break;
            	case 3:
                	c3.acquire();
                	caja3 += random.nextInt(100) + 1;
                	System.out.println("El cliente " + this.id + " está pagando en la Caja 3.");
                	c3.release();
                	break;
        	}
       	 
        	//Sale de la tienda
        	Thread.sleep(1000);  // Simulamos tiempo de salida
        	tienda.release();
        	System.out.println("El cliente " + this.id + " ha salido de la tienda.");


    	} catch (InterruptedException e) {
        	e.printStackTrace();
    	}
	}


	// Método main que ejecuta el programa
	public static void main(String[] args) {
    	int clientes = 0;
    	System.out.print("¿Cuántos clientes van a entrar hoy?: ");
    	clientes = sc.nextInt();


    	ArrayList<Thread> hilos = new ArrayList<>();


    	// Crear y ejecutar los hilos
    	for (int i = 1; i <= clientes; i++) {
        	Thread hilo = new TiendaDeportes(i);
        	hilos.add(hilo);
        	hilo.start();
    	}


    	// Esperar a que todos los hilos terminen
    	for (Thread hilo : hilos) {
        	try {
            	hilo.join();
        	} catch (InterruptedException e) {
            	System.out.println("Error al esperar al hilo.");
        	}
    	}


    	// Mostrar el total de cada caja y el total general
    	System.out.println("Total de la caja 1 es: " + caja1);
    	System.out.println("Total de la caja 2 es: " + caja2);
    	System.out.println("Total de la caja 3 es: " + caja3);
    	System.out.println("Total de todas las cajas es: " + (caja1 + caja2 + caja3));
   	 
    	// Cerrar el scanner
    	sc.close();
	}
}


