package Tema1_FicherosXML;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
 

public class EjercicioEmpleadosConXML {
	public static void main(String[] args) {
    	try {
        	File fichero = new File("C:\\NuevoDirectorio\\EjercicioEmpleados.dat");


        	// Declaración del fichero de acceso aleatorio
        	RandomAccessFile file = new RandomAccessFile(fichero, "rw"); // Para lectura y escritura
        	Scanner sc = new Scanner(System.in);


        	int opcion;


        	do {
            	System.out.println("\nMenú:");
            	System.out.println("1. Alta empleado");
            	System.out.println("2. Consulta empleado");
            	System.out.println("3. Dar de baja empleado");
            	System.out.println("4. Modificar salario de empleado");
            	System.out.println("5. Menú XML");
            	System.out.println("6. Salir");
            	System.out.print("Elija una opción: ");
            	opcion = sc.nextInt();


            	switch (opcion) {
                	case 1:
                    	insertarEmpleado(sc, file);
                    	break;
                	case 2:
                    	consultarEmpleado(sc, file);
                    	break;
                	case 3:
                    	eliminarEmpleado(sc, file);
                    	break;
                	case 4:
                    	modificarSalario(sc, file);
                    	break;
                	case 5:
                    	menuXML(sc, file);
                    	break;
                	case 6:
                    	System.out.println("Saliendo...");
                    	break;
                	default:
                    	System.out.println("Opción incorrecta.");
            	}
        	} while (opcion != 6);


        	file.close();
        	sc.close();


    	} catch (IOException e) {
        	System.out.println("Error al acceder al fichero: " + e.getMessage());
    	}
	}
    
	//Método para comprobar si existe un empleado para comprobar a la hora de dar de alta y controlar que no se puedan dar de
	//alta con el mismo id varios empleados. La diferencia con el método del final de obtenerPosici´´on sobretodo es que este
	//devuelve true o false para determinar si existe, miesntras que el de obtener posición devuelve la posición para usar en otros
	//métodos.
	public static boolean existeEmpleado(RandomAccessFile file, int id) {
    	try {
        	int posicion = (id - 1) * 75;   //La posición del registro es el id-1 (0) y el siguiente estará en el 75, el 3º en el 150, etc
       	 
        	if (posicion < file.length()) {
            	file.seek(posicion);  //Posición del registro
            	int idEmpleado = file.readInt();  //Leemos el ID del empleado
            	file.seek(posicion + 74);  //Leemos el estado
            	boolean estado = file.readBoolean();
           	 
            	return idEmpleado == id && estado;  //Si existe y está activo
        	}
    	} catch (IOException e) {
        	System.out.println("Error al buscar el empleado: " + e.getMessage());
    	}
    	return false;
	}


	//Método para dar de alta empleado, primero comprueba si el empleado existe, si no existe empiza a escribir en el fichero los
	//datos dados por consola. El estado no se pide por consola, cuando se da de alta directamente se pone TRUE.
	public static void insertarEmpleado(Scanner sc, RandomAccessFile file) {
    	try {
        	System.out.print("Introduzca el ID del empleado: ");
        	int id = sc.nextInt();


        	if (existeEmpleado(file, id)) {
            	System.out.println("El empleado con ID-" + id + " ya existe.");
            	return;
        	}


        	//Validación del DNI
        	String dni;
        	do {
            	System.out.print("Introduzca el DNI (exactamente 9 caracteres): ");
            	dni = sc.next();
            	if (dni.length() != 9) {
                	System.out.println("Error: El DNI debe tener exactamente 9 caracteres.");
            	}
        	} while (dni.length() != 9);


        	//Validación del nombre y apellidos
        	System.out.print("Introduzca el nombre y apellidos (máx 20 caracteres): ");
        	sc.nextLine();  // Limpiar buffer
        	String nombre = sc.nextLine();


        	//Validación del departamento
        	int departamento;
        	do {
            	System.out.println("Seleccione el departamento: ");
            	System.out.println("1. Administración");
            	System.out.println("2. RRHH");
            	System.out.println("3. Logística");
            	System.out.println("4. Informáticos");
            	System.out.println("5. Programadores");
            	System.out.print("Introduzca el número del departamento: ");
            	departamento = sc.nextInt();


            	if (departamento < 1 || departamento > 5) {
                	System.out.println("Error: El número de departamento debe estar entre 1 y 5.");
            	}
        	} while (departamento < 1 || departamento > 5);


        	//Validación del salario
        	System.out.print("Introduzca el salario: ");
        	double salario = sc.nextDouble();


        	boolean estado = true;


        	//Escribir los datos del empleado en el archivo
        	file.seek(file.length());
        	file.writeInt(id);


        	//Escribir DNI (9 caracteres)
        	StringBuffer bufferDni = new StringBuffer(dni);
        	bufferDni.setLength(9);
        	file.writeChars(bufferDni.toString());


        	//Escribir nombre y apellidos (20 caracteres)
        	StringBuffer bufferNombre = new StringBuffer(nombre);
        	bufferNombre.setLength(20);
        	file.writeChars(bufferNombre.toString());


        	// Escribir departamento, salario y estado
        	file.writeInt(departamento);
        	file.writeDouble(salario);
        	file.writeBoolean(estado);


        	System.out.println("Empleado insertado correctamente.");
    	} catch (IOException e) {
        	System.out.println("Error al insertar el empleado: " + e.getMessage());
    	}
	}


