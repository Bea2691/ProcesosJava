package semaforoConMonitores;

import java.util.concurrent.Semaphore;

public class CentroComercial {
    private final Semaphore semaforo = new Semaphore(10); //Máximo 10 personas en el centro, es muy pequeño
    private final String[] salas = {"sala 1", "sala 2", "sala 3", "sala 4"}; //No son objetos porque no tienen mas datos que su nombre
    private final boolean[] salaOcupada = {false, false, false, false}; //Estado de cada sala, por no crear un boolean por sala, va en un array

    //Este método se usa para entrar por el semaforo al centro comercial
    public void entrarCentro(int visitanteID) {
        try {
            semaforo.acquire(); 
            System.out.println("Visitante " + visitanteID + " ha entrado al centro comercial.");
            entrarSala(visitanteID); //Intentar entrar a una sala dentro del CC
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //Y este para salir del CC
    public void salirCentro(int visitanteID) {
        System.out.println("Visitante " + visitanteID + " ha salido del centro comercial.");
        semaforo.release(); // Liberar un lugar en el centro comercial
    }

    //Método para la entrada a la sala
    public void entrarSala(int visitanteID) {
        int salaID = -1; //Es -1 porque está sin asignar aún la sala
        while (salaID == -1) { 
            for (int i = 0; i < salas.length; i++) { //Al usar bucle for la primera asignacion de salas sera por orden de 0 hasta .lenght (0,1,2,3) y despues segun quedan vacias
            										//Se podría usar igualmente if-else sala 1, sala 2... o un switch y case con cada sala
                synchronized (this) { 				//Si pongo al metodo como public synchronized solo entran en la 'sala 1', por eso esta de esta manera que no me gusta
                    if (!salaOcupada[i]) { //Si está libre
                        salaOcupada[i] = true; //Marcar la sala como ocupada
                        salaID = i; // Asignar esta sala al visitante
                        System.out.println("Visitante " + visitanteID + " ha entrado en " + salas[salaID] + ".");
                        break;
                    }
                }
            } 

            if (salaID == -1) { //Si no te pueden asignar sala porque no hay libres
                try {
                    System.out.println("Visitante " + visitanteID + " esperando una sala libre...");
                    synchronized (this) {
                        wait(); //A esperar
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        usarSala(visitanteID, salaID); //Simulamos que espera

        //Aquí sale de la sala y notifica a otros visitantes que estan esperando
        liberarSala(visitanteID, salaID);
    }

   
   
    //Método para liberar la sala
    public synchronized void liberarSala(int visitanteID, int salaID) {
        salaOcupada[salaID] = false; //Liberamos la sala que se le asignó al visitante volviendola False de nuevo
        System.out.println("Visitante " + visitanteID + " ha salido de " + salas[salaID] + ".");
        notifyAll(); //Notificamos a los que esperan
    }

    private void usarSala(int visitanteID, int salaID) {
        try {
            //Simulamos el tiempo
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
