package semaforoConMonitores;

public class Main {
    public static void main(String[] args) {
        CentroComercial centro = new CentroComercial(); //Creamos el centro comercial

        //Creamos 20 visitantes 
        for (int i = 1; i <= 20; i++) {
            Visitante visitante = new Visitante(i, centro);
            visitante.start(); //Comenzamos el hilo
        }
    }
}

