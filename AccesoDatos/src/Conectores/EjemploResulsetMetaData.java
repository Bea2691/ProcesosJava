package Conectores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class EjemploResulsetMetaData {

	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/ejemplo", "root","");
			
			Statement sentencia = conexion.createStatement();
			Statement sentencia2 = conexion.createStatement();
			ResultSet rs= sentencia.executeQuery("Select * from departamentos");
			ResultSet rs2= sentencia2.executeQuery("Select * from empleados");
			
			
			//getMetaData()  Devuelve un objeto ResulsetMetaData con información sobre la BD del objeto de la conexión connection
			ResultSetMetaData rsmd = rs.getMetaData();
			ResultSetMetaData rsmd2 = rs2.getMetaData();
			
			//Numero de columnas del objeto Resulset
			int ncolumnas = rsmd.getColumnCount();
			int ncolumnas2 = rsmd2.getColumnCount();
			
			System.out.printf("Nombre de la tabla: %s  %n", rsmd.getTableName(3) );
			System.out.printf("Número de columnas recuperadas: %d %n", ncolumnas );
			System.out.printf("================================================%n");
			
			String nulos;
			for(int i=1; i<=ncolumnas; i++) {
				System.out.println("Columna: " + i);
				//Nombre de la columna y su tipo
				System.out.printf("Nombre: %s %n Tipo: %s %n", rsmd.getColumnName(i), rsmd.getColumnTypeName(i));
				
				//Admite nulos?
				if(rsmd.isNullable(i) == 0) {
					nulos = "NO";
					
				}else nulos= "SI";
				System.out.printf("Puede ser nula?: %s%n", nulos);
				System.out.printf("Ancho máximo de la columna: %d%n", rsmd.getColumnDisplaySize(i));
				
			}
			
			System.out.printf("%n%n");
			
			System.out.printf("Nombre de la tabla: %s  %n", rsmd2.getTableName(3) );
			System.out.printf("Número de columnas recuperadas: %d %n", ncolumnas2 );
			System.out.printf("================================================%n");
			
			
			for(int i=1; i<=ncolumnas2; i++) {
				System.out.println("Columna: " + i);
				//Nombre de la columna y su tipo
				System.out.printf("Nombre: %s %n Tipo: %s %n", rsmd2.getColumnName(i), rsmd2.getColumnTypeName(i));
				
				//Admite nulos?
				if(rsmd2.isNullable(i) == 0) {
					nulos = "NO";
					
				}else nulos= "SI";
				System.out.printf("Puede ser nula?: %s%n", nulos);
				System.out.printf("Ancho máximo de la columna: %d%n", rsmd2.getColumnDisplaySize(i));
				
			}
			
			
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}

}
