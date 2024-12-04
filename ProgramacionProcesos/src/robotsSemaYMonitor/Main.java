package robotsSemaYMonitor;

public class Main {
	public static void main(String[] args) { 
		Mesa mesa = new Mesa(); Montador montador = new Montador(mesa);
		Robot robotA = new Robot(mesa, "TipoA"); 
		Robot robotB = new Robot(mesa, "TipoB"); 
		Robot robotC = new Robot(mesa, "TipoC"); 
		montador.start(); 
		robotA.start(); 
		robotB.start(); 
		robotC.start(); }
	}