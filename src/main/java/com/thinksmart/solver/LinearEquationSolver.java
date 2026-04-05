package com.thinksmart.solver;

import java.util.ArrayList;
import java.util.List;

public class LinearEquationSolver {

    public List<String> solve(String equation) {

        List<String> steps = new ArrayList<>();

        try {
            steps.add("Given:");
            steps.add(equation);

            equation = equation.replace(" ", "");

            if (!equation.contains("=") || !equation.contains("x")) {
                throw new IllegalArgumentException("Invalid linear equation format.");
            }

            String[] sides = equation.split("=");
            String left = sides[0];
            int c = Integer.parseInt(sides[1]);

            // Normalize left side
            left = left.replace("-", "+-");

            String[] terms = left.split("\\+");

            int a = 0; // coefficient of x
            int b = 0; // constant

            for (String term : terms) {
                if (term.contains("x")) {
                    String coeff = term.replace("x", "");
                    if (coeff.equals("") || coeff.equals("+")) {
                        a += 1;
                    } else if (coeff.equals("-")) {
                        a -= 1;
                    } else {
                        a += Integer.parseInt(coeff);
                    }
                } else if (!term.isEmpty()) {
                    b += Integer.parseInt(term);
                }
            }

            steps.add("");
            steps.add("Step 1: Move constant to right side");
            int rhs = c - b;
            steps.add(a + "x = " + rhs);

            steps.add("");
            steps.add("Step 2: Divide both sides by " + a);
            double x = (double) rhs / a;
            steps.add("x = " + x);

            steps.add("");
            steps.add("Final Answer:");
            steps.add("x = " + x);

        } catch (Exception e) {
            steps.clear();
            steps.add("Error:");
            steps.add("Please enter equation in format: ax + b = c");
            steps.add("Examples:");
            steps.add("2x + 3 = 7");
            steps.add("-x + 5 = 3");
        }

        return steps;
    }
}

