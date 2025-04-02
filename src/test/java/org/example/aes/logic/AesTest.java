package org.example.aes.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AesTest {
//39 02 dc 19
//25 dc 11 6a
//84 09 85 0b
//1d fb 97 32


    @Test
    void encryptionAndDecryptionTest() {
        byte[] inputData = new byte[] {
                (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
                (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77,
                (byte)0x88, (byte)0x99, (byte)0xAA, (byte)0xBB,
                (byte)0xCC, (byte)0xDD, (byte)0xEE, (byte)0xFF, (byte)0x12
        };
        byte[] key = new byte[] {
                (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03,
                (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
                (byte)0x08, (byte)0x09, (byte)0x0A, (byte)0x0B,
                (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
                (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13,
                (byte)0x14, (byte)0x15, (byte)0x16, (byte)0x17,
                //00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17
        };
        Aes aes = new Aes(KeyParam.SIZE_192, inputData, key);
        byte[] res = aes.doEncryption();
        Aes aes1 = new Aes(KeyParam.SIZE_192, res, key);
        byte[] decrypted = aes1.doDecryption();
        assertArrayEquals(inputData, decrypted);
    }

    @Test
    void encryptionTest192() {
        byte[] inputData = new byte[] {
                (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
                (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77,
                (byte)0x88, (byte)0x99, (byte)0xAA, (byte)0xBB,
                (byte)0xCC, (byte)0xDD, (byte)0xEE, (byte)0xFF
        };
        byte[] key = new byte[] {
                (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03,
                (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
                (byte)0x08, (byte)0x09, (byte)0x0A, (byte)0x0B,
                (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
                (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13,
                (byte)0x14, (byte)0x15, (byte)0x16, (byte)0x17,
                //00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17
        };
        Aes aes = new Aes(KeyParam.SIZE_192, inputData, key);
        byte[] res = aes.doEncryption();
        for (int i = 0; i < res.length; i++) {
//            System.out.print(res[i] + " ");
            System.out.printf("%02X ", res[i]);
        }

    }

    @Test
    void encryptionTest256() {
        byte[] inputData = new byte[] {
                (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
                (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77,
                (byte)0x88, (byte)0x99, (byte)0xAA, (byte)0xBB,
                (byte)0xCC, (byte)0xDD, (byte)0xEE, (byte)0xFF
        };
        byte[] key = new byte[] {
                (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03,
                (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07,
                (byte)0x08, (byte)0x09, (byte)0x0A, (byte)0x0B,
                (byte)0x0C, (byte)0x0D, (byte)0x0E, (byte)0x0F,
                (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13,
                (byte)0x14, (byte)0x15, (byte)0x16, (byte)0x17,
                (byte)0x18, (byte)0x19, (byte)0x1a, (byte)0x1b,
                (byte)0x1c, (byte)0x1d, (byte)0x1e, (byte)0x1f
        };
        Aes aes = new Aes(KeyParam.SIZE_256, inputData, key);
        byte[] res = aes.doEncryption();
        for (int i = 0; i < res.length; i++) {
//            System.out.print(res[i] + " ");
            System.out.printf("%02X ", res[i]);
        }

    }

    @Test
    void encryptionTest128() {
//        byte[] inputData = new byte[] {
//                (byte)0x32, (byte)0x43, (byte)0xf6, (byte)0xa8,
//                (byte)0x88, (byte)0x5a, (byte)0x30, (byte)0x8d,
//                (byte)0x31, (byte)0x31, (byte)0x98, (byte)0xa2,
//                (byte)0xe0, (byte)0x37, (byte)0x07, (byte)0x34
//        };
        byte[] inputData = new byte[] {
                (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
                (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77,
                (byte)0x88, (byte)0x99, (byte)0xAA, (byte)0xBB,
                (byte)0xCC, (byte)0xDD, (byte)0xEE, (byte)0xFF
        };
        byte[] key = new byte[] {
                (byte)0x2B, (byte)0x7E, (byte)0x15, (byte)0x16,
                (byte)0x28, (byte)0xAE, (byte)0xD2, (byte)0xA6,
                (byte)0xAB, (byte)0xF7, (byte)0x15, (byte)0x88,
                (byte)0x09, (byte)0xCF, (byte)0x4F, (byte)0x3C
        };

        Aes aes = new Aes(KeyParam.SIZE_128, inputData, key);
        byte[] res = aes.doEncryption();
        for (int i = 0; i < res.length; i++) {
//            System.out.print(res[i] + " ");
            System.out.printf("%02X ", res[i]);
        }

    }

    @Test
    void rotWordTest() {
        byte [] word = {(byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03};
        byte[] res = Aes.rotWord(word, 3);
        for (int i = 0; i < 4; i++) {
            System.out.print(res[i]);
        }
    }

    @Test
    void sBoxTest() {
        byte [] word = {(byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03};
        byte[] res = Aes.subWord(word);
        for (int i = 0; i < 4; i++) {
            System.out.println(res[i]);
        }
    }

    @Test
    void testExpandKey() {
        byte[] inputData = new byte[] {
                (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
                (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77,
                (byte)0x88, (byte)0x99, (byte)0xAA, (byte)0xBB,
                (byte)0xCC, (byte)0xDD, (byte)0xEE, (byte)0xFF
        };
        byte[] key = new byte[] {
                (byte)0x2B, (byte)0x7E, (byte)0x15, (byte)0x16,
                (byte)0x28, (byte)0xAE, (byte)0xD2, (byte)0xA6,
                (byte)0xAB, (byte)0xF7, (byte)0x15, (byte)0x88,
                (byte)0x09, (byte)0xCF, (byte)0x4F, (byte)0x3C
        };
        byte[][] keys = new byte[(int) (4 * (10 + 1))][4];
        int j = 0;
        for (int i = 0; i < 4; i++) {
            keys[i][0] = key[j];
            keys[i][1] = key[++j];
            keys[i][2] = key[++j];
            keys[i][3] = key[++j];
            j++;
        }
        for (int i = 0; i < keys.length; i++) {
            for (int k = 0; k < 4; k++) {
//                System.out.print(keys[i][k] + " ");
                System.out.printf("%02X ", keys[i][k]);
            }
            System.out.println();

        }
        System.out.println();
        System.out.println();
        Aes aes = new Aes(KeyParam.SIZE_128, inputData, key);
        aes.expandKeys(keys);

        for (int i = 0; i < keys.length; i++) {
            System.out.print("\n" + (i+1) + ")");
            for (int k = 0; k < 4; k++) {
//                System.out.print(keys[i][k] + " ");
                System.out.printf("%02X ", keys[i][k]);
            }


        }

    }
    @Test
    void testExpandKey256() {
        byte[] inputData = new byte[] {
                (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
                (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77,
                (byte)0x88, (byte)0x99, (byte)0xAA, (byte)0xBB,
                (byte)0xCC, (byte)0xDD, (byte)0xEE, (byte)0xFF
        };
        byte[] key = new byte[] {
                (byte)0x60, (byte)0x3d, (byte)0xeb, (byte)0x10,
                (byte)0x15, (byte)0xca, (byte)0x71, (byte)0xbe,
                (byte)0x2b, (byte)0x73, (byte)0xae, (byte)0xf0,
                (byte)0x85, (byte)0x7d, (byte)0x77, (byte)0x81,
                (byte)0x1f, (byte)0x35, (byte)0x2c, (byte)0x07,
                (byte)0x3b, (byte)0x61, (byte)0x08, (byte)0xd7,
                (byte)0x2d, (byte)0x98, (byte)0x10, (byte)0xa3,
                (byte)0x09, (byte)0x14, (byte)0xdf, (byte)0xf4
        };

        //60 3d eb 10 15 ca 71 be 2b 73 ae f0 85 7d 77 81
                //1f 35 2c 07 3b 61 08 d7 2d 98 10 a3 09 14 df f4

        byte[][] keys = new byte[(int) (8 * (14 + 1))][4];
        for (int i = 0; i < 8; i++) {
            keys[i][0] = key[i * 4];
            keys[i][1] = key[i * 4 + 1];
            keys[i][2] = key[i * 4 + 2];
            keys[i][3] = key[i * 4 + 3];
        }

        for (int i = 0; i < keys.length; i++) {
            for (int k = 0; k < 4; k++) {
//                System.out.print(keys[i][k] + " ");
                System.out.printf("%02X ", keys[i][k]);
            }
            System.out.println();

        }
        System.out.println();
        System.out.println();
        Aes aes = new Aes(KeyParam.SIZE_256, inputData, key);
        keys = aes.expandKeys(keys);

        for (int i = 0; i < keys.length; i++) {
            System.out.print("\n" + (i) + ")");
            for (int k = 0; k < 4; k++) {
//                System.out.print(keys[i][k] + " ");
                System.out.printf("%02X ", keys[i][k]);
            }


        }

    }


}