package org.example.aes.logic;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

public class ElGamal {

    public BigInteger g, x, y, p;

    private static final Random rnd = new Random();

    public ElGamal(int bitLen) {
        generateKeys(bitLen);
    }

    public void generateKeys(int bitLen) {
        p = BigInteger.probablePrime(bitLen, rnd);
        do {
            g = new BigInteger(p.bitLength() - 1, rnd)
                    .mod(p.subtract(BigInteger.TWO))
                    .add(BigInteger.TWO);
        } while (g.modPow(p.subtract(BigInteger.ONE).shiftRight(1), p)
                .equals(BigInteger.ONE));
        do {
            x = new BigInteger(p.bitLength() - 1, rnd);
        } while (x.signum() == 0);
        y = g.modPow(x, p);
    }

    public Signature sign(String msg) {
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        BigInteger H = hashToBigInt(data).mod(p);

        BigInteger k, r, s;
        do {
            do {
                k = new BigInteger(p.bitLength() - 1, rnd);
            } while (!k.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE));

            r = g.modPow(k, p);
            BigInteger kInv = k.modInverse(p.subtract(BigInteger.ONE));
            s = kInv.multiply(H.subtract(x.multiply(r)))
                    .mod(p.subtract(BigInteger.ONE));
        } while (s.equals(BigInteger.ZERO));

        return new Signature(r, s);
    }

    public boolean verify(String msg, Signature sig) {
        if (sig.r.signum() <= 0 || sig.r.compareTo(p) >= 0) return false;
        if (sig.s.signum() < 0 || sig.s.compareTo(p.subtract(BigInteger.ONE)) >= 0) return false;

        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        BigInteger H = hashToBigInt(data).mod(p);

        BigInteger v1 = y.modPow(sig.r, p)
                .multiply(sig.r.modPow(sig.s, p))
                .mod(p);
        BigInteger v2 = g.modPow(H, p);

        return v1.equals(v2);
    }

    public record Signature(BigInteger r, BigInteger s) {
    }

    private static BigInteger hashToBigInt(byte[] msg) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            return new BigInteger(1, sha.digest(msg));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
