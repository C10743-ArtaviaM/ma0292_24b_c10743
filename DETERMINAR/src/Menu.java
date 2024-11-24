import java.util.Scanner;

/**
 * La clase {@code Menu} es responsable de gestionar el menú principal del juego
 * en modo consola. Esta clase permite a los jugadores seleccionar opciones como
 * leer las instrucciones, empezar el juego, refrescar los sistemas de
 * ecuaciones, mostrar el sistema actual, reiniciar el juego y salir del
 * programa. Además, gestiona la entrada de jugadores y la interacción con el
 * juego a través de la consola.
 * 
 * @author Mauricio Artavia Monge C10743 - Kenneth Delgado Cárdenas C22540
 */
public class Menu {
        /* =-=-= CONSTANTES =-=-= */
        /* =-=-= Declaracion de Constantes de Tipo String =-=-= */
        public static final String BLUE = "\033[34m"; // Azul
        public static final String CYAN = "\033[36m"; // Cian
        public static final String GREEN = "\033[32m"; // Verde
        public static final String RED = "\033[31m"; // Rojo
        public static final String RESET = "\033[0m"; // Resetear color
        public static final String YELLOW = "\033[33m"; // Amarillo

        /* =-=-= VARIABLES =-=-= */
        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        private int amountSystems;
        private int totalRounds;
        private int refreshCount;

        /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
        private double originalDeterminant;

        /* =-=-= Declaracion de Variables de Tipo Double[][] =-=-= */
        private double[][] actualSystem;
        private double[][] originalSystem;

        /* =-=-= Declaracion de Variables de Tipo Clase MathLib =-=-= */
        private MathLib mathLib;

        /* =-=-= Declaracion de Variables de Tipo Clase Player =-=-= */
        private Player[] players;

        /* =-=-= Declaracion de Variables de Tipo Clase Scanner =-=-= */
        private Scanner in;

        /* =-=-= Metodo Constructor de la clase Menu =-=-= */
        /**
         * Constructor de la clase {@code Menu}.
         * 
         * Este constructor inicializa los componentes necesarios para ejecutar el
         * juego, incluyendo la inicialización de los jugadores, la cantidad de sistemas
         * de ecuaciones y la creación de un sistema de ecuaciones aleatorio. Además,
         * realiza una copia del sistema de ecuaciones actual para asegurarse de que el
         * sistema original no sea modificado durante el juego.
         * 
         * @param playersList Un arreglo de cadenas que contiene los nombres de los
         *                    jugadores.
         *                    Este parámetro es utilizado para inicializar los jugadores
         *                    en el juego.
         */
        public Menu(String[] playersList) {
                this.refreshCount = 3;
                this.in = new Scanner(System.in);
                this.mathLib = new MathLib();
                this.players = new Player[3];
                this.amountSystems = getValidatedInput("Ingrese la cantidad de sistemas deseados: ", 3, 10);

                for (int i = 0; i < playersList.length; i++) {
                        this.players[i] = new Player(playersList[i], true);
                }

                generateAndVoteSystems(this.amountSystems);
                // Crea una copia de actualSystem para asegurarse de que originalSystem no se
                // vea afectado.
                originalSystem = new double[actualSystem.length][actualSystem[0].length];

                for (int i = 0; i < actualSystem.length; i++) {
                        for (int j = 0; j < actualSystem[i].length; j++) {
                                originalSystem[i][j] = actualSystem[i][j]; // Copia cada valor.
                        }
                }
        }

