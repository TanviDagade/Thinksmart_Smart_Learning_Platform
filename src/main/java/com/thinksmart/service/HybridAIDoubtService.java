package com.thinksmart.service;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

import com.thinksmart.classifier.MathClassifier;
import com.thinksmart.classifier.MathClassifier.ProblemType;
import com.thinksmart.solver.*;
import com.thinksmart.matrix.*;
import com.thinksmart.calculus.limit.*;
import com.thinksmart.calculus.differentiation.DifferentiationSolver;
import com.thinksmart.calculus.integration.IntegrationSolver;

@Service
public class HybridAIDoubtService {

    public String processQuestion(String question, HttpSession session) {

        if (question == null || question.trim().isEmpty()) {
            return "Please type your doubt.";
        }

        String lower = question.toLowerCase().trim();

        // ✅ STEP REFERENCE LOGIC (ONLY ONCE)
        if (lower.matches(".*step\\s*\\d+.*")) {
            int stepNum = Integer.parseInt(
                    lower.replaceAll(".*step\\s*(\\d+).*", "$1"));

            List<String> lastSteps =
                    (List<String>) session.getAttribute("lastSteps");

            if (lastSteps != null && stepNum <= lastSteps.size()) {
                return "Step " + stepNum + " details:\n"
                        + lastSteps.get(stepNum - 1);
            } else {
                return "Sorry, I cannot find that step.";
            }
        }

        // Greetings
        if (lower.equals("hi") || lower.equals("hello") || lower.equals("hey")) {
            return "Hello! I am ThinkSmart AI. Ask me any math doubt.";
        }

        if (lower.contains("how are you")) {
            return "I'm doing great! 😊 Ready to solve some math?";
        }

        if (lower.contains("thank")) {
            return "You're welcome. Keep learning and practicing.";
        }

        if (lower.contains("who are you")) {
            return "I am ThinkSmart AI, your offline mathematics assistant.";
        }

        if (lower.equals("bye") || lower.equals("goodbye")) {
            return "Goodbye! 👋 Come back whenever you need math help.";
        }

        if (lower.contains("help")) {
            return "Of course! 😊 Send me the question and I'll solve it step-by-step.";
        }

        if (lower.contains("can you")) {
            return "Yes, I can help! Please send your math question.";
        }

        // CLASSIFY MATH PROBLEM
        MathClassifier classifier = new MathClassifier();
        ProblemType type = classifier.classify(question);

        if (type == ProblemType.UNKNOWN) {
            return handleConceptQuestion(lower);
        }

        // SOLVE
        List<String> steps = solveInternally(type, question);

        // SAVE FOR STEP FOLLOW-UP
        session.setAttribute("lastSteps", steps);

        return formatSteps(steps);
    }

    // INTERNAL SOLVER ROUTING
    private List<String> solveInternally(ProblemType type, String question) {

        switch (type) {

            case LINEAR:
                return new LinearEquationSolver().solve(question);

            case QUADRATIC:
                return new QuadraticEquationSolver().solve(question);

            case CUBIC:
                return new CubicEquationSolver().solve(question);

            case SIMULTANEOUS:
                String[] eq = question.split("\\n");
                if (eq.length == 2) {
                    return new SimultaneousEquationSolver()
                            .solve(eq[0], eq[1]);
                }
                return List.of("Please provide two equations, one per line.");

            case MATRIX_ADD_SUBTRACT:
                return new MatrixAddSubtractSolver().solve(question);

            case MATRIX_DETERMINANT:
                return new DeterminantSolver().solve(question);

            case MATRIX_MULTIPLICATION:
                return new MatrixMultiplicationSolver().solve(question);

            case LIMIT:
                LimitResult result = new LimitSolver().solve(question);
                List<String> limitSteps = new ArrayList<>();
                limitSteps.addAll(result.getSteps());
                limitSteps.add("Final Answer: " + result.getAnswer());
                return limitSteps;

            case DIFFERENTIATION:
                return new DifferentiationSolver().solve(question);

            case INTEGRATION:
                return new IntegrationSolver().solve(question);

            default:
                return List.of("Unsupported problem type.");
        }
    }

    // ✅ CLEAN STEP FORMATTER (NO DOUBLE NUMBERING)
    private String formatSteps(List<String> steps) {

        StringBuilder sb = new StringBuilder();

        for (String step : steps) {
            sb.append(step).append("\n\n");
        }

        sb.append("Would you like me to explain any step further?");

        return sb.toString();
    }

    // CONCEPT HANDLER
    private String handleConceptQuestion(String lower) {

        if (lower.contains("derivative")) {
            return """
                Concept: Derivative
                
                A derivative represents the rate of change of a function.
                
                Example:
                If y = x²
                Then dy/dx = 2x
                """;
        }

        if (lower.contains("integration")) {
            return """
                Concept: Integration
                
                Integration is the reverse of differentiation.
                
                Example:
                ∫2x dx = x² + C
                """;
        }

        if (lower.contains("limit")) {
            return """
                Concept: Limit
                
                A limit tells what value a function approaches
                as input approaches a number.
                """;
        }

        if (lower.contains("matrix")) {
            return """
                Matrix Concept:
                
                A matrix is a rectangular arrangement of numbers.
                """;
        }

        return """
            I am specialized in mathematics.
            
            You can ask:
            - Solve equations
            - Matrix operations
            - Limits
            - Differentiation
            - Integration
            
            Please ask a math-related question.
            """;
    }
}