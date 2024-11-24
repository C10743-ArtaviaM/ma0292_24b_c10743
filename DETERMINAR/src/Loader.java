import java.awt.*;
import java.util.Scanner;
import javax.swing.*;

/**
 * La clase {@code Loader} es responsable de gestionar la carga inicial del
 * juego, la selección del modo de juego (interfaz gráfica o consola), y la
 * inicialización de los jugadores. También muestra una pantalla de carga
 * mientras el juego se está configurando.
 * 
 * @author Mauricio Artavia Monge C10743 - Kenneth Delgado Cárdenas C22540
 */
public class Loader {
    /* =-=-= CONSTANTES =-=-= */
    /* =-=-= Declaracion de Constantes de Tipo String =-=-= */
    public static final String BLUE = "\033[34m"; // Azul
    public static final String CYAN = "\033[36m"; // Cian
    public static final String GREEN = "\033[32m"; // Verde
    public static final String RED = "\033[31m"; // Rojo
    public static final String RESET = "\033[0m"; // Resetear color
    public static final String YELLOW = "\033[33m"; // Amarillo

    /**
     * Muestra la pantalla de carga inicial y permite al usuario seleccionar el modo
     * de juego (interfaz gráfica o consola integrada). Dependiendo de la opción
     * seleccionada, se inicia el juego en el modo correspondiente.
     */
    public static void load() {
        /* =-=-= Declaracion de Variables de Tipo Integer */
        int option;

        /* =-=-= Declaracion de Variables de Tipo Clase Menu =-=-= */
        Menu menu;

        // Muestra la pantalla de carga por 5 segundos.
        process(5000);

        // Obtenemos la opcion de modo de juego.
        option = selectMode();

        // Espera 2 segundos antes de continuar.
        process(2000);

        // Inicia el juego segun la opcion seleccionada.
        if (option == 1) {
            // Via Interfaz Grafica.
            MenuGUI gui = dataAccessViaGUI();
            gui.run();
        } else if (option == 2) {
            // Via Consola.
            menu = dataAccessViaConsole();
            menu.run();
        } else {
            System.out.println(CYAN + "Gracias por visitar. ¡Vuelve pronto!" + RESET);
        }
    }

    /**
     * Solicita al usuario que seleccione el modo de juego mediante una interfaz de
     * consola. Las opciones disponibles son:
     * - Interfaz Gráfica
     * - Consola Integrada
     * - Salir
     *
     * @return La opción seleccionada por el usuario (1, 2 o 3).
     */
    public static int selectMode() {
        Scanner in;
        int modeOption;

        in = new Scanner(System.in);

        System.out.println(BLUE + "\n=== Bienvenido a DETERMINAR ===" + RESET);
        System.out.println("Selecciona el modo de juego:");
        System.out.println("1. Interfaz Gráfica");
        System.out.println("2. Consola Integrada");
        System.out.println("3. Salir");
        System.out.print(CYAN + "=> : " + RESET);

        try {
            modeOption = Integer.parseInt(in.nextLine());
            if (modeOption < 1 || modeOption > 3) {
                System.out.println(
                        RED + "Opción inválida. Encerio desde ya estas fallando?\nIntenta de nuevo!" + RESET);
                return selectMode();
            }
        } catch (NumberFormatException e) {
            System.out
                    .println(RED + "Entrada inválida. Encerio desde ya estas fallando?\nIntenta de nuevo!" + RESET);
            return selectMode();
        }

        return modeOption;
    }

    /**
     * Solicita los nombres de los jugadores a través de la consola. Una vez
     * obtenidos, crea una instancia de la clase {@link Menu} con los jugadores y la
     * retorna.
     *
     * @return Una instancia de {@link Menu} con los jugadores ingresados.
     */
    public static Menu dataAccessViaConsole() {
        /* =-=-= Declaracion de Variables de Tipo Clase Scanner =-=-= */
        boolean correctName;

        /* =-=-= Declaracion de Variables de Tipo Clase Scanner =-=-= */
        Scanner in;

        /* =-=-= Declaracion de Variables de Tipo String =-=-= */
        String playerName;

        /* =-=-= Declaracion de Variables de Tipo String[] =-=-= */
        String[] playersList;

        playersList = new String[3];
        in = new Scanner(System.in);

        System.out.println(BLUE + "\n---- !Bienvenidos al juego Determinar¡ ----" + RESET);
        System.out.println(
                YELLOW + "Objetivo: Resolver la matriz con mayor determinante y maximizar tu puntaje!!!" + RESET);

        // Obtenemos los nombres de los jugadores.
        for (int i = 0; i < playersList.length; i++) {
            correctName = false;
            do {
                System.out.print(CYAN + "Digite el nombre para el jugador " + (i + 1) + ": " + RESET);
                playerName = in.nextLine();
                if (playerName != null && !playerName.equalsIgnoreCase("")) {
                    playersList[i] = playerName;
                    correctName = true;
                } else {
                    System.out.println(
                            RED + "Nombre Vacio, Really?. Estoy seguro tus padres te dieron un nombre xD\nIntenta de nuevo!"
                                    + RESET);
                }
            } while (!correctName);

            System.out.println(BLUE + "Bienvenido " + RESET + GREEN + playersList[i] + "!" + RESET);
        }

        return new Menu(playersList);
    }

    /**
     * Solicita los nombres de los jugadores a través de una interfaz gráfica
     * utilizando {@link JOptionPane}. Una vez obtenidos, crea una instancia de la
     * clase {@link MenuGUI} con los jugadores y la retorna.
     *
     * @return Una instancia de {@link MenuGUI} con los jugadores ingresados.
     */
    public static MenuGUI dataAccessViaGUI() {
        /* =-=-= Declaracion de Variables de Tipo String[] =-=-= */
        String[] playersList = new String[3];

        // Recolectamos los nombres de los jugadores utilizando JOptionPane.
        for (int i = 0; i < playersList.length; i++) {
            String playerName = JOptionPane.showInputDialog(null, "Ingrese el nombre del jugador " + (i + 1),
                    "Ingreso de Jugadores", JOptionPane.QUESTION_MESSAGE);
            if (playerName != null && !playerName.trim().isEmpty()) {
                playersList[i] = playerName.trim();
            } else {
                JOptionPane.showMessageDialog(null, "El nombre del jugador no puede estar vacío.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                i--; // Para que vuelva a pedir el nombre del jugador que quedó vacío.
            }
        }

        // Creamos y retornamos la instancia de MenuGUI
        return new MenuGUI(playersList);
    }

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

        // Mostramos la ventana de carga.
        loadingFrame.setVisible(true);

        // Simulacion de una carga de x segundos antes de continuar.
        try {
            Thread.sleep(time); // Espera x segundos ([x * 1000] milisegundos).
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Cierra la ventana de carga despues de la espera.
        loadingFrame.setVisible(false);
        loadingFrame.dispose(); // Liberacion de recursos del JFrame de carga.
    }
}
