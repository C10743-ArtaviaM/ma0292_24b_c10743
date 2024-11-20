import java.util.Scanner;

public class Menu {
        private MathLib mathLib;
        private double[][] actualSystem;
        private Player[] players;
        private double originalDeterminant;
        private final int MAX_ROUNDS = 5;
        private Scanner in;

        public Menu() {
                in = new Scanner(System.in);
                String playerName;
                this.mathLib = new MathLib();
                this.players = new Player[3]; // Tres Jugadores
                System.out.println("\n---- !Bienvenidos al juego Determinar¡ ----");
                for (int i = 0; i < players.length; i++) {
                        System.out.println("Digite su nombre para el jugador " + (i + 1) + ": ");
                        playerName = in.nextLine();
                        players[i] = new Player(playerName, true);
                        System.out.println("Bienvenido " + playerName + "!");
                }
        }

        public void run() {
                boolean continuar = true;

                while (continuar) {
                        System.out.println("\n=== Menú Principal ===");
                        System.out.println("1. Generar un nuevo sistema");
                        System.out.println("2. Mostrar sistema actual");
                        System.out.println("3. Salir");
                        System.out.print("¡Seleccione una  de las tres opciones que ofrece el juego!: ");

                        try {
                                int opcion = Integer.parseInt(in.nextLine());

                                switch (opcion) {
                                        case 1:
                                                beginGame();
                                                break;
                                        case 2:
                                                if (actualSystem != null) {
                                                        printSystem();
                                                } else {
                                                        System.out.println("No hay sistema generado aún.");
                                                }
                                                break;
                                        case 3:
                                                continuar = false;
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
                for (int i = 0; i < players.length; i++) {
                        players[i].setScore(0);
                        players[i].setDeterminant(0);
                        players[i].setPlayStatus(true);
                }

                boolean haveToRepeat = true;
                actualSystem = mathLib.generateAleatorySystem(3, 3, -5, 5);
                originalDeterminant = mathLib.determinante(actualSystem);
                System.out.println("Nuevo sistema generado:");
                printSystem();
                System.out.println("Determinante original: " + originalDeterminant);

                int round = 1;
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

                                        int row, column;
                                        double newValue;

                                        // Validar fila
                                        do {
                                                System.out.print("Ingresa la fila a modificar (1-3): ");
                                                row = Integer.parseInt(in.nextLine()) - 1;
                                                if (row < 0 || row >= actualSystem.length) {
                                                        System.out.println("Fila inválida. Debe estar entre 1 y 3.");
                                                }
                                        } while (row < 0 || row >= actualSystem.length);

                                        // Validar columna
                                        do {
                                                System.out.print("Ingresa la columna a modificar (1-3): ");
                                                column = Integer.parseInt(in.nextLine()) - 1;
                                                if (column < 0 || column >= actualSystem[0].length) {
                                                        System.out.println("Columna inválida. Debe estar entre 1 y 3.");
                                                }
                                        } while (column < 0 || column >= actualSystem[0].length);

                                        // Validar nuevo valor
                                        do {
                                                System.out.print("Ingresa el nuevo valor (-5 a 5): ");
                                                newValue = Double.parseDouble(in.nextLine());
                                                if (newValue < -5 || newValue > 5) {
                                                        System.out.println("Valor inválido. Debe estar entre -5 y 5.");
                                                }
                                        } while (newValue < -5 || newValue > 5);

                                        double previousValue = actualSystem[row][column];
                                        actualSystem[row][column] = newValue;
                                        double newDeterminant = mathLib.determinante(actualSystem);

                                        if (newDeterminant == originalDeterminant) {
                                                System.out.println("El determinante no cambió. Repite el turno.");
                                                actualSystem[row][column] = previousValue; // Revertir el cambio
                                                haveToRepeat = true;
                                                // continue;
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

                                        printSystem();
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

        private void updatePoints(Player player, double nuevoDeterminante) {
                double diference = Math.abs(originalDeterminant - nuevoDeterminante);
                if (nuevoDeterminante > 0) {
                        player.setScore(player.getScore() + diference);
                } else {
                        player.setScore(player.getScore() + nuevoDeterminante); // Suma determinante negativo
                }
        }

        private void getWinner() {
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

        private void printSystem() {
                for (double[] fila : actualSystem) {
                        for (double valor : fila) {
                                System.out.printf("%6d", (int) valor);
                        }
                        System.out.println();
                }
        }
}
