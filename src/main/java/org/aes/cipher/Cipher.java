package org.aes.cipher;

import com.google.common.primitives.UnsignedBytes;
import org.aes.keyMaster.ExtendKey;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;

/*
- This is Cipher class which provides encryption and decryption functionalities
- It will first do the PKCS#7 padding for the content passed
- The encryption and decryption functions will run based on the mode.
 */
public class Cipher {
    private byte[] key;
    private int numberOfRounds;
    private byte[] contentBytes;
    private byte[][] contentBlock;
    private byte[] encryptedContent;
    private byte[][] encryptedContentBlock;
    private byte[] outputContentBytes;
    private byte[][] outputContentBlock;
    private byte[] initializationVector = new byte[16];

    public Cipher(byte[] content, byte[] key) {
        this.key = key;
        contentBytes = padContent(content);
        numberOfRounds = setNumberOfRounds(key.length);
        contentBlock = new byte[contentBytes.length/16][16];
        encryptedContent = new byte[contentBytes.length];
        encryptedContentBlock = new byte[contentBytes.length/16][16];
        outputContentBytes = new byte[contentBytes.length];
        outputContentBlock = new byte[contentBytes.length/16][16];
        ExtendKey.keyExpansion(key, (key.length)/4);
        initializeContentBlocks();
    }

    //PKCS#7 padding
    private byte[] padContent(byte[] content) {
        int contentLength = content.length;
        int totalBytesToPad = 16 - (contentLength % 16);
        byte[] contentWithPadding = Arrays.copyOf(content, contentLength + totalBytesToPad);
        for (int i = 0; i < totalBytesToPad; i++) {
            contentWithPadding[contentWithPadding.length - 1 - i] = UnsignedBytes.parseUnsignedByte(String.valueOf(totalBytesToPad),
                    16);
        }
        return contentWithPadding;
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

    public byte[][] getContentBlock() {
        return contentBlock;
    }

    public byte[] getEncryptedContent() {
        return encryptedContent;
    }

    public byte[][] getEncryptedContentBlock() {
        return encryptedContentBlock;
    }

    public byte[] getOutputContentBytes() {
        return outputContentBytes;
    }

    public void encrypt(String encryptionMode) {
        switch (encryptionMode) {
            case "EC13":
                for (int i = 0; i < contentBlock.length; i++) {
                    encryptionOfABlock(i, contentBlock[i]);
                }
                break;

            case "CBC":
                initializationVector = generateInitializationVector();
                for (int i = 0; i < contentBlock.length; i++) {
                    if (i == 0) {
                        contentBlock[i] = XOROfByteArrays(Arrays.copyOfRange(contentBytes, 16*i, 16* (i+1)), initializationVector);
                    } else {
                        contentBlock[i] = XOROfByteArrays(Arrays.copyOfRange(contentBytes, 16*i, 16* (i+1)),
                                Arrays.copyOfRange(encryptedContent, 16* (i-1), 16*i));
                    }
                    encryptionOfABlock(i, contentBlock[i]);
                }
                break;
        }
    }

    private void encryptionOfABlock(int blockIndex, byte[] block) {
        byte[][] state = createState(block);
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
        convertToEncryptedContentFrom(state, blockIndex);
        createEncryptedContentBlock();
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
            tempWord[i] = XOROfByteArrays(tempWord[i], ExtendKey.keyExpandedWords[round * 4 + i]);
        }
        for (int i = 0; i < 4; i++) {
            for (int j= 0; j < 4; j++) {
                state[i][j] = tempWord[j][i];
            }
        }
        return state;
    }

    private static byte[] XOROfByteArrays(byte[] tempWord, byte[] roundConstantWord) {
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
            rotatedWord[(length-numberOfTimes+i)%length] = tempWord[i];
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

    public void decrypt(String encryptionMode) {
        switch (encryptionMode) {
            case "EC13":
                for (int i = 0; i < encryptedContentBlock.length; i++) {
                    decryptionOfABlock(i, encryptedContentBlock[i]);
                }
                removePadding();
                break;

            case "CBC":
                for (int i = 0; i < encryptedContentBlock.length; i++) {
                    decryptionOfABlock(i, encryptedContentBlock[i]);
                    if (i == 0) {
                        outputContentBlock[i] = XOROfByteArrays(Arrays.copyOfRange(outputContentBytes, 16*i, 16 * (i+1)),
                                initializationVector);
                    } else {
                        outputContentBlock[i] = XOROfByteArrays(Arrays.copyOfRange(outputContentBytes, 16*i, 16* (i+1)),
                                encryptedContentBlock[i-1]);
                    }
                }
                convertBlockToContent();
                removePadding();
                break;
        }
    }

    private void decryptionOfABlock(int blockIndex, byte[] block) {
        byte[][] state = createState(block);
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
        addToContent(state, blockIndex);
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
                state[i][j] = SBox.INV_SBOX[(state[i][j] >> 4) & 0x0f][state[i][j] & 0x0f];
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
    }

    private void convertBlockToContent() {
        for (int i = 0; i < outputContentBlock.length; i++) {
            System.arraycopy(outputContentBlock[i], 0, outputContentBytes, 16*i, 16);
        }
    }

    private void removePadding() {
        int outPutContentLen = outputContentBytes.length;
        int bytesToRemove = Integer.parseInt(UnsignedBytes.toString(outputContentBytes[outPutContentLen-1], 16));
        outputContentBytes = Arrays.copyOfRange(outputContentBytes, 0, outPutContentLen - bytesToRemove);
    }

    private byte[] generateInitializationVector() {
        String ivStr = RandomStringUtils.random(32, "0123456789abcdef");
        byte[] iv = convertToByteForm(ivStr);
        return iv;
    }

    private byte[] convertToByteForm(String keyString) {
        byte[] key = new byte[keyString.length()/2];
        for (int i = 0; i < keyString.length()/2; i++) {
            key[i] = UnsignedBytes.parseUnsignedByte(keyString.substring(i, i + 2), 16);
        }
        return key;
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