package com.thinksmart.calculus.limit;

public class SyntheticDivision {

    /**
     * Divides a polynomial by (x - a) using synthetic division
     * Returns the quotient polynomial
     */
    public static Polynomial divide(Polynomial poly, double a) {
        double[] coeffs = poly.coeffs;
        int n = coeffs.length;

        // Quotient will have degree n-1
        double[] quotient = new double[n - 1];
        double carry = coeffs[0]; // start with leading coefficient

        for (int i = 1; i < n; i++) {
            quotient[i - 1] = carry;
            carry = coeffs[i] + carry * a;
        }

        // carry is remainder (should be 0 if (x - a) is factor)
        if (Math.abs(carry) > 1e-8) {
            throw new ArithmeticException("(x - " + a + ") is not a factor. Remainder = " + carry);
        }

        return new Polynomial(quotient);
    }
}
