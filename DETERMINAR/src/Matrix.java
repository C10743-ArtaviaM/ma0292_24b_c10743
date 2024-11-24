import java.util.Random;

/**
 * La clase {@code Matrix} representa una matriz bidimensional que contiene
 * valores numéricos de tipo {@code double}. Proporciona métodos para generar
 * una matriz de valores aleatorios dentro de un rango dado, imprimir la matriz
 * en consola y obtener los datos de la matriz.
 * 
 * <p>
 * Esta clase es útil para trabajar con matrices en operaciones matemáticas,
 * especialmente en el contexto de sistemas de ecuaciones lineales.
 * </p>
 *
 * @author Mauricio Artavia Monge C10743 - Kenneth Delgado Cárdenas C22540
 */
public class Matrix {
    private double[][] data; // Almacenamiento de los datos de la matriz.
    private int rows; // Numero de filas de la matriz.
    private int columns; // Numero de columnas de la matriz.

    /* =-=-= Metodo Constructor de la clase Matrix =-=-= */
    /**
     * Constructor de la clase {@code Matrix}.
     * 
     * Crea una matriz de tamaño {@code rows} x {@code columns}, donde cada elemento
     * de la matriz es inicializado con su valor predeterminado (0.0).
     *
     * @param rows    El número de filas de la matriz.
     * @param columns El número de columnas de la matriz.
     */
    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];
    }

    // Metodo generacion aleatoria de matriz.
    /**
     * Genera una matriz de valores aleatorios dentro de un rango específico. Los
     * valores de la matriz son enteros generados aleatoriamente entre los valores
     * {@code min} y {@code max}.
     *
     * @param min El valor mínimo para los elementos de la matriz.
     * @param max El valor máximo para los elementos de la matriz.
     */
    public void generateMatrix(int min, int max) {
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = random.nextInt(max - min + 1) + min; // Generacion de valores aleatorios
            }
        }
    }

    // Metodo de impresion de la matriz.
    /**
     * Imprime la matriz en la consola con un formato de 2 decimales por celda. Cada
     * fila de la matriz se imprime en una nueva línea.
     */
    public void print() {
        System.out.println("MATRIX");
        for (double[] row : data) {
            for (double value : row) {
                System.out.printf("6.2f", value); // Formatea a 2 decimales.
            }
            System.out.println(); // Salto de linea despues de cada fila.
        }
    }

    /**
     * Obtiene los datos de la matriz como una matriz bidimensional de tipo
     * {@code double}.
     *
     * @return Una matriz bidimensional de tipo {@code double} con los datos de la
     *         matriz.
     */
    public double[][] getMatrix() {
        return data;
    }
}
