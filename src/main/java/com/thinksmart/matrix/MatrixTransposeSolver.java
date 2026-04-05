package com.thinksmart.matrix;

import java.util.ArrayList;
import java.util.List;

public class MatrixTransposeSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        // ✅ Clean input
        input = input.toLowerCase()
                .replace("transpose", "")
                .replace("t", "")
                .trim();

        double[][] matrix = MatrixParser.parse(input);

        int rows = matrix.length;
        int cols = matrix[0].length;

        steps.add("Step 1: Given matrix");
        steps.add(matrixToString(matrix));
        steps.add("");

        double[][] transpose = new double[cols][rows];

        steps.add("Step 2: Swap rows and columns");
        steps.add("");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                transpose[j][i] = matrix[i][j];

                steps.add("Element (" + (i+1) + "," + (j+1) + ") → ("
                        + (j+1) + "," + (i+1) + "): "
                        + (int) matrix[i][j]);
            }
        }

        steps.add("");
        steps.add("Step 3: Transpose matrix");
        steps.add(matrixToString(transpose));

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