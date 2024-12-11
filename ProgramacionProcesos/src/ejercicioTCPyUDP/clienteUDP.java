package ejercicioTCPyUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class clienteUDP {

	public static void main(String[] args) {
		String strMensaje;
		Scanner sc= new Scanner(System.in);
		DatagramSocket socketUDP;
		try {
			System.out.println("Cliente): Estableciendo parámetros de conexión...");
			
			InetAddress hostServidor = InetAddress.getByName("localhost");
			int puertoServidor = 49171;
			System.out.println("(Cliente) Creando socket...");
			socketUDP = new DatagramSocket();
			
			
			
			System.out.println("Introduzca su edad: ");
			strMensaje= sc.next();
			
			byte[] mensaje = strMensaje.getBytes();
			DatagramPacket peticion = new DatagramPacket(mensaje,mensaje.length,hostServidor,puertoServidor);
			socketUDP.send(peticion);
			
			System.out.println("(Cliente) Recibiendo respuesta...");
			byte[] buffer = new byte[64];
			DatagramPacket respuesta = new DatagramPacket(buffer,buffer.length,hostServidor,puertoServidor);
			socketUDP.receive(respuesta);
			System.out.println("(Cliente) Mensaje recibido: "+ new String(buffer));
			System.out.println("(Cliente) Cerrando socket...");
			socketUDP.close();
			System.out.println("(Cliente) Socket cerrado...");
			
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
