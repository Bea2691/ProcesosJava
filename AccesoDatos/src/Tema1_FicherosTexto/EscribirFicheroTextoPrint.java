package Tema1_FicherosTexto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EscribirFicheroTextoPrint {

	public static void main(String[] args) {
		//Descriptor
		File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//fichero2.txt");
		//Stream(flujo)
		try {
			FileWriter fic = new FileWriter(fichero, true); // con el true no sobreescribe, añade al final el texto nuevo
			PrintWriter ficprint = new PrintWriter(fic);
			
			for(int i=1; i<11; i++) {
				ficprint.println("Fila print numero: " + i);
				//Escribir un salto de linea para que cada añadir sea en una linea diferente.
				
			}
			//vaciamos el buffer
			ficprint.flush();
			ficprint.close();
		} catch(FileNotFoundException fnf) {
			System.out.println("Error al escribir");
		}catch (IOException e) {
			System.out.println("Fichero no encontrado");
			
		
		}

	}
}
