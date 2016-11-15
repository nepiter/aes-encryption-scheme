package org.aes.finiteField;

import com.google.common.primitives.UnsignedBytes;

import java.math.BigInteger;
import java.util.Objects;

public class Polynomial {
    private static final int mod = 283;
    private byte polynomial;

    public Polynomial(String polynomial) {
        this.polynomial = UnsignedBytes.parseUnsignedByte(polynomial, 16);
    }

    public byte getPolynomial() {
        return polynomial;
    }

    public void setPolynomial(String polynomial) {
        this.polynomial = UnsignedBytes.parseUnsignedByte(polynomial);
    }

    private static int modulus(byte m) {
        if (UnsignedBytes.toInt(m) < 256) {
            return m;
        }
        return UnsignedBytes.toInt(m) ^ Polynomial.mod;
    }

    public static Polynomial add(Polynomial p1, Polynomial p2) {
        BigInteger a = new BigInteger(String.valueOf(UnsignedBytes.toInt(p1.getPolynomial())));
        BigInteger b = new BigInteger(String.valueOf(UnsignedBytes.toInt(p2.getPolynomial())));
        BigInteger c = a.xor(b);
        String r = String.valueOf(Polynomial.modulus(UnsignedBytes.parseUnsignedByte(String.valueOf(c), 16)));
        return new Polynomial(r);
    }

    public static Polynomial multiplication(Polynomial p1,Polynomial p2){
        BigInteger product = BigInteger.valueOf(0);
        BigInteger one = BigInteger.valueOf(1);
        BigInteger two = BigInteger.valueOf(2);
        BigInteger multiplicand = new BigInteger(String.valueOf(UnsignedBytes.toInt(p1.getPolynomial())));
        BigInteger multiplier = new BigInteger(String.valueOf(UnsignedBytes.toInt(p2.getPolynomial())));

        while(multiplier.signum() == 1) {
            if(Objects.equals(multiplier.mod(two), one)) {
                product = BigInteger.valueOf(Polynomial.modulus(UnsignedBytes.parseUnsignedByte(String.valueOf(product.xor(multiplicand)), 16)));
            }
            multiplier = multiplier.shiftRight(1);
            multiplicand = multiplicand.shiftLeft(1);
            multiplicand = BigInteger.valueOf(Polynomial.modulus(UnsignedBytes.parseUnsignedByte(String.valueOf(multiplicand), 16)));
        }
        String r = String.valueOf(Polynomial.modulus(UnsignedBytes.parseUnsignedByte(String.valueOf(product), 16)));
        return new Polynomial(r);
    }

    public static Polynomial quotient(Polynomial p1, Polynomial p2) {
        int multiplicand = p1.getPolynomial();
        int multiplier = p2.getPolynomial();
        int quotient = 0;

        int t1 = Polynomial.log2(multiplicand);
        int t2 = Polynomial.log2(multiplier);

        while (t1 >= t2) {
            quotient = quotient | (1 << (t1 - t2));
            multiplicand = multiplicand ^ (multiplier << (t1 - t2));
            t1 = Polynomial.log2(multiplicand);
            t2 = Polynomial.log2(multiplier);
        }

        return new Polynomial(String.valueOf(quotient));
    }

    private static int log2(int number) {
        int b = 0;

        while (number > 0) {
            number = number >> 1;
            ++b;
        }
        return b;
    }

    public static Polynomial inverse(Polynomial p) {
        Polynomial a1 = new Polynomial("1");
        Polynomial a2 = new Polynomial("0");
        Polynomial a3 = new Polynomial(String.valueOf(Polynomial.mod));

        Polynomial b1 = new Polynomial("0");
        Polynomial b2 = new Polynomial("1");
        Polynomial b3 = new Polynomial(String.valueOf(p.getPolynomial()));

        while(b3.polynomial != 1){
            Polynomial quo = Polynomial.quotient(a3,b3);
            Polynomial t1 = b1;
            Polynomial t2 = b2;
            Polynomial t3 = b3;

            b1 = Polynomial.add(a1, Polynomial.multiplication(quo,b1));
            b2 = Polynomial.add(a2, Polynomial.multiplication(quo,b2));
            b3 = Polynomial.add(a3, Polynomial.multiplication(quo,b3));

            a1 = t1;
            a2 = t2;
            a3 = t3;
        }
        return b2;
    }
}
