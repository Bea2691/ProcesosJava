package examenBeatrizRedondo;

public class Ejercito2 extends Thread{

	Monitor stark;
	
	
	Ejercito2(Monitor stark){
		
		this.stark=stark;
	}
	
	public void run() {
		while(true) {
			try {
				
				Thread.sleep(1000); //Simulamos tiempo
				stark.ataque();		//Lanzamos ataque
				
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
   		 
        }

	}
	
	
	
}
