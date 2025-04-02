package org.example.aes.logic;

import java.util.Arrays;

public class Aes {
    private final byte[] inputData;
    private final byte[] key;
    private final int numberOfRounds;
    private final int nK;

    private static final int[] SBOX = {0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F,
            0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76, 0xCA, 0x82,
            0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C,
            0xA4, 0x72, 0xC0, 0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC,
            0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15, 0x04, 0xC7, 0x23,
            0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27,
            0xB2, 0x75, 0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52,
            0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84, 0x53, 0xD1, 0x00, 0xED,
            0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58,
            0xCF, 0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9,
            0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8, 0x51, 0xA3, 0x40, 0x8F, 0x92,
            0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
            0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E,
            0x3D, 0x64, 0x5D, 0x19, 0x73, 0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A,
            0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB, 0xE0,
            0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62,
            0x91, 0x95, 0xE4, 0x79, 0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E,
            0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08, 0xBA, 0x78,
            0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B,
            0xBD, 0x8B, 0x8A, 0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E,
            0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E, 0xE1, 0xF8, 0x98,
            0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55,
            0x28, 0xDF, 0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41,
            0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16};

    private static final int[] INV_SBOX = {0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5,
            0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB, 0x7C, 0xE3,
            0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4,
            0xDE, 0xE9, 0xCB, 0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D,
            0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E, 0x08, 0x2E, 0xA1,
            0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B,
            0xD1, 0x25, 0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4,
            0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92, 0x6C, 0x70, 0x48, 0x50,
            0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D,
            0x84, 0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4,
            0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06, 0xD0, 0x2C, 0x1E, 0x8F, 0xCA,
            0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B,
            0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF,
            0xCE, 0xF0, 0xB4, 0xE6, 0x73, 0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD,
            0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E, 0x47,
            0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E,
            0xAA, 0x18, 0xBE, 0x1B, 0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79,
            0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4, 0x1F, 0xDD,
            0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27,
            0x80, 0xEC, 0x5F, 0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D,
            0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF, 0xA0, 0xE0, 0x3B,
            0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53,
            0x99, 0x61, 0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1,
            0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D};

