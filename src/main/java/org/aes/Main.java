package org.aes;

import org.aes.cipher.Cipher;

public class Main {
    public static void main(String[] args) {
        String plainText = "this is for test";
        System.out.println("Original content: " + plainText);

        Cipher cipher = new Cipher(plainText);
    }
}
