package naufragos;

public class Isla {
    private int totalNaufragos = 50; 

   
    public synchronized int rescatarNaufragos(int idBarca) throws InterruptedException {
       
        while (totalNaufragos == 0) {
            wait(); //La barca espera si NO hay peña
        }
        
        
        int cantidad = Math.min((int) (Math.random() * 10) + 1, totalNaufragos);
        totalNaufragos -= cantidad;

        //Mostramos info
        System.out.println("Barca " + idBarca + " rescata " + cantidad + " naufragos. Quedan " + totalNaufragos + " en la isla.");

        //Notificamos a las otras barcas si quedan aun
        notifyAll();

        return cantidad; //Retornamos la cantidad de náufragos que rescató esta barca
    }

    //Verifica si quedan náufragos en la isla
    public synchronized boolean hayNaufragos() {
        return totalNaufragos > 0;
    }
}
