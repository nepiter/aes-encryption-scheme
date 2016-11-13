package org.aes.cipher;

import com.google.common.primitives.UnsignedBytes;
import org.aes.keyMaster.ExtendKey;
import org.aes.keyMaster.KeyGenerator;

import java.math.BigInteger;

public class Cipher {
    private static final byte[] key;

    private String content;
    private int wordsInkey;
    private int numberOfRounds;
    private int numberOfColummnsInState;
    private byte[] contentBytes;
    private byte[][] contentBlock;
    private byte[] encryptedContent;

    static {
        KeyGenerator keyGenerator = new KeyGenerator(16);
        key = keyGenerator.getInstance();
        ExtendKey.keyExpansion(key, (key.length)/4);
    }

    public Cipher(String content) {
        this.content = content;
        contentBytes = new byte[content.length()];
        contentBlock = new byte[content.length()/16][16];
        initializeContentBlocks();
    }

    private void initializeContentBlocks() {
        for (int i = 0; i < content.length(); i++) {
            contentBytes[i] = UnsignedBytes.parseUnsignedByte(content.substring(i, i+1), 16);
        }
        int index = 0;
        for (int i = 0; i < contentBytes.length/16; i++) {
            System.arraycopy(contentBytes, index, contentBlock[i], 0, 16);
            index += 16;
        }
    }

    public static byte[] getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }

    public byte[][] getContentBlock() {
        return contentBlock;
    }

    public void encrypt() {
        for (int i = 0; i < contentBlock.length; i++) {
            byte[][] state = createState(contentBlock[i]);
            int round = 0;
            state = addRoundKey(state, round);

            for (round = 1; round < 9; round++) {
                state = subBytes(state);
                state = shiftRows(state);
//                state = mixColumns(state);
                state = addRoundKey(state, round);
            }
            state = subBytes(state);
            state = shiftRows(state);
            state = addRoundKey(state, round);
//            encryptedContent = convertToEncryptedContentFrom(state);
        }
    }

    private byte[][] createState(byte[] inputBlock) {
        byte[][] s = new byte[4][4];
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < 4; j++) {
                s[i][j] = inputBlock[i + 4*j];
            }
        }
        return s;
    }

    private byte[][] addRoundKey(byte[][] state, int round) {
        byte[][] tempWord = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tempWord[i][j] = state[j][i];
            }
            tempWord[i] = XOROfWords(tempWord[i], ExtendKey.keyExpandedWords[round * 4 + i]);
        }
        for (int i = 0; i < 4; i++) {
            for (int j= 0; j < 4; j++) {
                state[i][j] = tempWord[j][i];
            }
        }
        return state;
    }

    private static byte[] XOROfWords(byte[] tempWord, byte[] roundConstantWord) {
        BigInteger firstWord = new BigInteger(tempWord);
        BigInteger secondWord = new BigInteger(roundConstantWord);
        return firstWord.xor(secondWord).toByteArray();
    }

    private byte[][] subBytes(byte[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = SBox.SBOX[(state[i][j] >> 4) & 0x0f][state[i][j] & 0x0f];
            }
        }
        return state;
    }

    private byte[][] shiftRows(byte[][] state) {
        for (int i = 0; i < 4; i++) {
            state[i] = leftRotateWord(state[i], i);
        }
        return state;
    }

    private static byte[] leftRotateWord(byte[] tempWord, int numberOfTimes) {
        int length = tempWord.length;
        byte[] rotatedWord = new byte[length];
        for (int i = 0; i < length; i++) {
            rotatedWord[(length-numberOfTimes+i)%length] = tempWord[i];
        }
        return rotatedWord;
    }
}
