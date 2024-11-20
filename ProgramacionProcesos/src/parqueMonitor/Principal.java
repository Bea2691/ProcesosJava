package parqueMonitor;

public class Principal {

    public static void main(String[] args) {
        int numTornos = 4; 
        int maxEntradasPorTorno = 30; 
        int totalEntradas = 100; 

        Monitor monitor = new Monitor(numTornos);
        
        
        Productor[] tornos = new Productor[numTornos];
        for (int i = 0; i < numTornos; i++) {
            tornos[i] = new Productor(monitor, i, totalEntradas / numTornos);
            tornos[i].start();
        }

        
        Consumidor consumidor = new Consumidor(monitor);
        consumidor.start();

        
        for (Productor torno : tornos) {
            try {
                torno.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

       
        monitor.terminarActividad();

       
        try {
            consumidor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}