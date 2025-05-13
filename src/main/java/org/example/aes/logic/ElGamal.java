package org.example.aes.logic;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

public class ElGamal {

    public BigInteger g, a, h, p;

    private static final Random rnd = new Random();

    public ElGamal(int bitLen) {
        generateKeys(bitLen);
    }

    public void generateKeys(int bitLen) {
        p = BigInteger.probablePrime(bitLen, rnd);
        g = new BigInteger(p.bitLength() - 1, rnd);
        a = new BigInteger(p.bitLength() - 1, rnd);
        h = g.modPow(a, p);
    }

    public Signature sign(String msg) {
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        BigInteger M = hashToBigInt(data).mod(p);

        BigInteger r, s1, s2;
        do {
            do {
                r = new BigInteger(p.bitLength() - 1, rnd);
            } while (!r.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE));

            s1 = g.modPow(r, p);
            BigInteger rInv = r.modInverse(p.subtract(BigInteger.ONE));
            s2 = rInv.multiply(M.subtract(a.multiply(s1)))
                    .mod(p.subtract(BigInteger.ONE));
        } while (s2.equals(BigInteger.ZERO));

        return new Signature(s1, s2);
    }

    public boolean verify(String msg, Signature sig) {
        if (sig.s1.signum() <= 0 || sig.s1.compareTo(p) >= 0) return false;
        if (sig.s2.signum() < 0 || sig.s2.compareTo(p.subtract(BigInteger.ONE)) >= 0) return false;

        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        BigInteger M = hashToBigInt(data).mod(p);

        BigInteger v1 = h.modPow(sig.s1, p)
                .multiply(sig.s1.modPow(sig.s2, p))
                .mod(p);
        BigInteger v2 = g.modPow(M, p);

        return v1.equals(v2);
    }

    public record Signature(BigInteger s1, BigInteger s2) {
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
