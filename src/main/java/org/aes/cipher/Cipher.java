package org.aes.cipher;

import org.aes.keyMaster.ExtendKey;
import org.aes.keyMaster.KeyGenerator;

public class Cipher {
    private static final byte[] key;

    private String content;

    static {
        KeyGenerator keyGenerator = new KeyGenerator(16);
        key = keyGenerator.getInstance();
        ExtendKey.keyExpansion(key, (key.length)/4);
    }

    public Cipher(String content) {
        this.content = content;
    }

    public static byte[] getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