        /**
         * Método principal que ejecuta el menú interactivo del juego, donde los
         * jugadores pueden elegir diferentes opciones relacionadas con el juego. Este
         * método controla el flujo del juego desde la selección del modo hasta la
         * ejecución de las rondas.
         * 
         * El menú permite:
         * - Ver las instrucciones del juego.
         * - Elegir el modo de juego (rápido, normal, extendido).
         * - Refrescar los sistemas de ecuaciones disponibles.
         * - Ver el sistema actual.
         * - Reiniciar el juego.
         * - Salir del juego.
         * 
         * El flujo del juego se gestiona mediante un ciclo de entrada de usuario en el
         * que se procesan las opciones seleccionadas y se ejecutan las acciones
         * correspondientes.
         * 
         * @throws IllegalArgumentException Si se ingresan opciones fuera de rango en el
         *                                  menú.
         */
        public void run() {
                /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
                boolean exitProcess = true;
                boolean playingProcess = true;
                boolean proceed = true;
                boolean resetProcess = true;

                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int exitOption;
                int gameOption;
                int option;
                int resetOption;

                showInstructions(); // Muestra las instrucciones del juego.

                // Ciclo principal para mostrar el menú y la gestion de sus opciones.
                while (proceed) {
                        // Impresion de las opciones del menú.
                        System.out.println(BLUE + "\n=== Menú Principal ===" + RESET);
                        System.out.println("1. Leer Instrucciones");
                        System.out.println("2. Empezar Juego");
                        System.out.println("3. Refrescar sistemas");
                        System.out.println("4. Mostrar sistema actual");
                        System.out.println("5. Reiniciar Juego");
                        System.out.println("6. Salir");
                        System.out.print(CYAN + "¡Seleccione una opción!: " + RESET);

                        try {
                                // Lectura de la opcion elegida por el usuario.
                                option = Integer.parseInt(in.nextLine());

                                // Procesamiento de la opción seleccionada.
                                switch (option) {
                                        case 1:
                                                // Muestra las instrucciones del juego.
                                                showInstructions();
                                                break;

                                        case 2:
                                                do {
                                                        try {
                                                                System.out.println(BLUE +
                                                                                "Modo de juego:"
                                                                                + RESET);
                                                                System.out.println(RED + "1. Modo rápido (3 rondas)"
                                                                                + RESET);
                                                                System.out.println(YELLOW + "2. Modo normal (5 rondas)"
                                                                                + RESET);
                                                                System.out.println(
                                                                                GREEN + "3. Modo extendido (10 rondas)"
                                                                                                + RESET);

                                                                System.out.println(
                                                                                "Selecciona un modo de juego ( 1 - 3 )");
                                                                gameOption = Integer.parseInt(in.nextLine());

                                                                // Selección del modo de juego.
                                                                if (gameOption >= 1 && gameOption <= 3) {
                                                                        if (gameOption == 1) {
                                                                                totalRounds = 3;
                                                                        } else if (gameOption == 2) {
                                                                                totalRounds = 5;
                                                                        } else if (gameOption == 3) {
                                                                                totalRounds = 10;
                                                                        }
                                                                        playingProcess = false;
                                                                        beginGame(); // Empezamos el Juego.
                                                                } else {
                                                                        System.out.println(
                                                                                        RED + "¡VAMOS! Elije un modo de juego, no le tengas miedo al exito!"
                                                                                                        + RESET);
                                                                }
                                                        } catch (NumberFormatException e) {
                                                                System.out.println(
                                                                                RED + "¡VAMOS! Elije un modo de juego, no le tengas miedo al exito!"
                                                                                                + RESET);
                                                        }
                                                } while (playingProcess);
                                                break;

                                        case 3:
                                                refreshSystems(); // Refrescamiento de los sistemas.
                                                break;

                                        case 4:
                                                // Muestra el sistema actual.
                                                if (actualSystem != null) {
                                                        printSystem(actualSystem);
                                                        System.out.println(
                                                                        BLUE + "Determinante actual: "
                                                                                        + originalDeterminant + RESET);
                                                } else {
                                                        System.out.println(YELLOW + "No hay sistema generado aún."
                                                                        + RESET);
                                                }
                                                break;

                                        case 5:
                                                // Opción para reiniciar el juego.
                                                do {
                                                        try {
                                                                System.out.println(
                                                                                YELLOW + "¿Estás seguro de que deseas resetear el juego?"
                                                                                                + RESET + CYAN +
                                                                                                "\n1) SI\n2) NO"
                                                                                                + RESET);
                                                                resetOption = Integer.parseInt(in.nextLine());

                                                                if (resetOption == 1) {
                                                                        resetGame(1); // Reinicio del juego.
                                                                        resetProcess = false;
                                                                } else if (resetOption == 2) {
                                                                        System.out.println(
                                                                                        CYAN + "¡Sigamos jugando con el mismo sistema!"
                                                                                                        + RESET);
                                                                        resetProcess = false;
                                                                } else {
                                                                        System.out.println(
                                                                                        RED + "¡VAMOS! Que es 1 o 2, no estamos eligiendo filas o valores :v"
                                                                                                        + RESET);
                                                                }
                                                        } catch (NumberFormatException e) {
                                                                System.out.println(
                                                                                RED + "¡VAMOS! Que es 1 o 2, no estamos eligiendo filas o valores :v"
                                                                                                + RESET);
                                                        }
                                                } while (resetProcess);
                                                break;

                                        case 6:
                                                // Opción de salida del juego.
                                                exitProcess = true;
                                                do {
                                                        try {
                                                                System.out.println(
                                                                                YELLOW + "¿Estás seguro de que deseas salir?"
                                                                                                + RESET + CYAN +
                                                                                                "\n1) SI\n2) NO"
                                                                                                + RESET);
                                                                exitOption = Integer.parseInt(in.nextLine());

                                                                if (exitOption == 1) {
                                                                        proceed = false; // Salimos del ciclo principal.
                                                                        System.out.println(CYAN +
                                                                                        "¡Gracias por Jugar!"
                                                                                        + RESET);
                                                                        exitProcess = false;
                                                                } else if (exitOption == 2) {
                                                                        System.out.println(CYAN +
                                                                                        "¡Sigamos Jugando!" + RESET);
                                                                        exitProcess = false;
                                                                } else {
                                                                        System.out.println(
                                                                                        RED + "¡UY! Alguien quedo traumado con el juego. Creo pusiste una mala opcion por error xD"
                                                                                                        + RESET);
                                                                }
                                                        } catch (NumberFormatException e) {
                                                                System.out.println(
                                                                                RED + "¡UY! Alguien quedo traumado con el juego. Creo pusiste una mala opcion por error xD"
                                                                                                + RESET);
                                                                continue; // Regresa a la pregunta del ciclo do-while
                                                        }
                                                } while (exitProcess);
                                                break;

                                        default:
                                                // Si la opción seleccionada no es válida.
                                                System.out.println(
                                                                RED + "Opción no válida. Elija una de las opciones que están disponibles."
                                                                                + RESET);
                                }
                        } catch (NumberFormatException e) {
                                System.out.println(RED + "Entrada inválida. Por favor, ingrese una opción válida."
                                                + RESET);
                        }
                }
                in.close();
        }

