package Conectores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class EjemploSentenciasPrep {

	public static void main(String[] args) {
		
		int dpto, filas;
		String dnombre, dloc ,d;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/ejemplo", "root","");
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("Número de departamento: ");
			dpto = sc.nextInt();
			sc.nextLine();
			
			System.out.print("Nombre del departamento: ");
			dnombre= sc.nextLine();
			
			
			System.out.print("Localidad: ");
			dloc = sc.nextLine();
			
			
			//Construir la sentencia preparada
			String sql = "Insert into departamentos values (?,?,?)";
			
			System.out.println("Sentencia Compilada: " + sql);
			
			PreparedStatement sentencia = conexion.prepareStatement(sql);
			sentencia.setInt(1, dpto);
			sentencia.setString(2, dnombre);
			sentencia.setString(3, dloc);
			
			filas = sentencia.executeUpdate();
			System.out.println("Filas afectadas" + filas);
			
			sentencia.close();
			conexion.close();
			sc.close();
			
			
			
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
