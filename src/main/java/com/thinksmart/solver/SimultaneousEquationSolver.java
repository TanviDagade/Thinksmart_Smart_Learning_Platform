package com.thinksmart.solver;

import java.util.ArrayList;
import java.util.List;

public class SimultaneousEquationSolver {

    public List<String> solve(String eq1, String eq2) {

        List<String> steps = new ArrayList<>();

        try {
            steps.add("Given equations:");
            steps.add(eq1);
            steps.add(eq2);

            // Remove spaces
            eq1 = eq1.replace(" ", "").replace("\r", "").trim();
            eq2 = eq2.replace(" ", "").replace("\r", "").trim();

            // Parse equations
            int[] e1 = parse(eq1);
            int[] e2 = parse(eq2);

            int a1 = e1[0], b1 = e1[1], c1 = e1[2];
            int a2 = e2[0], b2 = e2[1], c2 = e2[2];

            steps.add("");
            steps.add("Step 1: Write equations in standard form");
            steps.add(a1 + "x + " + b1 + "y = " + c1);
            steps.add(a2 + "x + " + b2 + "y = " + c2);

            steps.add("");
            steps.add("Step 2: Eliminate y");

            int m1 = b2;
            int m2 = b1;

            int A1 = a1 * m1;
            int B1 = b1 * m1;
            int C1 = c1 * m1;

            int A2 = a2 * m2;
            int B2 = b2 * m2;
            int C2 = c2 * m2;

            steps.add("Multiply equation 1 by " + m1);
            steps.add(A1 + "x + " + B1 + "y = " + C1);

            steps.add("Multiply equation 2 by " + m2);
            steps.add(A2 + "x + " + B2 + "y = " + C2);

            steps.add("");
            steps.add("Step 3: Subtract equations to eliminate y");

            int xCoeff = A1 - A2;
            int rhs = C1 - C2;

            steps.add(xCoeff + "x = " + rhs);

            int x = rhs / xCoeff;

            steps.add("");
            steps.add("Step 4: Solve for x");
            steps.add("x = " + x);

            steps.add("");
            steps.add("Step 5: Substitute x into equation 1");

            int y = (c1 - a1 * x) / b1;

            steps.add("y = " + y);

            steps.add("");
            steps.add("Final Answer:");
            steps.add("x = " + x + ", y = " + y);

        } catch (Exception e) {
            steps.clear();
            steps.add("Error:");
            steps.add("Enter equations in format:");
            steps.add("ax + by = c");
            steps.add("Example:");
            steps.add("2x + y = 5");
            steps.add("x - y = 1");
        }

        return steps;
    }

    // Parses ax + by = c
    private int[] parse(String eq) {
        eq = eq.replace("\r", "").trim();
        eq = eq.replace("-", "+-");
        String[] sides = eq.split("=");

        String left = sides[0];
        int c = Integer.parseInt(sides[1]);

        String[] terms = left.split("\\+");

        int a = 0, b = 0;

        for (String term : terms) {
            if (term.contains("x")) {
                String coeff = term.replace("x", "");
                a += coeff.equals("") ? 1 : coeff.equals("-") ? -1 : Integer.parseInt(coeff);
            } else if (term.contains("y")) {
                String coeff = term.replace("y", "");
                b += coeff.equals("") ? 1 : coeff.equals("-") ? -1 : Integer.parseInt(coeff);
            }
        }

        return new int[]{a, b, c};
    }
}
