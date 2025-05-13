package org.example.aes.logic;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ElGamalTest {
    @Test
    void signAndVerify_ok() {
        ElGamal eg = new ElGamal(512);
        String msg = "Hello, ElGamal!";

        ElGamal.Signature sig = eg.sign(msg);
        assertTrue(eg.verify(msg, sig), "Підпис має бути дійсним");
    }

    @Test
    void verifyFails_onModifiedMessage() {
        ElGamal eg = new ElGamal(512);
        String msg = "Original";
        ElGamal.Signature sig = eg.sign(msg);

        assertFalse(eg.verify("Original.", sig),
                "Підпис не повинен пройти для зміненого тексту");
    }

    @Test
    void verifyFails_onTamperedSignature() {
        ElGamal eg = new ElGamal(512);
        String msg = "Test data";
        ElGamal.Signature sig = eg.sign(msg);

        // Робимо shallow-копію з іншим s1
        BigInteger fakeR = sig.s1().add(BigInteger.ONE).mod(eg.p);
        ElGamal.Signature forged = new ElGamal.Signature(fakeR, sig.s2());

        assertFalse(eg.verify(msg, forged),
                "Підроблений підпис не повинен валідуватися");
    }

    @Test
    void verifyFails_onKeysChanged() {
        ElGamal eg = new ElGamal(2048);
        String msg = "Test data";
        ElGamal.Signature sig = eg.sign(msg);

        // Робимо shallow-копію з іншим s1
        eg.generateKeys(2048);

        assertFalse(eg.verify(msg, sig),
                "Підроблений підпис не повинен валідуватися");
    }
}