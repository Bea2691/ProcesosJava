package Tema1_FicherosTexto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class EscribirFicheroTextoBuffer {

	public static void main(String[] args) {
		//Descriptor
		File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//fichero2.txt");
		//Stream(flujo)
		try {
			FileWriter fic = new FileWriter(fichero, true); // con el true no sobreescribe, añade al final el texto nuevo
			BufferedWriter ficbuf = new BufferedWriter(fic);
			ficbuf.newLine(); //Salto de linea para que no añada desde la linea 0, sino de la siguiente
			for(int i=1; i<11; i++) {
				ficbuf.write("Fila numero: " + i);
				//Escribir un salto de linea para que cada añadir sea en una linea diferente.
				ficbuf.newLine();
			}
			//vaciamos el buffer
			ficbuf.flush(); //para forzar volcar la info al fichero
			ficbuf.close();
		} catch(FileNotFoundException fnf) {
			System.out.println("Error al escribir");
		}catch (IOException e) {
			System.out.println("Fichero no encontrado");
			
		
		}

	}
}
