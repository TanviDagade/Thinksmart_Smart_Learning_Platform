package com.thinksmart.geometry.cylinder;

import java.util.*;

public class CylinderSolver {

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

                double v = Math.PI * r * r * h;

                steps.add("Step 2: Volume formula");
                steps.add("= π × r² × h");
                steps.add("= " + v);

            } else {

                double sa = 2 * Math.PI * r * (r + h);

                steps.add("Step 2: Surface Area");
                steps.add("= 2πr(r + h)");
                steps.add("= " + sa);
            }

        } catch (Exception e) {
            steps.add("Invalid input. Example:");
            steps.add("cylinder 3 7");
        }

        return steps;
    }
}