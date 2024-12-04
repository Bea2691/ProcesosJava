package examenBeatrizRAccesoDatos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ejercicio2 {
	public static void main(String[] args) {
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection(
		    	    "jdbc:mysql://sql.freedb.tech/freedb_ejemplo", 
		    	    "freedb_ejemplo", 
		    	    "kz4ZA%hXjY&26jG"
		    	);
		   	 
						
			DatabaseMetaData dbmd = conexion.getMetaData();
			ResultSet resul, resul2;
			
		
			String usuario = dbmd.getUserName();
			

			System.out.println("==========================================");
			System.out.println("INFORMACION GENERAL SOBRE LA BASE DE DATOS");
			System.out.println("==========================================");
			System.out.println("Usuario: " + usuario);
			System.out.println("");
			
			
			
			//Descripción de los elementos de la base de datos: el nombre y el tipo de elemento
			resul = dbmd.getTables(null, "ejemplo", null, null);
			
			System.out.println("===================================================");
			System.out.println("INFORMACION SOBRE LOS ELEMENTOS DE LA BASE DE DATOS");
			System.out.println("===================================================");
			
			
			
			while(resul.next()) {
				
				String tabla = resul.getString(3);
				String tipo = resul.getString(4);
				System.out.printf("Tipo: %s - Nombre: %s%n", tipo, tabla);
				
				
			}
			
			//Mostrar de las tablas su contenido: campo llamado..., tipo de de elemento... y tamaño del elemento...
			resul = dbmd.getColumns(null, "ejemplo", null, null);
			System.out.println("");
			System.out.println("==================================================");
			System.out.println("INFORMACION SOBRE CAMPOS DE LOS ELEMENTOS DE LA BD");
			System.out.println("==================================================");
			while(resul.next()) { //En el ejercicio pide nombre del campo, tipo y tamaño, pero he puesto tambien tabla porque
				String tabla = resul.getString(3);	//hay dos tablas en la BD y tienen campos con el mismo nombre, como "id"
				String campo = resul.getString(4);
				String tipo = resul.getString(6);
				String tamaño = resul.getString(7);
				
				System.out.printf("\nTabla: %s - Campo: %s - Tipo: %s - Tamaño: %s", tabla, campo, tipo, tamaño);

				
				
			}
			
		
			conexion.close();
		
		} catch (ClassNotFoundException e) {
			System.out.println("No encuentra el controlador");
		} catch (SQLException e) { 
			
			System.out.println("Código de error:  " + e.getErrorCode());
			System.out.println("SQLState:         " + e.getSQLState());
			System.out.println("Mensaje:          " + e.getMessage());
			
			
		}

	}

}
