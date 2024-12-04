package examenBeatrizRedondo;

public class Ejercito1 extends Thread{

	Monitor lannister;
	
	
	Ejercito1(Monitor lannister){
		
		this.lannister=lannister;
		
	}
	public void run() {
		while(true){ 
   		  try {
   			 	//Simulamos el tiempo
        		Thread.sleep(1000);
        		
        		//Lanzamos ataque
				lannister.ataque();
				    		
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
   		 
        }

	}
	
	
	
}
