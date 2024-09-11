import java.awt.*;
import javax.swing.*;

public class Loader {
    /**
     * Método que simula una pantalla de carga antes de iniciar el menú principal.
     */
    public static void load() {
        /* =-=-= Declaracion de Variables de Tipo Clase =-=-= */
        Menu menu = new Menu();

        /* =-=-= Declaracion de Variables de Tipo Clase JFrame =-=-= */
        JFrame loadingFrame;

        /* =-=-= Declaracion de Variables de Tipo Clase JProgressBar =-=-= */
        JProgressBar progressBar;

        loadingFrame = new JFrame("DETERMINANTE | LOADING . . .");

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

        // Simulacion de una carga de 5 segundos antes de continuar.
        try {
            Thread.sleep(5000); // Espera 5 segundos (5000 milisegundos).
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Oculta el JFrame de carga después de los 5 segundos.
        loadingFrame.setVisible(false);
        loadingFrame.dispose(); // Liberacion de recursos del JFrame de carga.

        // Pasa el JFrame cargado como argumento al método run de Menu.
        menu.run();
    }
}
