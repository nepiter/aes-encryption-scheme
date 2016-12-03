package org.aes.cipher;

import com.google.common.primitives.UnsignedBytes;
import org.aes.keyMaster.ExtendKey;
import org.aes.keyMaster.KeyGenerator;

import java.util.Arrays;

public class Cipher {
    private byte[] key;
    private int numberOfRounds; //Nr
    private byte[] contentBytes;
    private byte[][] contentBlock;
    private byte[] encryptedContent;
    private byte[][] encryptedContentBlock;
    private byte[] outputContentBytes;
    private byte[] outputContentBytesWithoutPadding;
    private int originalContentLen;

    public Cipher(byte[] content, byte[] key) {
        this.key = key;
        originalContentLen = content.length;
        contentBytes = padContent(content);
        numberOfRounds = setNumberOfRounds(key.length);
        contentBlock = new byte[contentBytes.length/16][16];
        encryptedContent = new byte[contentBytes.length];
        encryptedContentBlock = new byte[contentBytes.length/16][16];
        outputContentBytes = new byte[contentBytes.length];
        outputContentBytesWithoutPadding = new byte[originalContentLen];
        ExtendKey.keyExpansion(key, (key.length)/4);
        initializeContentBlocks();
    }

    private byte[] padContent(byte[] content) {
        int totalBytesToPad = 16 - (content.length % 16);
        if (content.length % 16 != 0) {
            return Arrays.copyOf(content, content.length + totalBytesToPad);
        }
        return content;
    }

    private int setNumberOfRounds(int keyLengthInBytes) {
        switch (keyLengthInBytes) {
            case 16:
                return 10;
            case 24:
                return 12;
            case 32:
                return 14;
        }
        return 0;
    }

    private void initializeContentBlocks() {
        int index = 0;
        for (int i = 0; i < contentBytes.length/16; i++) {
            System.arraycopy(contentBytes, index, contentBlock[i], 0, 16);
            index += 16;
        }
    }

    public byte[] getKey() {
        return key;
    }

    public byte[][] getContentBlock() {
        return contentBlock;
    }

    public byte[][] getEncryptedContentBlock() {
        return encryptedContentBlock;
    }

    public byte[] getOutputContentBytes() {
        return outputContentBytes;
    }

