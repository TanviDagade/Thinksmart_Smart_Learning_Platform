package com.thinksmart.geometry.rectangle;
import java.util.*;

public class RectangleSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        try {
            String cleaned = input.toLowerCase();

            String[] nums = cleaned.replaceAll("[^0-9 ]", "").trim().split("\\s+");

            double l = Double.parseDouble(nums[0]);
            double b = Double.parseDouble(nums[1]);

            steps.add("Step 1: Identify dimensions");
            steps.add("Length = " + l + ", Breadth = " + b);

            if (cleaned.contains("area")) {

                double area = l * b;

                steps.add("Step 2: Use formula");
                steps.add("Area = l × b");
                steps.add("= " + l + " × " + b);
                steps.add("= " + area);

            } else if (cleaned.contains("perimeter")) {

                double p = 2 * (l + b);

                steps.add("Step 2: Use formula");
                steps.add("Perimeter = 2(l + b)");
                steps.add("= 2(" + l + " + " + b + ")");
                steps.add("= " + p);

            } else {
                steps.add("Area = " + (l * b));
                steps.add("Perimeter = " + (2 * (l + b)));
            }

        } catch (Exception e) {
            steps.add("Invalid input. Try:");
            steps.add("rectangle 5 3");
            steps.add("find area of rectangle length 5 breadth 3");
        }

        return steps;
    }
}