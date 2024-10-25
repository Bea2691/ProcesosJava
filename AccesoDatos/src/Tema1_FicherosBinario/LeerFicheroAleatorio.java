package Tema1_FicherosBinario;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LeerFicheroAleatorio {

	public static void main(String[] args) throws IOException {
		File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//empleados.dat");
		
		RandomAccessFile file= new RandomAccessFile(fichero, "r"); //Solo lectura
		
		int id, dep, posicion;
		Double salario;
		char apellidos[]= new char[10];
		char aux;
		String apellido;
		
		
		posicion = 0; // posicion inicial del puntero al principio del fichero
		
		for(;;) {
			file.seek(posicion); //Puntero apuntando en el byte de posicion, que es 0
			id = file.readInt();
			//Recorre uno a uno los caracteres de apellidos
			for(int i=0; i<apellidos.length;i++) {
				aux = file.readChar();
						apellidos[i] = aux;
				
			}
			apellido = new String(apellidos);// Convierte el array a String
			dep = file.readInt();// Lee el departamento
			salario = file.readDouble();// Lee el salario
			
			if(id>0) {
				System.out.printf("ID: %d, Apellido: %s, Departamento: %d, Salario: %.2f %n", id,apellido.trim(), dep, salario);
			}
			//Posicion del siguiente empleado, cada empleado ocupa 36 bytes
			posicion = posicion + 36; //Del 0 al 35 el primer registro
			
			if(file.getFilePointer()==file.length()) {
				break;
			}
		}
		file.close();
		
		

	}

}