        /**
         * Reinicia el estado del juego, ya sea completamente o solo restableciendo los
         * puntajes de los jugadores y el sistema de ecuaciones.
         * 
         * Si se selecciona un reinicio completo (`totalReset == 1`), se restablecen
         * tanto los puntajes de los jugadores como el sistema de ecuaciones, generando
         * uno nuevo y mostrándolo al usuario. Si solo se restablecen los puntajes
         * (`totalReset != 1`), el sistema de ecuaciones original se restaura a su
         * estado inicial.
         * 
         * @param totalReset Indica el tipo de reinicio. Si es 1, se realiza un reinicio
         *                   completo (puntajes y sistema de ecuaciones). Si es 0, solo
         *                   se restablecen los puntajes.
         */
        private void resetGame(int totalReset) {
                // Si totalReset es 1, resetea todo (Tanto puntajes como Sistema de Ecuaciones).
                for (Player player : players) {
                        // Restablecer puntajes, determinantes y el estado de juego de los jugadores.
                        player.setScore(0);
                        player.setDeterminant(0);
                        player.setPlayStatus(true); // Nos aseguramos de que todos los jugadores puedan jugar.
                }

                // Si se solicita un reinicio completo (totalReset == 1), se genera un nuevo
                // sistema de ecuaciones.
                if (totalReset == 1) {
                        // Generacion de un nuevo sistema de ecuaciones.
                        generateAndVoteSystems(amountSystems);

                        // Mostramos el mensaje de reinicio a los usuarios.
                        System.out.println(YELLOW + "\n¡El juego ha sido reiniciado!" + RESET);
                        System.out.println(YELLOW + "\n¡Los puntajes han sido reseteados!" + RESET);
                        System.out.println(YELLOW + "\n¡El sistema ha sido completamente reinicido!" + RESET);

                        // Imprime el nuevo sistema generado y su determinante.
                        printSystem(actualSystem); // Mostramos el nuevo sistema
                        System.out.println(BLUE + "Determinante original: " + RESET + GREEN +
                                        originalDeterminant + RESET);
                } else {
                        // Si no es un reinicio completo, restauramos el sistema original.
                        actualSystem = new double[originalSystem.length][originalSystem[0].length];

                        for (int i = 0; i < originalSystem.length; i++) {
                                for (int j = 0; j < originalSystem[i].length; j++) {
                                        actualSystem[i][j] = originalSystem[i][j]; // Restauracion del sistema original
                                }
                        }

                        // Mostramos el mensaje de restablecimiento de puntajes.
                        System.out.println(YELLOW + "\n¡Los puntajes han sido reseteados!" + RESET);
                }
        }

