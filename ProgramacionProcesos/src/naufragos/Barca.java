package naufragos;

public class Barca extends Thread {
    private Isla isla;
    private int id;

    public Barca(Isla isla, int id) {
        this.isla = isla;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (isla.hayNaufragos()) {
                //Rescatamos náufragos
                int naufragosRescatados = isla.rescatarNaufragos(id);

                //Simulamos que la barca tarda en rescatarlos
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
