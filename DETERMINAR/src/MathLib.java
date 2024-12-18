import java.util.Random;

/**
 * Clase que proporciona funciones matemáticas para el manejo sistemas de
 * ecuaciones, matrices y cálculo de determinantes.
 * 
 * @author Mauricio Artavia Monge C10743 - Kenneth Delgado Cárdenas C22540
 */
public class MathLib {

    /* =-=-= MANEJO DE MATRICES =-=-= */

    /* =-=-= CALCULO DE DETERMINANTE =-=-= */
    /**
     * Calcula el determinante de una matriz cuadrada.
     *
     * @param m La matriz cuadrada de la cual se calculará el determinante.
     * @return El determinante de la matriz.
     */
    public double determinante(double[][] m) {
        /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
        double data[] = new double[2];
        double detM = 0;

        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        int n = m.length;

        if (n == 1) { // Si Matriz es de Dimension 1x1
            /* =-=-= NO NECESARIA =-=-= */
            detM = m[0][0];
            /* =-=-= =-=-=--=-=-= =-=-= */
        } else if (n == 2) { // Si Matriz es de Dimension 2x2
            /* =-=-= NO NECESARIA =-=-= */
            detM = determinante2x2(m);
            /* =-=-= =-=-=--=-=-= =-=-= */
        } else { // Si Matriz es de Dimension nxn donde n >= 3
            data = triangularidad(m);
            if (data[1] == 1) { // Si es triangular
                detM = data[0];
            } else if (data[1] == -1) { // Si no es triangular
                detM = cofactores(m, n);
            }
        }
        return detM;
    }

    /* =-=-= CALCULO DETERMINANTE MATRIZ TRIANGULAR =-=-= */
    /**
     * Calcula el determinante de una matriz triangular.
     *
     * @param m La matriz triangular de la cual se calculará el determinante.
     * @return Un arreglo donde [0] es el determinante y [1] indica si es triangular
     *         (1) o no (-1).
     */
    public double[] triangularidad(double[][] m) {
        /* =-=-= Declaracion de Variables de Tipo Boolean =-=-= */
        boolean triangularidadSuperior = true;
        boolean triangularidadInferior = true;

        /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
        double data[] = new double[2];

        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        int detM = 1;

        for (int row = 0; row < m.length; row++) {
            for (int col = 0; col < m.length; col++) {
                if (m[row][col] != 0) {
                    if (row > col) {
                        triangularidadSuperior = false;
                    } else if (row < col) {
                        triangularidadInferior = false;
                    }
                }
            }
        }

        if (triangularidadInferior || triangularidadSuperior) {
            for (int i = 0; i < m.length; i++) {
                detM *= m[i][i];
            }

            data[0] = detM;
            data[1] = 1;
        } else {
            data[0] = detM;
            data[1] = -1;
        }
        return data;
    }

    /**
     * Calcula el determinante de una matriz 2x2.
     *
     * @param m La matriz 2x2 de la cual se calculará el determinante.
     * @return El determinante de la matriz 2x2.
     */
    public double determinante2x2(double[][] m) {
        return (m[0][0] * m[1][1]) - (m[0][1] * m[1][0]);
    }

    /* =-=-= MANEJO DETERMINANTE POR COFACTORES =-=-= */
    /**
     * Calcula el determinante de una matriz por cofactores.
     *
     * @param m La matriz de la cual se calculará el determinante por cofactores.
     * @param n El tamaño de la matriz.
     * @return El determinante de la matriz calculado por cofactores.
     */
    public double cofactores(double[][] m, int n) {
        /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
        double detM = 0;

        for (int i = 0; i < n; i++) {
            detM += cofactor(m, 0, i, n);
        }
        return detM;
    }

    /**
     * Calcula el cofactor de un elemento específico en una matriz.
     *
     * @param m   La matriz de la cual se calculará el cofactor.
     * @param row La fila del elemento para el cual se calculará el cofactor.
     * @param col La columna del elemento para el cual se calculará el cofactor.
     * @param n   El tamaño de la matriz.
     * @return El cofactor del elemento específico en la matriz.
     */
    public double cofactor(double[][] m, int row, int col, int n) {
        /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
        double detMenor = determinante(matrizMenor(m, row, col, n));
        double numeral = m[row][col];
        double signo = Math.pow(-1, row + col);

        double detM = signo * numeral * detMenor;
        return detM;
    }

    /**
     * Calcula la matriz menor obtenida al eliminar una fila y una columna
     * específica de la matriz original.
     *
     * @param m   La matriz de la cual se calculará el menor.
     * @param row La fila que se eliminará.
     * @param col La columna que se eliminará.
     * @param n   El tamaño de la matriz original.
     * @return La matriz menor obtenida.
     */
    public double[][] matrizMenor(double[][] m, int row, int col, int n) {
        /* =-=-= Declaracion de Variables de Tipo Double =-=-= */
        double[][] mMenor = new double[n - 1][n - 1];

        /* =-=-= Declaracion de Variables de Tipo Integer =-=-= */
        int rowMenor = 0, colMenor;

        for (int i = 0; i < n; i++) {
            if (i != row) {
                colMenor = 0;
                for (int j = 0; j < n; j++) {
                    if (j != col) {
                        mMenor[rowMenor][colMenor] = m[i][j];
                        colMenor++;
                    }
                }
                rowMenor++;
            }
        }
        return mMenor;
    }

    public boolean haveSolution(Matrix matrix) {
        double[][] data = matrix.getMatrix();
        return determinante(data) != 0;
    }

    /* =-=-= FIN MANEJO DE MATRICES =-=-= */

    /* =-=-= MANEJO DE SISTEMAS DE ECUACIONES =-=-= */

    /* =-=-= GENERACION SISTEMA ECUACIONES =-=-= */
    // Generacion de sistema de ecuaciones con solucion unica.
    /**
     * Genera un sistema de ecuaciones aleatorio con una solución única.
     * El sistema es una matriz de números aleatorios dentro de un rango dado.
     * Se asegura que el determinante de la matriz no sea cero, lo que garantiza que
     * el sistema tenga una solución única. Si el determinante es cero, genera un
     * nuevo sistema hasta que se cumpla esta condición.
     *
     * @param rows    El número de filas de la matriz (también el número de
     *                ecuaciones).
     * @param columns El número de columnas de la matriz (también el número de
     *                variables más un término independiente).
     * @param min     El valor mínimo que pueden tener los elementos de la matriz.
     * @param max     El valor máximo que pueden tener los elementos de la matriz.
     * @return Una matriz {@code double} que representa el sistema de ecuaciones
     *         aleatorio.
     */
    public double[][] generateAleatorySystem(int rows, int columns, int min, int max) {
        Random random = new Random();
        double[][] matrix;

        // Genera un sistema de ecuaciones
        do {
            matrix = new double[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    matrix[i][j] = random.nextInt(max - min + 1) + min; // Valores entre [min, max]
                }
            }
        } while (determinante(matrix) == 0); // Reintentar si no tiene solucion unica.
        return matrix;
    }

    /* =-=-= FIN MANEJO DE SISTEMA DE ECUACIONES =-=-= */

}
