package sincronizacionCuentaBancaria;

/* Realiza una aplicación para que los alumnos de 2º de DAM puedan ingresar 
 * y retirar dinero para las gestiones de la clase.
   No se podrán solapar las tareas en el tiempo.
 * */
public class principal {
	
	
	public static void main(String[] args) throws InterruptedException {
		cuenta c1 = new cuenta("asert",0);
		
		usuario u1 = new usuario("123","Javier",c1,20);
		usuario u2 = new usuario("456","Maria",c1,25);
		usuario u3 = new usuario("789","Alfredo",c1,14);
		usuario u4 = new usuario("525","Beatriz",c1,63);
		usuario u5 = new usuario("124","Miguel",c1,41);
		usuario u6 = new usuario("368","Lorena",c1,85);
		usuario u7 = new usuario("486","Lola",c1,98);
		usuario u8 = new usuario("111","Jesus",c1,10);
		usuario u9 = new usuario("365","Carmen",c1,8);
		
		
		
		u1.start();
		u2.start();
		u3.start();
		u4.start();
		u5.start();
		u6.start();
		u7.start();
		u8.start();
		u9.start();
		
		u1.join();
		u2.join();
		u3.join();
		u4.join();
		u5.join();
		u6.join();
		u7.join();
		u8.join();
		u9.join();
		
		System.out.println("Cantidad actual:" + c1.getSaldo());
				

	}

}