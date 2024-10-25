package Tema1_FicherosTexto;

import java.io.*;
import java.util.Scanner;
public class Ejercicio1 {

	public static void main(String[] args) {
		 File d = new File("C://Users//Alum_DAM//Desktop//nuevodir3");
		 File f = new File(d, "fichero");
		 File f2 = new File(d, "fichero2");
		 
		 
		 
		 if(d.exists()) {
			 
			 System.out.println("El directorio ya existe");
			 
		 }
		 else {
			 d.mkdir();
			 System.out.println("El nuevo directorio es " + d.getName());
			 try {
				 f.createNewFile();
				System.out.println("El nuevo archivo es " + f.getName());
			} catch (IOException e) {
				
				e.printStackTrace();
				System.out.println( "No se puede crear" + f.getName());
			}
			 try {
				 f2.createNewFile();
					System.out.println("El nuevo archivo es " + f2.getName());
					f2.renameTo(new File(d , "nuevo2"));
					System.out.println("El archivo " + f2.getName()+ " ha sido renombrado");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		 }
	}

}
