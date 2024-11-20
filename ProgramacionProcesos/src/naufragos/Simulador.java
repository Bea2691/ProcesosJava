package naufragos;

public class Simulador {
    public static void main(String[] args) {
        Isla isla = new Isla(); 
       
        Barca barca1 = new Barca(isla, 1);
        Barca barca2 = new Barca(isla, 2);
        Barca barca3 = new Barca(isla, 3);

        //Lanzamos  barcas
        barca1.start();
        barca2.start();
        barca3.start();

        //Esperamos a que todas las barcas terminen
        try {
            barca1.join();
            barca2.join();
            barca3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Todos los náufragos han sido rescatados.");
    }
}
