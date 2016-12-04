package org.aes;

import org.aes.cipher.Cipher;
import org.aes.keyMaster.KeyGenerator;

import java.util.Arrays;
/*
    CBC, CFB, OFB, CTR
 */

public class Main {
    public static void main(String[] args) {
        byte[] plainText = "This is a plaintext".getBytes();
        KeyGenerator keyGenerator = new KeyGenerator(16);
        byte[] key = keyGenerator.getInstance();
        System.out.println("Original content: " + new String(plainText));

        Cipher cipher = new Cipher(plainText, key);
        cipher.encrypt();
        cipher.decrypt();
        System.out.println("Decrypted content: " + new String(cipher.getOutputContentBytesWithoutPadding()));
        System.out.println("Original conent length: " + plainText.length);
        System.out.println("Decrypted conent length: " + cipher.getOutputContentBytesWithoutPadding().length);
    }
}
