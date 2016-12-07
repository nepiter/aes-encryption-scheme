package org.aes;

import org.aes.cipher.Cipher;
import org.aes.keyMaster.KeyGenerator;

import java.util.Arrays;

/*
This is the main class.
- This class will generate the key with size specified by user (128-bit, 192-bit or 256-bit keys) using KeyGenerator class
- It will pass plain text bytes and key bytes to the Cipher
- While calling encryption or decryption function it will also be passing mode selected by user
 */
public class Main {
    public static void main(String[] args) {
        Cipher.Mode encryptionMode = Cipher.Mode.EC13;
        byte[] plainText = "This is a plaintext.".getBytes();
        KeyGenerator keyGenerator = new KeyGenerator(32);
        byte[] key = keyGenerator.getInstance();

        Cipher cipher = new Cipher(plainText, key);
        cipher.encrypt(encryptionMode);
        cipher.decrypt(encryptionMode);

        System.out.println("---------------------------------------------------------");
        System.out.println("Original content: " + new String(plainText));
        System.out.println("Encrypted content: " + new String(cipher.getEncryptedContent()));
        System.out.println("Decrypted content: " + new String(cipher.getOutputContentBytes()));
        System.out.println("---------------------------------------------------------");
        System.out.println("Original content length: " + plainText.length);
        System.out.println("Decrypted content length: " + cipher.getOutputContentBytes().length);
        System.out.println("---------------------------------------------------------");
    }
}
