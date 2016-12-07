package org.aes.keyMaster;

import com.google.common.primitives.UnsignedBytes;
import org.apache.commons.lang3.RandomStringUtils;

/*
- KeyGenerator class will generate a hexadecimal key of length specified while creating key object
*/
public class KeyGenerator {
    private byte[] key;
    private int keyLengthInBytes;

    public KeyGenerator(int keyLengthInBytes) {
        this.keyLengthInBytes = keyLengthInBytes;
    }

    public byte[] getInstance() {
        String keyString = RandomStringUtils.random(keyLengthInBytes*2, "0123456789abcdef");
        key = convertToByteForm(keyString);
        return key;
    }

    private byte[] convertToByteForm(String keyString) {
        byte[] key = new byte[keyLengthInBytes];
        for (int i = 0; i < keyLengthInBytes; i++) {
            key[i] = UnsignedBytes.parseUnsignedByte(keyString.substring(i, i + 2), 16);
        }
        return key;
    }
}