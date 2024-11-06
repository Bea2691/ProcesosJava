package sincronizacionCuentaBancaria;

public class usuario extends Thread{
	protected String dni, nombre;
	protected int cantidad;
	protected cuenta c;
	
	protected usuario(String dni, String nombre, cuenta c, int cantidad) {
		this.dni = dni;
		this.nombre = nombre;
		this.c = c;
		this.cantidad=cantidad;
		
	}
	protected String getDni() {
		return dni;
	}
	protected void setDni(String dni) {
		this.dni = dni;
	}
	protected String getNombre() {
		return nombre;
	}
	protected void setNombre(String nombre) {
		this.nombre = nombre;
	}
	protected cuenta getC() {
		return c;
	}
	protected void setC(cuenta c) {
		this.c = c;
	}
	protected void ingresar(double cantidad) {
	    synchronized(c) {  // Sincroniza en el objeto `cuenta` compartido
	        try {
	            Thread.sleep(1000);
	            System.out.println("El usuario " + nombre + " ha entrado a ingresar");
	            c.setSaldo(c.getSaldo() + cantidad);
	            System.out.println("El usuario " + nombre + " ha salido de ingresar");
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}

	protected void retirar(double cantidad) {
	    synchronized(c) {  // Sincroniza en el objeto `cuenta` compartido
	        try {
	            Thread.sleep(1000);
	            System.out.println("El usuario " + nombre + " ha entrado a retirar");
	            c.setSaldo(c.getSaldo() - cantidad);
	            System.out.println("El usuario " + nombre + " ha salido de retirar");
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public void run() {
		ingresar(cantidad);
		
		
		retirar(cantidad);
		
		
	}
	
}