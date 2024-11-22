package Examen1Eva;


public class Ejercito1 extends Thread{

Monitor mon;
    
    Ejercito1(Monitor mon){
    	
        this.mon = mon;
     
    }
    public void run(){
    	
    	 while(true){ 
    		 
    		 try {
    			 
         		Thread.sleep(1000);
         		
         		//método de la clase monitor
				mon.ataque();
				    		
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		 
         }
    	
    }
	
}
