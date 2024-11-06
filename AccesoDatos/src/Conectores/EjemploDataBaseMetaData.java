package Conectores;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EjemploDataBaseMetaData {
	
	

	public static void main(String[] args) {
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/ejemplo", "root","");
			
			
			//getMetaData() --> Objeto de DataBaseMetaData, con informacion sobre la base de datos de la conexion realizada
			
			DatabaseMetaData dbmd = conexion.getMetaData();
			ResultSet resul, resul2;
			
			String nombre = dbmd.getDatabaseProductName();
			String driver = dbmd.getDriverName();
			String url = dbmd.getURL();
			String usuario = dbmd.getUserName();
			

			System.out.println("INFORMACION GENERAL SOBRE LA BASE DE DATOS");
			System.out.println("==========================================");
			System.out.println("Nombre:  " + nombre);
			System.out.println("Driver:  " + driver);
			System.out.println("URL:     " + url);
			System.out.println("Usuario: " + usuario);
			
			//Información de las tablas y las vistas de la BD:
			//Descripción de las tablas del catálogo,esquema o tabla indicados.
			resul = dbmd.getTables(null, "ejemplo", null, null);
			
			System.out.println("=====================================");
			System.out.println("INFORMACION SOBRE LAS TABLAS DE LA BD");
			System.out.println("=====================================");
			
			while(resul.next()) {
				String catalogo = resul.getString(1); //esto va en orden en el pdf: table_cat en la tabla corresponde a (1)
				String tabla = resul.getString(3);
				String tipo = resul.getString(4);
				System.out.printf("Tipo: %s - Catalogo: %s - Nombre: %s", tipo, catalogo, tabla);
				
			}
			//Mostrar las tablas
			resul = dbmd.getColumns(null, "ejemplo", null, null);
			
			while(resul.next()) {
				String tabla = resul.getString(3);
				String campo = resul.getString(4);
				String tipo = resul.getString(6);
				String tamaño = resul.getString(7);
				int nulos = resul.getInt(11);
				int numCol = resul.getInt(17);
				System.out.printf("\nTabla: %s - Campo: %s - Tipo: %s - Tamaño: %s - Nulos: %d - Número columna: %d", tabla, campo, tipo, tamaño, nulos, numCol);

				
				
			}
			
			
			resul= dbmd.getPrimaryKeys(null, null, "departamentos");
		//	resul2= dbmd.getPrimaryKeys(null, null, "empleados"); //No la tenemos no te rayes
			resul.next();
		//	resul2.next();
			
			String tabla = resul.getString(3);
			String columna = resul.getString(4);
			String clavepk = resul.getString(6);
			System.out.printf("\nTabla: %s - Columna: %s - Nombre Clave Primaria: %s", tabla, columna, clavepk);
			
			
			
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
