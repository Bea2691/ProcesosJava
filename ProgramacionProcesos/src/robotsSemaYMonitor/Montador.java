package robotsSemaYMonitor;

import java.util.Random;

public class Montador extends Thread {
    private final Mesa mesa;
    private final Random random = new Random();

    public Montador(Mesa mesa) {
        this.mesa = mesa;
    }

    @Override
    public void run() {
        String[] tipos = {"TipoA", "TipoB", "TipoC"};
        try {
            while (true) {
                String tipo = tipos[random.nextInt(tipos.length)];
                mesa.ponerObjeto(tipo);
                System.out.println("Montador colocó: " + tipo);
                Thread.sleep(500); // simula el tiempo de trabajo del montador
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
