package com.thinksmart.geometry.cone;

import java.util.*;

public class ConeSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        try {
            String cleaned = input.toLowerCase();

            String[] nums = cleaned.replaceAll("[^0-9 ]", "").trim().split("\\s+");

            double r = Double.parseDouble(nums[0]);
            double h = Double.parseDouble(nums[1]);

            steps.add("Step 1: Identify values");
            steps.add("r = " + r + ", h = " + h);

            if (cleaned.contains("volume")) {

                double v = (1.0/3) * Math.PI * r * r * h;

                steps.add("Step 2: Volume formula");
                steps.add("= 1/3 × π × r² × h");
                steps.add("= " + v);

            } else {

                double l = Math.sqrt(r*r + h*h);
                double sa = Math.PI * r * (r + l);

                steps.add("Step 2: Surface Area formula");
                steps.add("= πr(r + l)");
                steps.add("= " + sa);
            }

        } catch (Exception e) {
            steps.add("Invalid input. Example:");
            steps.add("cone 3 5");
        }

        return steps;
    }
}