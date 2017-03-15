package ua.nure.bpid;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by dmitry on 14.03.17.
 */
public class RSA {

    private static final int lengthBit = 5;

    public static void encrypt(String input) {
        BigInteger p = getKey(lengthBit), q = getKey(lengthBit);

        BigInteger n = p.multiply(q);

        BigInteger f = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = getLess(lengthBit, f);

        BigInteger d = generateKey(f, e);

        System.out.println("P``````````" + p);
        System.out.println("Q``````````" + q);
        System.out.println("N``````````" + n);
        System.out.println("F``````````" + f);
        System.out.println("E``````````" + e);
        System.out.println("D``````````" + d);

        char[] array = input.toCharArray();

        BigInteger[] arrInt = new BigInteger[array.length];

        arrInt[0] = BigInteger.valueOf(array[0]);

        for (int i = 1; i < array.length; i++) {
            arrInt[i] = BigInteger.valueOf(array[i] + array[i - 1]).mod(n);
        }

        for (BigInteger anArrInt : arrInt) {
            System.out.println(anArrInt);
        }

        for (int i = 0; i < arrInt.length; i++) {
            arrInt[i] = arrInt[i].modPow(e, n);
        }

        System.out.println("RSA.encrypt");
        for (BigInteger anArrInt : arrInt) {
            System.out.println(anArrInt);
        }
        decrypt(arrInt, d, n);
    }

    private static BigInteger getLess(int length, BigInteger f) {
        BigInteger numb = getKey(length);
        while (numb.compareTo(f) >= 1) {
            numb = getKey(length);
        }
        return numb;
    }

    private static BigInteger getKey(int length) {
        return BigInteger.probablePrime(length, new Random());
    }

    private static BigInteger generateKey(BigInteger phi, BigInteger e) {
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
        encrypt("asd");

    }


    private static void decrypt(BigInteger[] array, BigInteger d, BigInteger n) {
        BigInteger[] bigArr = new BigInteger[array.length];
        System.out.println("RSA.decrypt");
        for (int i = 0; i < array.length; i++) {
            bigArr[i] = array[i].modPow(d, n);
        }

        for (BigInteger anArrInt : bigArr) {
            System.out.println(anArrInt);
        }

    }
}
