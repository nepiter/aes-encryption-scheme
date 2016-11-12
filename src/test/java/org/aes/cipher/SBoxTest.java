package org.aes.cipher;

import com.google.common.primitives.UnsignedBytes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class SBoxTest {
    @Test
    public void foo() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.print(SBox.SBOX[i][j] + " " + SBox.SBOX[i][j]);
            }
            System.out.println();
        }
        assertEquals(16, SBox.SBOX.length);
    }
}