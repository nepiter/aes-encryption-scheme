package org.aes.keyMaster;

import com.google.common.primitives.UnsignedBytes;
import org.aes.cipher.SBox;

import java.math.BigInteger;

/*
- Implementation of key expansion algorithm
- Expanded words for key are stored so that it can be accessed during encryption and decryption
 */
public class ExtendKey {
    private final static byte[][] roundConstant =
            {
                    {UnsignedBytes.parseUnsignedByte("01", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)},
                    {UnsignedBytes.parseUnsignedByte("02", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)},
                    {UnsignedBytes.parseUnsignedByte("04", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)},
                    {UnsignedBytes.parseUnsignedByte("08", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)},
                    {UnsignedBytes.parseUnsignedByte("10", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)},
                    {UnsignedBytes.parseUnsignedByte("20", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)},
                    {UnsignedBytes.parseUnsignedByte("40", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)},
                    {UnsignedBytes.parseUnsignedByte("80", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)},
                    {UnsignedBytes.parseUnsignedByte("1b", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)},
                    {UnsignedBytes.parseUnsignedByte("36", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16), UnsignedBytes.parseUnsignedByte("00", 16)}
            };

    public static byte[][] keyExpandedWords;

    public static void keyExpansion(byte[] key, int wordsInKey) {
        keyExpandedWords = new byte[4*(wordsInKey + 7)][4];
        for (int i = 0; i < wordsInKey; i++) {
            for (int j = 0; j < 4; j++) {
                keyExpandedWords[i][j] = key[4*i + j];
            }
        }

        int i = wordsInKey;
        while (i < keyExpandedWords.length) {
            byte[] tempWord = keyExpandedWords[i - 1];
            if (i % wordsInKey == 0) {
                tempWord = leftRotateWord(tempWord, 1);
                tempWord = SBoxSubstitutionFor(tempWord);
                tempWord = XOROfWords(tempWord, roundConstant[(i/wordsInKey) - 1]);
            } else if (wordsInKey > 6 && (i % wordsInKey == 4)) {
                tempWord = SBoxSubstitutionFor(tempWord);
            }
            keyExpandedWords[i] = XOROfWords(keyExpandedWords[i - wordsInKey], tempWord);
            ++i;
        }
    }

    private static byte[] leftRotateWord(byte[] tempWord, int numberOfTimes) {
        int length = tempWord.length;
        byte[] rotatedWord = new byte[length];
        for (int i = 0; i < length; i++) {
            rotatedWord[(length-numberOfTimes+i)%length] = tempWord[i];
        }
        return rotatedWord;
    }

    private static byte[] SBoxSubstitutionFor(byte[] tempWord) {
        byte[] substitutedWord = new byte[tempWord.length];
        for (int i = 0; i < tempWord.length; i++) {
            substitutedWord[i] = SBox.SBOX[(tempWord[i] >> 4) & 0x0f][tempWord[i] & 0x0f];
        }
        return substitutedWord;
    }

    private static byte[] XOROfWords(byte[] tempWord, byte[] roundConstantWord) {
        byte[] temp = new byte[tempWord.length];
        for (int i = 0; i < tempWord.length; i++) {
            temp[i] = (byte) (tempWord[i] ^ roundConstantWord[i]);
        }
        return temp;
    }
}
