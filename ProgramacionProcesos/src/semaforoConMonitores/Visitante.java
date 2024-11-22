package semaforoConMonitores;

public class Visitante extends Thread {
    private final int id;
    private final CentroComercial centro;

    public Visitante(int id, CentroComercial centro) {
        this.id = id;
        this.centro = centro;
    }

    @Override
    public void run() {
        centro.entrarCentro(id); //Para que el hilo entre al centro comercial, de ahi llama al metodo entrarSala
        centro.salirCentro(id); //Para que el hilo salga del CC
    }
}