package com.thinksmart.matrix;

public class MatrixParser {

    public static double[][] parse(String input) {

        // Step 1: Clean input
        input = input.trim()
                .replaceAll("\\[\\[", "")        // remove starting [[
                .replaceAll("]]", "")            // remove ending ]]
                .replaceAll("\\],\\[", "\n")     // split rows like ],[
                .replaceAll("\\[|\\]|\\(|\\)", "") // remove remaining brackets
                .replaceAll(",", " ")            // commas → space
                .replaceAll(";", "\n");          // semicolon → new row

        // Step 2: Split rows
        String[] rows = input.split("\\n");

        int rowCount = rows.length;
        int colCount = rows[0].trim().split("\\s+").length;

        double[][] matrix = new double[rowCount][colCount];

        for (int i = 0; i < rowCount; i++) {

            String[] values = rows[i].trim().split("\\s+");

            if (values.length != colCount) {
                throw new IllegalArgumentException(
                        "Invalid matrix format.\n" +
                                "Examples:\n" +
                                "1 2\n3 4\n\n" +
                                "[1 2; 3 4]\n" +
                                "1,2,3;4,5,6"
                );
            }

            for (int j = 0; j < colCount; j++) {
                matrix[i][j] = Double.parseDouble(values[j]);
            }
        }

        return matrix;
    }
}