package Conectores;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class EjemploProcedimientos {

	public static void main(String[] args) {

		int dpto_no, salario, filas;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/ejemplo", "root","");
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("Número de departamento: ");
			dpto_no = sc.nextInt();
			sc.nextLine();
			
			System.out.print("Salario a aumentar: ");
			salario= sc.nextInt();
			sc.nextLine();
			
				
			
			String sql = "call subir_sal(?,?)";
			//Preparar la llamada al procedimiento
			
			CallableStatement llamada = conexion.prepareCall(sql);
			//llamada.setInt()
			llamada.setInt(1,dpto_no); //param1 en el procedimiento en el phpmyadmin
			llamada.setInt(2, salario);
			
			filas= llamada.executeUpdate();
			
			if(filas>0) {
				System.out.println("Subida de sueldo realizada sobre: " + filas + " empleados");
				
			}else System.out.println("No se ha subido el sueldo a nadie");
			
			llamada.close();
			conexion.close();
			
			
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

	}

}
