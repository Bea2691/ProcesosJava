package raton;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class SupermercadoSemaforo extends Thread {

	private static Semaphore caja1 = new Semaphore(1); // Número de hilos que adquieren dicho recurso a la vez
	protected int identificador = 0;
    private static Scanner sc = new Scanner(System.in);

    public SupermercadoSemaforo(int identificador) {
        this.identificador = identificador;
    }

    public void run() {
        try {
            caja1.acquire();
            
            
            System.out.println("La persona " + this.identificador +" entra en el super y le van a cobrar");
            Thread.sleep((long) (Math.random() * 2000) + 100); //tiempo simular el paciente en el centro de salud
            caja1.release(); // Paciente sale del centro de salud
            System.out.println("La persona " + this.identificador + " sale de la caja");
        } catch (InterruptedException e) {
            System.out.println("La persona " + this.identificador + " sale de la caja");
            e.printStackTrace();
        }
    } // Fin del método run

    public static void main(String[] args) {
        int pacientes = 0;
        System.out.println("Cuántas personas entran hoy en el supermercado: ");
        pacientes = sc.nextInt();
        for (int i = 1; i <= pacientes; i++) {
            new centroSalud(i).start();
            
        }
    }
}
