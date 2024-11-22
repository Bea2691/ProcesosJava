package Examen1Eva;
import java.util.*;

public class Monitor extends Thread{
	
	
	//atributos del caballero y ejercito 1
	int idCaballero1 = 1;
	String nombreCaballero1;
	static int victoriasEquipo1 = 0;
	int tiempoReaccion1 = 0;
	
	//atributos del caballero y ejercito 2
	int idCaballero2 = 1;
	String nombreCaballero2;
	static int victoriasEquipo2 = 0;
	int tiempoReaccion2 = 0;
	
	Monitor(){
		
		this.idCaballero1 = idCaballero1;
		this.nombreCaballero1 = nombreCaballero1;
		this.idCaballero2 = idCaballero2;
		this.nombreCaballero2 = nombreCaballero2;
		
	}
	
	public synchronized void ataque() throws InterruptedException {
		
		//Control de errores para que solo pida los nombres de los ejercitos la primera vez
		if(idCaballero1 == 1 && idCaballero2 == 1) {
			
			Scanner teclado = new Scanner(System.in);
			System.out.println("Nombre del ejercito 1:");
			nombreCaballero1 = teclado.next();
			System.out.println("Nombre del ejercito 2:");
			nombreCaballero2 = teclado.next();
			
		}
		
		//tiempo de reaccion aleatorizado de cada caballero
		tiempoReaccion1 = (int) (Math.random()*1000+1);
		tiempoReaccion2 = (int) (Math.random()*1000+1);
		
		//Mostrar por pantalla el tiempo de reaccion de cada caballero
		System.out.println("tiempo de reacción del caballero " + this.nombreCaballero1 + ": " + tiempoReaccion1);
		System.out.println("tiempo de reacción del caballero " + this.nombreCaballero2 + ": " + tiempoReaccion2);
		
		//Mostrar condicion en caso de que el caballero del ejercito 1 gane
		if(tiempoReaccion1 > tiempoReaccion2) {
			
			//Mostrar que caballero ha ganado y el marcador
			System.out.println("Gana el caballero " + this.nombreCaballero1 + this.idCaballero1);
			victoriasEquipo1++;
			System.out.println("Marcador:" + this.nombreCaballero1 + " " + victoriasEquipo1 + ":" + victoriasEquipo2 + " " + this.nombreCaballero2);			
			
			//Mostrar si el ejercito 1 ha tomado el castillo en caso de que tenga 3 victorias acumuladas
			//Y paramos el programa
			if(victoriasEquipo1 == 3) {
					
				System.out.println("CASTILLO TOMADO POR EL EJERCITO " + this.nombreCaballero1);
				System.exit(0);

			}	
		
		//Mostrar la condicion en caso de que el caballero del ejercito 2 gane
		}else if(tiempoReaccion2 > tiempoReaccion1){
			
			//Mostrar que caballero ha ganado y el marcador
			System.out.println("Gana el caballero " + this.nombreCaballero2 + this.idCaballero2);
			victoriasEquipo2++;
			System.out.println("Marcador:" + this.nombreCaballero1 + " " + victoriasEquipo1 + ":" + victoriasEquipo2 + " " + this.nombreCaballero2);
			
			//Mostrar si el ejercito 2 ha tomado el castillo en caso de que tenga 3 victorias acumuladas
			//Y paramos el programa
			if(victoriasEquipo2 == 3) {
					
				System.out.println("CASTILLO TOMADO POR EL EJERCITO " +nombreCaballero2);
				System.exit(0);

			}				
		}	
			
			//Aumentar uno el id de los 2 caballeros para la siguiente batalla
			idCaballero1++;
			idCaballero2++;
	}
	
}
