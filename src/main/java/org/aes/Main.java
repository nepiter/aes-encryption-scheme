package org.aes;

import org.aes.cipher.Cipher;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String plainText = "00112233445566778899aabbccddeeff";
        int keyLengthInBytes = 16;
        System.out.println("Original content: " + plainText);

        Cipher cipher = new Cipher(plainText, keyLengthInBytes);
        cipher.encrypt();
        cipher.decrypt();
        System.out.println("Decrypted content: " + new String(cipher.getOutputContentBytes()));
    }
}
