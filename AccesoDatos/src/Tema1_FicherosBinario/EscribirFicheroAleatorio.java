package Tema1_FicherosBinario;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class EscribirFicheroAleatorio {

	public static void main(String[] args) throws IOException {
		File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//empleados.dat");
		//Declaracion del fichero de acceso aleatorio
		
		RandomAccessFile file= new RandomAccessFile(fichero, "rw");	//Lectura y escritura
		
		//Arrays con los datos
		int dep[]= {10,20,10,10,30,30,20};
		String apellidos[] = {"FERNANDEZ","GARCIA","GIL","LOPEZ","REDONDO","GOMEZ", "SANZ"};
		double salario[]= {1200.45,2400.60,3000.0,1500.56,2700.25,3700.45,1750.65};
		
		StringBuffer buffer = null; //Buffer para almacenar los apellidos
		int n= apellidos.length; //Numero de elementos del array
		
		for(int i=0;i<n; i++) {
			file.writeInt(i+1); //Identificador del empleado
			
			buffer = new StringBuffer(apellidos[i]);
			buffer.setLength(10); //10 Caracteres para el apellido siempre
			file.writeChars(buffer.toString()); //Inserta apellidos
			
			file.writeInt(dep[i]); //Inserta departamento
			file.writeDouble(salario[i]);
			
			
		}
		file.close();
		
	}

}