        /**
         * Inicia el flujo principal del juego, gestionando las rondas y los turnos de
         * los jugadores. En cada ronda, los jugadores pueden modificar la matriz
         * mediante entradas validadas. El juego continúa hasta que un jugador alcanza
         * 21 puntos o se completan todas las rondas.
         * 
         * El método también gestiona las penalizaciones si un jugador realiza un
         * movimiento inválido o repite un determinante, y muestra información sobre el
         * progreso del juego, incluyendo el puntaje y el determinante actual.
         * 
         * El flujo de juego sigue este proceso:
         * 1. Restablecimiento del juego.
         * 2. Generación de un nuevo sistema de ecuaciones con su determinante.
         * 3. En cada ronda, los jugadores ingresan su movimiento.
         * 4. Validación de la fila, columna y el nuevo valor de la matriz.
         * 5. Actualización del puntaje si el movimiento es válido.
         * 6. Muestra el sistema actualizado y el puntaje de cada jugador.
         * 7. Al alcanzar 21 puntos o terminar las rondas, el juego finaliza.
         */
        private void beginGame() {
                /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
                boolean haveToRepeat; // Flag para repetir el turno en caso de errores.

                /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
                double newDeterminant; // Nuevo determinante calculado después de un movimiento.
                double previousValue; // Valor previo de la matriz antes de realizar un cambio.

                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int column; // Columna seleccionada por el jugador.
                int newValue; // Nuevo valor a insertar en la matriz.
                int round; // Contador de rondas.
                int row; // Fila seleccionada por el jugador

                // Inicializar juego.
                resetGame(0); // Restablece el estado del juego y los puntajes.

                // Generar el sistema de ecuaciones y calcular el determinante original.
                originalDeterminant = mathLib.determinante(actualSystem);
                System.out.println(GREEN + "Nuevo sistema generado:" + RESET);
                printSystem(originalSystem); // Muestra el sistema original
                System.out.println(BLUE + "Determinante original: " + RESET + GREEN +
                                originalDeterminant + RESET);

                round = 1; // Inicia la primera ronda

                while (round <= totalRounds) { // Iteracion de las rondas.
                        System.out.println(BLUE + "\n=== Ronda " + round + " ===" + RESET);

                        // Iterar de jugadores.
                        for (Player player : players) {
                                haveToRepeat = true; // Inicializamos el flag para repetir el turno en caso de errores.

                                while (haveToRepeat) { // El jugador repite su turno si comete un error.
                                        haveToRepeat = false; // Asumimos que el turno no será repetido a menos que haya
                                                              // un error.

                                        if (!player.getPlayStatus()) { // Si el jugador ha sido penalizado, pierde su
                                                                       // turno.
                                                System.out.println(RED + player.getName() + " pierde su turno."
                                                                + RESET);
                                                player.setPlayStatus(true); // Restablecer el estado del
                                                                            // jugador para el próximo turno.
                                                continue;
                                        }

                                        System.out.println(GREEN + player.getName() + ", es tu turno." + RESET);

                                        // Validar la fila seleccionada por el jugador.
                                        do {
                                                row = getValidatedInput("Ingresa la fila a modificar", 1, 3) - 1;

                                                if (row < 0 || row >= actualSystem.length) {
                                                        System.out.println(RED +
                                                                        "Fila inválida. Debe estar entre 1 y 3."
                                                                        + RESET);
                                                }
                                        } while (row < 0 || row >= actualSystem.length);

                                        // Validar la columna seleccionada por el jugador.
                                        do {
                                                column = getValidatedInput("Ingresa la columna a modificar", 1, 3) - 1;

                                                if (column < 0 || column >= actualSystem[0].length) {
                                                        System.out.println(RED +
                                                                        "Columna inválida. Debe estar entre 1 y 3."
                                                                        + RESET);
                                                }
                                        } while (column < 0 || column >= actualSystem[0].length);

                                        // Validar el nuevo valor ingresado por el jugador.
                                        do {
                                                newValue = getValidatedInput("Ingresa el nuevo valor", -5, 5);

                                                if (newValue < -5 || newValue > 5) {
                                                        System.out.println(RED +
                                                                        "Valor inválido. Debe estar entre -5 y 5."
                                                                        + RESET);
                                                }
                                        } while (newValue < -5 || newValue > 5);

                                        previousValue = actualSystem[row][column]; // Guardar el valor anterior.
                                        actualSystem[row][column] = newValue; // Actualizar la matriz con el nuevo
                                                                              // valor.
                                        newDeterminant = mathLib.determinante(actualSystem); // Calcular el nuevo
                                                                                             // determinante.

                                        // Si el determinante no cambia, se repite el turno.
                                        if (newDeterminant == originalDeterminant) {
                                                System.out.println(YELLOW +
                                                                "El determinante no cambió. Repite el turno."
                                                                + RESET);
                                                actualSystem[row][column] = previousValue; // Revertimos el cambio.
                                                haveToRepeat = true;
                                        }

                                        // Penalizamos al jugador si el determinante ya fue usado.
                                        if (newDeterminant == player.getDeterminant()) {
                                                System.out.println(
                                                                YELLOW + "El determinante ya se había usado. Pierdes el próximo turno."
                                                                                + RESET);
                                                player.setPlayStatus(false); // Penalizamos al jugador.
                                        } else {
                                                // Si el turno es válido, actualizamos el puntaje del jugador.
                                                if (!haveToRepeat) {
                                                        updatePoints(player, newDeterminant); // Actualizacion de
                                                                                              // puntos.
                                                        player.setDeterminant(newDeterminant); // Guardamos el nuevo
                                                                                               // determinante.
                                                }
                                        }

                                        // Mostramos el sistema actualizado y el puntaje del jugador.
                                        printSystem(actualSystem);
                                        System.out.println(BLUE + "Determinante actual: " + RESET + GREEN +
                                                        newDeterminant + RESET);
                                        System.out.println(BLUE + "Puntaje acumulado: " + RESET + GREEN +
                                                        player.getScore() + RESET);

                                        // Verificamos si el jugador ha ganado con 21 puntos exactos.
                                        if (player.getScore() == 21) {
                                                getWinner(); // Mostramos al ganador.
                                                getLoser(); // Mostramos al perdedor.
                                                getStats(); // Mostramos las estadísticas.
                                                return; // Finalizamos el juego.
                                        }
                                }
                        }
                        round++; // Incrementar el número de ronda.
                }
                getWinner(); // Mostramos al ganador.
                getLoser(); // Mostramos al perdedor.
                getStats(); // Mostramos las estadísticas.
        }

