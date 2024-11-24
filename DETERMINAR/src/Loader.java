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
        /* =-=-= Declaracion de Variables de Tipo Clase Menu =-=-= */
        Menu menu;
        int option;

        process(5000);

        option = selectMode();

        process(2000);

        if (option == 1) {
            MenuGUI gui = dataAccessViaGUI();
            gui.run();
        } else if (option == 2) {
            menu = dataAccessViaConsole();
            menu.run();
        }

        System.out.println(CYAN + "Gracias por visitar. ¡Vuelve pronto!" + RESET);
    }

    public static int selectMode() {
        Scanner in;
        int modeOption;

        in = new Scanner(System.in);

        System.out.println(BLUE + "\n=== Bienvenido a DETERMINAR ===" + RESET);
        System.out.println("Selecciona el modo de juego:");
        System.out.println("1. Interfaz Gráfica");
        System.out.println("2. Consola Integrada");
        System.out.println("3. Salir");
        System.out.print("Tu opción: ");

        try {
            modeOption = Integer.parseInt(in.nextLine());
            if (modeOption < 1 || modeOption > 3) {
                System.out.println("Opción inválida. Encerio desde ya estas fallando?\nIntenta de nuevo!");
                return selectMode();
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Encerio desde ya estas fallando?\nIntenta de nuevo!");
            return selectMode();
        }

        return modeOption;
    }

    public static Menu dataAccessViaConsole() {
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

    public static MenuGUI dataAccessViaGUI() {
        /* =-=-= Declaracion de Variables =-=-= */
        String[] playersList = new String[3];

        // Recolectar nombres de los jugadores utilizando JOptionPane
        for (int i = 0; i < playersList.length; i++) {
            String playerName = JOptionPane.showInputDialog(null, "Ingrese el nombre del jugador " + (i + 1),
                    "Ingreso de Jugadores", JOptionPane.QUESTION_MESSAGE);
            if (playerName != null && !playerName.trim().isEmpty()) {
                playersList[i] = playerName.trim();
            } else {
                JOptionPane.showMessageDialog(null, "El nombre del jugador no puede estar vacío.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                i--; // Para que vuelva a pedir el nombre del jugador que quedó vacío
            }
        }

        // Crear y retornar la instancia de MenuGUI
        return new MenuGUI(playersList);
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
