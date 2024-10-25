package Tema1_FicherosTexto;

import java.io.*;
public class VerDir2 {
   public static void main (String[] args) {
      

      File f = new File("C:\\Windows\\regedit.exe");
      if (f.exists())
      {
    	 System.out.println("Información Sobre El Fichero");
         System.out.println("Nombre del fichero: "+f.getName());
         System.out.println("Ruta              : "+f.getPath());
         System.out.println("Se puede escribir : "+f.canWrite());
         System.out.println("Tamaño            : "+(f.length()/1024) + " MB");
         System.out.println("Es un directorio  : "+f.isDirectory());
         System.out.println("Es un fichero     : "+f.isFile());
      }
      else
      {
    	  System.out.println("El Fichero "+f.getName()+" no existe");
      }
   }
} 