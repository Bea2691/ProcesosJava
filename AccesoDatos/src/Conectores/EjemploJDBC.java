package Conectores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EjemploJDBC {

	public static void main(String[] args) {
	
		try {
			//Cargar el driver con el metodo forname() de la clase Class.forname
			//Se le pasa como argumento una cadena con el nombre de la clase del driver, mysql en este caso
			Class.forName("com.mysql.jdbc.Driver");
			
			//Establecer la conexion con la base de datos: url, usuario y passwd
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/ejemplo", "root","");
			
			//Preparar la sentencia, en este caso una consulta
			Statement sentencia = conexion.createStatement();
			
			
			String sql = "Select * from departamentos";
			//Se ejecuta la sentencia, un Select como executeQuery y se recuperan los datos con Resulset
			//Internamente tiene un puntero al primer registro de la tabla de la BD
			ResultSet resul = sentencia.executeQuery(sql);
			//Recorrer el resutlado para visualizar las filas
			//Inicialmente el puntero esta antes del primer registro de la tabla
			while(resul.next())
				System.out.printf("%d,%s,%s,%n", resul.getInt("dept_no"), resul.getString("dnombre"), resul.getString("loc"));
			resul.close();
			sentencia.close();
			conexion.close();
			
		//Importar la libreria en el bluid path no olvidar o dara el ClassNotFoundException
		} catch (ClassNotFoundException e) {
			System.out.println("No encuentra el controlador");
		} catch (SQLException e) { 
			/*getMessaje(): Descripcion del mensaje
			 *getSQLState(): Codigo SQL estandar que identidica el error producido
			 *getErrorCode(): Código de error que lanza la BD, depende del proveedor
			 *getCause(): Devuelve una lista de objetos que han provocado el error
			 *getNextExcception(): Cadena de excepciones que se han producido
			 */
			System.out.println("Código de error:  " + e.getErrorCode());
			System.out.println("SQLState:         " + e.getSQLState());
			System.out.println("Mensaje:          " + e.getMessage());
			
			
			//El resultado del metodo getCause() es un objeto Throwable
			//Ahi aparecen todas las causas que han generado el error
			/*Throwable t = e.getCause();
			while(t != null) {
				System.out.println("Causa: " + t + "\n");
				t = t.getCause();
			}*/
			
		}

	}

}
