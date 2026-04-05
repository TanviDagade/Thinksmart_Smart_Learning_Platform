package com.thinksmart.solver;

import java.util.ArrayList;
import java.util.List;

public class QuadraticEquationSolver {

    public List<String> solve(String equation) {

        List<String> steps = new ArrayList<>();

        try {
            steps.add("Given:");
            steps.add(equation);

            equation = equation.replace(" ", "");

            String[] sides = equation.split("=");
            String left = sides[0];

            // Normalize signs
            left = left.replace("-", "+-");

            String[] terms = left.split("\\+");

            int a = 0, b = 0, c = 0;

            for (String term : terms) {
                if (term.contains("x^2")) {
                    String coeff = term.replace("x^2", "");
                    a += coeff.equals("") ? 1 : coeff.equals("-") ? -1 : Integer.parseInt(coeff);
                } else if (term.contains("x")) {
                    String coeff = term.replace("x", "");
                    b += coeff.equals("") ? 1 : coeff.equals("-") ? -1 : Integer.parseInt(coeff);
                } else if (!term.isEmpty()) {
                    c += Integer.parseInt(term);
                }
            }

            steps.add("");
            steps.add("Step 1: Identify coefficients");
            steps.add("a = " + a + ", b = " + b + ", c = " + c);

            steps.add("");
            steps.add("Step 2: Apply quadratic formula");
            steps.add("x = (-b ± √(b² - 4ac)) / 2a");

            int discriminant = b * b - 4 * a * c;
            steps.add("");
            steps.add("Step 3: Calculate discriminant");
            steps.add("D = " + discriminant);

            if (discriminant < 0) {
                steps.add("");
                steps.add("Final Answer:");
                steps.add("No real roots");
                return steps;
            }

            double sqrtD = Math.sqrt(discriminant);

            double x1 = (-b + sqrtD) / (2 * a);
            double x2 = (-b - sqrtD) / (2 * a);

            steps.add("");
            steps.add("Step 4: Calculate roots");
            steps.add("x₁ = " + x1);
            steps.add("x₂ = " + x2);

            steps.add("");
            steps.add("Final Answer:");
            steps.add("x = " + x1 + ", " + x2);

        } catch (Exception e) {
            steps.clear();
            steps.add("Error:");
            steps.add("Please enter quadratic equation in format:");
            steps.add("ax^2 + bx + c = 0");
            steps.add("Example:");
            steps.add("x^2 - 5x + 6 = 0");
        }

        return steps;
    }
}
