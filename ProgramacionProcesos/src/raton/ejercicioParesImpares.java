package raton;

import java.util.Scanner;

// Clase principal que implementa Runnable
public class ejercicioParesImpares implements Runnable {
    private int limite;

    // Variables estáticas para contar pares e impares
    private static int contadorPares = 0;
    private static int contadorImpares = 0;

    // Constructor que recibe el límite
    public ejercicioParesImpares(int limite) {
        this.limite = limite;
    }

    // Clase interna para contar números pares
    class HiloPares implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i <= limite; i += 2) {
                System.out.println("Par: " + i);
                contadorPares++; // Incrementa el contador de pares
            }
            System.out.println("Total de pares: " + contadorPares); // Muestra el total de pares
        }
    }

    // Clase interna para contar números impares
    class HiloImpares implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= limite; i += 2) {
                System.out.println("Impar: " + i);
                contadorImpares++; // Incrementa el contador de impares
            }
            System.out.println("Total de impares: " + contadorImpares); // Muestra el total de impares
        }
    }

    @Override
    public void run() {
        // Aquí podrías añadir alguna lógica si es necesario
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce un número: ");
        int numero = sc.nextInt();

        // Instanciar la clase principal con el número dado
        ejercicioParesImpares ejercicio = new ejercicioParesImpares(numero);

        // Crear los objetos Runnable para pares e impares
        HiloPares hiloPares = ejercicio.new HiloPares();
        HiloImpares hiloImpares = ejercicio.new HiloImpares();

        // Crear los hilos con los objetos Runnable
        Thread threadPares = new Thread(hiloPares);
        Thread threadImpares = new Thread(hiloImpares);

        // Iniciar ambos hilos
        threadPares.start();
        threadImpares.start();
    }
}
