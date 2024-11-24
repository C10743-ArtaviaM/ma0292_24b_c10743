import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGUI {
    private int total_Rounds = 5;
    private int refreshCount = 3;
    private int amountSystems;
    private double originalDeterminant;
    private double[][] actualSystem;
    private MathLib mathLib;
    private Player[] players;

    public MenuGUI(String[] playerNames) {
        mathLib = new MathLib();
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i], true);
        }
        amountSystems = getValidatedInput("Ingrese la cantidad de sistemas deseados (3-10):", 3, 10);
        generateAndVoteSystems(amountSystems);
    }

    public void run() {
        JFrame frame = new JFrame("Menú Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(7, 1)); // Añadimos una fila más para las instrucciones

        JLabel title = new JLabel("Menú Principal", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(title);

        JButton showInstructionsButton = new JButton("1. Mostrar Instrucciones");
        JButton startGameButton = new JButton("2. Empezar Juego");
        JButton refreshSystemsButton = new JButton("3. Refrescar Sistemas");
        JButton showSystemButton = new JButton("4. Mostrar Sistema Actual");
        JButton resetGameButton = new JButton("5. Reiniciar Juego");
        JButton exitButton = new JButton("6. Salir");

        frame.add(showInstructionsButton);
        frame.add(startGameButton);
        frame.add(refreshSystemsButton);
        frame.add(showSystemButton);
        frame.add(resetGameButton);
        frame.add(exitButton);

        // Acciones
        showInstructionsButton.addActionListener(e -> showInstructions());
        startGameButton.addActionListener(e -> selectGameMode());
        refreshSystemsButton.addActionListener(e -> refreshSystems());
        showSystemButton.addActionListener(e -> showCurrentSystem());
        resetGameButton.addActionListener(e -> resetGame(true));
        exitButton.addActionListener(e -> exitGame(frame));

        frame.setVisible(true);
    }

    private void showInstructions() {
        // Crear el JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Para apilar los componentes

        // Establecer un color de fondo para el panel
        // panel.setBackground(Color.LIGHT_GRAY);

        // Título de las instrucciones con un tamaño de fuente grande
        JLabel titleLabel = new JLabel("---- Instrucciones ----");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.BLUE); // Color azul para el título
        panel.add(titleLabel);

        // Espacio entre elementos
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Texto de las instrucciones con formato
        JTextArea instructionsText = new JTextArea();
        instructionsText.setText("Objetivo del juego:\n" +
                " - Resolver la matriz con mayor determinante.\n" +
                " - Maximizar tu puntaje.\n" +
                " - Competir contra otros jugadores para ser el mejor.\n\n" +
                "Presiona Aceptar para volver al menú principal.");
        instructionsText.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionsText.setEditable(false); // No editable por el usuario
        instructionsText.setBackground(Color.LIGHT_GRAY); // Fondo del texto similar al panel
        instructionsText.setWrapStyleWord(true); // Asegura que el texto no se desborde
        instructionsText.setLineWrap(true); // Habilitar el ajuste de líneas
        instructionsText.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(instructionsText);

        // Espacio entre el texto y el botón
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Mostrar el panel en un cuadro de diálogo
        JOptionPane.showMessageDialog(null, panel, "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }

    private void selectGameMode() {
        // Crear las opciones de la lista
        String[] options = { "Modo rápido (3 rondas)", "Modo normal (5 rondas)", "Modo extendido (10 rondas)" };

        // Crear el JComboBox
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setSelectedIndex(1); // Preseleccionar "Modo normal (5 rondas)"

        // Mostrar la lista en un JOptionPane
        int result = JOptionPane.showConfirmDialog(null,
                comboBox,
                "Selecciona un Modo de Juego",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        // Verificar si se presionó "Aceptar"
        if (result == JOptionPane.OK_OPTION) {
            int selectedIndex = comboBox.getSelectedIndex(); // Obtener el índice seleccionado
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
                    JOptionPane.showMessageDialog(null,
                            "Selección no válida. Usando el modo normal (5 rondas).",
                            "Modo de Juego",
                            JOptionPane.WARNING_MESSAGE);
                    total_Rounds = 5;
            }
            beginGame(); // Comienza el juego con el modo seleccionado
        } else {
            JOptionPane.showMessageDialog(null,
                    "No se seleccionó ningún modo. Volviendo al menú principal.",
                    "Modo de Juego",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void beginGame() {
        resetGame(false);
        originalDeterminant = mathLib.determinante(actualSystem);

        for (int round = 1; round <= total_Rounds; round++) {
            JOptionPane.showMessageDialog(null, "=== Ronda " + round + " ===", "Ronda",
                    JOptionPane.INFORMATION_MESSAGE);

            for (Player player : players) {
                if (!player.getPlayStatus()) {
                    JOptionPane.showMessageDialog(null, player.getName() + " pierde su turno.", "Turno perdido",
                            JOptionPane.WARNING_MESSAGE);
                    player.setPlayStatus(true);
                    continue;
                }

                // Reintento hasta que el jugador ingrese datos válidos
                boolean validTurn = false;
                while (!validTurn) {
                    String input = JOptionPane.showInputDialog(null, player.getName()
                            + ", ingresa fila (1-3), columna (1-3) y nuevo valor (-5 a 5) separados por comas:");

                    if (input == null)
                        return; // Si el jugador cancela el input, salimos.

                    String[] parts = input.split(",");

                    try {
                        int row = Integer.parseInt(parts[0].trim()) - 1;
                        int column = Integer.parseInt(parts[1].trim()) - 1;
                        int newValue = Integer.parseInt(parts[2].trim());

                        // Validación de fila, columna y valor
                        if (row < 0 || row > 2 || column < 0 || column > 2 || newValue < -5 || newValue > 5) {
                            throw new NumberFormatException("Valores fuera de rango.");
                        }

                        double previousValue = actualSystem[row][column];
                        actualSystem[row][column] = newValue;
                        double newDeterminant = mathLib.determinante(actualSystem);

                        // Validación del determinante
                        if (newDeterminant == originalDeterminant) {
                            JOptionPane.showMessageDialog(null, "El determinante no cambió. Repite el turno.",
                                    "Turno repetido", JOptionPane.WARNING_MESSAGE);
                            actualSystem[row][column] = previousValue; // Revertir el cambio
                            continue;
                        }

                        // Validación de que el determinante no se repita
                        if (newDeterminant == player.getDeterminant()) {
                            JOptionPane.showMessageDialog(null,
                                    "El determinante ya se había usado. Pierdes el próximo turno.", "Penalización",
                                    JOptionPane.WARNING_MESSAGE);
                            player.setPlayStatus(false); // Penalizar
                        } else {
                            // Solo actualizamos los puntos si el movimiento es válido
                            updatePoints(player, newDeterminant);
                            player.setDeterminant(newDeterminant);
                        }

                        // Mostrar la matriz actualizada y los puntos
                        showCurrentSystem();
                        JOptionPane.showMessageDialog(null, "Puntaje acumulado de " + player.getName() + ": "
                                + player.getScore(), "Puntaje", JOptionPane.INFORMATION_MESSAGE);

                        // Si el jugador alcanza 21 puntos, termina el juego
                        if (player.getScore() == 21) {
                            getWinner();
                            getLoser();
                            getStats();
                            return;
                        }

                        validTurn = true; // El turno fue válido, salimos del ciclo

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, inténtalo de nuevo.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        getWinner();
        getLoser();
        getStats();
    }

    private void generateAndVoteSystems(int amount) {
        double[][][] systems = new double[amount][3][4];

        // Generación de los sistemas de ecuaciones
        for (int i = 0; i < amount; i++) {
            do {
                systems[i] = mathLib.generateAleatorySystem(3, 4, -5, 5); // Generar sistema aleatorio
            } while (mathLib.determinante(systems[i]) == 0); // Asegurarnos de que el determinante no sea 0
        }

        String[] options = new String[amount];
        for (int i = 0; i < amount; i++) {
            options[i] = formatMatrixForDisplay(systems[i]); // Llamar a la función de formateo
        }

        int selected = JOptionPane.showOptionDialog(null, "Selecciona un sistema de ecuaciones para jugar:", "Votación",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (selected != -1) {
            actualSystem = systems[selected];
            originalDeterminant = mathLib.determinante(actualSystem);
            JOptionPane.showMessageDialog(null, "Sistema seleccionado:\n" + formatMatrixForDisplay(actualSystem)
                    + "\nDeterminante: " + originalDeterminant, "Sistema Seleccionado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método que formatea la matriz para ser más legible (con saltos de línea)
    private String formatMatrixForDisplay(double[][] matrix) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length - 1; j++) { // Imprimir todos los elementos excepto el último
                builder.append(String.format("%6.0f", matrix[i][j])).append(" ");
            }
            builder.append("| ").append(String.format("%6.0f", matrix[i][matrix[i].length - 1])).append("\n"); // El
                                                                                                               // último
                                                                                                               // elemento
                                                                                                               // con el
                                                                                                               // símbolo
                                                                                                               // "|"
        }
        return builder.toString();
    }

    private void refreshSystems() {
        if (refreshCount > 0) {
            generateAndVoteSystems(amountSystems);
            refreshCount--;
            JOptionPane.showMessageDialog(null, "Te quedan " + refreshCount + " refrescos disponibles.", "Refrescar",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Ya no puedes refrescar los sistemas.", "Refrescar",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void resetGame(boolean fullReset) {
        for (Player player : players) {
            player.setScore(0);
            player.setDeterminant(0);
            player.setPlayStatus(true);
        }

        if (fullReset) {
            generateAndVoteSystems(amountSystems);
        }
    }

    private void exitGame(JFrame frame) {
        int confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que deseas salir?", "Salir",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
        }
    }

    private void showCurrentSystem() {
        if (actualSystem != null) {
            // Crear un modelo de tabla para la matriz
            String[][] data = new String[actualSystem.length][actualSystem[0].length];
            for (int i = 0; i < actualSystem.length; i++) {
                for (int j = 0; j < actualSystem[i].length; j++) {
                    data[i][j] = String.format("%6.0f", actualSystem[i][j]);
                }
            }

            // Crear las columnas
            String[] columnNames = { "Columna 1", "Columna 2", "Columna 3", "Término Independiente" };

            // Crear la JTable con el modelo de datos
            JTable table = new JTable(data, columnNames);

            // Personalizar el renderizador de las celdas para hacer que se vean más
            // agradables
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    // Resaltar el término independiente (última columna)
                    if (column == 3) {
                        c.setBackground(new Color(255, 255, 153)); // Resaltar en amarillo claro
                    } else {
                        c.setBackground(Color.white); // Fondo blanco por defecto
                    }
                    return c;
                }
            });

            // Ajustar la tabla para que las columnas tengan un tamaño adecuado
            TableColumnModel columnModel = table.getColumnModel();
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                columnModel.getColumn(i).setPreferredWidth(100); // Ajuste de tamaño de las columnas
            }

            // Colocar la tabla dentro de un JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);

            // Crear un JLabel para mostrar el determinante actual
            String determinantText = "Determinante actual: " + String.format("%6.0f", originalDeterminant);
            JLabel determinantLabel = new JLabel(determinantText, JLabel.CENTER);

            // Crear un JPanel para organizar la tabla y el determinante
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER); // Agregar la tabla en el centro
            panel.add(determinantLabel, BorderLayout.SOUTH); // Agregar el determinante debajo

            // Mostrar la tabla y el determinante en un JOptionPane
            JOptionPane.showMessageDialog(null, panel, "Sistema Actual", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No hay sistema generado aún.", "Sistema Actual",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void getWinner() {
        Player winner = null;
        for (Player player : players) {
            if (winner == null || player.getScore() > winner.getScore()) {
                winner = player;
            }
        }

        if (winner != null && winner.getScore() > 0) {
            JOptionPane.showMessageDialog(null, "Felicidades, " + winner.getName() + " ha ganado con "
                    + winner.getScore() + " puntos.", "Ganador", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Nadie gana. Todos tienen puntajes negativos o nulos.", "Sin Ganador",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void getLoser() {
        // Aquí puedes agregar la lógica para obtener el perdedor
        Player loser = null;
        for (Player player : players) {
            if (loser == null || player.getScore() < loser.getScore()) {
                loser = player;
            }
        }

        if (loser != null && loser.getScore() > 0) {
            JOptionPane.showMessageDialog(null,
                    "Animo, " + loser.getName() + " habran mas oportunidades luego! (" + loser.getScore()
                            + " puntos)"
                            + loser.getScore() + " puntos.",
                    "Perdedor", JOptionPane.INFORMATION_MESSAGE);
        }
        // JOptionPane.showMessageDialog(null, "¡El juego terminó!", "Fin del Juego",
        // JOptionPane.INFORMATION_MESSAGE);
    }

    private void getStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("Estadísticas del Juego:\n\n");

        for (Player player : players) {
            stats.append(player.getName())
                    .append(" - Puntaje: ")
                    .append(player.getScore())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(null, stats.toString(), "Estadísticas del Juego",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatMatrix(double[][] matrix) {
        StringBuilder builder = new StringBuilder();
        for (double[] row : matrix) {
            for (int i = 0; i < row.length - 1; i++) {
                builder.append(String.format("%6.0f", row[i])).append(" ");
            }
            builder.append("| ").append(String.format("%6.0f", row[row.length - 1])).append("\n");
        }
        return builder.toString();
    }

    private int getValidatedInput(String prompt, int min, int max) {
        int value = 0; // Valor predeterminado
        boolean valid = false;

        do {
            try {
                String input = JOptionPane.showInputDialog(null,
                        prompt + " (" + min + " a " + max + "):",
                        "Entrada requerida",
                        JOptionPane.QUESTION_MESSAGE);

                // Si el usuario cancela o no ingresa nada
                if (input == null || input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Debes ingresar un número.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // Parsear el número
                value = Integer.parseInt(input.trim());

                // Validar rango
                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Entrada fuera de rango. Inténtalo de nuevo.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Entrada inválida. Debe ser un número entero.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!valid);

        return value;
    }

    private void updatePoints(Player player, double newDeterminant) {
        // Calculamos la diferencia absoluta entre el determinante original y el nuevo
        double difference = Math.abs(originalDeterminant - newDeterminant);

        // Si el determinante es positivo, aumentamos el puntaje en la diferencia
        if (newDeterminant > 0) {
            player.setScore(player.getScore() + difference);
        } else {
            // Si el determinante es negativo, sumamos el valor del determinante
            player.setScore(player.getScore() + newDeterminant);
        }
    }

}
