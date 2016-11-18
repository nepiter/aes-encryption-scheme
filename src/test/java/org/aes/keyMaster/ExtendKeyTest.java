package org.aes.keyMaster;

import com.google.common.primitives.UnsignedBytes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExtendKeyTest {
    private static final byte[] key =
            {
                    UnsignedBytes.parseUnsignedByte("8e", 16),
                    UnsignedBytes.parseUnsignedByte("73", 16),
                    UnsignedBytes.parseUnsignedByte("b0", 16),
                    UnsignedBytes.parseUnsignedByte("f7", 16),
                    UnsignedBytes.parseUnsignedByte("da", 16),
                    UnsignedBytes.parseUnsignedByte("0e", 16),
                    UnsignedBytes.parseUnsignedByte("64", 16),
                    UnsignedBytes.parseUnsignedByte("52", 16),
                    UnsignedBytes.parseUnsignedByte("c8", 16),
                    UnsignedBytes.parseUnsignedByte("10", 16),
                    UnsignedBytes.parseUnsignedByte("f3", 16),
                    UnsignedBytes.parseUnsignedByte("2b", 16),
                    UnsignedBytes.parseUnsignedByte("80", 16),
                    UnsignedBytes.parseUnsignedByte("90", 16),
                    UnsignedBytes.parseUnsignedByte("79", 16),
                    UnsignedBytes.parseUnsignedByte("e5", 16),
                    UnsignedBytes.parseUnsignedByte("62", 16),
                    UnsignedBytes.parseUnsignedByte("f8", 16),
                    UnsignedBytes.parseUnsignedByte("ea", 16),
                    UnsignedBytes.parseUnsignedByte("d2", 16),
                    UnsignedBytes.parseUnsignedByte("52", 16),
                    UnsignedBytes.parseUnsignedByte("2c", 16),
                    UnsignedBytes.parseUnsignedByte("6b", 16),
                    UnsignedBytes.parseUnsignedByte("7b", 16)
            };

    @Test
    public void foo() {
        ExtendKey.keyExpansion(key, key.length/4, 12);

        for (int i = 0; i < 52; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(UnsignedBytes.toString(ExtendKey.keyExpandedWords[i][j], 16));
            }
            System.out.println();
        }
        System.out.println(ExtendKey.keyExpandedWords[51].length);

        assertEquals("8e", UnsignedBytes.toString(ExtendKey.keyExpandedWords[0][0], 16));
    }
}