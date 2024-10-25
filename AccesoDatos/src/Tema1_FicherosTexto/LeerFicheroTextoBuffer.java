package Tema1_FicherosTexto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LeerFicheroTextoBuffer {

	public static void main(String[] args) throws IOException {
		//Descriptor		//Con modo BUFFER se lee linea a linea del archivo, con el otro modo es caracter a caracter
		File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//fichero.txt");
		//Stream
		FileReader fic = new FileReader(fichero);
		//Buffer
		BufferedReader ficbuf = new BufferedReader(fic);
		String linea;
		int i=1;
		while((linea = ficbuf.readLine()) != null) {
			System.out.print("Linea: " + i + " - ");
			i++;
			System.out.println(linea);
		}
		ficbuf.close();
		
	}

}
