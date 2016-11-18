package org.aes.cipher;

import com.google.common.primitives.UnsignedBytes;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

public class CipherTest {
    private static byte[] content = {
      UnsignedBytes.parseUnsignedByte("9f", 16),
      UnsignedBytes.parseUnsignedByte("56", 16),
      UnsignedBytes.parseUnsignedByte("38", 16),
      UnsignedBytes.parseUnsignedByte("22", 16),
      UnsignedBytes.parseUnsignedByte("f4", 16),
      UnsignedBytes.parseUnsignedByte("37", 16),
      UnsignedBytes.parseUnsignedByte("3f", 16),
      UnsignedBytes.parseUnsignedByte("d5", 16),
      UnsignedBytes.parseUnsignedByte("2d", 16),
      UnsignedBytes.parseUnsignedByte("f3", 16),
      UnsignedBytes.parseUnsignedByte("86", 16),
      UnsignedBytes.parseUnsignedByte("9c", 16),
      UnsignedBytes.parseUnsignedByte("98", 16),
      UnsignedBytes.parseUnsignedByte("7a", 16),
      UnsignedBytes.parseUnsignedByte("50", 16),
      UnsignedBytes.parseUnsignedByte("a1", 16),
      UnsignedBytes.parseUnsignedByte("80", 16),
      UnsignedBytes.parseUnsignedByte("82", 16),
      UnsignedBytes.parseUnsignedByte("38", 16),
      UnsignedBytes.parseUnsignedByte("ea", 16),
      UnsignedBytes.parseUnsignedByte("af", 16),
      UnsignedBytes.parseUnsignedByte("37", 16),
      UnsignedBytes.parseUnsignedByte("aa", 16),
      UnsignedBytes.parseUnsignedByte("67", 16),
      UnsignedBytes.parseUnsignedByte("55", 16),
      UnsignedBytes.parseUnsignedByte("ed", 16),
      UnsignedBytes.parseUnsignedByte("8a", 16),
      UnsignedBytes.parseUnsignedByte("b8", 16),
      UnsignedBytes.parseUnsignedByte("4d", 16),
      UnsignedBytes.parseUnsignedByte("90", 16),
      UnsignedBytes.parseUnsignedByte("2c", 16),
      UnsignedBytes.parseUnsignedByte("70", 16)
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
        String plainText = "00112233445566778899aabbccddeeff";
        Cipher cipher = new Cipher(plainText);
        cipher.encrypt();
        byte[][] encrypted = cipher.getEncryptedContentBlock();

        assertThat(encrypted.length*16*2, equalTo(plainText.getBytes().length));
    }

    @Test
    public void validateEntireAlgo() {
        String plainText = "00112233445566778899aabbccddeeff";
        Cipher cipher = new Cipher(plainText);
        cipher.encrypt();
        cipher.decrypt();
        byte[] output = cipher.getOutputContentBytes();
        StringBuilder sb = new StringBuilder();
        for (byte b : output) {
            sb.append(UnsignedBytes.toString(b, 16));
        }

        assertThat(sb.toString(), equalTo(plainText));
    }
}