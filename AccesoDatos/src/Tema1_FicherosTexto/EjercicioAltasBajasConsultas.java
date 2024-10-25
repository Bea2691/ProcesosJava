package Tema1_FicherosTexto;

import java.io.*;
import java.util.Scanner;

public class EjercicioAltasBajasConsultas {

    public static void main(String[] args) throws IOException, FileNotFoundException {
        Scanner sc = new Scanner(System.in);

        String nombre, dni, apellido, curso;
        int opcion;
        File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//ejercicioAltas.txt");

        do {
            System.out.println("1-Alta alumno");
            System.out.println("2-Baja alumno");
            System.out.println("3-Consultar alumno");
            System.out.println("4-Salir");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Introduzca el DNI del alumno");
                    dni = sc.next().toUpperCase();
                    System.out.println("Introduzca el nombre del alumno");
                    nombre = sc.next().toUpperCase();;
                    System.out.println("Introduzca el apellido del alumno");
                    apellido = sc.next().toUpperCase();;
                    System.out.println("Introduzca el curso del alumno");
                    curso = sc.next().toUpperCase();
                    
                    FileWriter fic = new FileWriter(fichero, true);
                    BufferedWriter ficbuf = new BufferedWriter(fic);

                    ficbuf.write(dni + " " + nombre + " " + apellido + " - " + curso);
                    ficbuf.newLine();

                    ficbuf.flush();
                    ficbuf.close();
                    break;

                case 2:
                    System.out.println("Introduzca el dni del alumno, no se equivoque");
                    dni = sc.next().toUpperCase();
                    String linea2;
                    boolean encontrado2 = false;

                    File ficheroTemp = new File("C://Users//Alum_DAM//Desktop//nuevodir3//ejercicioAltas_temp.txt");
                    FileReader fich2 = new FileReader(fichero);
                    BufferedReader ficbufread2 = new BufferedReader(fich2);
                    FileWriter fichTemp = new FileWriter(ficheroTemp);
                    BufferedWriter ficbufTemp = new BufferedWriter(fichTemp);

                    
                    while ((linea2 = ficbufread2.readLine()) != null) {
                        if (!linea2.startsWith(dni + " ")) {
                            ficbufTemp.write(linea2);
                            ficbufTemp.newLine();
                        } else {
                            encontrado2 = true; 
                        }
                    }

                    ficbufread2.close();
                    ficbufTemp.flush();
                    ficbufTemp.close();

                    
                    if (encontrado2) {
                        fichero.delete();  
                        ficheroTemp.renameTo(fichero);  
                        System.out.println("Alumno eliminado correctamente");
                    } else {
                        ficheroTemp.delete();  
                        System.out.println("Alumno inexistente");
                    }
                    break;


                case 3:
                    System.out.println("Introduzca el dni del alumno, no se equivoque");
                    dni = sc.next().toUpperCase();
                    String linea;
                    boolean encontrado = false;
                    FileReader fich = new FileReader(fichero);
                    BufferedReader ficbufread = new BufferedReader(fich);

                    
                    while ((linea = ficbufread.readLine()) != null) {
                        if (linea.startsWith(dni + " ")) {  
                            System.out.println("Alumno encontrado: " + linea.substring(9,linea.length()));
                            encontrado = true;
                            break;
                        }
                    }
                  
                    if (!encontrado) {
                        System.out.println("Alumno inexistente");
                    }
                    ficbufread.close();
                    break;

                case 4:
                    System.out.println("Gracias y un saludete.");
                    break;

                default:
                    System.out.println("Opción de menú no disponible.");
            }

        } while (opcion != 4);
    }
}

