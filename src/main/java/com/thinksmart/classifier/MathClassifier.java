package com.thinksmart.classifier;

public class MathClassifier {

    public enum ProblemType {
        LINEAR,
        QUADRATIC,
        CUBIC,
        SIMULTANEOUS,

        MATRIX_DETERMINANT,
        MATRIX_ADD_SUBTRACT,
        MATRIX_MULTIPLICATION,
        MATRIX_TRANSPOSE,
        MATRIX_INVERSE,
        MATRIX_ADJOINT,


        LIMIT,
        DIFFERENTIATION,
        INTEGRATION,

        GEOMETRY_CIRCLE,
        GEOMETRY_RECTANGLE,
        GEOMETRY_TRIANGLE,
        GEOMETRY_SQUARE,
        GEOMETRY_SPHERE,
        GEOMETRY_CONE,
        GEOMETRY_CYLINDER,

        UNKNOWN
    }

    public ProblemType classify(String input) {
        input = input.trim();

        String raw = input.trim();
        String normalized = raw.replaceAll("\\s+", "").toLowerCase();

        //limit
        input = input.toLowerCase().replaceAll("\\s+", "");

        if (normalized.startsWith("lim")) return ProblemType.LIMIT;

        // DIFFERENTIATION
        if (normalized.startsWith("d/dx")) return ProblemType.DIFFERENTIATION;

        // INTEGRATION
        if (normalized.startsWith("∫") || normalized.startsWith("int"))
            return ProblemType.INTEGRATION;

        // TRANSPOSE (VERY IMPORTANT - keep ABOVE matrix detection)
        if (normalized.contains("transpose") || normalized.endsWith("t")) {
            return ProblemType.MATRIX_TRANSPOSE;
        }

        // INVERSE
        if (normalized.contains("inverse") || normalized.contains("inv")) {
            return ProblemType.MATRIX_INVERSE;
        }

        //ADJOINT
        if (normalized.contains("adjoint") || normalized.contains("adj")) {
            return ProblemType.MATRIX_ADJOINT;
        }

        // DET shortcut
        if (normalized.startsWith("det")) {
            return ProblemType.MATRIX_DETERMINANT;
        }

        // MATRIX SMART DETECTION
        boolean hasMatrixPattern =
                raw.contains("[") || raw.contains("]")
                        || raw.contains(";")
                        || raw.contains("\n");

        // MATRIX ADDITION / SUBTRACTION
        if (hasMatrixPattern && (raw.contains("+") || raw.contains("-"))) {
            return ProblemType.MATRIX_ADD_SUBTRACT;
        }

        // MATRIX DETERMINANT (only numbers, multi-line)
        if (hasMatrixPattern && !raw.contains("+") && !raw.contains("-") && !raw.contains("*")) {
            return ProblemType.MATRIX_DETERMINANT;
        }

        // MATRIX MULTIPLICATION
        if (hasMatrixPattern && raw.contains("*")) {
            return ProblemType.MATRIX_MULTIPLICATION;
        }

        String[] lines = raw.split("\n");

        // SIMULTANEOUS EQUATIONS
        if (lines.length == 2 && normalized.contains("x") && normalized.contains("y")) {
            return ProblemType.SIMULTANEOUS;
        }

        input = input.replace(" ", "").toLowerCase();

        // GEOMETRY DETECTION
        if (normalized.contains("circle") || normalized.contains("radius")) {
            return ProblemType.GEOMETRY_CIRCLE;
        }

        if (normalized.contains("rectangle") || normalized.contains("length")) {
            return ProblemType.GEOMETRY_RECTANGLE;
        }

        if (normalized.contains("triangle") || normalized.contains("base")) {
            return ProblemType.GEOMETRY_TRIANGLE;
        }

        if (normalized.contains("square") || normalized.contains("side")) {
            return ProblemType.GEOMETRY_SQUARE;
        }

        if (normalized.contains("sphere") || normalized.contains("radius")) {
            return ProblemType.GEOMETRY_SPHERE;
        }

        if (normalized.contains("cone")) {
            return ProblemType.GEOMETRY_CONE;
        }

        if (normalized.contains("cylinder")) {
            return ProblemType.GEOMETRY_CYLINDER;
        }

        //EQUATIONS
        if (!normalized.contains("=")) return ProblemType.UNKNOWN;

        if (normalized.contains("x^3") || normalized.contains("x3"))
            return ProblemType.CUBIC;

        if (normalized.contains("x^2") || normalized.contains("x2"))
            return ProblemType.QUADRATIC;

        if (normalized.contains("x"))
            return ProblemType.LINEAR;

        return ProblemType.UNKNOWN;
    }

}
