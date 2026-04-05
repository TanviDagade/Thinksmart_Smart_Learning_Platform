package com.thinksmart.calculus.limit;

import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.*;
import com.thinksmart.math.util.FractionUtil;

public class LimitSolver {

    private static final double EPS = 1e-6;
    public LimitResult solve(String input) {

        List<String> steps = new ArrayList<>();

        steps.add("Given limit:");
        steps.add(input);
        steps.add("");

        // ---------- NORMALIZATION ----------
        input = input.toLowerCase();
        input = input.replace("lim", "");
        input = input.replace("→", "->");
        input = input.replaceAll("\\s+", "");
        input = input.replace("²", "^2").replace("³", "^3").replace("⁴", "^4");

        if (!input.contains("->")) {
            steps.add("Invalid limit format");
            return new LimitResult(steps, "Invalid input");
        }

        String afterArrow = input.substring(input.indexOf("->") + 2);

        boolean isInfinityLimit = afterArrow.startsWith("infinity")
                || afterArrow.startsWith("inf")
                || afterArrow.startsWith("∞");

        boolean isNegInfinityLimit = afterArrow.startsWith("-infinity")
                || afterArrow.startsWith("-inf")
                || afterArrow.startsWith("-∞");

        int i = 0;
        String function = afterArrow.substring(i);

        if (isInfinityLimit || isNegInfinityLimit) {
            // remove infinity symbol completely
            if (afterArrow.startsWith("-∞")) {
                function = afterArrow.substring(2);
            } else if (afterArrow.startsWith("-infinity")) {
                function = afterArrow.substring("-infinity".length());
            } else if (afterArrow.startsWith("∞")) {
                function = afterArrow.substring(1);
            } else if (afterArrow.startsWith("infinity")) {
                function = afterArrow.substring("infinity".length());
            } else {
                function = afterArrow.substring("inf".length());
            }

            steps.add(
                    isNegInfinityLimit
                            ? "Step 1: x approaches -infinity"
                            : "Step 1: x approaches infinity"
            );
            steps.add("");

            return solveInfinityLimit(function, steps, isNegInfinityLimit);
        }

        while (i < afterArrow.length() &&
                (Character.isDigit(afterArrow.charAt(i)) ||
                        afterArrow.charAt(i) == '.' ||
                        afterArrow.charAt(i) == '-')) {
            i++;
        }

        double a = Double.parseDouble(afterArrow.substring(0, i));
        function = afterArrow.substring(i);

        steps.add("Step 1: Identify approaching value");
        steps.add("x → " + fmt(a));
        steps.add("");

        // ---------- TRIG GUARD (🔥 IMPORTANT FIX) ----------
        if (function.contains("sin") || function.contains("cos") || function.contains("tan")) {
            steps.add("Trigonometric expression detected");
            steps.add("Symbolic polynomial simplification is not applicable");
            steps.add("");
            steps.add("Proceeding with numerical limit evaluation only");
            steps.add("");

            return confirmNumerically(function, a, null, steps);
        }

        // ---------- TRIGONOMETRIC SPECIAL CASE ----------
        if (function.equals("sin(x)/x") && Math.abs(a) < 1e-9) {
            steps.add("Trigonometric expression detected");
            steps.add("Using standard trigonometric limit:");
            steps.add("lim x→0 sin(x)/x = 1");
            return new LimitResult(steps, "1");
        }

        // ---------- DIRECT SUBSTITUTION ----------
        steps.add("Step 2: Direct substitution");
        double direct = evaluate(function, a);

        if (!Double.isNaN(direct) && !Double.isInfinite(direct)) {
            steps.add("f(" + fmt(a) + ") = " + fmt(direct));
            steps.add("");
            return confirmNumerically(function, a, direct, steps);
        }

        steps.add("Direct substitution gives indeterminate form (0/0)");
        steps.add("");

        // ---------- SPLIT NUMERATOR AND DENOMINATOR ----------
        String[] parts = function.split("/");
        if (parts.length != 2) {
            steps.add("Unable to split expression");
            return confirmNumerically(function, a, null, steps);
        }

        LimitType type = detectType(function, a);
        steps.add("Detected limit type: " + type);
        steps.add("");

        String numerator = parts[0];
        String denominator = parts[1];

        // ---------- EXPAND FACTORED NUMERATOR ----------
        String expandedNumerator = expandNumerator(numerator, steps);

        // ---------- SYMBOLIC SIMPLIFICATION ----------
        steps.add("Step 3: Symbolic simplification");
        steps.add("");

        Polynomial numPoly;
        try {
            numPoly = PolynomialParser.parse(expandedNumerator);
        } catch (Exception e) {
            steps.add("Expression is not a valid polynomial");
            steps.add("Switching to numerical evaluation only");
            return confirmNumerically(function, a, null, steps);
        }

        double valueAtA = numPoly.evaluate(a);

        if (Math.abs(valueAtA) < 1e-6) {

            steps.add("Check if (x − " + fmt(a) + ") is a factor of numerator");

            PolynomialDivisionResult div = syntheticDivide(expandedNumerator, a);

            if (Math.abs(div.remainder) < 1e-6) {
                steps.add("Factor numerator:");
                steps.add(expandedNumerator + " = (" + div.quotient + ") * (x - " + fmt(a) + ")");
                steps.add("Cancel common factor (x − " + fmt(a) + ")");
                steps.add("After cancellation:");
                steps.add(div.quotient);

                double ans = evaluate(div.quotient, a);

                steps.add("Substitute x = " + fmt(a));
                steps.add("= " + fmt(ans));
                steps.add("");

                steps.add("Step 4: Numerical confirmation");
                return confirmNumerically(function, a, ans, steps);
            } else {
                steps.add("(x − " + fmt(a) + ") is not a factor");
            }
        } else {
            steps.add("Numerator does not vanish at x = " + fmt(a));
        }

        steps.add("Step 4: Numerical evaluation only");
        return confirmNumerically(function, a, null, steps);
    }

