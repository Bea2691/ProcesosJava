package Examen1Eva;


public class Principal extends Thread{
	
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		Monitor guerra = new Monitor();
		
		//Lanzar hilos
		new Ejercito1(guerra).start();
		new Ejercito2(guerra).start();
		
	}

}
