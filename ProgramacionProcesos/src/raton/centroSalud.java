package raton;

	
import java.util.Scanner;
import java.util.concurrent.Semaphore;

	public class centroSalud extends Thread {

	    private static Semaphore centSal = new Semaphore(5); // Número de hilos que adquieren dicho recurso a la vez
	    protected int identificador = 0;
	    private static Scanner sc = new Scanner(System.in);

	    public centroSalud(int identificador) {
	        this.identificador = identificador;
	    }

	    public void run() {
	        try {
	            centSal.acquire(); // Hilo adquiere el semáforo o testigo
	            System.out.println("El paciente " + this.identificador + " entra en el centro de salud");
	            Thread.sleep((long) (Math.random() * 2000) + 100); //tiempo simular el paciente en el centro de salud
	            centSal.release(); // Paciente sale del centro de salud
	            System.out.println("El paciente " + this.identificador + " sale del centro de salud");
	        } catch (InterruptedException e) {
	            System.out.println("El paciente " + this.identificador + " sale del centro de salud");
	            e.printStackTrace();
	        }
	    } // Fin del método run

	    public static void main(String[] args) {
	        int pacientes = 0;
	        System.out.println("Cuántos pacientes tienen cita en el día de hoy: ");
	        pacientes = sc.nextInt();
	        for (int i = 1; i <= pacientes; i++) {
	            new centroSalud(i).start();
	        }
	    }
	}
