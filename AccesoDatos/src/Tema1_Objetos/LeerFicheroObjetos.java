package Tema1_Objetos;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

public class LeerFicheroObjetos {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		Persona persona;
		
		File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//ficheropersona.dat");
		ObjectInputStream dataIS = new ObjectInputStream( new FileInputStream (fichero));
		
		int i =1;
		
		try {
			
			while (true) {
					persona = (Persona) dataIS.readObject();
					System.out.print(i+ "=>");
					i++;
					System.out.print("Nombre: " + persona.getNombre() + ",   \t edad: " + persona.getEdad() + "\n");
			}
		}catch(EOFException e) {
			System.out.println("\nLectura de fichero finalizada");
		}
		dataIS.close();

	}

}
