package com.thinksmart.matrix;

import java.util.ArrayList;
import java.util.List;

public class MatrixAdjointSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        // ✅ CLEAN INPUT
        input = input.toLowerCase()
                .replaceAll("adjoint", "")
                .replaceAll("adj", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .trim();

        double[][] A = MatrixParser.parse(input);
        int n = A.length;

        steps.add("Step 1: Given matrix");
        steps.add(matrixToString(A));
        steps.add("");

        if (n == 2) {
            return solve2x2(A, steps);
        } else if (n == 3) {
            return solve3x3(A, steps);
        } else {
            steps.add("Currently only 2×2 and 3×3 supported.");
            return steps;
        }
    }

    // ================== 2x2 ==================
    private List<String> solve2x2(double[][] A, List<String> steps) {

        double a = A[0][0];
        double b = A[0][1];
        double c = A[1][0];
        double d = A[1][1];

        steps.add("Step 2: Cofactor matrix");
        steps.add("| " + (int)d + " " + (int)(-b) + " |");
        steps.add("| " + (int)(-c) + " " + (int)a + " |");
        steps.add("");

        double[][] adj = {
                { d, -b },
                { -c, a }
        };

        steps.add("Step 3: Adjoint matrix (transpose of cofactor)");
        steps.add(matrixToString(adj));

        return steps;
    }

    // ================== 3x3 ==================
    private List<String> solve3x3(double[][] A, List<String> steps) {

        int n = 3;
        double[][] cof = new double[n][n];

        steps.add("Step 2: Compute cofactors");
        steps.add("");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                double[][] sub = getSubMatrix(A, i, j);

                double minor = sub[0][0]*sub[1][1] - sub[0][1]*sub[1][0];

                double sign = ((i + j) % 2 == 0) ? 1 : -1;

                cof[i][j] = sign * minor;

                steps.add("C(" + (i+1) + "," + (j+1) + ") = "
                        + (int)sign + " × " + (int)minor
                        + " = " + (int)cof[i][j]);
            }
        }

        steps.add("");
        steps.add("Cofactor Matrix:");
        steps.add(matrixToString(cof));
        steps.add("");

        // transpose
        double[][] adj = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adj[j][i] = cof[i][j];
            }
        }

        steps.add("Step 3: Adjoint (transpose of cofactor)");
        steps.add(matrixToString(adj));

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