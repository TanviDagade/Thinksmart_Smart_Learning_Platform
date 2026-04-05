package com.thinksmart.matrix;

import java.util.ArrayList;
import java.util.List;

public class MatrixAddSubtractSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        // Detect operator
        String operator;
        if (input.contains("+")) {
            operator = "+";
        } else if (input.contains("-")) {
            operator = "-";
        } else {
            steps.add("Error: No operator (+ or -) found.");
            return steps;
        }

        // Split matrices
        double[][] A;
        double[][] B;

        // CASE 1: A: B: format
        if (input.contains("A:") && input.contains("B:")) {

            String[] splitByB = input.split("B:");
            String beforeB = splitByB[0];

            String matrixAInput = beforeB
                    .replace("A:", "")
                    .replace("+", "")
                    .replace("-", "")
                    .trim();

            String matrixBInput = splitByB[1].trim();

            A = MatrixParser.parse(matrixAInput);
            B = MatrixParser.parse(matrixBInput);

        }

        // CASE 2: Normal format
        else {

            String[] parts = input.split("[+-]");

            if (parts.length != 2) {
                steps.add("Error: Invalid matrix format.");
                return steps;
            }

            A = MatrixParser.parse(parts[0].trim());
            B = MatrixParser.parse(parts[1].trim());
        }

        boolean isAddition = operator.equals("+");

        int n = A.length;

        if (B.length != n || B[0].length != n) {
            steps.add("Error:");
            steps.add("Both matrices must be of the same size (n × n).");
            return steps;
        }

        steps.add("Step 1: Given matrices A and B");
        steps.add("Matrix A:");
        steps.add(matrixToString(A));
        steps.add("");
        steps.add("Matrix B:");
        steps.add(matrixToString(B));
        steps.add("");

        steps.add("Step 2: Perform matrix " +
                (isAddition ? "addition" : "subtraction") +
                " (A " + operator + " B)");
        steps.add("");

        double[][] result = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (isAddition) {
                    result[i][j] = A[i][j] + B[i][j];
                } else {
                    result[i][j] = A[i][j] - B[i][j];
                }

                steps.add("Element (" + (i + 1) + "," + (j + 1) + "): "
                        + (int) A[i][j] + " " + operator + " "
                        + (int) B[i][j] + " = "
                        + (int) result[i][j]);
            }
            steps.add("");
        }

        steps.add("Step 3: Final Result");
        steps.add(matrixToString(result));

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