	//Metodo para hacer la consulta del empleado. Primero obtiene la posición que determina que ese empleado está dado de alta
	//y después empezamos a leer y a mostrar por consola sus datos. Como el departamento hay que tratarlo como un INT pero quería
	//enseñar a que corresponde cada número, he hecho otro método que devuelve el nombr edel departamento según su número.
	public static void consultarEmpleado(Scanner sc, RandomAccessFile file) {
    	try {
        	System.out.print("Introduzca el ID del empleado: ");
        	int id = sc.nextInt();


        	int posicion = obtenerPosicionEmpleado(file, id);
        	if (posicion >= 0) {
            	file.seek(posicion);
            	int idEmpleado = file.readInt();


            	//Leer el DNI
            	char[] dniChars = new char[9];
            	for (int i = 0; i < dniChars.length; i++) {
                	dniChars[i] = file.readChar();
            	}
            	String dni = new String(dniChars);


            	//Leer el nombre y apellidos
            	char[] nombreChars = new char[20];
            	for (int i = 0; i < nombreChars.length; i++) {
                	nombreChars[i] = file.readChar();
            	}
            	String nombre = new String(nombreChars);


            	//Leer departamento, salario y estado
            	int departamento = file.readInt();
            	double salario = file.readDouble();
            	boolean estado = file.readBoolean();
            	//Si está dado de alta se muestran los datos
            	if (estado) {
                	System.out.println("Empleado encontrado:");
                	System.out.println("ID: " + idEmpleado);
                	System.out.println("DNI: " + dni.trim()); //el .trim controla los espacios
                	System.out.println("Nombre: " + nombre.trim());
                	System.out.println("Departamento: " + obtenerNombreDepartamento(departamento)); //Convertimos el número a nombre de departamento
                	System.out.printf("Salario: %.2f%n", salario);
            	} else {
                	System.out.println("Empleado no encontrado o inactivo.");
            	}
        	} else {
            	System.out.println("Empleado no encontrado.");
        	}
    	} catch (InputMismatchException e) {
        	System.out.println("Error: Entrada inválida. Por favor, introduzca un número.");
    	} catch (Exception e) {
        	System.out.println("Error inesperado: " + e.getMessage());
    	}
	}


	// Método que convierte el número de departamento en su nombre correspondiente. Como el departamento se pide con un int, de esta manera podemos
	//mostrar un String aunque almacenemos un int en los bytes
	public static String obtenerNombreDepartamento(int departamento) {
    	switch (departamento) {
        	case 1: return "Administración";
        	case 2: return "RRHH";
        	case 3: return "Logística";
        	case 4: return "Informáticos";
        	case 5: return "Programadores";
        	default: return "Desconocido";
    	}
	}


