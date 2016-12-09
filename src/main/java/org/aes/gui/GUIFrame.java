package org.aes.gui;

import org.aes.cipher.Cipher;
import org.aes.keyMaster.KeyGenerator;
import org.rsa.RSA;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;

public class GUIFrame extends JFrame {

    private String InputString = "";
    private String EncryptedString = "";
    private JComboBox<String> EncryptionBitcount;
    private JComboBox<String> EncryptionType;
    private JComboBox<String> PaddingType;
    private String bits;
    private String algorithm;
    private String mode;
    private byte[] key;
    private Cipher cipher;
    private RSA rsa;
    private BigInteger ciphertext;
    private BigInteger plaintext;
    long aesEncryptionTime;
    long aesDecryptionTime;
    long rsaEncryptionTime;
    long rsaDecryptionTime;

    public GUIFrame() {
        super("Encrypt_Testing");
        setBounds(100, 100, 600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);    

        JPanel plainPanel = new JPanel();
        plainPanel.setBounds(0, 0, 584, 339);
        getContentPane().add(plainPanel);
        plainPanel.setLayout(null);

        final JTextPane PlainPane = new JTextPane();
        PlainPane.setBounds(12, 29, 560, 133);
        plainPanel.add(PlainPane);

        JLabel PlaintextLabel = new JLabel("PlainText");
        PlaintextLabel.setBounds(12, 10, 57, 15);
        plainPanel.add(PlaintextLabel);         
        JPanel cipherPanel = new JPanel();
        cipherPanel.setBounds(0, 337, 584, 232);
        getContentPane().add(cipherPanel);
        cipherPanel.setLayout(null);

        JLabel CipherLabel = new JLabel("CipherText");
        CipherLabel.setBounds(12, 10, 70, 15);
        cipherPanel.add(CipherLabel);           

        final JTextPane CipherPane = new JTextPane();
        CipherPane.setBounds(12, 36, 560, 133);
        cipherPanel.add(CipherPane);            

        JPanel decipherPanel = new JPanel();
        decipherPanel.setBounds(0, 570, 584, 171);
        getContentPane().add(decipherPanel);
        decipherPanel.setLayout(null);                  
        JLabel DecipherLabel = new JLabel("DecipherText");
        DecipherLabel.setBounds(10, 1, 81, 24);
        decipherPanel.add(DecipherLabel);                   
        final JTextPane DecipherPane = new JTextPane();
        DecipherPane.setBounds(12, 24, 560, 133);
        decipherPanel.add(DecipherPane);   

        JButton DecryptedButton = new JButton("Decryption");
        DecryptedButton.addActionListener(arg0 -> {
            try{
                if (algorithm.equals("AES")) {
                    long aesDecryptionStartTime = System.currentTimeMillis();
                    cipher.decrypt(mode);
                    long aesDecryptionStopTime = System.currentTimeMillis();
                    aesDecryptionTime = aesDecryptionStopTime - aesDecryptionStartTime;
                    DecipherPane.setText(new String(cipher.getOutputContentBytes()));
                    System.out.println("AES Decryption time: " + aesDecryptionTime);
                } else {
                    long rsaDecryptionStartTime = System.currentTimeMillis();
                    plaintext = rsa.decrypt(ciphertext);
                    long rsaDecryptionStopTime = System.currentTimeMillis();
                    rsaDecryptionTime = rsaDecryptionStopTime - rsaDecryptionStartTime;
                    DecipherPane.setText(new String(plaintext.toByteArray()));
                    System.out.println("RSA Decryption time: " + rsaDecryptionTime);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        DecryptedButton.setBounds(231, 180, 130, 50);
        cipherPanel.add(DecryptedButton);
        setVisible(true);

        JButton EncryptedButton = new JButton("Encryption");
        EncryptedButton.addActionListener(arg0 -> {
            try {
            	if(InputString.equals("")) {
                    InputString = PlainPane.getText();
                }
                bits = ((String) EncryptionBitcount.getSelectedItem()).replaceAll(" ", "").substring(0, 3);
                algorithm = ((String) EncryptionType.getSelectedItem()).replaceAll(" ", "");
                mode = ((String) PaddingType.getSelectedItem()).replaceAll(" ", "");

                long aesEncryptionStartTime = System.currentTimeMillis();
                KeyGenerator keyGenerator = new KeyGenerator(Integer.parseInt(bits)/8);
                key = keyGenerator.getInstance();
                cipher = new Cipher(InputString.getBytes(), key);

                if(algorithm.equals("AES")) {
                    cipher.encrypt(mode);
                    long aesEncryptionStopTime = System.currentTimeMillis();
                    aesEncryptionTime = aesEncryptionStopTime - aesEncryptionStartTime;
                    CipherPane.setText(new String(cipher.getEncryptedContent()));
                    System.out.println("AES Encryption time: " + aesEncryptionTime);
                } else {
                    long rsaEncryptionStartTime = System.currentTimeMillis();
                    rsa = new RSA(InputString.getBytes().length*8);
                    plaintext = new BigInteger(InputString.getBytes());
                    ciphertext = rsa.encrypt(plaintext);
                    long rsaEncryptionStopTime = System.currentTimeMillis();
                    rsaEncryptionTime = rsaEncryptionStopTime - rsaEncryptionStartTime;
                    CipherPane.setText((new String(ciphertext.toByteArray())));
                    System.out.println("RSA Encryption time: " + rsaEncryptionTime);
                }
               } catch (Exception e) {
                    e.printStackTrace();
              }
            });
        EncryptedButton.setBounds(230, 278, 130, 50);
        plainPanel.add(EncryptedButton);            
        
        JButton BrowseButton = new JButton("Browse a File");
        BrowseButton.addActionListener(arg0 -> {
            try {
                GetFile a = new GetFile();
                InputString = a.select();
                PlainPane.setText(InputString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        BrowseButton.setBounds(230, 173, 130, 50);
        plainPanel.add(BrowseButton);
        
        String[] bitCount = { "128-Bit", "192-Bit", "256-Bit" };
        EncryptionBitcount = new JComboBox<>(bitCount);
        EncryptionBitcount.setToolTipText("");
        EncryptionBitcount.setFont(new Font("Tahoma", Font.PLAIN, 16));
        EncryptionBitcount.setBounds(39, 234, 94, 20);
        plainPanel.add(EncryptionBitcount);
        EncryptionBitcount.setModel(new DefaultComboBoxModel<>(new String[] {"  128-Bit", "  192-Bit", "  256-Bit"}));
        EncryptionBitcount.setSelectedIndex(2);
       
        String[] EncryptType = { "AES", "RSA"};
        EncryptionType = new JComboBox<>(EncryptType);
        EncryptionType.addActionListener(e -> {
        });
        EncryptionType.setFont(new Font("Tahoma", Font.PLAIN, 15));
        EncryptionType.setBounds(246, 234, 94, 20);
        plainPanel.add(EncryptionType);
        EncryptionType.setModel(new DefaultComboBoxModel<>(new String[] {"    AES", "    RSA"}));
        EncryptionType.setSelectedIndex(1);
        
        String[] TypeOfPadding = { "Type0","Type1","Type2","Type3","Type4","Type5" };
        PaddingType = new JComboBox<>(TypeOfPadding);
        PaddingType.setFont(new Font("Tahoma", Font.PLAIN, 13));
        PaddingType.setBounds(429, 234, 112, 20);
        plainPanel.add(PaddingType);
        PaddingType.setModel(new DefaultComboBoxModel<>(new String[] {"  EC13", "  CBC"}));
        PaddingType.setSelectedIndex(0);
    }
}