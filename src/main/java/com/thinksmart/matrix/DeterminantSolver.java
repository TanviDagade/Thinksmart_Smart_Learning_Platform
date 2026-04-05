package com.thinksmart.matrix;

import java.util.ArrayList;
import java.util.List;

public class DeterminantSolver {
    private int stepCount = 1;

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        // CLEAN det(...) wrapper
        input = input.toLowerCase()
                .replace("det", "")
                .replace("(", "")
                .replace(")", "")
                .trim();

        double[][] matrix = MatrixParser.parse(input);
        int n = matrix.length;

        steps.add("Step " + stepCount++ + ": Given matrix (" + n + " × " + n + ")");
        steps.add(matrixToString(matrix));
        steps.add("");

        steps.add("Step " + stepCount++ + ": Apply Laplace expansion along the first row");
        steps.add("");

        double det = determinant(matrix, steps, 0);

        steps.add("Step " + stepCount++ + ": Final determinant value");
        steps.add("det = " + det);
        steps.add("");

        return steps;
    }

    private double determinant(double[][] matrix, List<String> steps, int level) {

        int n = matrix.length;

        if (n == 1) {
            return matrix[0][0];
        }

        if (n == 2) {
            double val = matrix[0][0] * matrix[1][1]
                    - matrix[0][1] * matrix[1][0];

            steps.add("Step " + stepCount++ + ": Compute 2×2 determinant");
            steps.add(indent(level) +
                    "= (" + (int) matrix[0][0] + " × " + (int) matrix[1][1] +
                    " − " + (int) matrix[0][1] + " × " + (int) matrix[1][0] +
                    ") = " + val);
            steps.add("");

            return val;
        }

        double det = 0;

        for (int col = 0; col < n; col++) {

            double[][] sub = subMatrix(matrix, col);
            double sign = (col % 2 == 0) ? 1 : -1;

            steps.add(
                    indent(level) +
                            "Step " + stepCount++ + ": " +
                            ((sign > 0) ? "+ " : "- ") +
                            (int) matrix[0][col] +
                            " × determinant of sub-matrix"
            );
            steps.add("");

            det += sign * matrix[0][col]
                    * determinant(sub, steps, level + 1);
        }

        return det;
    }

    private double[][] subMatrix(double[][] matrix, int excludeCol) {

        int n = matrix.length;
        double[][] sub = new double[n - 1][n - 1];

        int r = 0;
        for (int i = 1; i < n; i++) {
            int c = 0;
            for (int j = 0; j < n; j++) {
                if (j == excludeCol) continue;
                sub[r][c++] = matrix[i][j];
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

    private String indent(int level) {
        return "  ".repeat(level);
    }

}
