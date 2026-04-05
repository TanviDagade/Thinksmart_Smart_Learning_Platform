package com.thinksmart.geometry.sphere;

import java.util.*;

public class SphereSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        try {
            String cleaned = input.toLowerCase();
            double r = Double.parseDouble(cleaned.replaceAll("[^0-9.]", ""));

            steps.add("Step 1: Identify radius");
            steps.add("r = " + r);

            if (cleaned.contains("volume")) {

                double v = (4.0/3) * Math.PI * r * r * r;

                steps.add("Step 2: Use formula");
                steps.add("Volume = 4/3 × π × r³");
                steps.add("= " + v);

            } else {

                double sa = 4 * Math.PI * r * r;

                steps.add("Step 2: Use formula");
                steps.add("Surface Area = 4 × π × r²");
                steps.add("= " + sa);
            }

        } catch (Exception e) {
            steps.add("Invalid input. Example:");
            steps.add("sphere radius 5");
        }

        return steps;
    }
}