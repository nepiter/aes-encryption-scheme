package org.aes.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIFrame extends JFrame {
    private String plain = "";
    private String temp;
    private String temp1;
    private String temp2;
    private JComboBox EncryptionBitcount;
    private JComboBox EncryptionType;
    private JComboBox PaddingType;

    public GUIFrame() {
        super("Encrypt_Testing");
        setBounds(100, 100, 600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JPanel plainPanel = new JPanel();
        plainPanel.setBounds(0, 0, 584, 287);
        getContentPane().add(plainPanel);
        plainPanel.setLayout(null);

        final JTextPane PlainPane = new JTextPane();
        PlainPane.setBounds(12, 29, 560, 118);
        plainPanel.add(PlainPane);
        JLabel PlaintextLabel = new JLabel("PlainText");
        PlaintextLabel.setBounds(12, 10, 57, 15);
        plainPanel.add(PlaintextLabel);

        JPanel cipherPanel = new JPanel();
        cipherPanel.setBounds(0, 284, 584, 287);
        getContentPane().add(cipherPanel);
        cipherPanel.setLayout(null);
        JLabel CipherLabel = new JLabel("CipherText");
        CipherLabel.setBounds(12, 10, 70, 15);
        cipherPanel.add(CipherLabel);

        final JTextPane CipherPane = new JTextPane();
        CipherPane.setBounds(12, 27, 560, 224);
        cipherPanel.add(CipherPane);
        JPanel decipherPanel = new JPanel();
        decipherPanel.setBounds(0, 570, 584, 292);
        getContentPane().add(decipherPanel);
        decipherPanel.setLayout(null);

        JLabel DecipherLabel = new JLabel("DecipherText");
        DecipherLabel.setBounds(12, 10, 81, 24);
        decipherPanel.add(DecipherLabel);
        final JTextPane DecipherPane = new JTextPane();
        DecipherPane.setBounds(12, 36, 560, 234);
        decipherPanel.add(DecipherPane);

        JButton DecryptedButton = new JButton("Decryption");
        DecryptedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String cipher,result_de;
                try{
                    cipher = CipherPane.getText();
                    // result_de = FileEncryption.decryptString(cipher);
                    result_de = "yashwanth telekula";
                    DecipherPane.setText(result_de);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        DecryptedButton.setBounds(229, 253, 124, 23);
        cipherPanel.add(DecryptedButton);
        setVisible(true);

        JButton EncryptedButton = new JButton("Encryption");
        EncryptedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0)
            {
                //PlainArea -> GetText! -> Plain
                //call encryptString(plain)
                //result <- FileEncryption.encryptString(plain)
                //CipherPane.setText(result)-> Show!
                String result_en;
                try {
                    if(plain != "") {
                        plain = PlainPane.getText();
                    } else {
                        plain = PlainPane.getText();
                    }
                    ////////////////////////////////////////////////////////////////////////////////////////////////////
                    result_en = "It's working";  //This line should be commented and
                    //in next line required function should
                    //be used which returns result_en.

                    temp = (String) EncryptionBitcount.getSelectedItem();
                    temp1 = (String) EncryptionType.getSelectedItem();
                    temp2 = (String) PaddingType.getSelectedItem();

                    temp =  temp.replaceAll(" ", "").substring(0, 3);
                    temp1 = temp1.replaceAll(" ", "");
                    temp2 = temp2.replaceAll(" ", "");
                    System.out.println(temp);
                    System.out.println(temp1);
                    System.out.println(temp2);
//                CipherPane.setText(result_en);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        EncryptedButton.setBounds(221, 235, 147, 41);
        plainPanel.add(EncryptedButton);

        JButton BrowseButton = new JButton("Browse a File");
        BrowseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0)
            {
                String result_en;
                try {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //plain = PlainPane.getFile();
                    System.out.println("Instead we get string from file.txt");
                    // result_en = FileEncryption.encryptString(plain, );
                    result_en = "This sentence be replaced by file return";
                    CipherPane.setText(result_en);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        BrowseButton.setBounds(236, 158, 112, 23);
        plainPanel.add(BrowseButton);

        String[] bitCount = { "128-Bit", "192-Bit", "256-Bit" };
        EncryptionBitcount = new JComboBox(bitCount);
        EncryptionBitcount.setToolTipText("");
        EncryptionBitcount.setFont(new Font("Tahoma", Font.PLAIN, 15));
        EncryptionBitcount.setBounds(72, 203, 94, 20);
        plainPanel.add(EncryptionBitcount);
        EncryptionBitcount.setModel(new DefaultComboBoxModel(new String[] {"  128-Bit", "  192-Bit", "  256-Bit"}));
        EncryptionBitcount.setSelectedIndex(2);

        String[] EncryptType = { "AES", "RSA"};
        EncryptionType = new JComboBox(EncryptType);
        EncryptionType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


            }
        });

        EncryptionType.setFont(new Font("Tahoma", Font.PLAIN, 15));
        EncryptionType.setBounds(219, 203, 94, 20);
        plainPanel.add(EncryptionType);
        EncryptionType.setModel(new DefaultComboBoxModel(new String[] {"    AES", "    RSA"}));
        EncryptionType.setSelectedIndex(1);

        String[] TypeOfPadding = { "Type0","Type1","Type2","Type3","Type4","Type5" };
        PaddingType = new JComboBox(TypeOfPadding);
        PaddingType.setFont(new Font("Tahoma", Font.PLAIN, 13));
        PaddingType.setBounds(365, 204, 112, 20);
        plainPanel.add(PaddingType);
        PaddingType.setModel(new DefaultComboBoxModel(new String[] {"  EC13", "  CBC"}));
        PaddingType.setSelectedIndex(0);
    }
}
