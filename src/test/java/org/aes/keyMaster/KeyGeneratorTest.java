package org.aes.keyMaster;

import com.google.common.primitives.UnsignedBytes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KeyGeneratorTest {
    @Test
    public void keyLengthIs16Bytes() {
        KeyGenerator keyGenerator = new KeyGenerator(16);
        byte[] key = keyGenerator.getInstance();

        assertEquals(16, key.length);
    }

    @Test
    public void verifyBytesAreProper() {
        String keyString = "2b7e151628aed2a6abf7158809cf4f3c";
        byte[] key = new byte[16];

        for (int i = 0; i < 16; i++) {
            key[i] = UnsignedBytes.parseUnsignedByte(keyString.substring(i, i+2), 16);
        }

        assertEquals("101011", UnsignedBytes.toString(key[0], 2));
    }
}