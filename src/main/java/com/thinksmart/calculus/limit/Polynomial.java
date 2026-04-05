package com.thinksmart.calculus.limit;

public class Polynomial {
    public double[] coeffs; // Highest degree first

    public Polynomial(double[] coeffs) {
        this.coeffs = coeffs;
    }

    public double evaluate(double x) {
        double result = 0;
        int power = coeffs.length - 1;
        for (double c : coeffs) {
            result += c * Math.pow(x, power);
            power--;
        }
        return result;
    }

    public int degree() {
        return coeffs.length - 1;
    }

    double leadingCoeff() {
        for (double c : coeffs) {
            if (Math.abs(c) > 1e-9) {
                return c;
            }
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int power = coeffs.length - 1;
        for (double c : coeffs) {
            if (Math.abs(c) < 1e-8) {
                power--;
                continue;
            }
            if (sb.length() > 0 && c > 0) sb.append("+");
            if (power == 0) sb.append(c);
            else if (power == 1) sb.append(c + "*x");
            else sb.append(c + "*x^" + power);
            power--;
        }
        return sb.toString();
    }
}
