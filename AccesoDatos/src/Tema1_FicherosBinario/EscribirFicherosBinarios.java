package Tema1_FicherosBinario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EscribirFicherosBinarios {

	public static void main(String[] args) throws IOException {
		File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//ficherobinario.dat"); //fichero binario .dat o .bin
		
		//Stream salida
		
		FileOutputStream fileout = new FileOutputStream(fichero, true);
		
		//Stream entrada
		
		FileInputStream filein = new FileInputStream (fichero);
		
		int i;
		
		for (i=1; i<100;i++) {
			//Escribir datos en el flujo de salida
			fileout.write(i);
			
		
		}
		//Leer el fichero
		while ((i= filein.read()) != -1) {
			System.out.println(i);
		}
		fileout.close();
		filein.close();
	}

}
