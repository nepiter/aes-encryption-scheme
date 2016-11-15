package org.aes.cipher;

import com.google.common.primitives.UnsignedBytes;
import org.aes.finiteField.Polynomial;
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
//    private byte[] encryptedContent;
    private byte[][] encryptedContentBlock;

    private byte[] outputContentBytes;

    static {
        KeyGenerator keyGenerator = new KeyGenerator(16);
        key = keyGenerator.getInstance();
        ExtendKey.keyExpansion(key, (key.length)/4);
    }

    public Cipher(String content) {
        this.content = content;
        contentBytes = new byte[content.length()];
        contentBlock = new byte[content.length()/16][16];
        encryptedContentBlock = new byte[content.length()/16][16];
        outputContentBytes = new byte[content.length()];
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

    public byte[] getOutputContentBytes() {
        return outputContentBytes;
    }

    public void encrypt() {
        for (int i = 0; i < contentBlock.length; i++) {
            byte[][] state = createState(contentBlock[i]);
            int round = 0;
            state = addRoundKey(state, round);

            for (round = 1; round < 9; round++) {
                state = subBytes(state);
                state = shiftRows(state);
                state = mixColumns(state);
                state = addRoundKey(state, round);
            }
            state = subBytes(state);
            state = shiftRows(state);
            state = addRoundKey(state, round);
            convertToEncryptedContentFrom(state, i);
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

    private byte[][] mixColumns(byte[][] state) {
        Polynomial[] temp = new Polynomial[4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                temp[j] = new Polynomial(String.valueOf(state[i][j]));
            }
            for (int j = 0; j < 4; j++) {
                state[i][j] = Polynomial.multiplication(temp[j], new Polynomial(String.valueOf(2))).getPolynomial();
                state[i][j] = Polynomial.add(new Polynomial(String.valueOf(state[i][j])),
                        Polynomial.multiplication(new Polynomial(String.valueOf(3)), temp[(j+1)%4])).getPolynomial();
                state[i][j] = Polynomial.add(new Polynomial(String.valueOf(state[i][j])),
                        temp[(j+2)%4]).getPolynomial();
                state[i][j] = Polynomial.add(new Polynomial(String.valueOf(state[i][j])),
                        temp[(j+3)%4]).getPolynomial();
            }
        }
        return state;
    }

    private void convertToEncryptedContentFrom(byte[][] state, int row) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                encryptedContentBlock[row][i + 4*j] = state[i][j];
            }
        }
    }

    public void decrypt() {
        for (int i = 0; i < encryptedContentBlock.length; i++) {
            byte[][] state = createStateForDecryption(encryptedContentBlock[i]);
            int round = 9;
            state = addRoundKey(state, round);

            for (round = 8; round > 0; round--) {
                state = invShiftRows(state);
                state = invSubBytes(state);
                state = addRoundKey(state, round);
                state = invMixColumns(state);
            }
            state = invShiftRows(state);
            state = invSubBytes(state);
            state = addRoundKey(state, round);
//            addToContent(state);
        }
    }

    private byte[][] createStateForDecryption(byte[] outputBlock) {
        byte[][] s = new byte[4][4];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            System.arraycopy(outputBlock, index, s[i], 0, 4);
            index += 4;
        }
        return s;
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
            rotatedWord[((i+numberOfTimes)%length)] = tempWord[i];
        }
        return rotatedWord;
    }

    private byte[][] invSubBytes(byte[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = SBox.INV_SBOX[(state[i][j] >> 4) & 0x0f][state[i][j] & 0x0f];
            }
        }
        return state;
    }

    private byte[][] invMixColumns(byte[][] state) {
        Polynomial[] temp = new Polynomial[4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                temp[j] = new Polynomial(String.valueOf(state[i][j]));
            }
            for (int j = 0; j < 4; j++) {
                state[i][j] = Polynomial.multiplication(temp[j], new Polynomial(String.valueOf(0x0e))).getPolynomial();
                state[i][j] = Polynomial.add(new Polynomial(String.valueOf(state[i][j])),
                        Polynomial.multiplication(new Polynomial(String.valueOf(0x0b)), temp[(j+1)%4])).getPolynomial();
                state[i][j] = Polynomial.add(new Polynomial(String.valueOf(state[i][j])),
                        Polynomial.multiplication(new Polynomial(String.valueOf(0x0d)), temp[(j+2)%4])).getPolynomial();
                state[i][j] = Polynomial.add(new Polynomial(String.valueOf(state[i][j])),
                        Polynomial.multiplication(new Polynomial(String.valueOf(0x09)), temp[(j+3)%4])).getPolynomial();
            }
        }
        return state;
    }
}
