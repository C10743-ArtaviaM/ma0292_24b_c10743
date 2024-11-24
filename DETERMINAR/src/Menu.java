import java.util.Scanner;

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
                // Crear una copia de actualSystem para asegurarse de que originalSystem no se
                // vea afectado
                originalSystem = new double[actualSystem.length][actualSystem[0].length];
                for (int i = 0; i < actualSystem.length; i++) {
                        for (int j = 0; j < actualSystem[i].length; j++) {
                                originalSystem[i][j] = actualSystem[i][j]; // Copiar cada valor
                        }
                }
        }

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

                showInstructions();

                while (proceed) {
                        System.out.println(BLUE + "\n=== Menú Principal ===" + RESET);
                        System.out.println("1. Leer Instrucciones");
                        System.out.println("2. Empezar Juego");
                        System.out.println("3. Refrescar sistemas");
                        System.out.println("4. Mostrar sistema actual");
                        System.out.println("5. Reiniciar Juego");
                        System.out.println("6. Salir");
                        System.out.print(CYAN + "¡Seleccione una opción!: " + RESET);

                        try {
                                option = Integer.parseInt(in.nextLine());

                                switch (option) {
                                        case 1:
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
                                                                                "Selecciones un modo de juego ( 1 - 3 )");
                                                                gameOption = Integer.parseInt(in.nextLine());

                                                                if (gameOption >= 1 && gameOption <= 3) {
                                                                        if (gameOption == 1) {
                                                                                totalRounds = 3;
                                                                        } else if (gameOption == 2) {
                                                                                totalRounds = 5;
                                                                        } else if (gameOption == 3) {
                                                                                totalRounds = 10;
                                                                        }
                                                                        playingProcess = false;
                                                                        beginGame(); // Empezamos Juego
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
                                                refreshSystems(); // Refrescamos Sistemas
                                                break;

                                        case 4:
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
                                                do {
                                                        try {
                                                                System.out.println(
                                                                                YELLOW + "¿Estás seguro de que deseas resetear el juego?"
                                                                                                + RESET + CYAN +
                                                                                                "\n1) SI\n2) NO"
                                                                                                + RESET);
                                                                resetOption = Integer.parseInt(in.nextLine());

                                                                if (resetOption == 1) {
                                                                        resetGame(1);
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
                                                                        proceed = false; // Salida
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

        private void resetGame(int totalReset) {
                // Si totalReset es 1, resetea todo y sistemas.
                for (Player player : players) {
                        player.setScore(0);
                        player.setDeterminant(0);
                        player.setPlayStatus(true); // Asegura que todos los jugadores puedan jugar.
                }

                if (totalReset == 1) {
                        // Generamos un nuevo sistema de ecuaciones.
                        generateAndVoteSystems(amountSystems);

                        // Mostramos el mensaje de reinicio.
                        System.out.println(YELLOW + "\n¡El juego ha sido reiniciado!" + RESET);
                        System.out.println(YELLOW + "\n¡Los puntajes han sido reseteados!" + RESET);
                        System.out.println(YELLOW + "\n¡El sistema ha sido completamente reinicido!" + RESET);
                        printSystem(actualSystem); // Mostrar el nuevo sistema
                        System.out.println(BLUE + "Determinante original: " + RESET + GREEN +
                                        originalDeterminant + RESET);
                } else {
                        // Copiar originalSystem a actualSystem para restaurarlo al estado inicial
                        actualSystem = new double[originalSystem.length][originalSystem[0].length];
                        for (int i = 0; i < originalSystem.length; i++) {
                                for (int j = 0; j < originalSystem[i].length; j++) {
                                        actualSystem[i][j] = originalSystem[i][j]; // Restaurar sistema original
                                }
                        }
                        System.out.println(YELLOW + "\n¡Los puntajes han sido reseteados!" + RESET);
                }
        }

        private void beginGame() {
                /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
                boolean haveToRepeat;

                /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
                double newDeterminant;
                double previousValue;

                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int round;
                int row, column, newValue;

                resetGame(0);

                originalDeterminant = mathLib.determinante(actualSystem);
                System.out.println(GREEN + "Nuevo sistema generado:" + RESET);
                printSystem(originalSystem);
                System.out.println(BLUE + "Determinante original: " + RESET + GREEN +
                                originalDeterminant + RESET);

                round = 1;
                while (round <= totalRounds) {
                        System.out.println(BLUE + "\n=== Ronda " + round + " ===" + RESET);

                        for (Player player : players) {
                                haveToRepeat = true;

                                while (haveToRepeat) {
                                        haveToRepeat = false;

                                        if (!player.getPlayStatus()) {
                                                System.out.println(RED + player.getName() + " pierde su turno."
                                                                + RESET);
                                                player.setPlayStatus(true); // Restablece para el próximo turno
                                                continue;
                                        }

                                        System.out.println(GREEN + player.getName() + ", es tu turno." + RESET);

                                        // Validar fila
                                        do {
                                                row = getValidatedInput("Ingresa la fila a modificar", 1, 3) - 1;

                                                if (row < 0 || row >= actualSystem.length) {
                                                        System.out.println(RED +
                                                                        "Fila inválida. Debe estar entre 1 y 3."
                                                                        + RESET);
                                                }
                                        } while (row < 0 || row >= actualSystem.length);

                                        // Validar columna
                                        do {
                                                column = getValidatedInput("Ingresa la columna a modificar", 1, 3) - 1;

                                                if (column < 0 || column >= actualSystem[0].length) {
                                                        System.out.println(RED +
                                                                        "Columna inválida. Debe estar entre 1 y 3."
                                                                        + RESET);
                                                }
                                        } while (column < 0 || column >= actualSystem[0].length);

                                        // Validar nuevo valor
                                        do {
                                                newValue = getValidatedInput("Ingresa el nuevo valor", -5, 5);

                                                if (newValue < -5 || newValue > 5) {
                                                        System.out.println(RED +
                                                                        "Valor inválido. Debe estar entre -5 y 5."
                                                                        + RESET);
                                                }
                                        } while (newValue < -5 || newValue > 5);

                                        previousValue = actualSystem[row][column];
                                        actualSystem[row][column] = newValue;
                                        newDeterminant = mathLib.determinante(actualSystem);

                                        if (newDeterminant == originalDeterminant) {
                                                System.out.println(YELLOW +
                                                                "El determinante no cambió. Repite el turno."
                                                                + RESET);
                                                actualSystem[row][column] = previousValue; // Revertir el cambio
                                                haveToRepeat = true;
                                        }

                                        if (newDeterminant == player.getDeterminant()) {
                                                System.out.println(
                                                                YELLOW + "El determinante ya se había usado. Pierdes el próximo turno."
                                                                                + RESET);
                                                player.setPlayStatus(false); // Penalizar
                                        } else {
                                                if (!haveToRepeat) {
                                                        updatePoints(player, newDeterminant);
                                                        player.setDeterminant(newDeterminant);
                                                }
                                        }

                                        printSystem(actualSystem);
                                        System.out.println(BLUE + "Determinante actual: " + RESET + GREEN +
                                                        newDeterminant + RESET);
                                        System.out.println(BLUE + "Puntaje acumulado: " + RESET + GREEN +
                                                        player.getScore() + RESET);

                                        if (player.getScore() == 21) {
                                                getWinner();
                                                getLoser();
                                                getStats();
                                                return;
                                        }
                                }
                        }
                        round++;
                }
                getWinner();
                getLoser();
                getStats();
        }

        private void updatePoints(Player player, double newDeterminantForUpdate) {
                /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
                double diference;

                diference = Math.abs(originalDeterminant - newDeterminantForUpdate);

                if (newDeterminantForUpdate > 0) {
                        player.setScore(player.getScore() + diference);
                } else {
                        player.setScore(player.getScore() + newDeterminantForUpdate); // Suma determinante negativo
                }
        }

        private void getWinner() {
                /* =-=-= Declaracion de Variables de Tipo Clase Player =-=-= */
                Player winner;

                winner = null;

                // Priorizar puntajes de 21
                for (Player player : players) {
                        if (player.getScore() == 21) {
                                winner = player;
                                break;
                        }
                }

                // Si nadie alcanzó 21, buscar el puntaje más alto
                for (Player player : players) {
                        if (winner == null || player.getScore() > winner.getScore()) {
                                winner = player;
                        }
                }

                if (winner.getScore() <= 0) {
                        System.out.println(YELLOW + "Nadie gana. Todos tienen puntajes negativos." + RESET);
                } else {
                        System.out.println(BLUE + "Felicidades," + RESET + GREEN + winner.getName() +
                                        RESET + BLUE + " ha ganado el juego con " + RESET + GREEN +
                                        winner.getScore() + RESET + BLUE + " puntos." + RESET);
                }
        }

        private void getLoser() {
                /* =-=-= Declaracion de Variables de Tipo Clase Player =-=-= */
                Player loser;

                loser = null;

                for (Player player : players) {
                        if (loser == null || player.getScore() < loser.getScore()) {
                                loser = player;
                        }
                }

                System.out.println(BLUE + "Animo," + RESET + GREEN + loser.getName() +
                                RESET + BLUE + " habran mas oportunidades luego! \t(" + RESET + GREEN +
                                loser.getScore() + RESET + BLUE + " puntos)" + RESET);
        }

        private void getStats() {
                System.out.println(BLUE + "Estadísticas del Juego:" + RESET);

                for (Player player : players) {
                        System.out.println(GREEN + player.getName() + RESET + " - Puntaje: " + GREEN +
                                        player.getScore() + RESET);
                }
        }

        private void printSystem(double[][] system) {
                for (int i = 0; i < system.length; i++) {
                        for (int j = 0; j < system[i].length - 1; j++) {
                                System.out.printf("%6d ", (int) system[i][j]);
                        }
                        System.out.printf("| %6d\n", (int) system[i][system[i].length - 1]); // Término independiente
                }
        }

        private void generateAndVoteSystems(int amount) {
                // Crear el arreglo dinámico para la cantidad deseada de sistemas
                /* =-=-= Declaracion de Variables de Tipo Double[][][] =-=-= */
                double[][][] systems = new double[amount][3][4];

                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int selectedSystem;

                System.out.println(YELLOW + "\nGenerando " + amount
                                + " sistemas de ecuaciones con solución única..." + RESET);

                // Generar sistemas aleatorios con solución única
                for (int i = 0; i < amount; i++) {
                        do {
                                systems[i] = mathLib.generateAleatorySystem(3, 4, -5, 5); // Generar matriz ampliada
                        } while (mathLib.determinante(systems[i]) == 0); // Rechazar sistemas sin solución única

                        System.out.println(BLUE + "\nSistema " + (i + 1) + ":" + RESET);
                        printSystem(systems[i]);
                }

                // Votación
                System.out.println(BLUE + "\n--- ¡Es hora de votar! ---" + RESET);
                selectedSystem = conductVoting(systems);

                // Asignar el sistema seleccionado como actual
                actualSystem = systems[selectedSystem];
                originalDeterminant = mathLib.determinante(actualSystem);
                System.out.println(BLUE + "\nSistema seleccionado:" + RESET);
                printSystem(actualSystem);
                System.out.println(
                                BLUE + "Determinante del sistema seleccionado: " + RESET + GREEN + originalDeterminant
                                                + RESET);
        }

        private void refreshSystems() {
                if (refreshCount > 0) {
                        System.out.println(YELLOW + "\n--- Refrescando sistemas ---" + RESET);
                        generateAndVoteSystems(amountSystems);
                        refreshCount--;
                        System.out.println(BLUE + "Te quedan " + RESET + YELLOW + refreshCount + RESET +
                                        BLUE + " refrescos disponibles." + RESET);
                } else {
                        System.out.println(RED + "Ya no puedes refrescar los sistemas." + RESET);
                }
        }

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

                // Determinar el índice del sistema más votado
                selectedSystem = 0;

                for (int i = 1; i < votes.length; i++) {
                        if (votes[i] > votes[selectedSystem]) {
                                selectedSystem = i;
                        }
                }

                return selectedSystem;
        }

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