        /**
         * Actualiza el puntaje de un jugador basado en el nuevo determinante calculado
         * después de un movimiento.
         * 
         * El puntaje se actualiza de acuerdo a la diferencia entre el determinante
         * original y el nuevo determinante. Si el nuevo determinante es positivo, se
         * suma la diferencia entre el original y el nuevo determinante al puntaje del
         * jugador. Si el nuevo determinante es negativo, se suma el valor del
         * determinante negativo al puntaje del jugador.
         * 
         * @param player                  El jugador cuyo puntaje se va a actualizar.
         * @param newDeterminantForUpdate El nuevo determinante calculado después de un
         *                                movimiento.
         */
        private void updatePoints(Player player, double newDeterminantForUpdate) {
                /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
                double diference; // Diferencia entre el determinante original y el nuevo determinante.

                // Calculamos la diferencia absoluta entre el determinante original y el nuevo.
                diference = Math.abs(originalDeterminant - newDeterminantForUpdate);

                // Si el nuevo determinante es positivo, sumamos la diferencia al puntaje del
                // jugador.
                if (newDeterminantForUpdate > 0) {
                        player.setScore(player.getScore() + diference);
                } else {
                        // Si el nuevo determinante es negativo, sumamos el valor del determinante
                        // negativo al puntaje.
                        player.setScore(player.getScore() + newDeterminantForUpdate); // Sumamos el determinante
                                                                                      // negativo.
                }
        }

