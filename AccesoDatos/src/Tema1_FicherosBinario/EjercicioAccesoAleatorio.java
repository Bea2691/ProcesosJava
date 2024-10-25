package Tema1_FicherosBinario;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EjercicioAccesoAleatorio {

    private static final int TAMANO_REGISTRO = 75;
    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//ejercicioempleado.dat");
            RandomAccessFile file = new RandomAccessFile(fichero, "rw");

            int opcion;
            do {
                menu();
                opcion = SC.nextInt();
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

        } catch (IOException e) {
            System.out.println("Error al acceder al fichero: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Algo inesperado ha ocurrido: " + e.getMessage());
        }
    }

    public static void menu() {
        System.out.println("\nOpciones:");
        System.out.println("1. Insertar empleado");
        System.out.println("2. Consultar empleado");
        System.out.println("3. Eliminar empleado");
        System.out.println("4. Modificar salario de empleado");
        System.out.println("5. Salir");
        System.out.print("Elija una opción: ");
    }

    public static void insertarEmpleado(RandomAccessFile file) {
        try {
            System.out.print("Introduzca el ID del empleado: ");
            int id = SC.nextInt();

            if (buscarEmpleado(file, id, true)) {
                System.out.println("El empleado con ID-" + id + " ya existe.");
                return;
            }

            System.out.print("Introduzca el DNI (9 caracteres): ");
            String dni = SC.next();
            System.out.print("Introduzca el nombre y apellidos (20 caracteres): ");
            SC.nextLine();
            String nombre = SC.nextLine();
            System.out.print("Introduzca el número de departamento: ");
            int departamento = SC.nextInt();
            System.out.print("Introduzca el salario: ");
            double salario = SC.nextDouble();

            file.seek(file.length());
            file.writeInt(id);

            for (int i = 0; i < 9; i++) file.writeChar(i < dni.length() ? dni.charAt(i) : ' ');
            for (int i = 0; i < 20; i++) file.writeChar(i < nombre.length() ? nombre.charAt(i) : ' ');

            file.writeInt(departamento);
            file.writeDouble(salario);
            file.writeBoolean(true);

            System.out.println("Empleado insertado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al insertar el empleado: " + e.getMessage());
        }
    }

    private static boolean buscarEmpleado(RandomAccessFile file, int id, boolean soloBuscar) throws IOException {
        file.seek(0);
        while (file.getFilePointer() < file.length()) {
            int idEmpleado = file.readInt();

            if (idEmpleado == id) {
                char[] dniChars = new char[9];
                for (int i = 0; i < 9; i++) {
                    dniChars[i] = file.readChar();
                }

                char[] nombreChars = new char[20];
                for (int i = 0; i < 20; i++) {
                    nombreChars[i] = file.readChar();
                }

                int departamento = file.readInt();
                double salario = file.readDouble();
                boolean estado = file.readBoolean();

                if (estado) {
                    if (!soloBuscar) {
                        System.out.println("Empleado encontrado:");
                        System.out.println("ID: " + idEmpleado);
                        System.out.println("DNI: " + new String(dniChars));
                        System.out.println("Nombre: " + new String(nombreChars).trim());
                        System.out.println("Departamento: " + departamento);
                        System.out.printf("Salario: %.2f%n", salario);
                    }
                    return true;
                }
            }

            file.seek(file.getFilePointer() + (TAMANO_REGISTRO - 4));
        }
        return false;
    }

    public static void consultarEmpleado(RandomAccessFile file) {
        try {
            System.out.print("Introduzca el ID del empleado a consultar: ");
            int id = SC.nextInt();

            if (!buscarEmpleado(file, id, false)) {
                System.out.println("Empleado no encontrado.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Entrada inválida. Por favor, introduzca un número.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    public static void eliminarEmpleado(RandomAccessFile file) {
        try {
            System.out.print("Introduzca el ID del empleado a eliminar: ");
            int id = SC.nextInt();

            if (buscarEmpleado(file, id, true)) {
                file.seek(file.getFilePointer() - 1); // Ir al booleano de estado
                file.writeBoolean(false);
                System.out.println("Empleado eliminado correctamente.");
            } else {
                System.out.println("Empleado no encontrado.");
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar el empleado: " + e.getMessage());
        }
    }

    public static void modificarSalario(RandomAccessFile file) {
        try {
            System.out.print("Introduzca el ID del empleado a modificar: ");
            int id = SC.nextInt();

            if (buscarEmpleado(file, id, true)) {
                file.seek(file.getFilePointer() + 66 - TAMANO_REGISTRO); // Posición del salario
                double salarioActual = file.readDouble();
                System.out.println("Salario actual: " + salarioActual);

                System.out.print("Introduzca la cantidad que desea aumentar: ");
                double aumento = SC.nextDouble();
                file.seek(file.getFilePointer() - 8); // Regresar a la posición del salario
                file.writeDouble(salarioActual + aumento);
                System.out.println("Nuevo salario: " + (salarioActual + aumento));
            } else {
                System.out.println("Empleado no encontrado.");
            }
        } catch (IOException e) {
            System.out.println("Error al modificar el salario: " + e.getMessage());
        }
    }
}
