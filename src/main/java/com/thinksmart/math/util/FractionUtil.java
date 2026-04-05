package com.thinksmart.math.util;

public class FractionUtil {

    // ---------- FOR numerator/denominator ----------
    public static String toFraction(double numerator, double denominator) {

        if (denominator == 0) return "Undefined";

        if (isInteger(numerator) && isInteger(denominator)) {

            long num = Math.round(numerator);
            long den = Math.round(denominator);

            long gcd = gcd(num, den);

            num /= gcd;
            den /= gcd;

            if (den < 0) {
                num = -num;
                den = -den;
            }

            return num + "/" + den;
        }

        return String.valueOf(numerator / denominator);
    }

    // ---------- FOR single double value ----------
    public static String toFraction(double value) {

        if (Double.isNaN(value) || Double.isInfinite(value))
            return String.valueOf(value);

        if (isInteger(value))
            return String.valueOf((long) Math.round(value));

        double tolerance = 1e-9;
        long maxDen = 1_000_000;

        long sign = value < 0 ? -1 : 1;
        value = Math.abs(value);

        long bestNum = 1;
        long bestDen = 1;
        double bestError = Double.MAX_VALUE;

        for (long den = 1; den <= maxDen; den *= 10) {
            long num = Math.round(value * den);
            double error = Math.abs(value - (double) num / den);

            if (error < bestError) {
                bestError = error;
                bestNum = num;
                bestDen = den;
            }

            if (error < tolerance) break;
        }

        long gcd = gcd(bestNum, bestDen);
        bestNum /= gcd;
        bestDen /= gcd;

        return (sign * bestNum) + "/" + bestDen;
    }

    // ---------- HELPERS ----------
    private static boolean isInteger(double v) {
        return Math.abs(v - Math.round(v)) < 1e-9;
    }

    private static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    public static double snapToRational(double value) {

        double tolerance = 1e-3;   // 🔥 relaxed tolerance
        int maxDen = 20;

        for (int den = 1; den <= maxDen; den++) {
            double num = Math.round(value * den);
            double candidate = num / den;

            if (Math.abs(candidate - value) < tolerance) {
                return candidate;
            }
        }

        return value;
    }
}
