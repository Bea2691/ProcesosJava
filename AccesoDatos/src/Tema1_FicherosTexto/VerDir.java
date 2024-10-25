package Tema1_FicherosTexto;

import java.io.*;
public class VerDir {
   public static void main (String[] args) {
      System.out.println("Ficheros en el directorio actual"); 
      File f = new File("c://windows");
      String[] archivos=f.list();  //Listar ficheros de un directorio
      for (int i=0;i<archivos.length;i++){
         System.out.println(archivos[i]);
      }
   }
} 