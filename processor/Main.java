package processor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static void printMatrix(double[][] matrix) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        for (double[] row : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf("%-10s\t", df.format(row[j]));
            }
            System.out.println();
        }
    }

    private static double[][] readMatrix(Scanner scanner, String s) {
        System.out.println("Enter size of " + s + " matrix: ");
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        System.out.println("Enter " + s + " matrix");
        double[][] matrix = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = scanner.nextDouble();
            }
        }
        return matrix;
    }

    private static double[][] takeMinor(double[][] matrix, int i, int j) {
        int n = matrix.length;
        double[][] res = new double[n - 1][n - 1];
        int resI = 0;
        int resJ;
        for (int sourceI = 0; sourceI < n; sourceI++) {
            if (sourceI == i) continue;
            resJ = 0;
            for (int sourceJ = 0; sourceJ < n; sourceJ++) {
                if (sourceJ == j) continue;
                res[resI][resJ] = matrix[sourceI][sourceJ];
                resJ++;
            }
            resI++;

        }
        return res;
    }

    private static double[][] addMatrices(double[][] a, double[][] b) {
        if (a.length == 0 && b.length == 0) return a;
        if (a.length != b.length || a[0].length != b[0].length) {
            throw new IllegalArgumentException();
            /*System.out.println("Illegal argument");
            return null;*/
        }
        int n = a.length;
        int m = a[0].length;
        double[][] res = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                res[i][j] = a[i][j] + b[i][j];
            }
        }
        return res;
    }

    private static double[][] multiplyMatrixToConst(double[][] matrix, double c) {
        if (matrix.length == 0) return matrix;
        int n = matrix.length;
        int m = matrix[0].length;
        double[][] res = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                res[i][j] = matrix[i][j] * c;
            }
        }
        return res;
    }

    private static double[][] multiplyMatrices(double[][] a, double[][] b) {
        int n1 = a.length;
        int n2 = b.length;
        if (n1 == 0 || n2 == 0) {
            throw new IllegalArgumentException();
        }
        int m1 = a[0].length;
        int m2 = b[0].length;
        double[][] res = new double[n1][m2];
        if (m1 != n2) {
            throw new IllegalArgumentException();
           /* System.out.println("ERROR");
            return null;*/
        }
        //  System.out.println(m1+ " "+ m2);
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < m2; j++) {
                for (int k = 0; k < m1; k++) {
                    res[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return res;
    }

    private static double[][] transpositionMatrix(double[][] matrix, int type) {
        if (matrix.length == 0) return matrix;
        int n = matrix.length;
        double[][] res = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                switch (type) {
                    case 1: //main
                        res[i][j] = matrix[j][i];
                        break;
                    case 2://side
                        res[i][j] = matrix[n - (j + 1)][n - (i + 1)];
                        break;
                    case 3://vertical
                        res[i][j] = matrix[i][n - (j + 1)];
                        break;
                    case 4://horizontal
                        res[i][j] = matrix[n - (i + 1)][j];
                        break;
                }
            }
        }
        return res;
    }

    private static double findDeterminant(double[][] matrix) {
        int n = matrix.length;
        if (matrix[0].length != n) throw new IllegalArgumentException();
        if (n == 1) {
            return matrix[0][0];
        }
        double res = 0;
        for (int i = 0; i < n; i++) {
            res += matrix[0][i] * findDeterminant(takeMinor(matrix, 0, i)) * Math.pow(-1, i);
        }
        return res;
    }

    private static double[][] inverseMatrix(double[][] matrix){
        int n = matrix.length;
        if(n!=matrix[0].length) throw new IllegalArgumentException();
        double determinant = findDeterminant(matrix);
        if(determinant == 0){
            return null;
        }
        double coeff = 1/determinant;
        double[][] cofactors = new double[n][n];
        for (int i = 0; i < n ; i++) {
            for (int j = 0; j < n; j++) {
                cofactors[i][j] = Math.pow(-1, i+j)*findDeterminant(takeMinor(matrix, i, j));
            }
        }
        return multiplyMatrixToConst(transpositionMatrix(cofactors, 1), coeff);
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        Scanner scanner = new Scanner(System.in);
        //scanner.useLocale(Locale.ENGLISH);
        int command;
        do {
            System.out.println("1. Add matrices\n" +
                    "2. Multiply matrix to a constant\n" +
                    "3. Multiply matrices\n" +
                    "4. Transpose matrix\n" +
                    "5. Calculate a determinant\n" +
                    "6. Inverse matrix\n" +
                    "0. Exit");
            command = scanner.nextInt();
            double[][] matrix;
            switch (command) {
                case 1:
                    System.out.println("Your choice: 1\n");
                    double[][] first = readMatrix(scanner, "first");
                    double[][] second = readMatrix(scanner, "second");
                    printMatrix(addMatrices(first, second));
                    break;
                case 2:
                    System.out.println("Your choice: 2\n");
                    matrix = readMatrix(scanner, "");
                    System.out.println("Enter a constant");
                    int c = scanner.nextInt();
                    printMatrix(multiplyMatrixToConst(matrix, c));
                    break;
                case 3:
                    System.out.println("Your choice: 3\n");
                    printMatrix(multiplyMatrices(readMatrix(scanner, "first"), readMatrix(scanner, "second")));
                    break;
                case 4:
                    System.out.println("1. Main diagonal\n" +
                            "2. Side diagonal\n" +
                            "3. Vertical line\n" +
                            "4. Horizontal line");
                    int addCommand = scanner.nextInt();
                    System.out.println("Your choice: " + addCommand);
                    if (addCommand < 1 || addCommand > 4) {
                        System.out.println("Unknown command\n");
                        break;
                    }
                    printMatrix(transpositionMatrix(readMatrix(scanner, "square"), addCommand));
                    break;
                case 5:
                    System.out.println("Your choice: 5\n");
                    System.out.println(findDeterminant(readMatrix(scanner, "square")));
                    break;
                case 6:
                    System.out.println("Your choice: 6\n");
                    matrix = inverseMatrix(readMatrix(scanner, "square"));
                    if(matrix == null){
                        System.out.println("The matrix has determinant is equal to 0 and can't be inverse");
                    }
                    else printMatrix(matrix);
                    break;
                case 0:
                    System.out.println("Your choice: 0\n");
                    break;
                default:
                    System.out.println("Unknown command\n");
            }
        } while (command != 0);


        //System.out.println("test");
    }
}
