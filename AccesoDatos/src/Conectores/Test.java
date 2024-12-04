package Conectores;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Test {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

    	Class.forName("com.mysql.jdbc.Driver"); 
        Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/alumnos", "root", "");
        Statement sentencia = conexion.createStatement();
        Scanner sc = new Scanner(System.in);

        int opcion = 0;

        do {
            System.out.println("Escoja una opcion del menú");
            System.out.println("1 - Buscar");
            System.out.println("2 - Cambiar curso");
            System.out.println("3 - Cambiar clase/especialidad");
            System.out.println("4 - Cambiar clase/especialidad, creando procedimiento");
            System.out.println("5 - Salir");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1: // Buscar alumno
                    System.out.println("Introduzca DNI para buscar alumno");
                    String DNI = sc.next();

                    String buscarDNI = "SELECT * FROM alumnos WHERE dni = ?";
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
                    String DNI2 = sc.next();

                    System.out.println("¿A qué curso quieres cambiar a este alumno?");
                    String cursoCambiado = sc.next();

                    if (cursoCambiado.equals("Primero") || cursoCambiado.equals("Segundo") || cursoCambiado.equals("Tercero")) {
                        String cambiarCurso = "UPDATE alumnos SET curso = ? WHERE dni = ?";
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
                    String DNI3 = sc.next();

                    System.out.println("¿A qué especialidad quieres cambiar a este alumno?");
                    String especialidadCambiada = sc.next();

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
                case 4: // Crear procedimiento y ejecutarlo
                    System.out.println("Introduzca DNI para buscar alumno");
                    String DNI4 = sc.next();

                    System.out.println("¿A qué especialidad quieres cambiar a este alumno?");
                    String nuevaEspecialidad = sc.next();

                    if (nuevaEspecialidad.equals("DAM") || nuevaEspecialidad.equals("DAW") || nuevaEspecialidad.equals("ASIR")
                            || nuevaEspecialidad.equals("SMR") || nuevaEspecialidad.equals("CAE")) {

                        // Crear procedimiento en la base de datos
                        String crearProcedimiento = """
                            CREATE PROCEDURE cambiarEspecialidadEnJava(
                                IN DNI4 VARCHAR(9), 
                                IN nuevaEspecialidad VARCHAR(20)
                            )
                            BEGIN
                                UPDATE alumnos 
                                SET clase = nuevaEspecialidad
                                WHERE dni = DNI4;
                            END;
                        """;

                        try (Statement crearProcStmt = conexion.createStatement()) {
                            // Intentar crear el procedimiento
                            crearProcStmt.execute(crearProcedimiento);
                            System.out.println("Procedimiento creado exitosamente.");
                        } catch (SQLException e) {
                            System.out.println("ERROR al crear el procedimiento. Es posible que ya exista.");
                        }

                        // Llamar al procedimiento creado
                        String llamarProcedimiento = "{CALL cambiarEspecialidadEnJava(?, ?)}";
                        try (CallableStatement llamada = conexion.prepareCall(llamarProcedimiento)) {
                            llamada.setString(1, DNI4);
                            llamada.setString(2, nuevaEspecialidad);

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
                case 5: // Salir
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción de menú incorrecta");
            }

        } while (opcion != 5); // Corregimos la condición para salir

        // Cerrar recursos
        sentencia.close();
        conexion.close();
        sc.close();
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
    WHERE dni = dni3;
END$$

DELIMITER ;*/

/* solo numeros
 * try {
            opcion = teclado.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Por favor, introduce un número válido.");
            teclado.nextLine(); // Limpia el buffer
            continue;
        }
 * 
 * */
/*
 * case 5: // Insertar nuevo alumno
    System.out.println("Introduzca DNI del nuevo alumno:");
    String nuevoDNI = sc.next();

    // Verificar si el alumno ya existe
    String verificarAlumno = "SELECT dni FROM alumno WHERE dni = ?";
    try (PreparedStatement psVerificar = conexion.prepareStatement(verificarAlumno)) {
        psVerificar.setString(1, nuevoDNI);
        ResultSet rsVerificar = psVerificar.executeQuery();

        if (rsVerificar.next()) {
            System.out.println("ERROR. Ya existe un alumno con ese DNI.");
        } else {
            // Si no existe, proceder con la inserción
            System.out.println("Introduzca nombre del nuevo alumno:");
            String nuevoNombre = sc.next();

            System.out.println("Introduzca apellidos del nuevo alumno:");
            String nuevosApellidos = sc.next();

            System.out.println("Introduzca clase/especialidad del nuevo alumno:");
            String nuevaClase = sc.next();

            System.out.println("Introduzca curso del nuevo alumno:");
            String nuevoCurso = sc.next();

            String insertarAlumno = "INSERT INTO alumno (dni, nombre, apellidos, clase, curso) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psInsertar = conexion.prepareStatement(insertarAlumno)) {
                psInsertar.setString(1, nuevoDNI);
                psInsertar.setString(2, nuevoNombre);
                psInsertar.setString(3, nuevosApellidos);
                psInsertar.setString(4, nuevaClase);
                psInsertar.setString(5, nuevoCurso);

                int filas = psInsertar.executeUpdate();
                if (filas > 0) {
                    System.out.println("Alumno insertado con éxito.");
                } else {
                    System.out.println("No se pudo insertar el alumno.");
                }
            } catch (SQLException e) {
                System.out.println("ERROR. No se pudo insertar el alumno. Verifique los datos.");
            }
        }
    } catch (SQLException e) {
        System.out.println("ERROR al verificar si el alumno ya existe.");
    }
    break;
 * */
/*___________________________________________________________________________
 * o por separado:
 * private static boolean existeAlumno(Connection conexion, String dni) {
    String consulta = "SELECT dni FROM alumno WHERE dni = ?";
    try (PreparedStatement psVerificar = conexion.prepareStatement(consulta)) {
        psVerificar.setString(1, dni);
        try (ResultSet rs = psVerificar.executeQuery()) {
            return rs.next(); // Devuelve true si hay un resultado, false si no.
        }
    } catch (SQLException e) {
        System.out.println("ERROR al verificar si el alumno ya existe.");
        return false; // En caso de error, asumimos que no existe para evitar problemas.
    }
}

*
*
*case 5: // Insertar nuevo alumno
    System.out.println("Introduzca DNI del nuevo alumno:");
    String nuevoDNI = sc.next();

    // Usar el método para comprobar si el alumno ya existe
    if (existeAlumno(conexion, nuevoDNI)) {
        System.out.println("ERROR. Ya existe un alumno con ese DNI.");
    } else {
        // Si no existe, proceder con la inserción
        System.out.println("Introduzca nombre del nuevo alumno:");
        String nuevoNombre = sc.next();

        System.out.println("Introduzca apellidos del nuevo alumno:");
        String nuevosApellidos = sc.next();

        System.out.println("Introduzca clase/especialidad del nuevo alumno:");
        String nuevaClase = sc.next();

        System.out.println("Introduzca curso del nuevo alumno:");
        String nuevoCurso = sc.next();

        String insertarAlumno = "INSERT INTO alumno (dni, nombre, apellidos, clase, curso) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement psInsertar = conexion.prepareStatement(insertarAlumno)) {
            psInsertar.setString(1, nuevoDNI);
            psInsertar.setString(2, nuevoNombre);
            psInsertar.setString(3, nuevosApellidos);
            psInsertar.setString(4, nuevaClase);
            psInsertar.setString(5, nuevoCurso);

            int filas = psInsertar.executeUpdate();
            if (filas > 0) {
                System.out.println("Alumno insertado con éxito.");
            } else {
                System.out.println("No se pudo insertar el alumno.");
            }
        } catch (SQLException e) {
            System.out.println("ERROR. No se pudo insertar el alumno. Verifique los datos.");
        }
    }
    break;*/


//joanna casco
