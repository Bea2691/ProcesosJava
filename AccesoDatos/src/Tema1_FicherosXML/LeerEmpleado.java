package Tema1_FicherosXML;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LeerEmpleado {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// Se puede leer un documento XML con una Instancia de DocumentBuilderFactory para construirse el parser 
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		
		//Obtener una instancia DOM del documento XML con el metodo DocumentBuilder
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		//Cargar el documento XML (parsear), con el metodo parse; Devuelve un nuevo objeto de un doc DOM
		Document document = builder.parse(new File("C://Users//Alum_DAM//Desktop//nuevodir3//empleados.xml"));
		
		//Eliminar nodos vacios y enlazar los adyacentes , si hubiera alguno
		
		System.out.printf("Elemento raiz: %s %n", document.getDocumentElement().getNodeName());
		//System.out.printf("Elemento raiz: " + document.getDocumentElement().getNodeName());  ES LO MISMO
		
		//Crear una lista con todos los nodos de empleado, de todo el documento
		//Tiene que existir la etiqueta en el fichero XML (distingue entre mayus y min)
		NodeList empleados = document.getElementsByTagName("empleado");
		
		System.out.println("Nodos de empeados en la lista: " + empleados.getLength());
		
		//Recorremos la lista
		
		for(int i = 0;i<empleados.getLength();i++) {
			//Obtener un nodo empleado completo
			Node emple = empleados.item(i);
			
			//Hay que comprobar que el nodo es realmente un elemento, se usa el metodo getNodeType()
			//Hay que comprobar que es de tipo 'Element_node'
			if (emple.getNodeType()== Node.ELEMENT_NODE) {
				//Etraer los elementos del nodo
				Element elemento = (Element) emple; 
				
				//Escribir el contenido/texto de cada elemento de empleado
				System.out.println("ID: " + elemento.getElementsByTagName("id").item(0).getTextContent());
				System.out.println("Apellido: " + elemento.getElementsByTagName("apellido").item(0).getTextContent());
				System.out.println("Departamente: " + elemento.getElementsByTagName("dep").item(0).getTextContent());
				System.out.println("Salario: " + elemento.getElementsByTagName("salario").item(0).getTextContent());
				
			}
			
		}
		
			

	}

}