    // ---------- NUMERICAL CONFIRMATION ----------
    private LimitResult confirmNumerically(String function, double a, Double symbolic, List<String> steps) {

        double left = evaluate(function, a - EPS);
        double right = evaluate(function, a + EPS);

        steps.add("Left-hand limit:");
        steps.add("f(" + fmt(a - EPS) + ") ≈ " + fmt(left));
        steps.add("");

        steps.add("Right-hand limit:");
        steps.add("f(" + fmt(a + EPS) + ") ≈ " + fmt(right));
        steps.add("");

        steps.add("Proceeding with numerical limit evaluation");
        steps.add("");

        if (Double.isNaN(left) || Double.isNaN(right) ||
                Double.isInfinite(left) || Double.isInfinite(right)) {

            steps.add("Limit does not exist");
            return new LimitResult(steps, "Does not exist");
        }

        if (isZero(left) && isZero(right)) {
            steps.add("Direct substitution gives 0/0");
            steps.add("This is an indeterminate form");
            steps.add("");
        }

        if (Math.abs(left - right) < 1e-4) {

            double rawAns = (symbolic != null) ? symbolic : (left + right) / 2;
            double snappedAns = FractionUtil.snapToRational(rawAns);

            String fractionAnswer = FractionUtil.toFraction(snappedAns);

            steps.add("Left and right limits match");
            steps.add("Final confirmed answer = " + fractionAnswer);

            return new LimitResult(steps, fractionAnswer);
        }

        steps.add("Left and right limits differ");
        steps.add("Limit does not exist");

        return new LimitResult(steps, "Does not exist");
    }

