import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La clase {@code MenuGUI} es responsable de gestionar la interfaz gráfica del
 * menú principal del juego. Proporciona opciones interactivas para el usuario,
 * como mostrar instrucciones, comenzar el juego, refrescar los sistemas de
 * ecuaciones, mostrar el sistema actual y reiniciar el juego. Esta clase maneja
 * la creación de botones y la interacción con el jugador a través de eventos
 * gráficos.
 * 
 * @author Mauricio Artavia Monge C10743 - Kenneth Delgado Cárdenas C22540
 */
public class MenuGUI {
    /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
    private double originalDeterminant;

    /* =-=-= Declaracion de Variables de Tipo Double[][] =-=-= */
    private double[][] actualSystem;

    /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
    private int total_Rounds;
    private int refreshCount;
    private int amountSystems;

    /* =-=-= Declaracion de Variables de Tipo Clase MathLib =-=-= */
    private MathLib mathLib;

    /* =-=-= Declaracion de Variables de Tipo Clase Player =-=-= */
    private Player[] players;

    /* =-=-= Metodo Constructor de la clase MenuGUI =-=-= */
    /**
     * Constructor de la clase {@code MenuGUI}.
     * 
     * Este constructor inicializa los jugadores, su número y la cantidad de
     * sistemas de ecuaciones que se utilizarán en el juego. También se encarga de
     * crear una instancia de la clase {@code MathLib} para manejar operaciones
     * matemáticas y genera los sistemas de ecuaciones que estarán disponibles en el
     * juego.
     * 
     * @param playerNames Un arreglo de cadenas que contiene los nombres de los
     *                    jugadores que participarán en el juego.
     */
    public MenuGUI(String[] playerNames) {
        // Inicialización del contador de refrescos disponibles.
        this.refreshCount = 3;

        // Inicialización de la clase MathLib para realizar operaciones matemáticas.
        this.mathLib = new MathLib();

        // Inicialización de la lista de jugadores basada en los nombres proporcionados.
        this.players = new Player[playerNames.length];

        for (int i = 0; i < playerNames.length; i++) {
            // Cada jugador recibe su nombre y el estado de "puede jugar" (true).
            this.players[i] = new Player(playerNames[i], true);
        }

        // Solicita al usuario la cantidad de sistemas de ecuaciones que desea (rango
        // entre 3 y 10)
        this.amountSystems = getValidatedInput("Ingrese la cantidad de sistemas deseados:", 3, 10);

        // Genera los sistemas de ecuaciones y permite la votación de los jugadores para
        // elegir uno
        generateAndVoteSystems(amountSystems);
    }

    /**
     * Método principal de la clase {@code MenuGUI} que crea y muestra el menú
     * gráfico del juego.
     * 
     * Este método configura la ventana principal de la aplicación utilizando
     * {@code JFrame} y añade botones para interactuar con el juego, como mostrar
     * instrucciones, iniciar el juego, refrescar los sistemas de ecuaciones,
     * mostrar el sistema actual, reiniciar el juego o salir. Además, asocia cada
     * botón con una acción correspondiente a través de {@code ActionListener}.
     */
    public void run() {
        /* =-=-= Declaracion de Variables de Tipo JButton =-=-= */
        JButton showInstructionsButton;
        JButton startGameButton;
        JButton refreshSystemsButton;
        JButton showSystemButton;
        JButton resetGameButton;
        JButton exitButton;

        /* =-=-= Declaracion de Variables de Tipo JFrame =-=-= */
        JFrame runFrame;

        /* =-=-= Declaracion de Variables de Tipo JLabel =-=-= */
        JLabel title;

        // Creamos el marco principal de la ventana (JFrame).
        runFrame = new JFrame("Menú Principal");
        runFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerramos la aplicación cuando se cierra la ventana.
        runFrame.setSize(400, 400);// Establecemos el tamaño de la ventana.
        runFrame.setLayout(new GridLayout(7, 1)); // Establecer el layout a GridLayout con 7 filas.

        // Crear un título en el centro de la ventana.
        title = new JLabel("Menú Principal", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20)); // Configuramos la fuente.
        runFrame.add(title);

