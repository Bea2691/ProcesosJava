package parqueMonitor;
public class Monitor {
    
    private int totalVisitantes; 
    private int[] visitantesPorTorno; 
    private boolean terminado; 
    private final int maxVisitantes = 100; 

    public Monitor(int numTornos) {
        this.totalVisitantes = 0;
        this.visitantesPorTorno = new int[numTornos];
        this.terminado = false;
    }

    public synchronized void registrarEntrada(int idTorno) {
        while (totalVisitantes >= maxVisitantes) { 
            try {
                System.out.println("Torno " + idTorno + " espera: parque lleno.");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        visitantesPorTorno[idTorno]++;
        totalVisitantes++;
        System.out.println("Torno " + idTorno + " registró un visitante. Total visitantes: " + totalVisitantes);
        notifyAll(); 
    }

    public synchronized void procesarEntradas() {
        while (!terminado) { //Esperar hasta que se marque como terminado
            try {
                wait(); //Esperar nuevas entradas o notificación de fin
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mostrarEstadisticas();
    }

    public synchronized void terminarActividad() {
        terminado = true;
        notifyAll(); //Despertar hilo
    }

    public synchronized void mostrarEstadisticas() {
        System.out.println("Estadísticas finales:");
        System.out.println("Total de visitantes: " + totalVisitantes);
        for (int i = 0; i < visitantesPorTorno.length; i++) {
            System.out.println("Torno " + i + ": " + visitantesPorTorno[i] + " visitantes.");
        }
    }
}