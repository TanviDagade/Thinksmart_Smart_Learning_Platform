package com.thinksmart.solver;

import java.util.ArrayList;
import java.util.List;

public class CubicEquationSolver {

    private static final double EPS = 1e-10;

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();
        int step = 1;

        try {

            input = normalizeInput(input);
            input = input.replace("=0", "");

            steps.add("Step " + step++ + ": Given equation");
            steps.add(input + " = 0");
            steps.add("");

            double a = getCoeff(input, "x^3");
            double b = getCoeff(input, "x^2");
            double c = getCoeff(input, "x");
            double d = getConstant(input);

            steps.add("Step " + step++ + ": Identify coefficients");
            steps.add("a = " + a + ", b = " + b + ", c = " + c + ", d = " + d);
            steps.add("");

            if (Math.abs(a) < EPS) {
                steps.add("Not a cubic equation.");
                return steps;
            }

            steps.add("Step " + step++ + ": Write equation in standard form");
            steps.add("ax³ + bx² + cx + d = 0");
            steps.add("");

            steps.add("Step " + step++ + ": Remove quadratic term using substitution");
            steps.add("Let x = y - (b / 3a)");
            double shift = -b / (3*a);
            steps.add("x = y + (" + clean(shift) + ")");
            steps.add("");

            double p = (3*a*c - b*b) / (3*a*a);
            double q = (2*b*b*b - 9*a*b*c + 27*a*a*d) / (27*a*a*a);

            steps.add("Step " + step++ + ": Form depressed cubic");
            steps.add("y³ + py + q = 0");
            steps.add("p = (3ac - b²) / 3a² = " + clean(p));
            steps.add("q = (2b³ - 9abc + 27a²d) / 27a³ = " + clean(q));
            steps.add("");

            double discriminant = (q*q)/4 + (p*p*p)/27;

            steps.add("Step " + step++ + ": Calculate Discriminant");
            steps.add("Δ = (q²/4) + (p³/27)");
            steps.add("Δ = " + clean(discriminant));
            steps.add("");

            if (discriminant < -EPS) {

                steps.add("Since Δ < 0, equation has three distinct real roots.");
                steps.add("");

                double r = Math.sqrt(-p*p*p/27);
                double phi = Math.acos(-q/(2*Math.sqrt(-p*p*p/27)));

                steps.add("Using trigonometric solution:");
                steps.add("yₖ = 2√(-p/3) cos((φ + 2kπ)/3)");
                steps.add("");

                double y1 = 2*Math.sqrt(-p/3)*Math.cos(phi/3);
                double y2 = 2*Math.sqrt(-p/3)*Math.cos((phi+2*Math.PI)/3);
                double y3 = 2*Math.sqrt(-p/3)*Math.cos((phi+4*Math.PI)/3);

                double x1 = y1 + shift;
                double x2 = y2 + shift;
                double x3 = y3 + shift;

                steps.add("Step " + step++ + ": Final Roots");
                steps.add("x₁ = " + clean(x1));
                steps.add("x₂ = " + clean(x2));
                steps.add("x₃ = " + clean(x3));

            } else if (discriminant > EPS) {

                steps.add("Since Δ > 0, equation has one real root and two complex roots.");
                steps.add("");

                steps.add("Using Cardano’s formula:");
                steps.add("y = ∛(-q/2 + √Δ) + ∛(-q/2 - √Δ)");
                steps.add("");

                double sqrtD = Math.sqrt(discriminant);

                double u = cubeRoot(-q/2 + sqrtD);
                double v = cubeRoot(-q/2 - sqrtD);

                double y1 = u + v;
                double x1 = y1 + shift;

                double realPart = -(u + v)/2 + shift;
                double imaginaryPart = (Math.sqrt(3)/2)*(u - v);

                steps.add("Compute:");
                steps.add("u = " + clean(u));
                steps.add("v = " + clean(v));
                steps.add("");

                steps.add("Step " + step++ + ": Final Roots");
                steps.add("x₁ (real) = " + clean(x1));
                steps.add("x₂ = " + clean(realPart) + " + " + clean(imaginaryPart) + "i");
                steps.add("x₃ = " + clean(realPart) + " - " + clean(imaginaryPart) + "i");

            }else {

                steps.add("Since Δ = 0, equation has repeated real roots.");
                steps.add("");

                double u = cubeRoot(-q/2);

                double y1 = 2*u;
                double y2 = -u;

                double x1 = y1 + shift;
                double x2 = y2 + shift;

                steps.add("Step " + step++ + ": Final Roots");
                steps.add("x₁ = " + clean(x1));
                steps.add("x₂ = " + clean(x2) + " (repeated)");
            }


        } catch (Exception e) {

            steps.clear();
            steps.add("Error: Unable to process equation.");
            steps.add("Please ensure the image is clear and equation is valid.");
        }

        return steps;
    }

    private double getCoeff(String eq, String term) {

        if (term.equals("x")) {
            return getLinearCoeff(eq);
        }

        int idx = eq.indexOf(term);
        if (idx == -1) return 0;

        int start = idx - 1;
        while (start >= 0 && eq.charAt(start) != '+' && eq.charAt(start) != '-') {
            start--;
        }

        String coeff = (start < 0) ? eq.substring(0, idx) : eq.substring(start, idx);

        if (coeff.equals("") || coeff.equals("+")) return 1;
        if (coeff.equals("-")) return -1;

        return safeParse(coeff);
    }

    private double getLinearCoeff(String eq) {

        eq = eq.replaceAll("[+-]?\\d*\\.?\\d*x\\^3", "");
        eq = eq.replaceAll("[+-]?\\d*\\.?\\d*x\\^2", "");

        int idx = eq.indexOf("x");
        if (idx == -1) return 0;

        int start = idx - 1;
        while (start >= 0 && eq.charAt(start) != '+' && eq.charAt(start) != '-') {
            start--;
        }

        String coeff = (start < 0) ? eq.substring(0, idx) : eq.substring(start, idx);

        if (coeff.equals("") || coeff.equals("+")) return 1;
        if (coeff.equals("-")) return -1;

        return safeParse(coeff);
    }

    private double getConstant(String eq) {

        eq = eq.replaceAll("[+-]?\\d*\\.?\\d*x\\^3", "");
        eq = eq.replaceAll("[+-]?\\d*\\.?\\d*x\\^2", "");
        eq = eq.replaceAll("[+-]?\\d*\\.?\\d*x", "");

        if (eq == null || eq.isEmpty() || eq.equals("+") || eq.equals("-"))
            return 0;

        return safeParse(eq);
    }

    private double safeParse(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private double cubeRoot(double x) {
        return (x >= 0) ? Math.pow(x, 1.0/3.0) : -Math.pow(-x, 1.0/3.0);
    }

    private String normalizeInput(String input) {

        // Convert everything to lowercase first
        input = input.toLowerCase();

        input = input.replaceAll("\\s+", "");

        // Fix OCR power mistakes
        input = input.replace("x³", "x^3");
        input = input.replace("x²", "x^2");
        input = input.replace("x3", "x^3");
        input = input.replace("x2", "x^2");

        // Fix unicode minus
        input = input.replace("−", "-");

        // Fix fancy equals
        input = input.replace("＝", "=");

        return input;
    }

    private double clean(double value) {
        if (Math.abs(value) < EPS) return 0;
        return Math.round(value * 1000000.0) / 1000000.0;
    }
}
