package Tema1_FicherosTexto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EscribirFicheroTexto {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File f = new File("C://Users//Alum_DAM//Desktop//nuevodir3//fichero.txt");
		
		FileWriter fichescritura = new FileWriter(f);
		
		String palabra = "Bea ten un buen lunes";
		
		char[] cad = palabra.toCharArray(); //Convierte un string en un array de caracteres
		
		for (int i=0; i<cad.length; i++) {
			fichescritura.write(cad[i]);
		}
		fichescritura.append('*');
		fichescritura.close();
	}

}
