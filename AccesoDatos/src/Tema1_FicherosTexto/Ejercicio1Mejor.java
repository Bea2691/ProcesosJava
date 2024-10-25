package Tema1_FicherosTexto;
import java.io
.*;
public class Ejercicio1Mejor {
   public static void main (String[] args) {
      
      File d = new File("C://Users//Alum_DAM//Desktop//nuevodir3");
      File f1 = new File(d, "Fichero1.txt");
      File f2 = new File(d, "Fichero2.txt");
      if(d.mkdir()) {
      try{
    	  if (f1.createNewFile())
    	      System.out.println("Fichero1.txt creado correctamente");
    	  else
    	      System.out.println("No se puede crear Fichero1.txt");
    	  if (f2.createNewFile())
    	      System.out.println("Fichero2.txt creado correctamente");
    	  else
    	      System.out.println("No se puede crear Fichero2.txt");
      }catch (IOException e) {e.printStackTrace();}
      
      if (f1.renameTo(new File(d, "Fichero1Nuevo.txt"))) {
    	  System.out.println("Fichero '" + f1.getName() + "' renombrado correctamente como 'Fichero1Nuevo.txt'");
      }else {
    	  System.out.println("No se ha podido renombrar el fichero " + f1.getName());
      }
   }else {
	   System.out.println("El directorio ya existe");
   }
      } 
} 