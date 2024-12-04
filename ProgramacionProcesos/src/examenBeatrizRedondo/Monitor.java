package examenBeatrizRedondo;

import java.util.Scanner;

public class Monitor {
	
	
	 
	private int idCaballero1 = 1; //id del jugador para el "numero de orden", que en el print yo llamo "turno"
	private String nombreCaballero1; //Nombre que introduce el participante 1
	private static int batallaGanada1 = 0;	//Para ir incrementando las batallas que gana el 1
	private int tiempoCaballero1 = 0;	//Para almacenar el tiempo random que se genera en cada turno
	
	
	private int idCaballero2 = 1;	//Lo mismo para el jugador 2
	private String nombreCaballero2;
	private static int batallaGanada2 = 0;
	private int tiempoCaballero2 = 0;
	
	Scanner sc = new Scanner(System.in);
	
	
	public Monitor(){ //Constructor
		
		this.idCaballero1= idCaballero1;
		this.idCaballero2=idCaballero2;
		this.nombreCaballero1=nombreCaballero1;
		this.nombreCaballero2=nombreCaballero2;
				
		
	}
	
	
	//metodo para gestionar el ataque de los usuarios, usando monitores, "synchronized"
	public synchronized void ataque() { 
		
		if(idCaballero1 == 1 && idCaballero2 == 1) { //Estan inicializadas en 1,
													 //esto es para que lo pregunte solo una vez no en cada ataque
			System.out.println("Nombre del Castillo 1:");
			nombreCaballero1 = sc.next().toUpperCase();			//Pide al usuario que introduzca el nombre
			System.out.println("Nombre del Castillo 2:"); 
			nombreCaballero2 = sc.next().toUpperCase();
			
		}
		while(batallaGanada1 <3 && batallaGanada2 <3) { //Bucle para que mientras las victorias no sean mayor a 3 sigan atacando
		
			
			tiempoCaballero1 = (int) (Math.random()*1000+1); //Generamos aleatoriamente un numero (Se pide que se menor de 1s)
			tiempoCaballero2 = (int) (Math.random()*1000+1); //Lo mismo para el segundo jugador
			
			System.out.println("Tiempo que tarda-> " + nombreCaballero1 + " <-en asestar el golpe: " + tiempoCaballero1);
			System.out.println("Tiempo que tarda-> " + nombreCaballero2 + " <-en asestar el golpe: " + tiempoCaballero2);
			
			//Si el jugador1 es más rapido que el jugador2
			if(tiempoCaballero1 < tiempoCaballero2) {
				
				
				System.out.println("Ha ganado esta batalla " + nombreCaballero1 + ", turno:" + idCaballero1);
				batallaGanada1++;
				System.out.println("Marcador actual:" + nombreCaballero1 + " " + batallaGanada1 + ":" + batallaGanada2 + " " + nombreCaballero2);			
				
								
				if(batallaGanada1 == 3) {
						
					System.out.println("Castillo tomado por el Ejercito de: " + nombreCaballero1);
				

				}
			//si el jugador 2 es más rápido que el jugador 1
			}else if(tiempoCaballero2 < tiempoCaballero1) {
				
				System.out.println("Ha ganado esta batalla " + nombreCaballero2 + ", turno:" + idCaballero2);
				batallaGanada2++;
				System.out.println("Marcador actual:" + nombreCaballero1 + " " + batallaGanada1 + ":" + batallaGanada2 + " " + nombreCaballero2);			
				
							
				if(batallaGanada2 == 3) {
						
					System.out.println("Castillo tomado por: " + nombreCaballero2);
			

				}
			}
			idCaballero1++; //Aumentamos el contador id de ambos jugadores para saber en que turno estamos
			idCaballero2++;
		}
	}

	

}
