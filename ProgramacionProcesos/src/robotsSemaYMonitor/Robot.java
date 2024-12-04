package robotsSemaYMonitor;

public class Robot extends Thread {
    private final Mesa mesa;
    private final String tipo;

    public Robot(Mesa mesa, String tipo) {
        this.mesa = mesa;
        this.tipo = tipo;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String objeto = mesa.recogerObjeto(tipo);
                if (objeto != null) {
                    System.out.println(tipo + " recogió y empaquetó: " + objeto);
                    mesa.incrementarContador();
                    Thread.sleep(500); // simula el tiempo de trabajo del robot
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
