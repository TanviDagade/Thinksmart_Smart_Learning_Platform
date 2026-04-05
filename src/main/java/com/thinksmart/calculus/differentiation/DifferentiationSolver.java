package com.thinksmart.calculus.differentiation;

import java.util.ArrayList;
import java.util.List;

public class DifferentiationSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        steps.add("Given function:");
        steps.add(input);
        steps.add("");

        input = input.replace("d/dx", "").replace(" ", "");

        steps.add("Step 1: Use power rule");
        steps.add("d/dx (x^n) = n·x^(n−1)");
        steps.add("");

        int power = Integer.parseInt(
                input.substring(input.indexOf("^") + 1)
        );

        steps.add("n = " + power);
        steps.add("");

        int newPower = power - 1;

        steps.add("Step 2: Multiply exponent and reduce power");
        steps.add("Result = " + power + "x^" + newPower);
        steps.add("");

        steps.add("Final Answer:");
        steps.add(power + "x^" + newPower);

        return steps;
    }
}
