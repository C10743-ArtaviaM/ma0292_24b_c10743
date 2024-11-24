import java.awt.*;
import java.util.Scanner;

import javax.swing.*;

public class Loader {
    /* =-=-= CONSTANTES =-=-= */
    /* =-=-= Declaracion de Constantes de Tipo String =-=-= */
    public static final String BLUE = "\033[34m"; // Azul
    public static final String CYAN = "\033[36m"; // Cian
    public static final String GREEN = "\033[32m"; // Verde
    public static final String RESET = "\033[0m"; // Resetear color
    public static final String YELLOW = "\033[33m"; // Amarillo

    /**
     * Método que prepara el juego antes de iniciar el menú principal.
     */
    public static void load() {
        process(5000);
        /* =-=-= Declaracion de Variables de Tipo Clase Menu =-=-= */
        Menu menu;

        menu = dataAccess();

        // Pasa el JFrame cargado como argumento al método run de Menu.
        process(2000);
        menu.run();
    }

    public static Menu dataAccess() {
        /* =-=-= Declaracion de Variables de Tipo Clase Scanner =-=-= */
        Scanner in;
        /* =-=-= Declaracion de Variables de Tipo String[] =-=-= */
        String[] playersList;

        playersList = new String[3];
        in = new Scanner(System.in);

        System.out.println(GREEN + "\n---- !Bienvenidos al juego Determinar¡ ----" + RESET);
        System.out.println(
                YELLOW + "Objetivo: Resolver la matriz con mayor determinante y maximizar tu puntaje!!!" + RESET);

        for (int i = 0; i < playersList.length; i++) {
            System.out.print(CYAN + "Digite el nombre para el jugador " + (i + 1) + ": " + RESET);
            playersList[i] = in.nextLine();
            System.out.println(BLUE + "Bienvenido " + RESET + GREEN + playersList[i] + "!" + RESET);
        }

        return new Menu(playersList);
    }

    /**
     * Método que simula una pantalla de carga.
     */
    public static void process(int time) {
        /* =-=-= Declaracion de Variables de Tipo Clase JFrame =-=-= */
        JFrame loadingFrame;

        /* =-=-= Declaracion de Variables de Tipo Clase JProgressBar =-=-= */
        JProgressBar progressBar;

        loadingFrame = new JFrame("DETERMINAR | LOADING . . .");

        loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadingFrame.setSize(500, 100);
        loadingFrame.setLocationRelativeTo(null); // Centralizacion de la ventana en la pantalla.

        // Creacion de una barra de progreso indeterminada para la carga.
        progressBar = new JProgressBar();

        progressBar.setIndeterminate(true);
        progressBar.setString("Cargando...");
        progressBar.setStringPainted(true);
        loadingFrame.add(progressBar, BorderLayout.CENTER);

        // Hace visible el JFrame de carga.
        loadingFrame.setVisible(true);

        // Simulacion de una carga de x segundos antes de continuar.
        try {
            Thread.sleep(time); // Espera x segundos ([x * 1000] milisegundos).
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Oculta el JFrame de carga después de los 5 segundos.
        loadingFrame.setVisible(false);
        loadingFrame.dispose(); // Liberacion de recursos del JFrame de carga.
    }
}
