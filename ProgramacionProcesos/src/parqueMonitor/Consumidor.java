package parqueMonitor;

public class Consumidor extends Thread {

    private Monitor monitor;

    public Consumidor(Monitor monitor) {
        this.monitor = monitor;
    }

    public void run() {
        monitor.procesarEntradas();
    }
}

