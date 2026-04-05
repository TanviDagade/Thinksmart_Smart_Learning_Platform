package com.thinksmart.geometry.triangle;
import java.util.*;

public class TriangleSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        try {
            String cleaned = input.toLowerCase();

            String[] nums = cleaned.replaceAll("[^0-9 ]", "").trim().split("\\s+");

            double base = Double.parseDouble(nums[0]);
            double height = Double.parseDouble(nums[1]);

            steps.add("Step 1: Identify values");
            steps.add("Base = " + base + ", Height = " + height);

            double area = 0.5 * base * height;

            steps.add("Step 2: Use formula");
            steps.add("Area = 1/2 × base × height");
            steps.add("= 1/2 × " + base + " × " + height);
            steps.add("= " + area);

        } catch (Exception e) {
            steps.add("Invalid input. Try:");
            steps.add("triangle 5 4");
            steps.add("find area of triangle base 5 height 4");
        }

        return steps;
    }
}