	//Con el mñetodo eliminar empleado lo que hacemos es buscar la posicion ID+estado del empleado y si está en true lo cambiamos
	//a false. Es la manera de eliminarlo de manera lógica pero que siga apareciendo en el fichero.
	public static void eliminarEmpleado(Scanner sc, RandomAccessFile file) {
    	try {
        	System.out.print("Introduzca el ID del empleado a eliminar: ");
        	int id = sc.nextInt();


        	int posicion = obtenerPosicionEmpleado(file, id);
        	if (posicion >= 0) {
            	file.seek(posicion + 74);  //Vamos al byte del estado que estará en true
            	file.writeBoolean(false);  //y lo ponemos a false para dar al empleado de baja
            	System.out.println("Empleado eliminado correctamente.");
        	} else {
            	System.out.println("Empleado no encontrado.");
        	}
    	} catch (IOException e) {
        	System.out.println("Error al eliminar el empleado: " + e.getMessage());
    	}
	}


	//Metodo para modificar el salario, Obtenemos la posición del empleado y vamos al byte de su salario actual y lo leemos,
	//pedimos por consola el nuevo salario y lo sobreescribimos en la posición de byte del salario y mostramos el nuevo salario.
	public static void modificarSalario(Scanner sc, RandomAccessFile file) {
    	try {
        	System.out.print("Introduzca el ID del empleado para modificar el salario: ");
        	int id = sc.nextInt();


        	long posicion = obtenerPosicionEmpleado(file, id);
        	if (posicion >= 0) {
         	 
            	file.seek(posicion + 66); // Posición del byte del salario
            	double salarioActual = file.readDouble();
            	System.out.println("Salario actual: " + salarioActual);


            	//Pedimos el nuevo salario
            	System.out.print("Introduzca el nuevo salario: ");
            	double nuevoSalario = sc.nextDouble();


            	//Volver a la posición del salario para sobrescribirlo
            	file.seek(posicion + 66);
            	file.writeDouble(nuevoSalario);  //Sobrescribimos el salario
            	System.out.println("Nuevo salario asignado: " + nuevoSalario);
        	} else {
            	System.out.println("Empleado no encontrado.");
        	}
    	} catch (IOException e) {
        	System.out.println("Error al modificar el salario: " + e.getMessage());
    	}
	}


	//Con este metodo obtenemos la posicion del empleado para usar en otros métodos como eliminar empleado. Buscamos el id que
	//queremos y de 75 en 75 comprueba el id coincidente y si su estado es true para verificar que no está dado de baja.
	public static int obtenerPosicionEmpleado(RandomAccessFile file, int id) {
    	try {
        	//Calculamos la posición del registro en el archivo (registro 1 empieza en 0, registro 2 en 75, etc)
        	int posicion = (id - 1) * 75;


        	if (posicion < file.length()) {
            	file.seek(posicion);
            	int idEmpleado = file.readInt();  


            	//Si el ID coincide, leemos el estado (último byte)
            	file.seek(posicion + 74);  //Vamos a la posición del estado
            	boolean estado = file.readBoolean(); //Leemos el estado que en este caso es un valor booleano


            	if (idEmpleado == id && estado) { //Si el idEmpleado es igual al id que hemos buscado y el estado es true
                	return posicion;  //Devolvemos la posición
            	}
        	}
    	} catch (IOException e) {
        	System.out.println("Error al obtener la posición del empleado: " + e.getMessage());
    	}
    	return -1;  //Devolvemos -1 si el empleado no se encuentra
	}






	public static void menuXML(Scanner sc, RandomAccessFile file) {
    	int opcionXML;
    	do {
        	System.out.println("\nMenú XML:");
        	System.out.println("1. Convertir archivo a XML");
        	System.out.println("2. Buscar empleado por ID en XML");
        	System.out.println("3. Salir al menú principal");
        	System.out.print("Elija una opción: ");
        	opcionXML = sc.nextInt();


        	switch (opcionXML) {
            	case 1:
                	try {
                    	convertirArchivoAXML(file);
                	} catch (IOException | TransformerException e) {
                    	System.out.println("Error al convertir el archivo a XML: " + e.getMessage());
                	}
                	break;
            	case 2:
                	buscarEmpleadoEnXML(sc);
                	break;
            	case 3:
                	System.out.println("Volviendo al menú principal...");
                	break;
            	default:
                	System.out.println("Opción incorrecta.");
        	}
    	} while (opcionXML != 3);
	}


