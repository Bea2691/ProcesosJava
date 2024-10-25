package Tema1_FicherosXML;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class CrearEmpleado {

	public static void main(String[] args) throws IOException, TransformerException {
		
		int id,dep, posicion=0;
		double salario;
		char apellido[] = new char[10], aux;
		String apellidos;
		
		File fichero = new File("C://Users//Alum_DAM//Desktop//nuevodir3//empleados.dat");
		
		System.out.println("Fichero: " + fichero);
		
		//Fuente de datos para el contenido del fichero XML
		RandomAccessFile file = new RandomAccessFile(fichero, "r");
		
		//DocumentBuilderFactory y DocumentBuilder: para cargar documentos desde una fuente de datos
		//Creamos una instancia de DocumentBuilderFactory (factory) para construir el parser
		//Excepcion que puede ocurrir: ParserConfigurationException 
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			//Obtener una instancia DOM de un documento XML
			//Se usa el metodo newDocumentBuilder de la clase DOcumentBuilderFactory
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			//Se trata de crear un documento vacio document y el nodo raiz es empleados
			//Para crear objetos Document con un nodo raiz se usa DOMImplementation
			//se crea una instancia del registro DOMImplementation para crear despuesla raiz
			
			DOMImplementation implementation = builder.getDOMImplementation();
			//createDocument(Espacio de nombres(URI)/Null, Nodo raiz, Tipo de documento/Null)
			Document document = implementation.createDocument(null, "Empleados", null);
			
			//Asignar versión del documentos XML
			//Version 1.1 en caso de querer usar caraceres UNICODE, sino usar -> 1.0
			document.setXmlVersion("1.0");
			
			//Recorrer el fichero empleados para crear un nodo por cada registro
			//Cada nodo va a tener 4 hijo, uno por cada dato del empleado.
			for(;;) { //en un futuro nada de bucles infinito usar un do while o while
				file.seek(posicion); //Posiciona al principio del fichero
				//Lee el id del empleado
				id= file.readInt();
				//Lee apellido
				for(int i=0; i<apellido.length;i++) {
					aux=file.readChar();
					apellido[i]= aux;
				}
				apellidos = new String(apellido);
				//Lee departamento
				dep= file.readInt();
				
				//Lee salario
				salario=file.readDouble();
				if(id>0) {
					//Creamos elemento tomando como etiqueta el parametro indicado (createElement)
					Element raiz = document.createElement("empleado");
					//Se añade el nodo a la raiz del documento
					document.getDocumentElement().appendChild(raiz);
					//Se crea un metodo para la creacion de los elementos. 
					//A este metodo se le pasa la etiqueta, la información, el nodo raiz y la referencia al documento.
					//Añadir id
					CrearElemento("id", Integer.toString(id),raiz, document);
					//Añadir apellido
					CrearElemento("apellido", apellidos.trim(),raiz, document);
					//Añadir departamento
					CrearElemento("dep", Integer.toString(dep),raiz, document);
					//Añadir salario
					CrearElemento("salario", Double.toString(salario),raiz, document);
									
							
				}
				//Longitud del registro = 36
				posicion = posicion + 36;
				
				if(file.getFilePointer() == file.length()) {
					break; // Finaliza el bucle for
					}
				
				//Enlazamos con el documento creado (de tipo DOM)
				Source source = new DOMSource(document);
				
				//Result result = new StreamResult(System.out);   Salida por consola
				//Resultado en un fichero
				Result result = new StreamResult(new File("C://Users//Alum_DAM//Desktop//nuevodir3//empleados.xml"));
				
				//Transforma el DOM creado en un documento XML con TransformerFactory
				//En primer lugar se instancia la clase Transformer
				Transformer transformar = TransformerFactory.newInstance().newTransformer();
				
				//Se transforma la información generada (source) a un fichero del que se ha creado su descriptor (result)
				transformar.transform(source, result);
				
				
				
			}
									
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		file.close();
		

	}
	static void CrearElemento(String datoEmple, String valor, Element raiz, Document document) {
		//Crea la  etiqueta del elemento hijo recibido (datoEmple) y el contenido (valor)
		//Pertenece al elemento raiz (raiz) de documento (document)
		Element elem = document.createElement(datoEmple);
		//Se añade el contenido del nodo creado
		Text text = document.createTextNode(valor);
		//Añadir el elemento hijo a su raiz
		raiz.appendChild(elem);
		//Añadir el texto al nodo hijo
		elem.appendChild(text);
			
	}

}
