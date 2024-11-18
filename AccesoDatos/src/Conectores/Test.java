package Conectores;

import java.sql.*;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

    	Class.forName("com.mysql.jdbc.Driver"); // Usa el driver actualizado
        Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/alumnos", "root", "");
        Statement sentencia = conexion.createStatement();
        Scanner teclado = new Scanner(System.in);

        int opcion = 0;

        do {
            System.out.println("¿Qué desea hacer?");
            System.out.println("1. Buscar    2. Cambiar Curso    3. Cambiar Especialidad    4. Salir");
            opcion = teclado.nextInt();

            switch (opcion) {
                case 1: // Buscar alumno
                    System.out.println("Introduzca DNI para buscar alumno");
                    String DNI = teclado.next();

                    String buscarDNI = "SELECT * FROM alumno WHERE dni = ?";
                    try (PreparedStatement psBuscar = conexion.prepareStatement(buscarDNI)) {
                        psBuscar.setString(1, DNI);
                        ResultSet rsBuscarDNI = psBuscar.executeQuery();

                        if (rsBuscarDNI.next()) {
                            do {
                                String dni = rsBuscarDNI.getString(1);
                                String nombre = rsBuscarDNI.getString(2);
                                String apellido = rsBuscarDNI.getString(3);
                                String clase = rsBuscarDNI.getString(4);
                                String curso = rsBuscarDNI.getString(5);
                                System.out.printf("DNI: %s - Nombre: %s - Apellidos: %s - Clase: %s - Curso: %s%n",
                                        dni, nombre, apellido, clase, curso);
                            } while (rsBuscarDNI.next());
                        } else {
                            System.out.println("ERROR. Este DNI no existe");
                        }
                    }
                    break;

                case 2: // Cambiar curso
                    System.out.println("Introduzca DNI para buscar alumno");
                    String DNI2 = teclado.next();

                    System.out.println("¿A qué curso quieres cambiar a este alumno?");
                    String cursoCambiado = teclado.next();

                    if (cursoCambiado.equals("Primero") || cursoCambiado.equals("Segundo") || cursoCambiado.equals("Tercero")) {
                        String cambiarCurso = "UPDATE alumno SET curso = ? WHERE dni = ?";
                        try (PreparedStatement psCambiar = conexion.prepareStatement(cambiarCurso)) {
                            psCambiar.setString(1, cursoCambiado);
                            psCambiar.setString(2, DNI2);
                            int filas = psCambiar.executeUpdate();
                            if (filas > 0) {
                                System.out.println("Curso cambiado con éxito");
                            } else {
                                System.out.println("ERROR. No se encontró el alumno con el DNI proporcionado.");
                            }
                        }
                    } else {
                        System.out.println("ERROR. Ese curso no existe");
                    }
                    break;

                case 3: // Cambiar especialidad
                    System.out.println("Introduzca DNI para buscar alumno");
                    String DNI3 = teclado.next();

                    System.out.println("¿A qué especialidad quieres cambiar a este alumno?");
                    String especialidadCambiada = teclado.next();

                    if (especialidadCambiada.equals("DAM") || especialidadCambiada.equals("DAW") || especialidadCambiada.equals("ASIR")
                            || especialidadCambiada.equals("SMR") || especialidadCambiada.equals("CAE")) {

                        String procedimiento = "{CALL cambiarespecialidad3(?, ?)}";
                        try (CallableStatement llamada = conexion.prepareCall(procedimiento)) {
                            llamada.setString(1, DNI3);
                            llamada.setString(2, especialidadCambiada);

                            int filas = llamada.executeUpdate();
                            if (filas > 0) {
                                System.out.printf("Especialidad cambiada con éxito para %d alumno(s).%n", filas);
                            } else {
                                System.out.println("No se encontró el alumno o no se realizó el cambio.");
                            }
                        }
                    } else {
                        System.out.println("ERROR. Esa especialidad no existe");
                    }
                    break;

                case 4: // Salir
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción de menú incorrecta");
            }

        } while (opcion != 4); // Corregimos la condición para salir

        // Cerrar recursos
        sentencia.close();
        conexion.close();
        teclado.close();
    }
}

/*Procedimiento para sql
 * DELIMITER $$
 * 

CREATE DEFINER=`root`@`localhost` PROCEDURE `cambiarEspecialidad3`(
    IN DNI3 VARCHAR(9), 
    IN especialidadCambiada VARCHAR(20)
)
BEGIN
    UPDATE alumno 
    SET clase = especialidadCambiada
    WHERE dni = DNI3;
END$$

DELIMITER ;*/
