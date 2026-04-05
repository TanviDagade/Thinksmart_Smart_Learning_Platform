package com.thinksmart.calculus.integration;

import java.util.ArrayList;
import java.util.List;

public class IntegrationSolver {

    public List<String> solve(String input) {

        List<String> steps = new ArrayList<>();

        steps.add("Given integral:");
        steps.add(input);
        steps.add("");

        input = input.replace("∫", "").replace("dx", "").replace(" ", "");

        steps.add("Step 1: Apply power rule");
        steps.add("∫ x^n dx = x^(n+1)/(n+1) + C");
        steps.add("");

        int power = Integer.parseInt(
                input.substring(input.indexOf("^") + 1)
        );

        int newPower = power + 1;

        steps.add("n = " + power);
        steps.add("");

        steps.add("Step 2: Increase power and divide");
        steps.add("Result = x^" + newPower + "/" + newPower + " + C");
        steps.add("");

        steps.add("Final Answer:");
        steps.add("x^" + newPower + "/" + newPower + " + C");

        return steps;
    }
}
