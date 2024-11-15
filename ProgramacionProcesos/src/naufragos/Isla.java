package naufragos;

public class Isla {
    private int totalNaufragos = 50; // Inicialmente hay 50 náufragos en la isla

    // Método para que una barca rescate náufragos de la isla
    public synchronized int rescatarNaufragos(int idBarca) throws InterruptedException {
        // Si no hay náufragos, la barca debe esperar
        while (totalNaufragos == 0) {
            wait(); // La barca espera si no hay náufragos
        }

        // Determinamos cuántos náufragos rescatará esta barca (entre 1 y 10)
        int cantidad = Math.min((int) (Math.random() * 10) + 1, totalNaufragos);
        totalNaufragos -= cantidad;

        // Mostrar qué barca está rescatando los náufragos
        System.out.println("Barca " + idBarca + " rescata " + cantidad + " naufragos. Quedan " + totalNaufragos + " en la isla.");

        // Notificamos a las barcas si quedan náufragos
        notifyAll();

        return cantidad; // Retornamos la cantidad de náufragos que rescató esta barca
    }

    // Verifica si quedan náufragos en la isla
    public synchronized boolean hayNaufragos() {
        return totalNaufragos > 0;
    }
}
