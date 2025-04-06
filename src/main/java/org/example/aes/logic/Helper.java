package org.example.aes.logic;

import javax.swing.*;
import java.util.Arrays;
import java.util.Random;

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

    public static byte[] generateKey(KeyParam keyParam){
        int p = 0;
        switch (keyParam){
            case SIZE_128:
                p = 16;
                break;
            case SIZE_192:
                p = 24;
                break;
            case SIZE_256:
                p = 32;
                break;
        }
        byte[] key = new byte[p];
        for (int i = 0; i < p; i++) {
            new Random().nextBytes(key);
        }
        return key;
    }

    public static byte[] hexToBytes(String tekst)
    {
        if (tekst == null) { return null;}
        else if (tekst.length() < 2) { return null;}
        else { if (tekst.length()%2!=0)tekst+='0';
            int dl = tekst.length() / 2;
            byte[] wynik = new byte[dl];
            for (int i = 0; i < dl; i++)
            { try{
                wynik[i] = (byte) Integer.parseInt(tekst.substring(i * 2, i * 2 + 2), 16);
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Problem z przekonwertowaniem HEX->BYTE.\n Sprawdź wprowadzone dane.", "Problem z przekonwertowaniem HEX->BYTE", JOptionPane.ERROR_MESSAGE); }
            }
            return wynik;
        }
    }

    public static String bytesToHex(byte bytes[]) {
        byte rawData[] = bytes;
        StringBuilder hexText = new StringBuilder();
        String initialHex = null;
        int initHexLength = 0;

        for (int i = 0; i < rawData.length; i++) {
            int positiveValue = rawData[i] & 0x000000FF;
            initialHex = Integer.toHexString(positiveValue);
            initHexLength = initialHex.length();
            while (initHexLength++ < 2) {
                hexText.append("0");
            }
            hexText.append(initialHex);
        }
        return hexText.toString();
    }
}
