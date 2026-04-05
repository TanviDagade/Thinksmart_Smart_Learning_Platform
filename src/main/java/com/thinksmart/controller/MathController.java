package com.thinksmart.controller;

import com.thinksmart.calculus.differentiation.DifferentiationSolver;
import com.thinksmart.calculus.integration.IntegrationSolver;
import com.thinksmart.calculus.limit.LimitResult;
import com.thinksmart.calculus.limit.LimitSolver;
import com.thinksmart.classifier.MathClassifier;
import com.thinksmart.database.User;
import com.thinksmart.database.UserActivity;
import com.thinksmart.matrix.DeterminantSolver;
import com.thinksmart.matrix.MatrixAddSubtractSolver;
import com.thinksmart.matrix.MatrixMultiplicationSolver;
import com.thinksmart.matrix.MatrixTransposeSolver;
import com.thinksmart.matrix.MatrixInverseSolver;
import com.thinksmart.matrix.MatrixAdjointSolver;
import com.thinksmart.solver.CubicEquationSolver;
import com.thinksmart.solver.LinearEquationSolver;
import com.thinksmart.solver.QuadraticEquationSolver;
import com.thinksmart.solver.SimultaneousEquationSolver;
import com.thinksmart.geometry.circle.CircleSolver;
import com.thinksmart.geometry.rectangle.RectangleSolver;
import com.thinksmart.geometry.triangle.TriangleSolver;
import com.thinksmart.geometry.square.SquareSolver;
import com.thinksmart.geometry.sphere.SphereSolver;
import com.thinksmart.geometry.cone.ConeSolver;
import com.thinksmart.geometry.cylinder.CylinderSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.thinksmart.database.UserActivityRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MathController {

    @Autowired
    private UserActivityRepository activityRepository;

    @PostMapping("/math/solve")
    public String solveMath(
            @RequestParam("mathProblem") String mathProblem,
            Model model,
            jakarta.servlet.http.HttpSession session) {

        MathClassifier classifier = new MathClassifier();
        MathClassifier.ProblemType type = classifier.classify(mathProblem);

        List<String> steps;

        switch (type) {

            case LINEAR:
                LinearEquationSolver linearSolver = new LinearEquationSolver();
                steps = linearSolver.solve(mathProblem);
                break;

            case QUADRATIC:
                QuadraticEquationSolver quadraticSolver = new QuadraticEquationSolver();
                steps = quadraticSolver.solve(mathProblem);
                break;

            case CUBIC:
                CubicEquationSolver cubicSolver = new CubicEquationSolver();
                steps = cubicSolver.solve(mathProblem);
                break;

            case SIMULTANEOUS:
                SimultaneousEquationSolver ses = new SimultaneousEquationSolver();

                String[] equations = mathProblem.split("\\n");

                if (equations.length != 2) {
                    steps = List.of(
                            "Error:",
                            "Please enter TWO equations, one per line.",
                            "Example:",
                            "2x + y = 5",
                            "x - y = 1"
                    );
                } else {
                    steps = ses.solve(equations[0], equations[1]);
                }
                break;

            case MATRIX_ADD_SUBTRACT:
                MatrixAddSubtractSolver addSubSolver = new MatrixAddSubtractSolver();
                steps = addSubSolver.solve(mathProblem);
                break;

            case MATRIX_DETERMINANT:
                DeterminantSolver detSolver = new DeterminantSolver();
                steps = detSolver.solve(mathProblem);
                break;

            case MATRIX_MULTIPLICATION:
                MatrixMultiplicationSolver mms = new MatrixMultiplicationSolver();
                steps = mms.solve(mathProblem);
                break;

            case MATRIX_TRANSPOSE:
                MatrixTransposeSolver ts = new MatrixTransposeSolver();
                steps = ts.solve(mathProblem);
                break;

            case MATRIX_INVERSE:
                MatrixInverseSolver is = new MatrixInverseSolver();
                steps = is.solve(mathProblem);
                break;

            case MATRIX_ADJOINT:
                MatrixAdjointSolver mas = new MatrixAdjointSolver();
                steps = mas.solve(mathProblem);
                break;

            case LIMIT:
                LimitSolver limitSolver = new LimitSolver();
                LimitResult result = limitSolver.solve(mathProblem);

                steps = new java.util.ArrayList<>();
                steps.addAll(result.getSteps());
                steps.add("");
                steps.add("Final Answer:");
                steps.add(result.getAnswer());
                break;

            case DIFFERENTIATION:
                DifferentiationSolver diffSolver = new DifferentiationSolver();
                steps = diffSolver.solve(mathProblem);
                break;

            case INTEGRATION:
                IntegrationSolver intSolver = new IntegrationSolver();
                steps = intSolver.solve(mathProblem);
                break;

            case GEOMETRY_CIRCLE:
                CircleSolver circleSolver = new CircleSolver();
                steps = circleSolver.solve(mathProblem);
                break;

            case GEOMETRY_RECTANGLE:
                RectangleSolver rectangleSolver = new RectangleSolver();
                steps = rectangleSolver.solve(mathProblem);
                break;

            case GEOMETRY_TRIANGLE:
                TriangleSolver triangleSolver = new TriangleSolver();
                steps = triangleSolver.solve(mathProblem);
                break;

            case GEOMETRY_SQUARE:
                SquareSolver ss = new SquareSolver();
                steps = ss.solve(mathProblem);
                break;

            case GEOMETRY_SPHERE:
                SphereSolver sps = new SphereSolver();
                steps = sps.solve(mathProblem);
                break;

            case GEOMETRY_CONE:
                ConeSolver cs = new ConeSolver();
                steps = cs.solve(mathProblem);
                break;

            case GEOMETRY_CYLINDER:
                CylinderSolver cyl = new CylinderSolver();
                steps = cyl.solve(mathProblem);
                break;

            default:
                steps = List.of(
                        "Error:",
                        "Unsupported or invalid math problem.",
                        "Currently supported:",
                        "Linear equations",
                        "Quadratic equations",
                        "Simultaneous equations",
                        "Matrix determinant (n × n)",
                        "Matrix addition & subtraction (n × n)",
                        "Matrix Multuplication (n*n)",
                        "Limits",
                        "Differentiation",
                        "Integration"
                );
        }

        String formattedSteps = String.join("\n", steps);
        model.addAttribute("steps", formattedSteps);
        model.addAttribute("input", mathProblem);

        User user = (User) session.getAttribute("loggedInUser");

        if(user != null){

            UserActivity activity = new UserActivity();

            activity.setEmail(user.getEmail());
            activity.setProblem(mathProblem);
            activity.setTopic(type.toString());
            activity.setSolvedAt(LocalDateTime.now());

            activityRepository.save(activity);
        }

        return "math";
    }
}
