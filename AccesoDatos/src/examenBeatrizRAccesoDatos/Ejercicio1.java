package examenBeatrizRAccesoDatos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

public class Ejercicio1 {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		//Conexión con la base de datos
		Class.forName("com.mysql.jdbc.Driver"); //bd: alumnos
        Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/alumnos", "root", "");
      
        Scanner sc = new Scanner(System.in);

        int opcion = 0;
        
        do {
        	menu();
        	opcion = sc.nextInt();
        	
        	switch(opcion) {
        	//Case para buscar al alumno
        	case 1: 
                System.out.println("Introduzca DNI para buscar alumno");
                String DNI = sc.next();

                String buscarDNI = "SELECT * FROM alumnos WHERE dni = ?"; //consulta para buscar
                try (PreparedStatement psBuscar = conexion.prepareStatement(buscarDNI)) { //se prepara la sentencia y se le pasa la variable dni1
                    psBuscar.setString(1, DNI);
                    ResultSet rsBuscarDNI = psBuscar.executeQuery(); //Ejecuta la sentencia

                    if (rsBuscarDNI.next()) {
                        do {
                            String dni = rsBuscarDNI.getString(1); //Obtiene los datos de la BD y los garda en las variables declaradas
                            String nombre = rsBuscarDNI.getString(2);
                            String especialidad = rsBuscarDNI.getString(3);
                            String curso = rsBuscarDNI.getString(4);		//Imprimimos los datos del alumno
                            System.out.printf(" - DNI: %s%n - Nombre: %s%n - Especialidad: %s%n - Curso: %s%n",dni, nombre, especialidad, curso);
                        } while (rsBuscarDNI.next());
                    } else {
                        System.out.println("ERROR. Este DNI no existe");
                    }
                }
                break;
            //Case para cambiar el curso al alumno
        	case 2:
        		System.out.println("Introduzca el DNI del alumno al que desea cambia el CURSO");
        		String dni2= sc.next();
        		

                System.out.println("Introduzca el curso al que desea cambiar al alumno");
                String cursoNuevo = sc.next().toUpperCase(); //Pedimos el nuevo curso

                if (cursoNuevo.equals("PRIMERO") || cursoNuevo.equals("SEGUNDO") || cursoNuevo.equals("TERCERO")) {
                	
                	//Preparamos la sentencia conPreparedStatement y la ejecutamos
                    String cambiarCurso = "UPDATE alumnos SET curso = ? WHERE dni = ?";
                    try (PreparedStatement psCambiar = conexion.prepareStatement(cambiarCurso)) {
                        psCambiar.setString(1, cursoNuevo); //cada ? de la sentencia mysql se sustituye por las varaibles nuevas con setString
                        psCambiar.setString(2, dni2);
                        int filas = psCambiar.executeUpdate();
                        if (filas > 0) {
                            System.out.println("Curso cambiado con éxito");
                        } else {
                            System.out.println("NO existe alumno con ese DNI");
                        }
                    }
                } else {
                    System.out.println("Curso inexistente. Introduzca: Primero, Segundo o Tercero.");
                }
                break;

        	//Case para cambiar la especialidad al alumno
        	case 3: 
        		System.out.println("Introduzca DNI del alumno al que le desea cambiar la especialidad");
	            String dni3 = sc.next();
	
	            System.out.println("Introduzca especialidad: DAM, SAW, ASIR, SMR o CAE");
	            String especialidadNueva = sc.next().toUpperCase(); //Pedimos la nueva especialidad
	
	            if (especialidadNueva.equals("DAM") || especialidadNueva.equals("DAW") || especialidadNueva.equals("ASIR")
	                    || especialidadNueva.equals("SMR") || especialidadNueva.equals("CAE")) {
	
	                String procedimiento = "{CALL cambiarespecialidad3(?, ?)}"; //Llamamos al procedimiento creado en la base de datos
	                try (CallableStatement llamada = conexion.prepareCall(procedimiento)) { 
	                    llamada.setString(1, dni3);
	                    llamada.setString(2, especialidadNueva); //Le pasamos las variables introducidas por el usuario dni y especialidad
	
	                    int filas = llamada.executeUpdate();
	                    if (filas > 0) { 	
	                        System.out.printf("Especialidad cambiada para %d alumno(s).%n", filas);
	                    } else {
	                        System.out.println("Alumno no existente.");
	                    }
	                }
	            } else {
	                System.out.println("Especialidad no existente.");
	            }
            break;
        	case 4:
                System.out.println("Adios!!!!");
                break;

            default:
                System.out.println("Opción de menú incorrecta");
        	}
        	       	     	
        	
        }while(opcion!=4);
		
        //Cerramos todas las conexiones
      
        conexion.close();
        sc.close();
	}
	public static void menu() {
		System.out.println("Selecciona una opción del menú:");
    	System.out.println("1 - Buscar al alumno por DNI");
    	System.out.println("2 - Cambiar el curso del alumno");
    	System.out.println("3 - Cambiar la especialidad del alumno");
    	System.out.println("4 - Salir");
	}
}


