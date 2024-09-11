import Libs.MathLib;

public class Menu {
        public void run() {
                System.out.println("Estamos entrando al Menu");

                System.out.println("Procedemos a probar los determinantes");

                double matrizNormal[][] = { { 1, 2, 3 },
                                { 4, 5, 6 },
                                { 7, 8, 9 } };

                double matrizTInferior[][] = { { 1, 0, 0 },
                                { 4, 5, 0 },
                                { 7, 8, 9 } };

                double matrizTSuperior[][] = { { 1, 2, 3 },
                                { 0, 5, 6 },
                                { 0, 0, 9 } };

                double matrizN2[][] = { { 1, 2 },
                                { 3, 4 } };

                double matrizN2TInferior[][] = { { 1, 0 },
                                { 3, 4 } };

                double matrizN2TSuperior[][] = { { 1, 2 },
                                { 0, 4 } };

                double matrizN1[][] = { { 1 } };

                MathLib mLib = new MathLib();

                System.out.println("Determinante del 3x3 es: " + mLib.determinante(matrizNormal));
                System.out.println(
                                "Determinante del 3x3 Triangular Inferior es: " + mLib.determinante(matrizTInferior));
                System.out.println(
                                "Determinante del 3x3 Triangular Superior es: " + mLib.determinante(matrizTSuperior));
                System.out.println("Determinante del 2x2 es: " + mLib.determinante(matrizN2));
                System.out.println(
                                "Determinante del 2x2 Triangular Inferior es: " + mLib.determinante(matrizN2TInferior));
                System.out.println(
                                "Determinante del 2x2 Triangular Superior es: " + mLib.determinante(matrizN2TSuperior));
                System.out.println("Determinante del 1x1 es: " + mLib.determinante(matrizN1));

                System.exit(0);
        }
}