    private double evaluate(String expr, double x) {
        try {
            return new ExpressionBuilder(expr).variable("x").build().setVariable("x", x).evaluate();
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private String fmt(double x) {
        if (Math.abs(x - Math.round(x)) < 1e-6) return String.valueOf((long) Math.round(x));
        return String.format("%.6f", x);
    }

    // ---------- EXPAND NUMERATOR (handles (a)(b)) ----------
    private String expandNumerator(String expr, List<String> steps) {
        // Detect multiplication of two polynomials
        if (expr.contains(")(")) {
            String[] factors = expr.split("\\)\\(");
            String first = factors[0];
            String second = factors[1];

            // Add back removed parentheses
            if (!first.startsWith("(")) first = "(" + first;
            if (!second.endsWith(")")) second = second + ")";

            return multiplyPolynomials(first, second);
        }
        return expr; // single polynomial, no multiplication
    }

    private String multiplyPolynomials(String p1, String p2) {

        // Clean the strings
        if (p1.startsWith("(") && p1.endsWith(")")) p1 = p1.substring(1, p1.length()-1);
        if (p2.startsWith("(") && p2.endsWith(")")) p2 = p2.substring(1, p2.length()-1);

        p1 = p1.replace("*", "");
        p2 = p2.replace("*", "");

        Polynomial poly1 = cleanAndParse(p1);
        Polynomial poly2 = cleanAndParse(p2);
       poly1 = PolynomialParser.parse(p1);
       poly2 = PolynomialParser.parse(p2);

        int deg1 = poly1.coeffs.length - 1;
        int deg2 = poly2.coeffs.length - 1;
        double[] result = new double[deg1 + deg2 + 1];

        for (int i = 0; i <= deg1; i++) {
            for (int j = 0; j <= deg2; j++) {
                result[i + j] += poly1.coeffs[i] * poly2.coeffs[j];
            }
        }
        return reducedToString(new Polynomial(result));
    }

    private Polynomial cleanAndParse(String polyStr) {
        // Remove outer parentheses
        polyStr = polyStr.trim();
        if (polyStr.startsWith("(") && polyStr.endsWith(")")) {
            polyStr = polyStr.substring(1, polyStr.length() - 1);
        }
        // Remove multiplication symbols if any
        polyStr = polyStr.replace("*", "");
        // Parse
        return PolynomialParser.parse(polyStr);
    }

    // ---------- REDUCE POLYNOMIAL TO STRING ----------
    private String reducedToString(Polynomial p) {
        StringBuilder sb = new StringBuilder();
        int power = p.coeffs.length - 1;

        for (double c : p.coeffs) {
            if (Math.abs(c) < 1e-6) {
                power--;
                continue;
            }
            if (sb.length() > 0) sb.append(c > 0 ? " + " : " - ");
            else if (c < 0) sb.append("-");
            double absC = Math.abs(c);
            if (!(Math.abs(absC - 1.0) < 1e-6 && power != 0)) sb.append((int) absC);
            if (power > 1) sb.append("x^").append(power);
            else if (power == 1) sb.append("x");
            power--;
        }

        return sb.length() == 0 ? "0" : sb.toString();
    }

    // ---------- SYNTHETIC DIVISION ----------
    private static class PolynomialDivisionResult {
        String quotient;
        double remainder;
        PolynomialDivisionResult(String q, double r) { quotient = q; remainder = r; }
    }

    private PolynomialDivisionResult syntheticDivide(String poly, double a) {
        Polynomial p = PolynomialParser.parse(poly);
        double[] coeffs = p.coeffs;
        List<Double> result = new ArrayList<>();
        result.add(coeffs[0]);
        for (int i = 1; i < coeffs.length; i++) result.add(coeffs[i] + result.get(i - 1) * a);
        double remainder = result.get(result.size() - 1);
        result.remove(result.size() - 1);

        StringBuilder q = new StringBuilder();
        int degree = result.size() - 1;
        for (int i = 0; i < result.size(); i++) {
            double c = result.get(i);
            if (Math.abs(c) < 1e-6) continue;
            if (q.length() > 0) q.append(c > 0 ? "+":"-");
            double absC = Math.abs(c);
            if (!(Math.abs(absC - 1.0) < 1e-6 && degree-i!=0)) q.append((int)absC);
            if (degree - i > 1) q.append("x^").append(degree - i);
            else if (degree - i == 1) q.append("x");
        }

        return new PolynomialDivisionResult(q.length() == 0 ? "0" : q.toString(), remainder);
    }

    private LimitType detectType(String expr, double a) {

        expr = expr.toLowerCase();

        if (expr.contains("sin") || expr.contains("cos") || expr.contains("tan")) {
            return LimitType.TRIGONOMETRIC;
        }

        if (expr.contains("/") && expr.contains("x")) {
            return LimitType.FACTOR_CANCEL;
        }

        if (Double.isInfinite(a)) {
            return LimitType.INFINITY_OVER_INFINITY;
        }

        if (expr.equals("1/x")) {
            return LimitType.ONE_OVER_X;
        }
        return LimitType.DIRECT_SUBSTITUTION;
    }

    private static boolean isZero(double value) {

        return Math.abs(value) < 1e-8;
    }

    private LimitResult solveInfinityLimit(String function,
                                           List<String> steps,
                                           boolean isNegInfinity) {

        steps.add("Identify highest power of x in numerator and denominator");

        Polynomial[] polys = splitAndParse(function);
        Polynomial num = polys[0];
        Polynomial den = polys[1];

        int numDegree = num.degree();
        int denDegree = den.degree();

        steps.add("Degree of numerator = " + numDegree);
        steps.add("Degree of denominator = " + denDegree);

        if (numDegree > denDegree) {
            steps.add("Numerator grows faster");
            return new LimitResult(steps, "Infinity");
        }

        if (numDegree < denDegree) {
            steps.add("Denominator grows faster");
            return new LimitResult(steps, "0");
        }

        if (!function.contains("/")) {
            steps.add("Invalid rational function for infinity limit");
            return new LimitResult(steps, "Invalid input");
        }

        double ratio = num.leadingCoeff() / den.leadingCoeff();
        // If x → -∞ and highest power is odd, sign flips
        if (isNegInfinity && numDegree % 2 == 1) {
            ratio = -ratio;
        }
        String frac = FractionUtil.toFraction(ratio);

        steps.add("Degrees are equal");
        steps.add("Limit equals ratio of leading coefficients");
        steps.add("Final answer = " + frac);

        steps.add("Numerator coeffs = " + Arrays.toString(num.coeffs));
        steps.add("Denominator coeffs = " + Arrays.toString(den.coeffs));

        return new LimitResult(steps, frac);
    }

    private Polynomial[] splitAndParse(String expr) {
        String[] parts = expr.split("/");
        return new Polynomial[] {
                PolynomialParser.parse(parts[0]),
                PolynomialParser.parse(parts[1])
        };
    }
}
