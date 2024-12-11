package ejercicioTCPyUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class serverUDP {

	  public static void main(String[] args) {
	        DatagramSocket socket;
	        try {
	            System.out.println("(Servidor) Creando socket...");
	            socket = new DatagramSocket(49171);
	            System.out.println("(Servidor) Recibiendo datagrama...");

	            byte[] bufferLectura = new byte[64];

	            DatagramPacket datagramaEntrada = new DatagramPacket(bufferLectura, bufferLectura.length);

	            socket.receive(datagramaEntrada);
	            String mensajeRecibido = new String(bufferLectura).trim();
	            
	            System.out.println("(Servidor) Edad recibida del cliente: " + mensajeRecibido);

	            
	            int edad = Integer.parseInt(mensajeRecibido);
	            String respuesta = comprobarEdad(edad);

	            System.out.println("(Servidor) Enviando respuesta al cliente...");
	            byte[] mensajeEnviado = respuesta.getBytes();
	            DatagramPacket datagramaSalida = new DatagramPacket(mensajeEnviado, mensajeEnviado.length, datagramaEntrada.getAddress(), datagramaEntrada.getPort());

	            socket.send(datagramaSalida);

	            System.out.println("(Servidor) Cerrando socket...");
	            socket.close();
	            System.out.println("(Servidor) Socket cerrado...");
	        } catch (SocketException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (NumberFormatException e) {
	            System.out.println("(Servidor) Error: El mensaje recibido no es un número válido.");
	        }
	    }

	    public static String comprobarEdad(int edad) {
	        if (edad >= 18) {
	            return "Eres mayor de edad";
	        } else {
	            return "Eres menor de edad";
	        }
	    }
	}