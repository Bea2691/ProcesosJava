package Tema1_FicherosBinario;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Ejercicio2AccesoAleatorio {

	private static final int TAMANO_REGISTRO = 75; // Tamaño fijo del registro en bytes

	public static void main(String[] args) {
    	try {
        	File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//ejercicioempleado.dat");

        	// Declaración del fichero de acceso aleatorio
        	RandomAccessFile file = new RandomAccessFile(fichero, "rw"); // lectura y escritura
        	Scanner sc = new Scanner(System.in);

        	int opcion;

        	do {
            	System.out.println("\nOpciones:");
            	System.out.println("1. Insertar empleado");
            	System.out.println("2. Consultar empleado");
            	System.out.println("3. Eliminar empleado");
            	System.out.println("4. Modificar salario de empleado");
            	System.out.println("5. Salir");
            	System.out.print("Elija una opción: ");
            	opcion = sc.nextInt();

            	switch (opcion) {
                	case 1:
                    	insertarEmpleado(file);
                    	break;
                	case 2:
                    	consultarEmpleado(file);
                    	break;
                	case 3:
                    	eliminarEmpleado(file);
                    	break;
                	case 4:
                    	modificarSalario(file);
                    	break;
                	case 5:
                    	System.out.println("Saliendo...");
                    	break;
                	default:
                    	System.out.println("Opción incorrecta.");
            	}
        	} while (opcion != 5);

        	file.close();
        	sc.close();

    	} catch (IOException e) {
        	System.out.println("Error al acceder al fichero: " + e.getMessage());
    	} catch (Exception e) {
        	System.out.println("Error inesperado: " + e.getMessage());
    	}
	}

	public static void insertarEmpleado(RandomAccessFile file) {
    	try {
        	Scanner sc = new Scanner(System.in);
        	System.out.print("Introduzca el ID del empleado: ");
        	int id = sc.nextInt();

        	if (buscarEmpleado(file, id)) {
            	System.out.println("El empleado con ID-" + id + " ya existe.");
            	return;
        	}

        	System.out.print("Introduzca el DNI (9 caracteres): ");
        	String dni = sc.next();
        	System.out.print("Introduzca el nombre y apellidos (20 caracteres): ");
        	sc.nextLine();  // Limpiar el buffer
        	String nombre = sc.nextLine();
        	System.out.print("Introduzca el departamento: ");
        	int departamento = sc.nextInt();
        	System.out.print("Introduzca el salario: ");
        	double salario = sc.nextDouble();
        	boolean estado = true; // Empleado activo

        	// Escribir los datos del empleado en el archivo
        	file.seek(file.length()); // Mover el puntero al final del archivo
        	file.writeInt(id);

        	// Escribir DNI (9 caracteres)
        	StringBuffer bufferDni = new StringBuffer(dni);
        	bufferDni.setLength(9);  // Ajustamos a 9 caracteres
        	file.writeChars(bufferDni.toString());

        	// Escribir nombre y apellidos (20 caracteres)
        	StringBuffer bufferNombre = new StringBuffer(nombre);
        	bufferNombre.setLength(20);
        	file.writeChars(bufferNombre.toString());

        	file.writeInt(departamento);
        	file.writeDouble(salario);
        	file.writeBoolean(estado);

        	System.out.println("Empleado insertado correctamente.");
    	} catch (IOException e) {
        	System.out.println("Error al insertar el empleado: " + e.getMessage());
    	}
	}

	public static void consultarEmpleado(RandomAccessFile file) {
    	try {
        	Scanner sc = new Scanner(System.in);
        	System.out.print("Introduzca el ID del empleado a consultar: ");
        	int id = sc.nextInt();

        	if (!buscarEmpleado(file, id)) {
            	System.out.println("Empleado no encontrado.");
        	}
    	} catch (InputMismatchException e) {
        	System.out.println("Error: Entrada inválida. Por favor, introduzca un número.");
    	} catch (Exception e) {
        	System.out.println("Error inesperado: " + e.getMessage());
    	}
	}


	public static boolean buscarEmpleado(RandomAccessFile file, int id) {
    	try {
        	file.seek(0);  // Comenzar desde el inicio del archivo

        	while (file.getFilePointer() < file.length()) {
            	// Leer el ID del empleado
            	int idEmpleado = file.readInt();

            	// Leer el DNI (9 caracteres)
            	char[] dniChars = new char[9];
            	for (int i = 0; i < dniChars.length; i++) {
                	dniChars[i] = file.readChar();
            	}
            	String dni = new String(dniChars);

            	// Leer el nombre y apellidos (20 caracteres)
            	char[] nombreChars = new char[20];
            	for (int i = 0; i < nombreChars.length; i++) {
                	nombreChars[i] = file.readChar();
            	}
            	String nombre = new String(nombreChars);

            	// Leer departamento, salario y estado
            	int departamento = file.readInt();
            	double salario = file.readDouble();
            	boolean estado = file.readBoolean();

            	// Verificar si es el empleado que estamos buscando y si está activo
            	if (idEmpleado == id && estado) {
                	// Imprimir los datos del empleado
                	System.out.println("Empleado encontrado:");
                	System.out.println("ID: " + idEmpleado);
                	System.out.println("DNI: " + dni);
                	System.out.println("Nombre: " + nombre.trim());
                	System.out.println("Departamento: " + departamento);
                	System.out.printf("Salario: %.2f%n", salario);
                	return true;
            	}

            	// Avanzar al siguiente registro
            	file.seek(file.getFilePointer() + (75 - 75));  // Ajustar el puntero si es necesario
        	}
    	} catch (IOException e) {
        	System.out.println("Error al buscar el empleado: " + e.getMessage());
    	}
    	return false;  // Empleado no encontrado
	}

	public static void eliminarEmpleado(RandomAccessFile file) {
    	try {
        	Scanner sc = new Scanner(System.in);
        	System.out.print("Introduzca el ID del empleado a eliminar: ");
        	int id = sc.nextInt();

        	long posicion = obtenerPosicionEmpleado(file, id);
        	if (posicion >= 0) {
            	file.seek(posicion + 74); // Posición del estado (después del salario)
            	file.writeBoolean(false); // Cambiar el estado a inactivo
            	System.out.println("Empleado eliminado correctamente (borrado lógico).");
        	} else {
            	System.out.println("Empleado no encontrado.");
        	}
    	} catch (IOException e) {
        	System.out.println("Error al eliminar el empleado: " + e.getMessage());
    	}
	}

	public static void modificarSalario(RandomAccessFile file) {
    	try {
        	Scanner sc = new Scanner(System.in);
        	System.out.print("Introduzca el ID del empleado para modificar el salario: ");
        	int id = sc.nextInt();

        	long posicion = obtenerPosicionEmpleado(file, id);
        	if (posicion >= 0) {
            	file.seek(posicion + 66); // Posición del salario
            	double salarioActual = file.readDouble();
            	System.out.println("Salario actual: " + salarioActual);

            	System.out.print("Introduzca la cantidad de aumento: ");
            	double aumento = sc.nextDouble();
            	double nuevoSalario = salarioActual + aumento;

            	file.seek(posicion + 66); // Volver a la posición del salario
            	file.writeDouble(nuevoSalario);
            	System.out.println("Nuevo salario: " + nuevoSalario);
        	} else {
            	System.out.println("Empleado no encontrado.");
        	}
    	} catch (IOException e) {
        	System.out.println("Error al modificar el salario: " + e.getMessage());
    	}
	}

	public static long obtenerPosicionEmpleado(RandomAccessFile file, int id) {
    	try {
        	file.seek(0);  // Comenzar desde el inicio del archivo

        	while (file.getFilePointer() < file.length()) {
            	// Leer el ID del empleado
            	int idEmpleado = file.readInt();

            	// Saltar al final del registro para leer el estado
            	file.seek(file.getFilePointer() + 70);  // Saltar DNI, nombre, departamento, salario
            	boolean estado = file.readBoolean();

            	if (idEmpleado == id && estado) {
                	// Volver al inicio del registro si se encontró el empleado
                	return file.getFilePointer() - 75;
            	}

            	// Avanzar al siguiente registro
            	file.seek(file.getFilePointer() + (TAMANO_REGISTRO - 75));  // Ajustar el puntero
        	}
    	} catch (IOException e) {
        	System.out.println("Error al obtener la posición del empleado: " + e.getMessage());
    	}
    	return -1;
	}
}

