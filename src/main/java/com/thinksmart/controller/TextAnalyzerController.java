package com.thinksmart.controller;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import com.thinksmart.classifier.MathClassifier;
import com.thinksmart.classifier.MathClassifier.ProblemType;
import com.thinksmart.solver.*;
import com.thinksmart.matrix.*;
import com.thinksmart.calculus.limit.*;
import com.thinksmart.calculus.differentiation.DifferentiationSolver;
import com.thinksmart.calculus.integration.IntegrationSolver;


import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/text-analyzer")
public class TextAnalyzerController {

    // ✅ LOAD PAGE
    @GetMapping
    public String showPage() {
        return "text-analyzer";
    }

    // ✅ HANDLE IMAGE UPLOAD
    @PostMapping("/analyze")
    public String analyzeImage(
            @RequestParam("image") MultipartFile image,
            Model model
    ) {

        try {
            File tempFile = File.createTempFile("upload-", ".png");
            image.transferTo(tempFile);

            Tesseract tesseract = new Tesseract();
            tesseract.setLanguage("eng");

            String extractedText = tesseract.doOCR(tempFile);
            System.out.println("RAW OCR TEXT: " + extractedText);
            System.out.println("CLEANED TEXT: " + extractedText);

            extractedText = extractedText
                    .replaceAll("×", "*")
                    .replaceAll("÷", "/")
                    .replaceAll("–", "-")
                    .replaceAll("—", "-")
                    .replaceAll("²", "^2")
                    .replaceAll("³", "^3");

            extractedText = extractedText
                    .replaceAll("[^0-9xX+\\-*/=.^()\\s]", "")
                    .trim();

            extractedText = extractedText.replace("X", "x");
            extractedText = fixPowers(extractedText);

            System.out.println("FINAL TEXT BEFORE CLASSIFIER: " + extractedText);

            MathClassifier classifier = new MathClassifier();
            ProblemType type = classifier.classify(extractedText);

            List<String> steps;

            switch (type) {

                case LINEAR:
                    steps = new LinearEquationSolver().solve(extractedText);
                    break;

                case QUADRATIC:
                    steps = new QuadraticEquationSolver().solve(extractedText);
                    break;

                case CUBIC:
                    steps = new CubicEquationSolver().solve(extractedText);
                    break;

                case SIMULTANEOUS:
                    String[] equations = extractedText.split("\\n");
                    if (equations.length == 2) {
                        steps = new SimultaneousEquationSolver().solve(equations[0], equations[1]);
                    } else {
                        steps = List.of("Error: Please provide two equations.");
                    }
                    break;

                case MATRIX_ADD_SUBTRACT:
                    steps = new MatrixAddSubtractSolver().solve(extractedText);
                    break;

                case MATRIX_DETERMINANT:
                    steps = new DeterminantSolver().solve(extractedText);
                    break;

                case MATRIX_MULTIPLICATION:
                    steps = new MatrixMultiplicationSolver().solve(extractedText);
                    break;

                case LIMIT:
                    LimitResult result = new LimitSolver().solve(extractedText);
                    steps = new java.util.ArrayList<>();
                    steps.addAll(result.getSteps());
                    steps.add("");
                    steps.add("Final Answer:");
                    steps.add(result.getAnswer());
                    break;

                case DIFFERENTIATION:
                    steps = new DifferentiationSolver().solve(extractedText);
                    break;

                case INTEGRATION:
                    steps = new IntegrationSolver().solve(extractedText);
                    break;

                default:
                    steps = List.of("Error: Unsupported math problem.");
            }

            String formattedSteps = String.join("\n", steps);
            model.addAttribute("result", formattedSteps);

        } catch (Exception e) {
            model.addAttribute("result", "Error processing image.");
            e.printStackTrace();
        }

        return "text-analyzer";
    }

    private String fixPowers(String text) {

        text = text.replaceAll("x2", "x^2");
        text = text.replaceAll("x3", "x^3");

        text = text.replaceAll("x2", "x^2");
        text = text.replaceAll("x3", "x^3");

        // Fix common OCR mistake: x42 instead of x^2
        text = text.replaceAll("x42", "x^2");

        return text;
    }

}