	public static void convertirArchivoAXML(RandomAccessFile file) throws IOException, TransformerException {
    	int id, dep, posicion = 0;
    	double salario;
    	char[] dni = new char[9], nombre = new char[20];
    	char aux;
    	String dniStr, nombreStr;


    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


    	try {
        	DocumentBuilder builder = factory.newDocumentBuilder();
        	DOMImplementation implementation = builder.getDOMImplementation();
        	Document document = implementation.createDocument(null, "Empleados", null);
        	document.setXmlVersion("1.0");


        	for (;;) {
            	file.seek(posicion);
            	id = file.readInt();
            	for (int i = 0; i < dni.length; i++) {
                	aux = file.readChar();
                	dni[i] = aux;
            	}
            	dniStr = new String(dni);


            	for (int i = 0; i < nombre.length; i++) {
                	aux = file.readChar();
                	nombre[i] = aux;
            	}
            	nombreStr = new String(nombre);


            	dep = file.readInt();
            	salario = file.readDouble();
            	boolean estado = file.readBoolean();


            	if (id > 0 && estado) {
                	Element raiz = document.createElement("empleado");
                	document.getDocumentElement().appendChild(raiz);


                	CrearElemento("Id", Integer.toString(id), raiz, document);
                	CrearElemento("DNI", dniStr.trim(), raiz, document);
                	CrearElemento("Nombre", nombreStr.trim(), raiz, document);
                	CrearElemento("Departamento", Integer.toString(dep), raiz, document);
                	CrearElemento("Salario", Double.toString(salario), raiz, document);
            	}


            	posicion = posicion + 75;


            	if (file.getFilePointer() == file.length()) {
                	break;
            	}
        	}


        	Source source = new DOMSource(document);
        	Result result = new StreamResult(new File("C:\\NuevoDirectorio\\Empleados.xml"));
        	Transformer transformer = TransformerFactory.newInstance().newTransformer();
        	transformer.transform(source, result);


        	System.out.println("Archivo convertido a XML correctamente.");


    	} catch (ParserConfigurationException e) {
        	System.out.println("Error de configuración del parser: " + e.getMessage());
    	}
	}


	public static void buscarEmpleadoEnXML(Scanner sc) {
    	try {
        	System.out.print("Introduzca el ID del empleado a buscar en el XML: ");
        	int idBuscado = sc.nextInt();


        	File xmlFile = new File("C:\\NuevoDirectorio\\Empleados.xml");
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder builder = factory.newDocumentBuilder();
        	Document document = builder.parse(xmlFile);


        	document.getDocumentElement().normalize();


        	NodeList empleados = document.getElementsByTagName("empleado");


        	boolean encontrado = false;
        	for (int i = 0; i < empleados.getLength(); i++) {
            	Node empleado = empleados.item(i);
            	if (empleado.getNodeType() == Node.ELEMENT_NODE) {
                	Element elemento = (Element) empleado;
                	int id = Integer.parseInt(elemento.getElementsByTagName("Id").item(0).getTextContent());
                	if (id == idBuscado) {
                    	encontrado = true;
                    	String dni = elemento.getElementsByTagName("DNI").item(0).getTextContent();
                    	String nombre = elemento.getElementsByTagName("Nombre").item(0).getTextContent();
                    	String departamento = elemento.getElementsByTagName("Departamento").item(0).getTextContent();
                    	String salario = elemento.getElementsByTagName("Salario").item(0).getTextContent();
                   	 
                    	System.out.println("Empleado encontrado:");
                    	System.out.println("ID: " + id);
                    	System.out.println("DNI: " + dni);
                    	System.out.println("Nombre: " + nombre);
                    	System.out.println("Departamento: " + departamento);
                    	System.out.println("Salario: " + salario);
                    	break;
                	}
            	}
        	}


        	if (!encontrado) {
            	System.out.println("Empleado con ID " + idBuscado + " no encontrado en el XML.");
        	}


    	} catch (Exception e) {
        	System.out.println("Error al buscar el empleado en el XML: " + e.getMessage());
    	}
	}


	static void CrearElemento(String datoEmpleado, String valor, Element raiz, Document document) {
    	Element e1 = document.createElement(datoEmpleado);
    	Text text = document.createTextNode(valor);
    	raiz.appendChild(e1);
    	e1.appendChild(text);
	}


}