        // Creamos los botones para las distintas acciones del juego.
        showInstructionsButton = new JButton("1. Leer Instrucciones");
        startGameButton = new JButton("2. Empezar Juego");
        refreshSystemsButton = new JButton("3. Refrescar Sistemas");
        showSystemButton = new JButton("4. Mostrar Sistema Actual");
        resetGameButton = new JButton("5. Reiniciar Juego");
        exitButton = new JButton("6. Salir");

        // Añadimos los botones a la ventana.
        runFrame.add(showInstructionsButton);
        runFrame.add(startGameButton);
        runFrame.add(refreshSystemsButton);
        runFrame.add(showSystemButton);
        runFrame.add(resetGameButton);
        runFrame.add(exitButton);

        // Asignamos las acciones a los botones.
        showInstructionsButton.addActionListener(e -> showInstructions());
        startGameButton.addActionListener(e -> selectGameMode());
        refreshSystemsButton.addActionListener(e -> refreshSystems());
        showSystemButton.addActionListener(e -> showCurrentSystem());
        resetGameButton.addActionListener(e -> resetGame(true));
        exitButton.addActionListener(e -> exitGame(runFrame));

        // Hacemos visible la ventana.
        runFrame.setVisible(true);
    }

    /**
     * Muestra una ventana con las instrucciones del juego.
     * 
     * Este método crea un panel con un título y un área de texto que describe el
     * objetivo del juego y las reglas. Luego muestra este panel en un cuadro de
     * diálogo de tipo {@code JOptionPane}.
     * 
     * El texto incluye las instrucciones sobre cómo jugar, incluyendo la meta del
     * juego, cómo resolver la matriz con mayor determinante y la manera de competir
     * con otros jugadores. También informa al jugador que debe presionar "OK" para
     * regresar al menú principal.
     */
    private void showInstructions() {
        /* =-=-= Declaracion de Variables de Tipo JLabel =-=-= */
        JLabel titleLabel;

        /* =-=-= Declaracion de Variables de Tipo JPanel =-=-= */
        JPanel panel;

        /* =-=-= Declaracion de Variables de Tipo JTextArea =-=-= */
        JTextArea instructionsText;

        // Creamos el JPanel para contener los componentes.
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Apilamos los componentes verticalmente.

        // Creamos el título de las instrucciones con fuente grande y color azul.
        titleLabel = new JLabel("---- Instrucciones ----");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente en negrita y tamaño grande.
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centramos el texto.
        titleLabel.setForeground(Color.BLUE); // Color azul para el título.
        panel.add(titleLabel); // Añadimos al panel.

        // Añadimos espacio entre los elementos.
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Creamos un JTextArea para mostrar el texto de las instrucciones.
        instructionsText = new JTextArea();
        instructionsText.setText("Objetivo del juego:\n" +
                " - Resolver la matriz con mayor determinante.\n" +
                " - Maximizar tu puntaje.\n" +
                " - Competir contra otros jugadores para ser el mejor.\n\n" +
                "Presiona OK para volver al menú principal.");
        instructionsText.setFont(new Font("Arial", Font.PLAIN, 16)); // Fuente de texto.
        instructionsText.setEditable(false); // No editable por el usuario.
        instructionsText.setBackground(Color.LIGHT_GRAY); // Fondo del texto similar al panel.
        instructionsText.setWrapStyleWord(true); // Aseguramos que el texto no se desborde.
        instructionsText.setLineWrap(true); // Habilitamos el ajuste de líneas.
        instructionsText.setAlignmentX(Component.CENTER_ALIGNMENT); // Centralizamos el texto.
        panel.add(instructionsText); // Añadimos el área de texto al panel.

        // Añadimos espacio entre el texto y el botón.
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Mostramos el panel en un cuadro de diálogo.
        JOptionPane.showMessageDialog(null, panel, "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un cuadro de diálogo para que el jugador seleccione el modo de juego.
     * 
     * Este método presenta al jugador una lista de opciones (mediante un JComboBox)
     * donde puede elegir entre tres modos de juego: rápido (3 rondas), normal (5
     * rondas) y extendido (10 rondas). Luego, dependiendo de la selección del
     * jugador, el número de rondas del juego se ajusta. Si no se selecciona ninguna
     * opción o se presiona "Cancelar", el juego volverá al menú principal.
     * 
     * El método también maneja la validación de la selección y se asegura de que se
     * elija una opción válida.
     * Después de hacer la selección, el juego inicia con el modo elegido.
     */
    private void selectGameMode() {
        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        int result; // Resultado de la confirmación de la selección.
        int selectedIndex; // Índice de la opción seleccionada en el JComboBox.

        /* =-=-= Declaracion de Variables de Tipo JComboBox =-=-= */
        JComboBox<String> comboBox;

        /* =-=-= Declaracion de Variables de Tipo String[] =-=-= */
        // Creamos las opciones de la lista para los modos de juego.
        String[] options = { "Modo rápido (3 rondas)", "Modo normal (5 rondas)", "Modo extendido (10 rondas)" };

        // Creamos un JComboBox con las opciones de modo de juego.
        comboBox = new JComboBox<>(options);
        comboBox.setSelectedIndex(1); // Preseleccionar el modo normal (5 rondas).

        // Mostrar el JComboBox en un cuadro de diálogo (JOptionPane).
        result = JOptionPane.showConfirmDialog(null,
                comboBox,
                "Selecciona un Modo de Juego",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        // Verificar si el usuario seleccionó una opción y presionó "Aceptar".
        if (result == JOptionPane.OK_OPTION) {
            selectedIndex = comboBox.getSelectedIndex(); // Obtenemos el índice seleccionado.

            // Ajustamos el número de rondas según la selección.
            switch (selectedIndex) {
                case 0:
                    total_Rounds = 3; // Modo rápido
                    break;
                case 1:
                    total_Rounds = 5; // Modo normal
                    break;
                case 2:
                    total_Rounds = 10; // Modo extendido
                    break;
                default:
                    // Si la selección no es válida, establecer un valor predeterminado.
                    JOptionPane.showMessageDialog(null,
                            "¡VAMOS! ¿le tuviste miedo al exito? Usaremos el modo normal supongo . . .",
                            "Modo de Juego",
                            JOptionPane.WARNING_MESSAGE);
                    total_Rounds = 5;
            }

            beginGame(); // Comienza el juego con el modo seleccionado
        } else {
            // Si no se seleccionó un modo, regresar al menú principal.
            JOptionPane.showMessageDialog(null,
                    "¡VAMOS! ¿le tuviste miedo al exito? Vuelve cuando te sientas preparado.",
                    "Modo de Juego",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Inicia y maneja el flujo de las rondas del juego.
     * 
     * Este método es responsable de controlar el flujo de cada ronda del juego.
     * Para cada ronda, permite que los jugadores ingresen una fila, columna y un
     * valor para modificar la matriz.
     * Se asegura de que las entradas sean válidas y que el determinante cambie
     * antes de permitir que el jugador registre su movimiento.
     * 
     * El juego continúa hasta que un jugador alcanza 21 puntos o todas las rondas
     * han sido jugadas. Además, se verifica que el determinante no se repita entre
     * los jugadores y se asignan puntos de acuerdo con el cambio en el
     * determinante. Si un jugador comete un error en su turno, el sistema le
     * permitirá intentar nuevamente.
     * 
     * Después de completar todas las rondas, el juego determinará al ganador y al
     * perdedor y mostrará las estadísticas.
     */
    private void beginGame() {
        /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
        boolean validTurn;

        /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
        double newDeterminant;
        double previousValue;

        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        int column;
        int newValue;
        int row;

        /* =-=-= Declaracion de Variables de Tipo String =-=-= */
        String input;

        /* =-=-= Declaracion de Variables de Tipo String[] =-=-= */
        String[] parts;

        resetGame(false);
        // Calculamos el determinante original del sistema actual.
        originalDeterminant = mathLib.determinante(actualSystem);

        // Loop para las rondas del juego.
        for (int round = 1; round <= total_Rounds; round++) {
            // Muestra el número de la ronda en una ventana emergente.
            JOptionPane.showMessageDialog(null, "=== Ronda " + round + " ===", "Ronda",
                    JOptionPane.INFORMATION_MESSAGE);

            // Loop para cada jugador en cada ronda.
            for (Player player : players) {
                // Si el jugador no puede jugar, le pierde el turno y pasa al siguiente.
                if (!player.getPlayStatus()) {
                    JOptionPane.showMessageDialog(null, player.getName() + " pierde su turno.", "Turno perdido",
                            JOptionPane.WARNING_MESSAGE);
                    player.setPlayStatus(true); // Restablece para el próximo turno
                    continue;
                }

                // Reintenta hasta que el jugador ingrese datos válidos.
                validTurn = false;
                while (!validTurn) {
                    // Solicitamos la entrada del jugador.
                    input = JOptionPane.showInputDialog(null, player.getName()
                            + ", ingresa fila (1 a 3), columna (1 a 3) y nuevo valor (-5 a 5) separados por comas:");

                    if (input == null)
                        return; // Si el jugador cancela el input, salimos.

                    // Divide la entrada por las comas
                    parts = input.split(",");

                    try {
                        // Convierte las entradas a los valores correspondientes.
                        row = Integer.parseInt(parts[0].trim()) - 1;
                        column = Integer.parseInt(parts[1].trim()) - 1;
                        newValue = Integer.parseInt(parts[2].trim());

                        // Validación de la fila, columna y el valor.
                        if (row < 0 || row > 2 || column < 0 || column > 2 || newValue < -5 || newValue > 5) {
                            throw new NumberFormatException("Valores fuera de rango.");
                        }

                        // Almacena el valor anterior de la matriz para restaurarlo si es necesario.
                        previousValue = actualSystem[row][column];
                        // Modifica la matriz con el nuevo valor.
                        actualSystem[row][column] = newValue;
                        // Calcula el nuevo determinante
                        newDeterminant = mathLib.determinante(actualSystem);

                        // Verificamos si el determinante ha cambiado.
                        if (newDeterminant == originalDeterminant) {
                            JOptionPane.showMessageDialog(null, "El determinante no cambió. Repite el turno.",
                                    "Turno repetido", JOptionPane.WARNING_MESSAGE);
                            // Restaura el valor anterior si el determinante no cambió.
                            actualSystem[row][column] = previousValue;
                            continue;
                        }

                        // Verifica si el jugador ha usado el mismo determinante antes.
                        if (newDeterminant == player.getDeterminant()) {
                            JOptionPane.showMessageDialog(null,
                                    "El determinante ya se había usado. Pierdes el próximo turno.", "Penalización",
                                    JOptionPane.WARNING_MESSAGE);
                            // Penaliza al jugador si usa el mismo determinante
                            player.setPlayStatus(false);
                        } else {
                            // Si el turno es válido, actualiza los puntos del jugador y el determinante.
                            updatePoints(player, newDeterminant);
                            player.setDeterminant(newDeterminant);
                        }

                        // Muestra la matriz actualizada y el puntaje acumulado.
                        showCurrentSystem();
                        JOptionPane.showMessageDialog(null, "Puntaje acumulado de " + player.getName() + ": "
                                + player.getScore(), "Puntaje", JOptionPane.INFORMATION_MESSAGE);

                        // Si un jugador alcanza 21 puntos, termina el juego.
                        if (player.getScore() == 21) {
                            getWinner();
                            getLoser();
                            getStats();
                            return;
                        }

                        // El turno es válido, termina el ciclo.
                        validTurn = true;

                    } catch (Exception ex) {
                        // Si ocurre un error en la entrada, muestra un mensaje y solicita los datos
                        // nuevamente.
                        JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, inténtalo de nuevo.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        // Al finalizar todas las rondas, determina el ganador, el perdedor y muestra
        // las estadísticas
        getWinner();
        getLoser();
        getStats();
    }

    /**
     * Genera un número determinado de sistemas de ecuaciones aleatorios y permite
     * que los jugadores voten por uno de ellos para jugar.
     * 
     * Este método realiza los siguientes pasos:
     * 1. Genera una cantidad específica de sistemas de ecuaciones con valores
     * aleatorios, asegurándose de que
     * cada sistema tenga una solución única (determinante diferente de cero).
     * 2. Muestra estos sistemas de ecuaciones en un cuadro de diálogo para que los
     * jugadores voten por uno de
     * ellos.
     * 3. Una vez seleccionado el sistema por los jugadores, lo asigna como el
     * sistema actual y calcula su
     * determinante.
     * 
     * @param amount El número de sistemas de ecuaciones a generar.
     */
    private void generateAndVoteSystems(int amount) {
        /* =-=-= Declaracion de Variables de Tipo Double[][][] =-=-= */
        double[][][] systems;

        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        int selected;

        /* =-=-= Declaracion de Variables de Tipo String[] =-=-= */
        String[] options;

        // Inicializamos el arreglo para almacenar los sistemas de ecuaciones.
        systems = new double[amount][3][4];

        // Generación de los sistemas de ecuaciones.
        for (int i = 0; i < amount; i++) {
            do {
                // Generamos un sistema aleatorio con valores entre -5 y 5, asegurándonos que
                // sea válido.
                systems[i] = mathLib.generateAleatorySystem(3, 4, -5, 5);
            } while (mathLib.determinante(systems[i]) == 0); // Aseguramos de que el determinante no sea 0.
        }

        // Convertimos cada sistema generado en una cadena formateada para mostrarlo.
        options = new String[amount];
        for (int i = 0; i < amount; i++) {
            options[i] = formatMatrixForDisplay(systems[i]); // Llamamos a la función de formateo.
        }

        // Mostramos un cuadro de diálogo con los sistemas generados para que los
        // jugadores elijan.
        selected = JOptionPane.showOptionDialog(null, "Selecciona un sistema de ecuaciones para jugar:", "Votación",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        // Si el jugador selecciona un sistema (es decir, si no canceló la opción)
        if (selected != -1) {
            // Asignamos el sistema seleccionado como el sistema actual.
            actualSystem = systems[selected];
            // Calculamos y asignamos el determinante del sistema seleccionado.
            originalDeterminant = mathLib.determinante(actualSystem);
            // Mostrar el sistema seleccionado y su determinante en un cuadro de mensaje.
            JOptionPane.showMessageDialog(null, "Sistema seleccionado:\n" + formatMatrixForDisplay(actualSystem)
                    + "\nDeterminante: " + originalDeterminant, "Sistema Seleccionado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método que formatea la matriz para ser más legible (con saltos de línea)
    /**
     * Formatea una matriz en una representación legible con saltos de línea y
     * separación visual para los términos independientes.
     * 
     * El formato incluye:
     * - Cada fila de la matriz en una nueva línea.
     * - Los términos independientes (última columna) separados por el símbolo "|".
     * 
     * @param matrix La matriz a formatear.
     * @return Una representación en texto de la matriz, lista para ser mostrada en
     *         pantalla.
     */
    private String formatMatrixForDisplay(double[][] matrix) {
        /* =-=-= Declaracion de Variables de Tipo StringBuilder =-=-= */
        StringBuilder builder;

        builder = new StringBuilder(); // Inicialización del StringBuilder.

        // Recorremos las filas de la matriz
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length - 1; j++) {
                // Agregar cada elemento excepto el último, formateado para alineación.
                builder.append(String.format("%6.0f", matrix[i][j])).append(" ");
            }
            // Agregar el último elemento con el símbolo "|" y un salto de línea.
            builder.append("| ").append(String.format("%6.0f", matrix[i][matrix[i].length - 1])).append("\n");
        }
        return builder.toString(); // Retorna la representación formateada de la matriz.
    }

    /**
     * Refresca los sistemas de ecuaciones disponibles y permite a los jugadores
     * seleccionar un nuevo sistema para jugar.
     *
     * Este método:
     * - Permite al usuario refrescar los sistemas hasta un número limitado de
     * veces.
     * - Genera nuevos sistemas de ecuaciones si hay refrescos disponibles.
     * - Muestra un mensaje indicando la cantidad de refrescos restantes o una
     * advertencia si ya no
     * hay refrescos disponibles.
     */
    private void refreshSystems() {
        // Verificamos si hay refrescos disponibles
        if (refreshCount > 0) {
            generateAndVoteSystems(amountSystems); // Generamos nuevos sistemas.
            refreshCount--; // Reducimos el contador de refrescos disponibles.
            // Informamos al jugador cuántos refrescos quedan.
            JOptionPane.showMessageDialog(null, "Te quedan " + refreshCount + " refrescos disponibles.", "Refrescar",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Muestra un mensaje de advertencia si no hay refrescos disponibles.
            JOptionPane.showMessageDialog(null, "Ya no puedes refrescar los sistemas.", "Refrescar",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Resetea el estado del juego, reiniciando los puntajes de los jugadores y,
     * opcionalmente generando un nuevo sistema de ecuaciones.
     * 
     * Este método realiza:
     * - Reinicio de puntajes y estados de los jugadores.
     * - Generación de nuevos sistemas de ecuaciones si el reinicio es completo.
     * 
     * @param fullReset Indica si el reinicio es completo:
     *                  - {@code true}: Reinicia todo, incluyendo los sistemas de
     *                  ecuaciones.
     *                  - {@code false}: Solo reinicia los puntajes y restaura el
     *                  sistema actual.
     */
    private void resetGame(boolean fullReset) {
        // Reiniciar el puntaje, determinante y el estado de cada jugador.
        for (Player player : players) {
            player.setScore(0); // Reinicia el puntaje a 0.
            player.setDeterminant(0); // Reinicia el determinante almacenado.
            player.setPlayStatus(true); // Habilita al jugador para jugar.
        }

        // Si el reinicio es completo, generar nuevos sistemas.
        if (fullReset) {
            generateAndVoteSystems(amountSystems); // Genera nuevos sistemas.
        }
    }

    /**
     * Gestiona la salida del juego mediante una confirmación.
     * 
     * Este método muestra un cuadro de diálogo que pregunta al usuario si desea
     * salir del juego. Si el usuario confirma, cierra el `JFrame` asociado.
     * 
     * @param frame El marco principal del juego que será cerrado si se confirma la
     *              salida.
     */
    private void exitGame(JFrame frame) {
        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        int confirm;

        // Muestra un cuadro de diálogo de confirmación.
        confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que deseas salir?", "Salir",
                JOptionPane.YES_NO_OPTION);

        // Si el usuario confirma, cerrar el marco
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose(); // Liberar recursos y cerrar el marco.
        }
    }

    /**
     * Muestra el sistema de ecuaciones actual en un formato tabular junto con su
     * determinante.
     * 
     * Este método:
     * - Organiza los datos del sistema en una tabla (`JTable`).
     * - Resalta la columna de términos independientes.
     * - Muestra el determinante actual del sistema debajo de la tabla.
     * 
     * Si no hay un sistema generado, se muestra un mensaje de advertencia.
     */
    private void showCurrentSystem() {
        /* =-=-= Declaracion de Variables de Tipo JLabel =-=-= */
        JLabel determinantLabel;

        /* =-=-= Declaracion de Variables de Tipo JPanel =-=-= */
        JPanel panel;

        /* =-=-= Declaracion de Variables de Tipo JScrollPane =-=-= */
        JScrollPane scrollPane;

        /* =-=-= Declaracion de Variables de Tipo JTable =-=-= */
        JTable table;

        /* =-=-= Declaracion de Variables de Tipo String =-=-= */
        String determinantText;

        /* =-=-= Declaracion de Variables de Tipo String[][] =-=-= */
        String[][] data;

        /* =-=-= Declaracion de Variables de Tipo TableColumnModel =-=-= */
        TableColumnModel columnModel;

        // Verificar si hay un sistema actual generado.
        if (actualSystem != null) {
            // Creamos un modelo de tabla con los datos del sistema actual.
            data = new String[actualSystem.length][actualSystem[0].length];
            for (int i = 0; i < actualSystem.length; i++) {
                for (int j = 0; j < actualSystem[i].length; j++) {
                    data[i][j] = String.format("%6.0f", actualSystem[i][j]); // Formatear valores de la matriz.
                }
            }

            // Creamos las columnas para la tabla.
            String[] columnNames = { "Columna 1", "Columna 2", "Columna 3", "Término Independiente" };

            // Instanciamos la JTable con los datos y las columnas.
            table = new JTable(data, columnNames);

            // Personalizamos el renderizador de las celdas para hacer que se vean más
            // agradables.
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    // Resaltamos el término independiente (última columna).
                    if (column == 3) {
                        c.setBackground(new Color(255, 255, 153)); // Resaltamos en amarillo claro el fondo.
                    } else {
                        c.setBackground(Color.white); // Fondo blanco por defecto.
                    }
                    return c; // Retornamos el componente configurado.
                }
            });

            // Ajustar el ancho de las columnas.
            columnModel = table.getColumnModel();
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                columnModel.getColumn(i).setPreferredWidth(100); // Ancho predeterminado.
            }

            // Envolvemos la tabla en un JScrollPane.
            scrollPane = new JScrollPane(table);

            // Creamos un JLabel para mostrar el determinante actual.
            determinantText = "Determinante actual: " + String.format("%6.0f", originalDeterminant);
            determinantLabel = new JLabel(determinantText, JLabel.CENTER);

            // Configuramos un JPanel para organizar la tabla y el determinante.
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER); // Tabla en el centro del panel.
            panel.add(determinantLabel, BorderLayout.SOUTH); // Determinante debajo de la tabla.

            // Mostramos la tabla y el determinante en un JOptionPane.
            JOptionPane.showMessageDialog(null, panel, "Sistema Actual", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Mensaje de advertencia si no hay sistema generado.
            JOptionPane.showMessageDialog(null, "No hay sistema generado aún.", "Sistema Actual",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Determina y muestra al ganador del juego.
     * 
     * El ganador es priorizado si su puntaje alcanza los 21 puntos. Si no hay
     * jugadores con puntaje 21, este método recorre la lista de jugadores para
     * encontrar al que tenga la mayor puntuación. Si hay un jugador con un puntaje
     * mayor a 0, se muestra como ganador. En caso de que todos los puntajes sean
     * negativos o nulos, se informa que no hay ganador.
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

        // Buscamos al jugador con la puntuación más alta.
        for (Player player : players) {
            if (winner == null || player.getScore() > winner.getScore()) {
                winner = player;
            }
        }

        // Mostramos el resultado en función de la puntuación del ganador.
        if (winner != null && winner.getScore() > 0) {
            JOptionPane.showMessageDialog(null, "Felicidades, " + winner.getName() + " ha ganado con "
                    + winner.getScore() + " puntos.", "Ganador", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Nadie gana. Todos tienen puntajes negativos o nulos.", "Sin Ganador",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Determina y muestra al jugador con la menor puntuación (perdedor).
     * 
     * Este método recorre la lista de jugadores para encontrar al que tenga la
     * puntuación más baja. Si el perdedor tiene una puntuación mayor a 0, se
     * muestra un mensaje de ánimo.
     */
    private void getLoser() {
        /* =-=-= Declaracion de Variables de Tipo Clase Player =-=-= */
        Player loser;

        loser = null;

        // Buscamos al jugador con la puntuación más baja.
        for (Player player : players) {
            if (loser == null || player.getScore() < loser.getScore()) {
                loser = player;
            }
        }

        // Mostramos un mensaje de ánimo al perdedor.
        if (loser != null && loser.getScore() > 0) {
            JOptionPane.showMessageDialog(null,
                    "Animo " + loser.getName() + ", habran mas oportunidades luego! (" + loser.getScore()
                            + " puntos)"
                            + loser.getScore() + " puntos.",
                    "Perdedor", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Muestra las estadísticas del juego.
     * 
     * Este método genera un resumen con el nombre y la puntuación de cada jugador,
     * y lo presenta en un cuadro de diálogo.
     */
    private void getStats() {
        /* =-=-= Declaracion de Variables de Tipo SringBuilder =-=-= */
        StringBuilder stats;

        stats = new StringBuilder();
        stats.append("Estadísticas del Juego:\n\n");

        // Construimos las estadísticas para cada jugador.
        for (Player player : players) {
            stats.append(player.getName())
                    .append(" - Puntaje: ")
                    .append(player.getScore())
                    .append("\n");
        }

        // Mostramos las estadísticas en un cuadro de diálogo.
        JOptionPane.showMessageDialog(null, stats.toString(), "Estadísticas del Juego",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Solicita al usuario una entrada numérica validada dentro de un rango.
     * 
     * Este método muestra un cuadro de diálogo para que el usuario ingrese un
     * número. Valida que la entrada sea un entero y que esté dentro de los límites
     * especificados. Si la entrada no es válida, se muestra un mensaje de error y
     * se solicita nuevamente.
     * 
     * @param prompt el mensaje que se mostrará al usuario.
     * @param min    el valor mínimo aceptable.
     * @param max    el valor máximo aceptable.
     * @return el número entero ingresado por el usuario dentro del rango
     *         especificado.
     */
    private int getValidatedInput(String prompt, int min, int max) {
        /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
        boolean valid;

        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        int value;

        /* =-=-= Declaracion de Variables de Tipo String =-=-= */
        String input;

        value = 0; // Valor predeterminado
        valid = false;

        do {
            try {
                // Solicitamos una entrada al usuario.
                input = JOptionPane.showInputDialog(null,
                        prompt + " (" + min + " a " + max + "):",
                        "Entrada requerida",
                        JOptionPane.QUESTION_MESSAGE);

                // Validamos si el usuario cancela o deja el campo vacío.
                if (input == null || input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Debes ingresar un número.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // Intentamos convertir la entrada a entero.
                value = Integer.parseInt(input.trim());

                // Verificamos si el valor está dentro del rango.
                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Entrada fuera de rango. Inténtalo de nuevo.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                // Manejamos entradas no numéricas.
                JOptionPane.showMessageDialog(null,
                        "Entrada inválida. Debe ser un número entero.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!valid);

        return value;
    }

    /**
     * Actualiza el puntaje del jugador basado en el determinante calculado.
     * 
     * Este método compara el determinante calculado con el determinante original y
     * calcula la diferencia absoluta. Si el determinante es positivo, se suma la
     * diferencia al puntaje del jugador. Si es negativo, se suma directamente el
     * valor del determinante.
     * 
     * @param player         el jugador cuyo puntaje será actualizado.
     * @param newDeterminant el nuevo determinante calculado para el sistema.
     */
    private void updatePoints(Player player, double newDeterminant) {
        /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
        double difference;

        // Calculamos la diferencia absoluta entre el determinante original y el nuevo.
        difference = Math.abs(originalDeterminant - newDeterminant);

        // Si el determinante es positivo, aumentamos el puntaje en la diferencia.
        if (newDeterminant > 0) {
            player.setScore(player.getScore() + difference);
        } else {
            // Si el determinante es negativo, sumamos el valor del determinante.
            player.setScore(player.getScore() + newDeterminant);
        }
    }
}
