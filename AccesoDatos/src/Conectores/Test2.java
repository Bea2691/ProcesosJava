package Conectores;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test2 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
    	// TODO Auto-generated method stub

    	
    	Class.forName("com.mysql.jdbc.Driver");
    	Connection conexion = DriverManager.getConnection(
    	    "jdbc:mysql://sql.freedb.tech/freedb_ejemplo", 
    	    "freedb_ejemplo", 
    	    "kz4ZA%hXjY&26jG"
    	);
   	 
    	DatabaseMetaData dbmd = conexion.getMetaData();
    	ResultSet resul, resul2, resul3;

    	//1
    	String nombre = dbmd.getDatabaseProductName();
    	String driver = dbmd.getDriverName();
    	String url = dbmd.getURL();
    	String usuario = dbmd.getUserName();
   	 
    	System.out.println("INFORMACION SOBRE LA BASE DE DATOS");
    	System.out.println("-------------------------------");
    	System.out.printf("Nombre: %s %n", nombre);
    	System.out.printf("Driver: %s %n", driver);
    	System.out.printf("URL: %s %n", url);
    	System.out.printf("Usuario: %s %n", usuario);
    	System.out.printf("%n%n%n");
    	//2
    	resul = dbmd.getTables(null, "BXWTYHpCvX", null, null);
   	 
    	System.out.println("INFORMACION SOBRE LAS TABLAS DE LA BASE DE DATOS");
    	System.out.println("-----------------------------------");
   	 
    	while(resul.next()) {
       	 
        	String catalogo = resul.getString(1);
        	String tabla = resul.getString(3);
        	String tipo = resul.getString(4);
        	System.out.printf("Tipo: %s - Catalogo: %s - Nombre: %s%n", tipo, catalogo, tabla);
       	 
       	 
    	}
   	 
    	System.out.printf("%n%n%n");
   	 
   	 
    	//3
    	System.out.println("INFORMACION SOBRE LOS CAMPOS DE LAS TABLAS");
    	System.out.println("-----------------------------------");
    	resul = dbmd.getColumns(null,"prueba",null,null);
   	 
    	while(resul.next()) {
       	 
        	String tabla = resul.getString(3);
        	String campo = resul.getString(4);
        	String tipo = resul.getString(6);
        	String tamanyo = resul.getString(7);
        	int nulos = resul.getInt(11);
        	System.out.printf("Tabla: %s - Campo: %s - Tipo: %s - Tamaño: %s - ¿Nulo?: - %s%n",tabla,campo,tipo,tamanyo,nulos);    
       	 
    	}
   	 
    	resul = dbmd.getPrimaryKeys(null,"BXWTYHpCvX", "clientes");
    	resul2 = dbmd.getPrimaryKeys(null, "BXWTYHpCvX", "productos");
    	resul3 = dbmd.getPrimaryKeys(null, "BXWTYHpCvX", "ventas");
    	resul.next();
    	resul2.next();
    	resul3.next();
    	String tabla = resul.getString(3);
    	String nombreColumna = resul.getString(4);
    	String clavepk = resul.getString(6);
    	int seq = resul.getShort(5);
    	System.out.printf("tabla: %s - Columna: %s - Nombre Clave Primaria: %s %n", tabla, nombreColumna, clavepk);
   	 
    	String tabla2 = resul2.getString(3);
    	String nombreColumna2 = resul2.getString(4);
    	String clavepk2 = resul2.getString(6);
    	System.out.printf("tabla: %s - Columna: %s - Nombre Clave Primaria: %s %n", tabla2, nombreColumna2, clavepk2);
   	 
    	String tabla3 = resul3.getString(3);
    	String nombreColumna3 = resul3.getString(4);
    	String clavepk3 = resul3.getString(6);
    	System.out.printf("tabla: %s - Columna: %s - Nombre Clave Primaria: %s %n", tabla3, nombreColumna3, clavepk3);
   	 
   	 
	}

}

