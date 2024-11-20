import java.util.Random;

public class Matrix {
    private double[][] data;
    private int rows;
    private int columns;

    // Constructor
    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];
    }

    // Metodo generacion aleatoria de matriz.
    public void generateMatrix(int min, int max) {
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = random.nextInt(max - min + 1) + min;
            }
        }
    }

    // Metodo de impresion de la matriz.
    public void print() {
        System.out.println("MATRIX");
        for (double[] row : data) {
            for (double value : row) {
                System.out.printf("6.2f", value);
            }
            System.out.println();
        }
    }

    public double[][] getMatrix() {
        return data;
    }
}
