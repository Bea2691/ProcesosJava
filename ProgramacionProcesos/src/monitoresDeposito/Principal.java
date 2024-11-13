package monitoresDeposito;

public class Principal {
	  
    public static void main(String[] args) {
        
        Monitor varCompartida=new Monitor();
        
        new Productor(varCompartida).start();
        new Consumidor(varCompartida).start();
    }
}