        /**
         * Determina al ganador del juego.
         * 
         * El ganador es priorizado si su puntaje alcanza los 21 puntos. Si no hay
         * jugadores con puntaje 21, se selecciona al jugador con el puntaje más alto.
         * Si el puntaje más alto es 0 o negativo, se considera que nadie ha ganado.
         * 
         * Se muestra el nombre del ganador y su puntaje, o un mensaje indicando que
         * nadie ha ganado si todos tienen puntajes negativos o nulos.
         */
        private void getWinner() {
                /* =-=-= Declaracion de Variables de Tipo Clase Player =-=-= */
                Player winner;

                winner = null;

                // Priorizamos puntajes de 21.
                for (Player player : players) {
                        if (player.getScore() == 21) {
                                winner = player;
                                break;
                        }
                }

                // Si nadie alcanzó 21 puntos, buscar el puntaje más alto.
                for (Player player : players) {
                        if (winner == null || player.getScore() > winner.getScore()) {
                                winner = player;
                        }
                }

                if (winner.getScore() <= 0) {
                        System.out.println(YELLOW + "Nadie gana. Todos tienen puntajes negativos." + RESET);
                } else {
                        System.out.println(BLUE + "Felicidades, " + RESET + GREEN + winner.getName() +
                                        RESET + BLUE + " ha ganado el juego con " + RESET + GREEN +
                                        winner.getScore() + RESET + BLUE + " puntos." + RESET);
                }
        }

        /**
         * Determina al perdedor del juego.
         * 
         * El perdedor es el jugador con el puntaje más bajo. Si hay varios con el mismo
         * puntaje, se selecciona al primero encontrado con ese puntaje.
         * 
         * Se imprime el nombre del jugador con el puntaje más bajo y se le da ánimo
         * para futuras oportunidades.
         */
        private void getLoser() {
                /* =-=-= Declaracion de Variables de Tipo Clase Player =-=-= */
                Player loser;

                loser = null;

                // Encuentra el jugador con el puntaje más bajo
                for (Player player : players) {
                        if (loser == null || player.getScore() < loser.getScore()) {
                                loser = player;
                        }
                }

                System.out.println(BLUE + "Animo " + RESET + GREEN + loser.getName() +
                                RESET + BLUE + ", habran mas oportunidades luego! \t(" + RESET + GREEN +
                                loser.getScore() + RESET + BLUE + " puntos)" + RESET);
        }

