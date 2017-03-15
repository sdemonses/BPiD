package ua.nure.bpid;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by dmitry on 14.03.17.
 */
public class RSA {

    private static final int DEFAULT_LENGTH_BIT = 256;

    private int lengthBit = 5;

    private BigInteger d;
    private BigInteger n;
    private BigInteger e;

    public RSA() {
        lengthBit = DEFAULT_LENGTH_BIT;
    }

    public RSA(int lengthBit) {
        this.lengthBit = lengthBit;
    }

    private BigInteger getLess(int length, BigInteger f) {
        BigInteger numb = getKey(length);
        while (numb.compareTo(f) >= 1) {
            numb = getKey(length);
        }
        return numb;
    }

    private BigInteger getKey(int length) {
        return BigInteger.probablePrime(length, new Random());
    }

    public BigInteger[] encrypt(String input) {
        char[] array = input.toCharArray();

        BigInteger[] arrInt = new BigInteger[array.length];

        arrInt[0] = BigInteger.valueOf(array[0]).modPow(e,n);

        for (int i = 1; i < array.length; i++) {
            arrInt[i] = BigInteger.valueOf(array[i] + array[i - 1]).mod(n).modPow(e,n);
        }

        return arrInt;
    }

    private String decrypt(BigInteger[] array) {
        array[0] = array[0].modPow(d, n);

        for (int i = 1; i < array.length; i++) {
            array[i] = array[i].modPow(d, n);
            array[i] = array[i].subtract(array[i - 1]).mod(n);
        }
        char[] chars = new char[array.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) array[i].intValue();
        }

        return new String(chars);
    }

    private void generateKeys() {
        BigInteger p = getKey(lengthBit), q = getKey(lengthBit);
        n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = getLess(lengthBit, phi);
        d = identifyDKey(phi, e);
    }

    private BigInteger identifyDKey(BigInteger phi, BigInteger e) {
        BigInteger q, r, x1, x2, y1, y2;
        BigInteger[] mas = new BigInteger[2];
        if (e.compareTo(BigInteger.ZERO) == 0) {
            mas[0] = BigInteger.ONE;
            mas[1] = BigInteger.ZERO;
            return phi;
        }

        x2 = BigInteger.ONE;
        x1 = BigInteger.ZERO;
        y2 = BigInteger.ZERO;
        y1 = BigInteger.ONE;

        while (e.compareTo(BigInteger.ZERO) > 0) {
            q = phi.divide(e);
            r = phi.subtract(q.multiply(e));
            mas[0] = x2.subtract(q.multiply(x1));
            mas[1] = y2.subtract(q.multiply(y1));
            phi = e;
            e = r;
            x2 = x1;
            x1 = mas[0];
            y2 = y1;
            y1 = mas[1];
        }

        mas[0] = x2;
        mas[1] = y2;
        if (y2.compareTo(BigInteger.ZERO) < 0)
            return (phi.subtract(y2));
        return y2;
    }

    public static void main(String[] args) {
        String input = "Hello guys";
        RSA rsa = new RSA();
        rsa.generateKeys();
        BigInteger[] arr = rsa.encrypt(input);
        System.out.println(rsa.decrypt(arr));

    }

}
