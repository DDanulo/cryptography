package org.example.aes.logic;

import java.util.Arrays;

public class Helper {
    public static byte[] addPadding (byte[] input){
        int length = input.length;
        int m = length % 16;
        if (m == 0){
            return input;
        }
        int a = 16-m;
        byte[] output = new byte[length + a];

        for (int i = 0; i < output.length; i++) {
            if (i < length){
                output[i] = input[i];
            }else {
                output[i] = 0;
            }
        }
        output[output.length-1] = (byte) a;
        return output;
    }

    public static byte[] removePadding(byte[] input){
        int length = input.length;
        if (length%16 != 0){
            throw new RuntimeException();
        }
        byte[] lastWord = Arrays.copyOfRange(input, length-16, length);
        int numberOfZeros = lastWord[15];
        for (int i = numberOfZeros-1; i >= length-numberOfZeros; i--) {
            if(lastWord[i] != 0) {
                return input;
            }
        }
        byte[] result = new byte[length-numberOfZeros];
        System.arraycopy(input, 0, result, 0, result.length);
        return result;
    }
}