        /**
         * Muestra las estadísticas del juego, incluyendo el nombre y el puntaje de cada
         * jugador.
         * 
         * Este método recorre la lista de jugadores y muestra sus nombres junto con su
         * puntaje final.
         */
        private void getStats() {
                System.out.println(BLUE + "Estadísticas del Juego:" + RESET);

                for (Player player : players) {
                        System.out.println(GREEN + player.getName() + RESET + " - Puntaje: " + GREEN +
                                        player.getScore() + RESET);
                }
        }

        /**
         * Imprime el sistema de ecuaciones en formato matricial.
         * 
         * Este método recibe un sistema de ecuaciones representado por una matriz y lo
         * imprime en formato de tabla. La última columna de cada fila se muestra como
         * el término independiente, separado por una línea vertical.
         * 
         * @param system La matriz que representa el sistema de ecuaciones a imprimir.
         */
        private void printSystem(double[][] system) {
                for (int i = 0; i < system.length; i++) {
                        for (int j = 0; j < system[i].length - 1; j++) {
                                System.out.printf("%6d ", (int) system[i][j]);
                        }
                        System.out.printf("| %6d\n", (int) system[i][system[i].length - 1]); // Término independiente
                }
        }

        /**
         * Genera un número determinado de sistemas de ecuaciones aleatorios con
         * solución única y permite al jugador seleccionar uno de ellos para ser
         * utilizado en el juego.
         * 
         * El método genera la cantidad de sistemas de ecuaciones especificada y los
         * presenta al jugador. Luego, se realiza una votación en la que el jugador
         * selecciona el sistema que desea utilizar. Una vez que se selecciona el
         * sistema, se asigna como el sistema actual para el juego.
         * 
         * @param amount La cantidad de sistemas de ecuaciones a generar y presentar al
         *               jugador.
         */
        private void generateAndVoteSystems(int amount) {
                // Creacion el arreglo dinámico para la cantidad deseada de sistemas.
                /* =-=-= Declaracion de Variables de Tipo Double[][][] =-=-= */
                double[][][] systems = new double[amount][3][4];

                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int selectedSystem;

                System.out.println(YELLOW + "\nGenerando " + amount
                                + " sistemas de ecuaciones con solución única..." + RESET);

                // Generacion de sistemas aleatorios con solución única.
                for (int i = 0; i < amount; i++) {
                        do {
                                systems[i] = mathLib.generateAleatorySystem(3, 4, -5, 5); // Generamos una matriz
                                                                                          // ampliada.
                        } while (mathLib.determinante(systems[i]) == 0); // Rechazamos los sistemas sin solución única.

                        System.out.println(BLUE + "\nSistema " + (i + 1) + ":" + RESET);
                        printSystem(systems[i]);
                }

                // Votación
                System.out.println(BLUE + "\n--- ¡Es hora de votar! ---" + RESET);
                selectedSystem = conductVoting(systems);

                // Asignamos el sistema seleccionado como actual.
                actualSystem = systems[selectedSystem];
                originalDeterminant = mathLib.determinante(actualSystem);
                System.out.println(BLUE + "\nSistema seleccionado:" + RESET);
                printSystem(actualSystem);
                System.out.println(
                                BLUE + "Determinante del sistema seleccionado: " + RESET + GREEN + originalDeterminant
                                                + RESET);
        }

        /**
         * Refresca los sistemas de ecuaciones generados, generando nuevos sistemas y
         * permitiendo al jugador votar por uno de ellos nuevamente.
         * 
         * Este método solo podrá ser llamado si hay "refrescos" disponibles. Después de
         * cada llamada, la cantidad de refrescos disminuye en uno. Si no quedan
         * refrescos disponibles, se muestra un mensaje informando que no es posible
         * refrescar más sistemas.
         */
        private void refreshSystems() {
                if (refreshCount > 0) {
                        System.out.println(YELLOW + "\n--- Refrescando sistemas ---" + RESET);
                        generateAndVoteSystems(amountSystems); // Generamos y se da la votación de nuevos sistemas.
                        refreshCount--; // Disminución de la cantidad de refrescos disponibles.
                        System.out.println(BLUE + "Te quedan " + RESET + YELLOW + refreshCount + RESET +
                                        BLUE + " refrescos disponibles." + RESET);
                } else {
                        System.out.println(RED + "Ya no puedes refrescar los sistemas." + RESET);
                }
        }

