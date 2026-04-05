package com.thinksmart.calculus.limit;

import java.util.*;

public class PolynomialParser {

    public static Polynomial parse(String expr) {

        expr = expr.replace(" ", "").replace("(", "").replace(")", "");

        // Normalize: replace - with +- so we can split safely
        expr = expr.replace("-", "+-");
        if (expr.startsWith("+")) {
            expr = expr.substring(1);
        }

        String[] terms = expr.split("\\+");

        // First pass: find max power
        int maxPower = 0;
        for (String term : terms) {
            if (term.contains("x")) {
                if (term.contains("^")) {
                    int p = Integer.parseInt(term.substring(term.indexOf("^") + 1));
                    maxPower = Math.max(maxPower, p);
                } else {
                    maxPower = Math.max(maxPower, 1);
                }
            }
        }

        double[] coeffs = new double[maxPower + 1];

        // Second pass: fill coefficients
        for (String term : terms) {
            if (term.isEmpty()) continue;

            double coeff;
            int power;

            if (term.contains("x")) {

                // ----- COEFFICIENT -----
                String beforeX = term.substring(0, term.indexOf("x"));

                if (beforeX.equals("") || beforeX.equals("+")) {
                    coeff = 1;
                } else if (beforeX.equals("-")) {
                    coeff = -1;
                } else {
                    coeff = Double.parseDouble(beforeX);
                }

                // ----- POWER -----
                if (term.contains("^")) {
                    power = Integer.parseInt(term.substring(term.indexOf("^") + 1));
                } else {
                    power = 1;
                }

            } else {
                // Constant term
                coeff = Double.parseDouble(term);
                power = 0;
            }

            coeffs[maxPower - power] += coeff;
        }

        return new Polynomial(coeffs);
    }
}
