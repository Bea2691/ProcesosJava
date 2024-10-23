package raton;

public class cRaton implements Runnable {
	protected String nombre;
	protected int tiempoAlimentacion;
	
	public cRaton(String nombre, int tiempo) {
		this.nombre=nombre;
		this.tiempoAlimentacion=tiempo;
	}
	
	public void run() {
		try {
			System.out.printf("El raton %s ha comenzado a alimentarse%n", nombre);
			Thread.sleep(tiempoAlimentacion *1000); //pausa la ejecucion del hilo
			System.out.printf("El raton %s ha terminado de alimentarse%n", nombre);
		
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		cRaton fievel = new cRaton("Fievel", 4);
		cRaton jerry = new cRaton("Jerry", 5);
		cRaton pinky = new cRaton("Pinky", 3);
		cRaton mickey = new cRaton("Mickey", 6);
		new Thread(fievel).start();
		new Thread(jerry).start();
		new Thread(pinky).start();
		new Thread(mickey).start();
		//Cada raton comienza a comer de inmediato
}
	}