        /**
         * Solicita al usuario un valor entero dentro de un rango válido, y asegura que
         * la entrada esté dentro del rango especificado antes de devolver el valor.
         * 
         * Este método realiza una validación para asegurarse de que el valor ingresado
         * sea un número entero dentro del rango definido por los parámetros `min` y
         * `max`. Si el valor ingresado no es válido, solicita al usuario que ingrese un
         * valor correcto.
         * 
         * @param prompt El mensaje que se le presenta al usuario solicitando la
         *               entrada.
         * @param min    El valor mínimo válido para la entrada.
         * @param max    El valor máximo válido para la entrada.
         * @return El valor ingresado por el usuario, asegurándose de que esté dentro
         *         del rango válido.
         */
        private int getValidatedInput(String prompt, int min, int max) {
                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int value;

                do {
                        System.out.print(CYAN + prompt + " (" + min + " a " + max + "): " + RESET);
                        try {
                                value = Integer.parseInt(in.nextLine());

                                if (value >= min && value <= max) {
                                        return value;
                                } else {
                                        System.out.println(RED + "Entrada fuera de rango. Inténtalo de nuevo." + RESET);
                                }
                        } catch (NumberFormatException e) {
                                System.out.println(RED + "Entrada inválida. Debe ser un número entero." + RESET);
                        }
                } while (true);
        }

        /**
         * Permite realizar una votación entre los sistemas generados y devuelve el
         * índice del sistema más votado.
         * 
         * Este método solicita a cada jugador que seleccione uno de los sistemas
         * generados. Los jugadores pueden votar por el sistema que deseen utilizar. Al
         * finalizar la votación, el método determina qué sistema obtuvo más votos y lo
         * selecciona como el sistema actual.
         * 
         * @param systems Los sistemas de ecuaciones generados de los cuales el jugador
         *                puede votar.
         * @return El índice del sistema más votado.
         */
        private int conductVoting(double[][][] systems) {
                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int choice;
                int selectedSystem;

                /* =-=-= Declaracion de Variables de Tipo Integer[] =-=-= */
                int[] votes = new int[systems.length];

                // Cada jugador vota por su sistema favorito
                for (Player player : players) {
                        choice = getValidatedInput(
                                        player.getName() + ", selecciona el sistema con el que deseas jugar", 1,
                                        systems.length);
                        votes[choice - 1]++;
                }

                // Determina el índice del sistema más votado
                selectedSystem = 0;

                for (int i = 1; i < votes.length; i++) {
                        if (votes[i] > votes[selectedSystem]) {
                                selectedSystem = i;
                        }
                }

                return selectedSystem;
        }

        /**
         * Muestra las instrucciones del juego en la consola.
         * 
         * Este método imprime en la consola una serie de instrucciones que explican el
         * objetivo del juego, cómo se juega y qué se espera de los jugadores. Al
         * finalizar, se espera que el jugador presione ENTER para volver al menú
         * principal.
         */
        public void showInstructions() {
                System.out.println(GREEN + "\n---- Instrucciones ----" + RESET);
                System.out.println(YELLOW + "Objetivo del juego:" + RESET);
                System.out.println(" - Resolver la matriz con mayor determinante.");
                System.out.println(" - Maximizar tu puntaje.");
                System.out.println(" - Competir contra otros jugadores para ser el mejor.");
                System.out.println(CYAN + "\nPresione ENTER para volver al menú principal." + RESET);

                in.nextLine(); // Pausa para que el usuario lea las instrucciones.
        }
}