    public byte[] getOutputContentBytesWithoutPadding() {
        return outputContentBytesWithoutPadding;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void encrypt() {
        for (int i = 0; i < contentBlock.length; i++) {
            byte[][] state = createState(contentBlock[i]);
            int round = 0;
            state = addRoundKey(state, round);

            for (round = 1; round < numberOfRounds; round++) {
                state = subBytes(state);
                state = shiftRows(state);
                state = mixColumns(state);
                state = addRoundKey(state, round);
            }
            state = subBytes(state);
            state = shiftRows(state);
            state = addRoundKey(state, round);
            convertToEncryptedContentFrom(state, i);
            createEncryptedContentBlock();
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
        byte[] temp = new byte[tempWord.length];
        for (int i = 0; i < tempWord.length; i++) {
            temp[i] = (byte) (tempWord[i] ^ roundConstantWord[i]);
        }
        return temp;
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
            rotatedWord[(length-numberOfTimes+i)%length] = tempWord[i];//(i+numberOfTimes)%length)
        }
        return rotatedWord;
    }

    private byte[][] mixColumns(byte[][] state) {
        byte[] temp = new byte[4];
        byte[][] newState = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                temp[j] = byteMultiplicationWith2(state[j][i]);
                byte t = byteMultiplicationWith3(state[(j+1)%4][i]);
                temp[j] = byteAddition(temp[j], t);
                temp[j] = byteAddition(temp[j], state[(j+2)%4][i]);
                temp[j] = byteAddition(temp[j], state[(j+3)%4][i]);
                newState[j][i] = temp[j];
            }
        }
        return newState;
    }

    private byte byteMultiplicationWith2(byte b) {
        byte temp = (byte) (b << 1);
        if (b < 0) {
            temp = (byte) (temp ^ 0x1b);
        }
        return temp;
    }

    private byte byteMultiplicationWith3(byte b) {
        byte temp = byteMultiplicationWith2(b);
        return (byte) (temp ^ b);
    }

    private byte byteAddition(byte b1, byte b2) {
        return (byte) (b1 ^ b2);
    }

    private void convertToEncryptedContentFrom(byte[][] state, int row) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                encryptedContent[(i + 4*j) + row*16] = state[i][j];
            }
        }
    }

    private void createEncryptedContentBlock() {
        int index = 0;
        for (int i = 0; i < encryptedContent.length/16; i++) {
            System.arraycopy(encryptedContent, index, encryptedContentBlock[i], 0, 16);
            index += 16;
        }
    }

    public void decrypt() {
        for (int i = 0; i < encryptedContentBlock.length; i++) {
            byte[][] state = createState(encryptedContentBlock[i]); //byte[][] state = createState(contentBlock[i]);
            int round = numberOfRounds;
            state = addRoundKey(state, round);

            for (round = numberOfRounds - 1; round > 0; round--) {
                state = invShiftRows(state);
                state = invSubBytes(state);
                state = addRoundKey(state, round);
                state = invMixColumns(state);
            }
            state = invShiftRows(state);
            state = invSubBytes(state);
            state = addRoundKey(state, round);
            addToContent(state, i);
        }
    }

    private byte[][] invShiftRows(byte[][] state) {
        for (int i = 0; i < 4; i++) {
            state[i] = rightRotateWord(state[i], i);
        }
        return state;
    }

    private static byte[] rightRotateWord(byte[] tempWord, int numberOfTimes) {
        int length = tempWord.length;
        byte[] rotatedWord = new byte[length];
        for (int i = 0; i < length; i++) {
            rotatedWord[(i+numberOfTimes)%length] = tempWord[i];
        }
        return rotatedWord;
    }

    private byte[][] invSubBytes(byte[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = SBox.INV_SBOX[(state[i][j] >> 4) & 0x0f][state[i][j] & 0x0f]; //SBox.SBOX[(state[i][j] >> 4) & 0x0f][state[i][j] & 0x0f]
            }
        }
        return state;
    }

    private byte[][] invMixColumns(byte[][] state) {
        byte[] temp = new byte[4];
        byte[][] newState = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                temp[j] = byteMultiplicationWith0e(state[j][i]);
                byte t1 = byteMultiplicationWith0b(state[(j+1)%4][i]);
                temp[j] = byteAddition(temp[j], t1);
                byte t2 = byteMultiplicationWith0d(state[(j+2)%4][i]);
                temp[j] = byteAddition(temp[j], t2);
                byte t3 = byteMultiplicationWith9(state[(j+3)%4][i]);
                temp[j] = byteAddition(temp[j], t3);
                newState[j][i] = temp[j];
            }
        }
        return newState;
    }

    private byte byteMultiplicationWith9(byte b) {
        byte temp = byteMultiplicationWith2(b);
        temp = byteMultiplicationWith2(temp);
        temp = byteMultiplicationWith2(temp);
        return byteAddition(temp, b);
    }

    private byte byteMultiplicationWith0b(byte b) {
        byte temp = byteMultiplicationWith2(b);
        temp = byteMultiplicationWith2(temp);
        temp = byteAddition(temp, b);
        temp = byteMultiplicationWith2(temp);
        return byteAddition(temp, b);
    }

    private byte byteMultiplicationWith0d(byte b) {
        byte temp = byteMultiplicationWith2(b);
        temp = byteAddition(temp, b);
        temp = byteMultiplicationWith2(temp);
        temp = byteMultiplicationWith2(temp);
        return byteAddition(temp, b);
    }

    private byte byteMultiplicationWith0e(byte b) {
        byte temp = byteMultiplicationWith2(b);
        temp = byteAddition(temp, b);
        temp = byteMultiplicationWith2(temp);
        temp = byteAddition(temp, b);
        return byteMultiplicationWith2(temp);
    }

    private void addToContent(byte[][] state, int row) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                outputContentBytes[(i + 4*j) + row*16] = state[i][j];
            }
        }
        System.arraycopy(outputContentBytes, 0, outputContentBytesWithoutPadding, 0, originalContentLen);
    }

    private void printState(byte[][] state) {
        System.out.println("------------------------------");
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                System.out.print(" " + UnsignedBytes.toString(state[j][i], 16));
            }
        }
        System.out.println();
        System.out.println("------------------------------");
    }
}
