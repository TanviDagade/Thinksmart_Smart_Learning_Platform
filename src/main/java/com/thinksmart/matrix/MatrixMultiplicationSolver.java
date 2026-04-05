package com.thinksmart.matrix;

import java.util.ArrayList;
import java.util.List;

public class MatrixMultiplicationSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();
        int step = 1;

        // Split matrices using *
        double[][] A;
        double[][] B;

        // CASE 1: A: B: format
        if (input.contains("A:") && input.contains("B:")) {

            String[] splitByB = input.split("B:");
            String beforeB = splitByB[0];

            String matrixAInput = beforeB
                    .replace("A:", "")
                    .replace("*", "")
                    .trim();

            String matrixBInput = splitByB[1].trim();

            A = MatrixParser.parse(matrixAInput);
            B = MatrixParser.parse(matrixBInput);

        }

        // CASE 2: Normal format
        else {

            String[] parts = input.split("\\*");

            if (parts.length != 2) {
                steps.add("Error: Invalid matrix format.");
                return steps;
            }

            A = MatrixParser.parse(parts[0].trim());
            B = MatrixParser.parse(parts[1].trim());
        }

        int n = A.length;

        int rowsA = A.length;
        int colsA = A[0].length;
        int rowsB = B.length;
        int colsB = B[0].length;

        if (colsA != rowsB) {
            steps.add("Error:");
            steps.add("Matrix multiplication not possible.");
            steps.add("Number of columns of Matrix A must equal number of rows of Matrix B.");
            steps.add("");
            steps.add("Matrix A: " + rowsA + " × " + colsA);
            steps.add("Matrix B: " + rowsB + " × " + colsB);
            return steps;
        }

        steps.add("Step " + step++ + ": Given matrices A and B");
        steps.add("Matrix A:");
        steps.add(matrixToString(A));
        steps.add("");
        steps.add("Matrix B:");
        steps.add(matrixToString(B));
        steps.add("");

        double[][] C = new double[rowsA][colsB];

        steps.add("Step " + step++ + ": Multiply rows of A with columns of B");
        steps.add("");

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {

                steps.add("Computing element (" + (i + 1) + "," + (j + 1) + "):");

                double sum = 0;
                StringBuilder calc = new StringBuilder("= ");

                for (int k = 0; k < colsA; k++) {
                    sum += A[i][k] * B[k][j];
                    calc.append((int) A[i][k])
                            .append("×")
                            .append((int) B[k][j]);
                    if (k < colsA - 1) calc.append(" + ");
                }

                C[i][j] = sum;
                calc.append(" = ").append((int) sum);

                steps.add(calc.toString());
                steps.add("");
            }
        }

        steps.add("Step " + step++ + ": Final Result Matrix ("
                + rowsA + " × " + colsB + ")");
        steps.add(matrixToString(C));

        return steps;
    }

    private String matrixToString(double[][] m) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : m) {
            sb.append("| ");
            for (double v : row) {
                sb.append((int) v).append(" ");
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}
