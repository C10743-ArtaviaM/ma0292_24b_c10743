/**
 * La clase {@code App} es el punto de entrada principal del programa.
 * Esta clase es la encargada de iniciar el proceso de carga antes de que el
 * menú principal del juego sea mostrado.
 * 
 * 
 */
public class App {
    /**
     * El método principal de la aplicación que arranca el juego.
     * Este método invoca el método {@link Loader#load()} para mostrar la pantalla
     * de carga antes de iniciar el juego.
     *
     * @param args Los argumentos de la línea de comandos (no utilizados en este
     *             caso).
     * @throws Exception Si ocurre algún error durante la carga o la ejecución.
     */
    public static void main(String[] args) throws Exception {
        Loader.load(); // Pantalla de carga
    }
}
