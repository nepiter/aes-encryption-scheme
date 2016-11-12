package org.aes.keyMaster;

import com.google.common.primitives.UnsignedBytes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExtendKeyTest {
    private static final byte[] key =
            {
                    UnsignedBytes.parseUnsignedByte("2b", 16),
                    UnsignedBytes.parseUnsignedByte("7e", 16),
                    UnsignedBytes.parseUnsignedByte("15", 16),
                    UnsignedBytes.parseUnsignedByte("16", 16),
                    UnsignedBytes.parseUnsignedByte("28", 16),
                    UnsignedBytes.parseUnsignedByte("ae", 16),
                    UnsignedBytes.parseUnsignedByte("d2", 16),
                    UnsignedBytes.parseUnsignedByte("a6", 16),
                    UnsignedBytes.parseUnsignedByte("ab", 16),
                    UnsignedBytes.parseUnsignedByte("f7", 16),
                    UnsignedBytes.parseUnsignedByte("15", 16),
                    UnsignedBytes.parseUnsignedByte("88", 16),
                    UnsignedBytes.parseUnsignedByte("09", 16),
                    UnsignedBytes.parseUnsignedByte("cf", 16),
                    UnsignedBytes.parseUnsignedByte("4f", 16),
                    UnsignedBytes.parseUnsignedByte("3c", 16),
            };

    @Test
    public void foo() {
        ExtendKey.keyExpansion(key, key.length/4);

        for (int i = 0; i < 44; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(UnsignedBytes.toString(ExtendKey.keyExpandedWords[i][j], 16));
            }
            System.out.println();
        }

        assertEquals("2b", UnsignedBytes.toString(ExtendKey.keyExpandedWords[0][0], 16));
    }
}