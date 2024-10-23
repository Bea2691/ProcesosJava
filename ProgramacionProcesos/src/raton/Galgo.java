package raton;

public class Galgo implements Runnable {

	private String nombre;
	private int tiempoCarrera;
	private static int posicion = 1; //se pone static para que esa variable sea accesible y comun para todos los objetos galgo

	public Galgo(String nombre, int tiempo) {
    	this.nombre = nombre;
    	this.tiempoCarrera = tiempo; 
	}

	@Override
	public void run() {
    	try {
        	System.out.printf("El galgo %s ha comenzado la carrera%n", nombre);
        	Thread.sleep(tiempoCarrera * 1000); // Simula el tiempo de carrera
        	System.out.printf("El galgo %s ha llegado en la posición %d%n", nombre, posicion);
        	posicion++; 
    	} catch (InterruptedException e) {
        	e.printStackTrace();
    	}
	}

	public static void main(String[] args) {
    	
    	Galgo galgo1 = new Galgo("Galgo 1", 4);
    	Galgo galgo2 = new Galgo("Galgo 2", 5);
    	Galgo galgo3 = new Galgo("Galgo 3", 3);
    	Galgo galgo4 = new Galgo("Galgo 4", 6);

    	// Iniciar los hilos para cada galgo
    	new Thread(galgo1).start();
    	new Thread(galgo2).start();
    	new Thread(galgo3).start();
    	new Thread(galgo4).start();
	}
}

