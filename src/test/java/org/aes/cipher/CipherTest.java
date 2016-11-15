package org.aes.cipher;

import com.google.common.primitives.UnsignedBytes;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CipherTest {
    private static final byte[] contentBytes =
            {
                    UnsignedBytes.parseUnsignedByte("00", 16),
                    UnsignedBytes.parseUnsignedByte("11", 16),
                    UnsignedBytes.parseUnsignedByte("22", 16),
                    UnsignedBytes.parseUnsignedByte("33", 16),
                    UnsignedBytes.parseUnsignedByte("44", 16),
                    UnsignedBytes.parseUnsignedByte("55", 16),
                    UnsignedBytes.parseUnsignedByte("66", 16),
                    UnsignedBytes.parseUnsignedByte("77", 16),
                    UnsignedBytes.parseUnsignedByte("88", 16),
                    UnsignedBytes.parseUnsignedByte("99", 16),
                    UnsignedBytes.parseUnsignedByte("aa", 16),
                    UnsignedBytes.parseUnsignedByte("bb", 16),
                    UnsignedBytes.parseUnsignedByte("cc", 16),
                    UnsignedBytes.parseUnsignedByte("dd", 16),
                    UnsignedBytes.parseUnsignedByte("ee", 16),
                    UnsignedBytes.parseUnsignedByte("ff", 16)
            };

    @Test
    public void validateContentBlock() {
        String plainText = "00112233445566778899aabbccddeeff";
        Cipher cipher = new Cipher(plainText);

        byte[][] contenBlock = cipher.getContentBlock();

        for (int i = 0; i < contenBlock.length; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.print(UnsignedBytes.toString(contenBlock[i][j], 16));
            }
            System.out.println();
        }

        assertEquals(plainText.length()/16, contenBlock.length);
        assertEquals(16, contenBlock[0].length);
    }

    @Test
    public void validateEncryptedContent() {
        Cipher cipher = new Cipher(Arrays.toString(contentBytes));

    }
}