package com.thinksmart.geometry.square;

import java.util.*;

public class SquareSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        try {
            String cleaned = input.toLowerCase();

            String number = cleaned.replaceAll("[^0-9.]", "");
            double side = Double.parseDouble(number);

            steps.add("Step 1: Identify side");
            steps.add("Side = " + side);

            if (cleaned.contains("area")) {

                double area = side * side;

                steps.add("Step 2: Use formula");
                steps.add("Area = side²");
                steps.add("= " + side + "²");
                steps.add("= " + area);

            } else if (cleaned.contains("perimeter")) {

                double p = 4 * side;

                steps.add("Step 2: Use formula");
                steps.add("Perimeter = 4 × side");
                steps.add("= 4 × " + side);
                steps.add("= " + p);

            } else {
                steps.add("Area = " + (side * side));
                steps.add("Perimeter = " + (4 * side));
            }

        } catch (Exception e) {
            steps.add("Invalid input. Example:");
            steps.add("square 5");
        }

        return steps;
    }
}