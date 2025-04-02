package org.example.aes.logic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {
    @Test
    void paddingTest() {
        byte[] a = {1, 2, 3, 4, 5};
        byte[] b = Helper.addPadding(a);
        System.out.println(Arrays.toString(b));
        assertEquals(16, b.length);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte)11}, b);
        byte[] c = new byte[]{1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 1};

        b = Helper.addPadding(c);
        System.out.println(Arrays.toString(b));

        assertEquals(32, b.length);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                (byte)11, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0,0,15}, b);
        byte[] d = Helper.addPadding(b);
        System.out.println(Arrays.toString(d));
        assertArrayEquals(b, d);
    }

    @Test
    void RemovePaddingTest() {
        byte[] a = {1, 2, 3, 4, 5};
        byte[] b = Helper.addPadding(a);
        byte[] c = Helper.removePadding(b);
        System.out.println(Arrays.toString(c));
        assertArrayEquals(c, a);

        c = new byte[]{1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 1};
        b = Helper.addPadding(c);
        byte[] d = Helper.removePadding(b);
        System.out.println(Arrays.toString(d));
        assertArrayEquals(c, d);
    }
}