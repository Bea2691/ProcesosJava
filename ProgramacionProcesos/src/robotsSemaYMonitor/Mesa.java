package robotsSemaYMonitor;

import java.util.concurrent.Semaphore;

public class Mesa {
    private final String[] objetos = new String[5];
    private final Semaphore espacioDisponible = new Semaphore(5);
    private int indice = 0;
    private int contador = 0;

    public synchronized void ponerObjeto(String objeto) throws InterruptedException {
        espacioDisponible.acquire();
        while (indice == objetos.length) {
            wait();
        }
        objetos[indice] = objeto;
        indice++;
        notifyAll();
    }

    public synchronized String recogerObjeto(String tipo) throws InterruptedException {
        while (indice == 0 || !hayObjetoDeTipo(tipo)) {
            wait();
        }
        for (int i = 0; i < indice; i++) {
            if (objetos[i].equals(tipo)) {
                String objeto = objetos[i];
                objetos[i] = objetos[indice - 1];
                objetos[indice - 1] = null;
                indice--;
                espacioDisponible.release();
                notifyAll();
                return objeto;
            }
        }
        return null;
    }

    private boolean hayObjetoDeTipo(String tipo) {
        for (int i = 0; i < indice; i++) {
            if (objetos[i].equals(tipo)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void incrementarContador() {
        contador++;
        System.out.println("Total de objetos empaquetados: " + contador);
    }
}
