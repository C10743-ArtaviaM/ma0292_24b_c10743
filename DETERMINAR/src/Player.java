public class Player {
    /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
    private boolean canPlay;

    /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
    private double determinant;
    private double score;

    /* =-=-= Declaracion de Variables de Tipo String =-=-= */
    private String name;

    /* =-=-= METODO CONSTRUCTOR DE LA CLASE PLAYER */
    public Player (String name, boolean canPlay) {
        this.name = name;
        this.score = 0;
        this.canPlay = true;
        this.determinant = 0;
    }

    /* =-=-= MODIFICADORES DE CLASE =-=-= */
    public double getDeterminant() { return this.determinant; }

    public void setDeterminant(double determinant) { this.determinant = determinant; }

    public String getName() { return this.name; }

    public double getScore() { return this.score; }

    public void setScore(double score) { this.score = score; }

    public boolean getPlayStatus() { return canPlay; }

    public void setPlayStatus(boolean playStatus) { this.canPlay = playStatus; }
}
