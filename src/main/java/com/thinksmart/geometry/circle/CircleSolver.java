package com.thinksmart.geometry.circle;

import java.util.*;

public class CircleSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        try {
            String cleaned = input.toLowerCase();

            // Extract radius
            String number = cleaned.replaceAll("[^0-9.]", "");
            double r = Double.parseDouble(number);

            steps.add("Step 1: Identify radius");
            steps.add("r = " + r);

            // Decide operation
            if (cleaned.contains("area")) {

                double area = Math.PI * r * r;

                steps.add("Step 2: Use formula");
                steps.add("Area = π × r²");
                steps.add("= π × " + r + "²");
                steps.add("= " + area);

            } else if (cleaned.contains("circumference") || cleaned.contains("perimeter")) {

                double c = 2 * Math.PI * r;

                steps.add("Step 2: Use formula");
                steps.add("Circumference = 2 × π × r");
                steps.add("= 2 × π × " + r);
                steps.add("= " + c);

            } else {
                // Default: show both
                double area = Math.PI * r * r;
                double c = 2 * Math.PI * r;

                steps.add("Area = " + area);
                steps.add("Circumference = " + c);
            }

        } catch (Exception e) {
            steps.add("Invalid input. Try:");
            steps.add("circle radius 5");
            steps.add("find area of circle 5");
        }

        return steps;
    }
}