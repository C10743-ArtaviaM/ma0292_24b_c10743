import java.util.Scanner;

public class Menu {
        /* =-=-= CONSTANTES =-=-= */
        /* =-=-= Declaracion de Constantes de Tipo Integer =-=-= */
        private final int MAX_ROUNDS = 5;

        /* =-=-= VARIABLES =-=-= */
        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        private int refreshCount;
        private int amountSystems;

        /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
        private double originalDeterminant;

        /* =-=-= Declaracion de Variables de Tipo Double[][] =-=-= */
        private double[][] actualSystem;

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
        }

        public void run() {
                /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
                boolean proceed = true;

                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int option;

                while (proceed) {
                        System.out.println("\n=== Menú Principal ===");
                        System.out.println("1. Empezar Juego");
                        System.out.println("2. Refrescar sistemas");
                        System.out.println("3. Mostrar sistema actual");
                        System.out.println("4. Salir");
                        System.out.print("¡Seleccione una opción!: ");

                        try {
                                option = Integer.parseInt(in.nextLine());

                                switch (option) {
                                        case 1:
                                                beginGame();
                                                break;

                                        case 2:
                                                refreshSystems();
                                                break;

                                        case 3:
                                                if (actualSystem != null) {
                                                        printSystem(actualSystem);
                                                        System.out.println(
                                                                        "Determinante actual: " + originalDeterminant);
                                                } else {
                                                        System.out.println("No hay sistema generado aún.");
                                                }
                                                break;

                                        case 4:
                                                proceed = false;
                                                System.out.println("¡Gracias por Jugar!");
                                                break;

                                        default:
                                                System.out.println(
                                                                "Opción no válida. Elija una de las opciones que están disponibles.");
                                }
                        } catch (NumberFormatException e) {
                                System.out.println("Entrada inválida. Por favor, ingrese una opción válida.");
                        }
                }
                in.close();
        }

        private void beginGame() {
                /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
                boolean haveToRepeat = true;

                /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
                double newDeterminant;
                double previousValue;

                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int round = 1;
                int row, column, newValue;

                for (int i = 0; i < players.length; i++) {
                        players[i].setScore(0);
                        players[i].setDeterminant(0);
                        players[i].setPlayStatus(true);
                }

                originalDeterminant = mathLib.determinante(actualSystem);
                System.out.println("Nuevo sistema generado:");
                printSystem(actualSystem);
                System.out.println("Determinante original: " + originalDeterminant);

                while (round <= MAX_ROUNDS) {
                        System.out.println("\n=== Ronda " + round + " ===");

                        for (Player player : players) {
                                haveToRepeat = true;

                                while (haveToRepeat) {
                                        haveToRepeat = false;

                                        if (!player.getPlayStatus()) {
                                                System.out.println(player.getName() + " pierde su turno.");
                                                player.setPlayStatus(true); // Restablece para el próximo turno
                                                continue;
                                        }

                                        System.out.println(player.getName() + ", es tu turno.");

                                        // Validar fila
                                        do {
                                                System.out.print("Ingresa la fila a modificar (1-3): ");
                                                row = getValidatedInput("Ingresa la fila a modificar", 1, 3) - 1;

                                                if (row < 0 || row >= actualSystem.length) {
                                                        System.out.println("Fila inválida. Debe estar entre 1 y 3.");
                                                }
                                        } while (row < 0 || row >= actualSystem.length);

                                        // Validar columna
                                        do {
                                                System.out.print("Ingresa la columna a modificar (1-3): ");
                                                column = getValidatedInput("Ingresa la columna a modificar", 1, 3) - 1;

                                                if (column < 0 || column >= actualSystem[0].length) {
                                                        System.out.println("Columna inválida. Debe estar entre 1 y 3.");
                                                }
                                        } while (column < 0 || column >= actualSystem[0].length);

                                        // Validar nuevo valor
                                        do {
                                                System.out.print("Ingresa el nuevo valor (-5 a 5): ");
                                                newValue = getValidatedInput("Ingresa el nuevo valor", -5, 5);

                                                if (newValue < -5 || newValue > 5) {
                                                        System.out.println("Valor inválido. Debe estar entre -5 y 5.");
                                                }
                                        } while (newValue < -5 || newValue > 5);

                                        previousValue = actualSystem[row][column];
                                        actualSystem[row][column] = newValue;
                                        newDeterminant = mathLib.determinante(actualSystem);

                                        if (newDeterminant == originalDeterminant) {
                                                System.out.println("El determinante no cambió. Repite el turno.");
                                                actualSystem[row][column] = previousValue; // Revertir el cambio
                                                haveToRepeat = true;
                                        }

                                        if (newDeterminant == player.getDeterminant()) {
                                                System.out.println(
                                                                "El determinante ya se había usado. Pierdes el próximo turno.");
                                                player.setPlayStatus(false); // Penalizar
                                        } else {
                                                if (!haveToRepeat) {
                                                        updatePoints(player, newDeterminant);
                                                        player.setDeterminant(newDeterminant);
                                                }
                                        }

                                        printSystem(actualSystem);
                                        System.out.println("Determinante actual: " + newDeterminant);
                                        System.out.println("Puntaje acumulado: " + player.getScore());

                                        if (player.getScore() >= 21) {
                                                System.out.println(player.getName() + " ha ganado el juego con "
                                                                + player.getScore() + " puntos.");
                                                return;
                                        }
                                }
                        }
                        round++;
                }
                getWinner();
        }

        private void updatePoints(Player player, double newDeterminantForUpdate) {
                /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
                double diference = Math.abs(originalDeterminant - newDeterminantForUpdate);

                if (newDeterminantForUpdate > 0) {
                        player.setScore(player.getScore() + diference);
                } else {
                        player.setScore(player.getScore() + newDeterminantForUpdate); // Suma determinante negativo
                }
        }

        private void getWinner() {
                /* =-=-= Declaracion de Variables de Tipo Clase Player =-=-= */
                Player winner = null;

                for (Player player : players) {
                        if (winner == null || player.getScore() > winner.getScore()) {
                                winner = player;
                        }
                }

                if (winner.getScore() <= 0) {
                        System.out.println("Nadie gana. Todos tienen puntajes negativos.");
                } else {
                        System.out.println(winner.getName() + " gana con " + winner.getScore() + " puntos.");
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

                System.out.println("\nGenerando " + amount + " sistemas de ecuaciones con solución única...");

                // Generar sistemas aleatorios con solución única
                for (int i = 0; i < amount; i++) {
                        do {
                                systems[i] = mathLib.generateAleatorySystem(3, 4, -5, 5); // Generar matriz ampliada
                        } while (mathLib.determinante(systems[i]) == 0); // Rechazar sistemas sin solución única

                        System.out.println("\nSistema " + (i + 1) + ":");
                        printSystem(systems[i]);
                }

                // Votación
                System.out.println("\n--- ¡Es hora de votar! ---");
                selectedSystem = conductVoting(systems);

                // Asignar el sistema seleccionado como actual
                actualSystem = systems[selectedSystem];
                originalDeterminant = mathLib.determinante(actualSystem);
                System.out.println("\nSistema seleccionado:");
                printSystem(actualSystem);
                System.out.println("Determinante del sistema seleccionado: " + originalDeterminant);
        }

        private void refreshSystems() {
                if (refreshCount > 0) {
                        System.out.println("\n--- Refrescando sistemas ---");
                        generateAndVoteSystems(amountSystems);
                        refreshCount--;
                        System.out.println("Te quedan " + refreshCount + " refrescos disponibles.");
                } else {
                        System.out.println("Ya no puedes refrescar los sistemas.");
                }
        }

        private int getValidatedInput(String prompt, int min, int max) {
                /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
                int value;

                do {
                        System.out.print(prompt + " (" + min + "-" + max + "): ");
                        try {
                                value = Integer.parseInt(in.nextLine());
                                if (value >= min && value <= max) {
                                        return value;
                                } else {
                                        System.out.println("Entrada fuera de rango. Inténtalo de nuevo.");
                                }
                        } catch (NumberFormatException e) {
                                System.out.println("Entrada inválida. Debe ser un número entero.");
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
}
