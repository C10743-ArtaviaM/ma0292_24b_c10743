/**
 * Representa a un jugador dentro del juego.
 * 
 * La clase Player contiene la información sobre un jugador, como su nombre, su
 * puntaje, su determinante y su capacidad para jugar en la partida actual.
 * También incluye métodos para acceder y modificar estas propiedades,
 * permitiendo un control sobre su estado durante el juego.
 * 
 * @author Mauricio Artavia Monge C10743 - Kenneth Delgado Cárdenas C22540
 */
public class Player {
    public static final String GREEN = "\033[32m"; // Verde
    public static final String RESET = "\033[0m"; // Resetear color
    /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
    private boolean canPlay; // Estado que indica si el jugador puede jugar en su turno.

    /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
    private double determinant; // El determinante generado por el jugador durante su turno.
    private double score; // El puntaje acumulado del jugador.

    /* =-=-= Declaracion de Variables de Tipo String =-=-= */
    private String name; // Nombre del jugador.

    /* =-=-= METODO CONSTRUCTOR DE LA CLASE PLAYER */
    /**
     * Constructor que crea un jugador con un nombre y un estado de juego.
     * 
     * @param name    El nombre del jugador.
     * @param canPlay El estado inicial que indica si el jugador puede jugar.
     */
    public Player(String name, boolean canPlay) {
        this.name = name;
        this.score = 0;
        this.canPlay = true;
        this.determinant = 0;
    }

    /* =-=-= MODIFICADORES DE CLASE =-=-= */
    /**
     * Obtiene el determinante del jugador.
     * 
     * @return El determinante actual del jugador.
     */
    public double getDeterminant() {
        return this.determinant;
    }

    /**
     * Establece el determinante del jugador.
     * 
     * @param determinant El nuevo determinante del jugador.
     */
    public void setDeterminant(double determinant) {
        this.determinant = determinant;
    }

    /**
     * Obtiene el nombre del jugador.
     * 
     * @return El nombre del jugador.
     */
    public String getName() {
        return (this.name);
    }

    /**
     * Obtiene el puntaje acumulado del jugador.
     * 
     * @return El puntaje actual del jugador.
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Establece el puntaje del jugador.
     * 
     * @param score El nuevo puntaje del jugador.
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Obtiene el estado de juego del jugador (si puede jugar en su turno).
     * 
     * @return El estado de juego del jugador. `true` si puede jugar, `false` si no.
     */
    public boolean getPlayStatus() {
        return canPlay;
    }

    /**
     * Establece el estado de juego del jugador.
     * 
     * @param playStatus El nuevo estado de juego del jugador.
     */
    public void setPlayStatus(boolean playStatus) {
        this.canPlay = playStatus;
    }
}
