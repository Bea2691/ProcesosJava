package Tema1_Objetos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class EscribirFicheroObjetos {

	public static void main(String[] args) {
		Persona persona;
		
		File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//ficheropersona.dat");
		
		//Stream
		try {
			FileOutputStream ficherosalida = new FileOutputStream(fichero);
			
			
			//Conecta el flujo de byte al flujo de datos
			
			ObjectOutputStream dataOS = new ObjectOutputStream(ficherosalida);
			
			String nombres []={"Lorena","Bea","Alex","Marlon","Ivan","Jorge","David"};
			int edades []= {14,15,16,17,18,19,20};
			
			System.out.println("Grabando lso datos de las personas");
			
			for(int i=0; i<edades.length;i++) {
				persona = new Persona(nombres[i],edades[i]);
				dataOS.writeObject(persona);
			}
			System.out.println("Datos de las personas guardados perfectamente");
			
			
		} catch (FileNotFoundException e) {
			System.out.println("No se encuentra el fichero");
			
		} catch (IOException e) {
			
			System.out.println("Error de acceso al fichero");
		}

	}

}
