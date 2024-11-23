import java.util.Scanner;

public class Menu {
        /* =-=-= CONSTANTES =-=-= */
        public static final String BLUE = "\033[34m"; // Azul
        public static final String CYAN = "\033[36m"; // Cian
        public static final String GREEN = "\033[32m"; // Verde
        public static final String RED = "\033[31m"; // Rojo
        public static final String RESET = "\033[0m"; // Resetear color
        public static final String YELLOW = "\033[33m"; // Amarillo
    
        /* =-=-= VARIABLES =-=-= */
        private int amountSystems;
        private int totalRounds;
        private int refreshCount;
    
        private double originalDeterminant;
    
        private double[][] actualSystem;
    
        private MathLib mathLib;
    
        private Player[] players;
    
        private Scanner in;
    
        /* =-=-= Constructor de la clase Menu =-=-= */
        public Menu(String[] playersList) {
            this.refreshCount = 3;
            this.in = new Scanner(System.in);
            this.mathLib = new MathLib();
            this.players = new Player[playersList.length];
            this.amountSystems = 0; // Inicialmente no hay sistemas configurados.
    
            for (int i = 0; i < playersList.length; i++) {
                this.players[i] = new Player(playersList[i], true);
            }
        }
    
        /**
         * Método para configurar el juego antes de iniciar las rondas.
         * Este método se debe llamar explícitamente después de crear la instancia.
         */
        public void setupGame() {
            this.amountSystems = getValidatedInput("Ingrese la cantidad de sistemas deseados: ", 3, 10);
            generateAndVoteSystems(this.amountSystems); // Genera los sistemas y permite votar.
        }
    
        /**
         * Método principal del juego. Ofrece opciones para jugar, refrescar sistemas,
         * reiniciar el juego, etc.
         */
        public void run() {
            boolean proceed = true;
    
            while (proceed) {
                System.out.println(BLUE + "\n=== Menú Principal ===" + RESET);
                System.out.println("1. Empezar Juego");
                System.out.println("2. Refrescar sistemas");
                System.out.println("3. Mostrar sistema actual");
                System.out.println("4. Reiniciar Juego");
                System.out.println("5. Salir");
                System.out.print(CYAN + "¡Seleccione una opción!: " + RESET);
    
                try {
                    int option = Integer.parseInt(in.nextLine());
    
                    switch (option) {
                        case 1:
                            startGame();
                            break;
    
                        case 2:
                            refreshSystems();
                            break;
    
                        case 3:
                            if (actualSystem != null) {
                                printSystem(actualSystem);
                                System.out.println(
                                        BLUE + "Determinante actual: " + originalDeterminant + RESET);
                            } else {
                                System.out.println(YELLOW + "No hay sistema generado aún." + RESET);
                            }
                            break;
    
                        case 4:
                            resetGame(1);
                            break;
    
                        case 5:
                            System.out.println(CYAN + "¡Gracias por jugar! ¡Hasta la próxima!" + RESET);
                            proceed = false; // Salir del programa.
                            break;
    
                        default:
                            System.out.println(RED + "Opción no válida. Intente nuevamente." + RESET);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(RED + "Entrada inválida. Por favor, ingrese una opción válida." + RESET);
                }
            }
        }
    
        private void startGame() {
            boolean playingProcess = true;
    
            while (playingProcess) {
                try {
                    System.out.println(BLUE + "Modo de juego:" + RESET);
                    System.out.println(RED + "1. Modo rápido (3 rondas)" + RESET);
                    System.out.println(YELLOW + "2. Modo normal (5 rondas)" + RESET);
                    System.out.println(GREEN + "3. Modo extendido (10 rondas)" + RESET);
    
                    totalRounds = getValidatedInput("Selecciona un modo de juego", 1, 3);
    
                    if (totalRounds >= 1 && totalRounds <= 3) {
                        totalRounds = (totalRounds == 1 ? 3 : (totalRounds == 2 ? 5 : 10));
                        playingProcess = false;
                        beginGame(); // Iniciar el juego.
                    } else {
                        System.out.println(RED + "¡Elige un modo de juego válido!" + RESET);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(RED + "Entrada inválida. Intente nuevamente." + RESET);
                }
            }
        }
    
        private void resetGame(int totalReset) {
            for (Player player : players) {
                player.setScore(0);
                player.setDeterminant(0);
                player.setPlayStatus(true);
            }
    
            if (totalReset == 1) {
                generateAndVoteSystems(amountSystems);
                System.out.println(YELLOW + "\n¡El juego ha sido reiniciado!" + RESET);
            } else {
                System.out.println(YELLOW + "\n¡Los puntajes han sido reiniciados!" + RESET);
            }
        }
    
        private void beginGame() {
            // Lógica del juego, como las rondas y los turnos (sin cambios aquí).
        }
    
        private void generateAndVoteSystems(int amount) {
            double[][][] systems = new double[amount][3][4];
    
            for (int i = 0; i < amount; i++) {
                do {
                    systems[i] = mathLib.generateAleatorySystem(3, 4, -5, 5);
                } while (mathLib.determinante(systems[i]) == 0);
    
                System.out.println(BLUE + "\nSistema " + (i + 1) + ":" + RESET);
                printSystem(systems[i]);
            }
    
            int selectedSystem = conductVoting(systems);
            actualSystem = systems[selectedSystem];
            originalDeterminant = mathLib.determinante(actualSystem);
    
            System.out.println(BLUE + "\nSistema seleccionado:" + RESET);
            printSystem(actualSystem);
            System.out.println(BLUE + "Determinante: " + originalDeterminant + RESET);
        }
    
        private void refreshSystems() {
            if (refreshCount > 0) {
                generateAndVoteSystems(amountSystems);
                refreshCount--;
                System.out.println(BLUE + "Te quedan " + RESET + YELLOW + refreshCount + RESET +
                        BLUE + " refrescos disponibles." + RESET);
            } else {
                System.out.println(RED + "No puedes refrescar los sistemas." + RESET);
            }
        }
    
        private int getValidatedInput(String prompt, int min, int max) {
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
            int[] votes = new int[systems.length];
    
            for (Player player : players) {
                int choice = getValidatedInput(
                        player.getName() + ", selecciona el sistema con el que deseas jugar", 1,
                        systems.length);
                votes[choice - 1]++;
            }
    
            int selectedSystem = 0;
            for (int i = 1; i < votes.length; i++) {
                if (votes[i] > votes[selectedSystem]) {
                    selectedSystem = i;
                }
            }
    
            return selectedSystem;
        }
    
        private void printSystem(double[][] system) {
            for (int i = 0; i < system.length; i++) {
                for (int j = 0; j < system[i].length - 1; j++) {
                    System.out.printf("%6d ", (int) system[i][j]);
                }
                System.out.printf("| %6d\n", (int) system[i][system[i].length - 1]);
            }
        }
    }