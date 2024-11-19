import java.util.Scanner;

public class Menu {
        private MathLib mathLib;
        private double[][] actualSystem;

        public Menu() {
                this.mathLib = new MathLib();
        }

        public void run() {
                Scanner in = new Scanner(System.in);
                boolean continuar = true;

                while (continuar) {
                        System.out.println("\n=== Menú Principal ===");
                        System.out.println("1. Generar un nuevo sistema");
                        System.out.println("2. Mostrar sistema actual");
                        System.out.println("3. Salir");
                        System.out.print("Seleccione una opción: ");
                        int opcion = in.nextInt();

                        switch (opcion) {
                                case 1:
                                        actualSystem = mathLib.generateAleatorySystem(3, 3, -10, 10);
                                        System.out.println("Nuevo sistema generado:");
                                        imprimirSistema();
                                        break;
                                case 2:
                                        if (actualSystem != null) {
                                                imprimirSistema();
                                        } else {
                                                System.out.println("No hay sistema generado aún.");
                                        }
                                        break;
                                case 3:
                                        continuar = false;
                                        System.out.println("¡Hasta luego!");
                                        break;
                                default:
                                        System.out.println("Opción no válida. Intente de nuevo.");
                        }
                }
                in.close();
        }

        private void imprimirSistema() {
                for (double[] fila : actualSystem) {
                        for (double valor : fila) {
                                System.out.printf("%6d", (int) valor);
                        }
                        System.out.println();
                }
        }
}
