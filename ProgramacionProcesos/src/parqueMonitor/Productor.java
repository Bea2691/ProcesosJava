package parqueMonitor;

public class Productor extends Thread {

    private Monitor monitor;
    private int idTorno;
    private int entradas;

    public Productor(Monitor monitor, int idTorno, int entradas) {
        this.monitor = monitor;
        this.idTorno = idTorno;
        this.entradas = entradas;
    }

    public void run() {
        for (int i = 0; i < entradas; i++) {
            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monitor.registrarEntrada(idTorno);
        }
    }
}