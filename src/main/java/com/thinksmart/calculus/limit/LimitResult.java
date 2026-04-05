package com.thinksmart.calculus.limit;

import java.util.List;

public class LimitResult {

    private List<String> steps;
    private String answer;

    public LimitResult(List<String> steps, String answer) {
        this.steps = steps;
        this.answer = answer;
    }

    public List<String> getSteps() {
        return steps;
    }

    public String getAnswer() {
        return answer;
    }
}
