import java.awt.*;
import java.util.Scanner;

import javax.swing.*;

public class Loader {
    public static final String BLUE = "\033[34m"; // Azul
    public static final String CYAN = "\033[36m"; // Cian
    public static final String GREEN = "\033[32m"; // Verde
    public static final String RESET = "\033[0m"; // Resetear color
    public static final String YELLOW = "\033[33m"; // Amarillo
    public static final String RED = "\033[31m"; // Rojo

    //public static void main(String[] args) {
    //    Loader loader = new Loader(); // Crear instancia de Loader
    //    loader.displayMenu(); // Llama al método de instancia
    //}

    public void displayMenu() {
        Scanner in = new Scanner(System.in);
        int option;

        while (true) {
            System.out.println(GREEN + "\n---- ¡Bienvenidos al juego Determinar! ----" + RESET);
            System.out.println("Seleccione una opción:");
            System.out.println(YELLOW + "1. Leer instrucciones" + RESET);
            System.out.println(YELLOW + "2. Iniciar el juego" + RESET);
            System.out.println(YELLOW + "3. Salir" + RESET);

            System.out.print(CYAN + "Opción: " + RESET);
            try {
                option = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(RED + "Por favor, ingrese un número válido." + RESET);
                continue;
            }

            switch (option) {
                case 1:
                    showInstructions();
                    break;
                case 2:
                    startGame();
                    return; // Sal del bucle del menú después de iniciar el juego.
                case 3:
                    System.out.println(BLUE + "Gracias por jugar. ¡Hasta la próxima!" + RESET);
                    return; // Finaliza el programa.
                default:
                    System.out.println(RED + "Opción no válida. Intente nuevamente." + RESET);
            }
        }
    }

    public void showInstructions() {
        System.out.println(GREEN + "\n---- Instrucciones ----" + RESET);
        System.out.println(YELLOW + "Objetivo del juego:" + RESET);
        System.out.println(" - Resolver la matriz con mayor determinante.");
        System.out.println(" - Maximizar tu puntaje.");
        System.out.println(" - Competir contra otros jugadores para ser el mejor.");
        System.out.println(CYAN + "\nPresione ENTER para volver al menú principal." + RESET);

        new Scanner(System.in).nextLine(); // Pausa para que el usuario lea las instrucciones.
    }

    public void startGame() {
        System.out.println(GREEN + "\n---- ¡Iniciando el juego! ----" + RESET);
        process(5000); // Pantalla de carga.

        Menu menu = dataAccess(); // Inicializa `Menu` con los nombres de los jugadores.
        menu.setupGame(); // Configura el juego (antes de iniciar las rondas).
        process(2000); // Simula una carga adicional.
        menu.run(); // Ejecuta el juego.
    }

    public Menu dataAccess() {
        Scanner in = new Scanner(System.in);
        String[] playersList = new String[3];

        System.out.println(GREEN + "\n---- ¡Preparando el juego! ----" + RESET);

        for (int i = 0; i < playersList.length; i++) {
            System.out.print(CYAN + "Digite el nombre para el jugador " + (i + 1) + ": " + RESET);
            playersList[i] = in.nextLine();
            System.out.println(BLUE + "Bienvenido " + RESET + GREEN + playersList[i] + "!" + RESET);
        }

        return new Menu(playersList); // Devuelve un objeto Menu con la lista de jugadores.
    }

    public void process(int time) {
        JFrame loadingFrame = new JFrame("DETERMINAR | LOADING . . .");
        JProgressBar progressBar = new JProgressBar();

        loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadingFrame.setSize(500, 100);
        loadingFrame.setLocationRelativeTo(null);

        progressBar.setIndeterminate(true);
        progressBar.setString("Cargando...");
        progressBar.setStringPainted(true);
        loadingFrame.add(progressBar, BorderLayout.CENTER);

        loadingFrame.setVisible(true);

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadingFrame.setVisible(false);
        loadingFrame.dispose();
    }
}