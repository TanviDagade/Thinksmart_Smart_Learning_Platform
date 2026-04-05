package com.thinksmart.matrix;

import java.util.ArrayList;
import java.util.List;

public class MatrixInverseSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        input = input.toLowerCase()
                .replaceAll("inverse", "")
                .replaceAll("inv", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .trim();

        double[][] A = MatrixParser.parse(input);

        int n = A.length;

        if (n == 2) {
            return solve2x2(A, steps);
        } else if (n == 3) {
            return solve3x3(A, steps);
        } else {
            steps.add("Currently only 2×2 and 3×3 inverse supported.");
            return steps;
        }
    }

    private String matrixToString(double[][] m) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : m) {
            sb.append("| ");
            for (double v : row) {
                sb.append(String.format("%.2f", v)).append(" ");
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    private List<String> solve2x2(double[][] A, List<String> steps) {

        double a = A[0][0];
        double b = A[0][1];
        double c = A[1][0];
        double d = A[1][1];

        steps.add("Step 1: Given matrix");
        steps.add(matrixToString(A));
        steps.add("");

        double det = a * d - b * c;

        steps.add("Step 2: Determinant");
        steps.add("det = " + det);
        steps.add("");

        if (det == 0) {
            steps.add("Matrix not invertible.");
            return steps;
        }

        double[][] inv = {
                { d / det, -b / det },
                { -c / det, a / det }
        };

        steps.add("Step 3: Inverse Matrix");
        steps.add(matrixToString(inv));

        return steps;
    }

    private List<String> solve3x3(double[][] A, List<String> steps) {

        int n = 3;

        steps.add("Step 1: Given matrix");
        steps.add(matrixToString(A));
        steps.add("");

        // ✅ STEP 2: Determinant
        double det =
                A[0][0] * (A[1][1]*A[2][2] - A[1][2]*A[2][1])
                        - A[0][1] * (A[1][0]*A[2][2] - A[1][2]*A[2][0])
                        + A[0][2] * (A[1][0]*A[2][1] - A[1][1]*A[2][0]);

        steps.add("Step 2: Determinant of 3×3");
        steps.add("det = " + det);
        steps.add("");

        if (det == 0) {
            steps.add("Matrix not invertible (det = 0)");
            return steps;
        }

        // ✅ STEP 3: Cofactor matrix
        double[][] cof = new double[n][n];

        steps.add("Step 3: Cofactor matrix");
        steps.add("");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                double[][] sub = getSubMatrix(A, i, j);

                double minor = sub[0][0]*sub[1][1] - sub[0][1]*sub[1][0];

                double sign = ((i + j) % 2 == 0) ? 1 : -1;

                cof[i][j] = sign * minor;

                steps.add("C(" + (i+1) + "," + (j+1) + ") = " + (int)cof[i][j]);
            }
        }

        steps.add("");
        steps.add("Cofactor Matrix:");
        steps.add(matrixToString(cof));
        steps.add("");

        // ✅ STEP 4: Adjoint (transpose)
        double[][] adj = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adj[j][i] = cof[i][j];
            }
        }

        steps.add("Step 4: Adjoint matrix");
        steps.add(matrixToString(adj));
        steps.add("");

        // ✅ STEP 5: Inverse
        double[][] inv = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv[i][j] = adj[i][j] / det;
            }
        }

        steps.add("Step 5: Inverse matrix (1/det × adj)");
        steps.add(matrixToString(inv));

        return steps;
    }

    private double[][] getSubMatrix(double[][] A, int row, int col) {

        double[][] sub = new double[2][2];
        int r = 0;

        for (int i = 0; i < 3; i++) {
            if (i == row) continue;

            int c = 0;
            for (int j = 0; j < 3; j++) {
                if (j == col) continue;

                sub[r][c++] = A[i][j];
            }
            r++;
        }

        return sub;
    }
}