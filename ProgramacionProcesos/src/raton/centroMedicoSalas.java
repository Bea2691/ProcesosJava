package raton;

import java.util.concurrent.Semaphore;

public class centroMedicoSalas extends Thread{
	private static Semaphore semaforoSala = new Semaphore(1);
	private static Semaphore semaforoSala2 = new Semaphore(1);
	private static Semaphore semaforoSala3 = new Semaphore(1);
	
	private static Semaphore semaforoCentro = new Semaphore(3);
	protected String Persona;
	protected int numeroSala = (int) (Math.random()*3);



public centroMedicoSalas(String persona) {
	super();
	Persona = persona;
}




public void run() {

try {
	semaforoCentro.acquire();
	System.out.println(this.Persona+" ha entrado en el Hospital");
	Thread.sleep((long)(Math.random()*8000)+100);

		switch(this.numeroSala){
			case 0:
			semaforoSala.acquire();
			System.out.println(this.Persona+" ha entrado en la sala de Pediatria");
			Thread.sleep((long)(Math.random()*8000)+100);
			System.out.println(this.Persona+" ha salido de la sala de Pediatria");
			semaforoSala.release();
			case 1:
			semaforoSala2.acquire();
			System.out.println(this.Persona+" ha entrado en la sala de Cardiologia");
			Thread.sleep((long)(Math.random()*8000)+100);
			System.out.println(this.Persona+" ha salido de la sala de Cardiologia");
			semaforoSala2.release();
			break;
			case 2:
			semaforoSala3.acquire();
			System.out.println(this.Persona+" ha entrado en la sala de Cuidados Intensivos");
			Thread.sleep((long)(Math.random()*8000)+100);
			System.out.println(this.Persona+" ha salido de la sala de Cuidados Intensivos");
			semaforoSala3.release();
			break;
			}

		} catch (InterruptedException ex) {
		System.out.println(ex.getMessage());

	}
	System.out.println(this.Persona+" ha salido del Hospital");
	semaforoCentro.release();

}


public static void main(String[] args) throws InterruptedException {
	centroMedicoSalas Paco = new centroMedicoSalas("Paco");
	centroMedicoSalas Navazo = new centroMedicoSalas("Navazo");
	centroMedicoSalas Carlos = new centroMedicoSalas("Carlos");
	centroMedicoSalas Miguel = new centroMedicoSalas("Miguel");
	centroMedicoSalas Maria = new centroMedicoSalas("Maria");
	centroMedicoSalas Bea = new centroMedicoSalas("Bea");
	centroMedicoSalas Lorena = new centroMedicoSalas("Lorena");
	centroMedicoSalas Alejandro = new centroMedicoSalas("Alejandro");
	centroMedicoSalas Aaron = new centroMedicoSalas("Aaron");
	centroMedicoSalas Pablo = new centroMedicoSalas("Pablo");
	centroMedicoSalas David = new centroMedicoSalas("David");
			Paco.start();
			Navazo.start();
			Carlos.start();
			Miguel.start();
			Maria.start();
			Bea.start();
			Lorena.start();
			Alejandro.start();
			Aaron.start();
			Pablo.start();
			David.start();
			try {
				Paco.join();
				Navazo.join();
				Carlos.join();
				Miguel.join();
				Maria.join();
				Bea.join();
				Lorena.join();
				Alejandro.join();
				Aaron.join();
				Pablo.join();
				David.join();
			}catch (InterruptedException e) {
	            e.printStackTrace();
	     }
	}
}