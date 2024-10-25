package Tema1_FicherosTexto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LeerFicheroTexto {

	public static void main(String[] args) throws FileNotFoundException, IOException{
		// Se declara descriptor al fichero de texto
		File f = new File("C://Users//Alum_DAM//Desktop//nuevodir3//fichero2.txt");
		
		// Establecer flujo(stream) de entrada con el fichero (lectura de datos)
		FileReader fichlectura = new FileReader(f);
		int i;
		
		// Bucle de lectura de caracteres
		while ((i = fichlectura.read()) != -1) {
			System.out.print((char)i);
		}
		fichlectura.close();
	}

}
