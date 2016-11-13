package org.aes.cipher;

import com.google.common.primitives.UnsignedBytes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CipherTest {
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
}