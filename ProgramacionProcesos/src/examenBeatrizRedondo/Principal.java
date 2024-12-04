package examenBeatrizRedondo;

import java.util.Scanner;

/*
 * Hacer un programa que simule la competición por tomar un castillo por dos ejércitos.

La toma del castillo dependerá de las batallas que ganen los caballeros de ambos ejércitos, que lucharán por turnos.

El primer caballero que aseste un golpe con la espada gana el turno. El primer ejercito que gane 3 batallas gana el juego.

Para la simulación, se utilizarán dos hilos que imprimirán por pantalla un identificador de hilo
(Compuesto por tu nombre y un numero de orden: ejemplo DAVID1), el número de veces que lleva ganada la batalla el ejército.

Cuando uno de los hilos llegue a ganar 3 veces, imprimirá además un mensaje "CASTILLO TOMADO POR EL EJERCITOX" 
y finalizará el programa.

Para que ambos hilos tengan igualdad de oportunidades y para simular la capacidad de reacción de un hilo sobre el otro, 
se generará aleatoriamente en cada turno un tiempo de espera diferente a cada hilo de no mas de 1 segundo para asestar el golpe.

Se valorará en este orden la implementación correcta de elementos (5 puntos), 
la correcta ejecución del programa (3 puntos), el orden y claridad de la estructura del programa (1 puntos),
 la documentación interna del programa (1 puntos).
 * 
 * */
public class Principal extends Thread{
	
	public static void main(String[] args) {
	
	
	Monitor juegoTronos = new Monitor(); //Arrancamos el monitor

	new Ejercito1(juegoTronos).start();	//y lanzamos los hilos de ambos ejércitos
	new Ejercito2(juegoTronos).start();
	

	}
}