    public static final byte[][] rCon = {
            {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00}, // не використовується, для зручності індексації з 1
            {(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x1B, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x36, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x6C, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0xD8, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0xAB, (byte) 0x00, (byte) 0x00, (byte) 0x00},
            {(byte) 0x4D, (byte) 0x00, (byte) 0x00, (byte) 0x00}
    };


    /**
     * creates an object that will hold input data and key.
     * Also defines the nR and nK parameters of AES algorithm depending on key parameter.
     * @param keyParam which key length AES should use
     * @param inputData byte array of data to encrypt/decrypt
     * @param key byte array of key for encryption/decryption
     */
    public Aes(KeyParam keyParam, byte[] inputData, byte[] key) {
        this.inputData = Helper.addPadding(inputData);;
        this.key = key;
        numberOfRounds = switch (keyParam) {
            case SIZE_128 -> 10;
            case SIZE_192 -> 12;
            case SIZE_256 -> 14;
        };
        nK = switch (keyParam) {
            case SIZE_128 -> 4;
            case SIZE_192 -> 6;
            case SIZE_256 -> 8;
        };
    }

    /**
     * method to encrypt data
     * to use you need to create an AES object, pass the key and the input data in the constructor,
     * then call this method
     * @return byte[] array of encrypted data
     */
    public byte[] doEncryption() {
        int dataLength = inputData.length;
        double amountOfBlocks = Math.ceil((double) dataLength / 16);
        byte[][][] state = new byte[(int) amountOfBlocks][4][4];

        for (int i = 0; i < amountOfBlocks; i++) {
            for (int j = 0; j < 16; j++) {
                state[i][j % 4][j / 4] = inputData[i * 16 + j];
            }
        }

        byte[][] keys = new byte[(int) (nK * (numberOfRounds + 1))][4];
        for (int i = 0; i < nK; i++) {
            keys[i][0] = key[i * 4];
            keys[i][1] = key[i * 4 + 1];
            keys[i][2] = key[i * 4 + 2];
            keys[i][3] = key[i * 4 + 3];
        }
        keys = expandKeys(keys);
        byte[] result = new byte[dataLength];
        for (int i = 0; i < amountOfBlocks; i++) {
            state[i] = addRoundKey(state[i], keys, 0);
            for (int k = 1; k < numberOfRounds + 1; k++) {
                state[i] = subWord(state[i]);
                state[i][1] = rotWord(state[i][1], 1);
                state[i][2] = rotWord(state[i][2], 2);
                state[i][3] = rotWord(state[i][3], 3);
                if (k != numberOfRounds) {
                    state[i] = mixCol(state[i]);
                }
                state[i] = addRoundKey(state[i], keys, k);
            }
        }
        int index = 0;
        for (int i = 0; i < state.length; i++) {
            for (int k = 0; k < state[0].length; k++) {
                for (int l = 0; l < state[0][0].length; l++) {
                    result[index++] = state[i][l][k];
                }
            }
        }

        return result;
    }

    /**
     * method to decrypt data
     * to use you need to create an AES object, pass the key and the input data in the constructor,
     * then call this method
     * @return byte[] array of decryptedData
     */
    public byte[] doDecryption(){
        int dataLength = inputData.length;
        double amountOfBlocks = Math.ceil((double) dataLength / 16);
        byte[][][] state = new byte[(int) amountOfBlocks][4][4];

        for (int i = 0; i < amountOfBlocks; i++) {
            for (int j = 0; j < 16; j++) {
                state[i][j % 4][j / 4] = inputData[i * 16 + j];
            }
        }

        byte[][] keys = new byte[(int) (nK * (numberOfRounds + 1))][4];
        for (int i = 0; i < nK; i++) {
            keys[i][0] = key[i * 4];
            keys[i][1] = key[i * 4 + 1];
            keys[i][2] = key[i * 4 + 2];
            keys[i][3] = key[i * 4 + 3];
        }
        keys = expandKeys(keys);
        byte[] result = new byte[dataLength];
        for (int i = 0; i < amountOfBlocks; i++) {
            state[i] = addRoundKey(state[i], keys, numberOfRounds);
            for (int k = numberOfRounds-1; k >= 0; k--) {
                state[i][1] = rotWord(state[i][1], 3);
                state[i][2] = rotWord(state[i][2], 2);
                state[i][3] = rotWord(state[i][3], 1);
                state[i] = invSubWord(state[i]);
                state[i] = addRoundKey(state[i], keys, k);
                if (k != 0) {
                    state[i] = invMixCol(state[i]);
                }
            }
        }
        int index = 0;
        for (int i = 0; i < state.length; i++) {
            for (int k = 0; k < state[0].length; k++) {
                for (int l = 0; l < state[0][0].length; l++) {
                    result[index++] = state[i][l][k];
                }
            }
        }
        result = Helper.removePadding(result);
        return result;
    }

    private byte[][] mixCol(byte[][] input) {
        int[] result = new int[4];
        for (int j = 0; j < 4; j++) {
            result[0] = xTimes2(input[0][j]) ^ xTimes3(input[1][j]) ^ input[2][j] ^ input[3][j];
            result[1] = input[0][j] ^ xTimes2(input[1][j]) ^ xTimes3(input[2][j]) ^ input[3][j];
            result[2] = input[0][j] ^ input[1][j] ^ xTimes2(input[2][j]) ^ xTimes3(input[3][j]);
            result[3] = xTimes3(input[0][j]) ^ input[1][j] ^ input[2][j] ^ xTimes2(input[3][j]);
            for (int i = 0; i < 4; i++) {
                input[i][j] = (byte) result[i];
            }
        }
        return input;
    }

    private byte[][] invMixCol(byte[][] input) {
        int[] result = new int[4];
        for (int j = 0; j < 4; j++) {
            result[0] = xTimes14(input[0][j]) ^ xTimes11(input[1][j]) ^ xTimes13(input[2][j]) ^ xTimes9(input[3][j]);
            result[1] = xTimes9(input[0][j]) ^ xTimes14(input[1][j]) ^ xTimes11(input[2][j]) ^ xTimes13(input[3][j]);
            result[2] = xTimes13(input[0][j]) ^ xTimes9(input[1][j]) ^ xTimes14(input[2][j]) ^ xTimes11(input[3][j]);
            result[3] = xTimes11(input[0][j]) ^ xTimes13(input[1][j]) ^ xTimes9(input[2][j]) ^ xTimes14(input[3][j]);
            for (int i = 0; i < 4; i++) {
                input[i][j] = (byte) result[i];
            }
        }
        return input;
    }

    byte xTimes2(byte x) {
        int value = x & 0xff;
        int res = value << 1;
        if ((value & 0x80) != 0) {
            res = res ^ 0x1b;
        }
        return (byte) (res & 0xff);
    }

    byte xTimes3(byte x) {
        return (byte) (xTimes2(x) ^ x);
    }

    byte xTimes9(byte x) {
        return (byte) ((xTimes2(xTimes2(xTimes2(x)))) ^ x);
    }

    byte xTimes11(byte x){
        return (byte) (xTimes2((byte) (xTimes2(xTimes2(x)) ^ x)) ^ x);
    }

    byte xTimes13(byte x){
        return (byte) (xTimes2(xTimes2((byte) (xTimes2(x) ^ x))) ^ x);
    }

    byte xTimes14(byte x){
        return xTimes2((byte) (xTimes2((byte) (xTimes2(x) ^ x)) ^ x));
    }

    byte[][] addRoundKey(byte[][] state, byte[][] key, int round) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = (byte) (state[i][j] ^ key[j + round * 4][i]);
            }
        }
        return state;
    }

    public byte[][] expandKeys(byte[][] keys) {
        for (int i = nK; i < keys.length; i++) {
            byte[] iMinus1 = keys[i - 1];
            byte[] iMinusNK = keys[i - nK];
            if (i % nK == 0) {
                iMinus1 = keySchedule(iMinus1, i);
            }
            if (numberOfRounds == 14 && (i-4) % 8 == 0 ){
                iMinus1 = subWord(Arrays.copyOf(iMinus1, iMinus1.length));
            }
            for (int j = 0; j < 4; j++) {
                keys[i][j] = (byte) (iMinus1[j] ^ iMinusNK[j]);
            }
        }
        return keys;
    }

    private byte[] keySchedule(byte[] iMinus1, int i) {
        iMinus1 = rotWord(iMinus1, 1);
        subWord(iMinus1);
        for (int j = 0; j < 4; j++) {
            iMinus1[j] = (byte) (rCon[i / nK][j] ^ iMinus1[j]);
        }
        return iMinus1;
    }

    public static byte[] rotWord(byte[] input, int amount) {
        byte[] temp = new byte[input.length];
        for (int i = amount; i < input.length + amount; i++) {
            temp[i - amount] = input[i % input.length];
        }
        return temp;
    }

    public static byte[] subWord(byte[] input) {
        for (int i = 0; i < input.length; i++) {
            input[i] = (byte) SBOX[input[i] & 0xff];
        }
        return input;
    }

    public static byte[][] subWord(byte[][] input) {
        return getBytes(input, SBOX);
    }

    public static byte[][] invSubWord(byte[][] input) {
        return getBytes(input, INV_SBOX);
    }

    private static byte[][] getBytes(byte[][] input, int[] invSbox) {
        byte[][] result = new byte[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                result[i][j] = (byte) invSbox[input[i][j] & 0xff];
            }
        }
        return result;
    }


}