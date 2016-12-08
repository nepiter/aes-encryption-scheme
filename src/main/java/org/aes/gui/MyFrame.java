import java.awt.event.ActionEvent;    
import java.awt.event.ActionListener;
import javax.swing.JButton;    
import javax.swing.JFrame;    
import javax.swing.JLabel;    
import javax.swing.JPanel;    
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;    
public class MyFrame1 extends JFrame
{

private int BitChoice=0;
private int EncryptChoice=0; //0 for AES and 1 for RSA. 
private int PaddingChoice = 0;
private String InputString="";
private String EncryptedString="";
private String temp;
private String temp1;
private String temp2;
private JComboBox EncryptionBitcount;
private JComboBox EncryptionType;
private JComboBox PaddingType; 

    public MyFrame()
    {
        super("Encrypt_Testing");
        setBounds(100, 100, 600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);    
        // PlainText Panel
       
        
        JPanel plainPanel = new JPanel();
        plainPanel.setBounds(0, 0, 584, 339);
        getContentPane().add(plainPanel);
        plainPanel.setLayout(null);                 
        //PlainPane -> data setting -> final!
        final JTextPane PlainPane = new JTextPane();
        PlainPane.setBounds(12, 29, 560, 133);
        plainPanel.add(PlainPane);
        // PlainText Label
        JLabel PlaintextLabel = new JLabel("PlainText");
        PlaintextLabel.setBounds(12, 10, 57, 15);
        plainPanel.add(PlaintextLabel);         
        // CipherText Panel
        JPanel cipherPanel = new JPanel();
        cipherPanel.setBounds(0, 337, 584, 232);
        getContentPane().add(cipherPanel);
        cipherPanel.setLayout(null);    
        // CipherText Label
        JLabel CipherLabel = new JLabel("CipherText");
        CipherLabel.setBounds(12, 10, 70, 15);
        cipherPanel.add(CipherLabel);           
        //CipherPane -> data setting -> final!
        final JTextPane CipherPane = new JTextPane();
        CipherPane.setBounds(12, 36, 560, 133);
        cipherPanel.add(CipherPane);            
        //Decipher Panel
        JPanel decipherPanel = new JPanel();
        decipherPanel.setBounds(0, 570, 584, 171);
        getContentPane().add(decipherPanel);
        decipherPanel.setLayout(null);                  
        //Decipher Label
        JLabel DecipherLabel = new JLabel("DecipherText");
        DecipherLabel.setBounds(10, 1, 81, 24);
        decipherPanel.add(DecipherLabel);                   
        //Decipher TextPane
        final JTextPane DecipherPane = new JTextPane();
        DecipherPane.setBounds(12, 24, 560, 133);
        decipherPanel.add(DecipherPane);   

        //Decryption Button
        JButton DecryptedButton = new JButton("Decryption");
        DecryptedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String cipher,result_de;
                try{
                    cipher = CipherPane.getText();
                    ////////////////////////////////////////////////////////////////////////////////////////////
                   // result_de = (cipher);
                    result_de = "yashwanth telekula";
                    DecipherPane.setText(result_de);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        DecryptedButton.setBounds(231, 180, 130, 50);
        cipherPanel.add(DecryptedButton);
        setVisible(true);                                   
        // Encryption Button
        JButton EncryptedButton = new JButton("Encryption");
        EncryptedButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) 
        {
        	String result_en;
            try {
            	if(!InputString.equals(""))
                InputString = PlainPane.getText();
                
            	
                
            	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                result_en = "It's working"; 
                temp = (String) EncryptionBitcount.getSelectedItem();
                temp1 = (String) EncryptionType.getSelectedItem();
                temp2 = (String) PaddingType.getSelectedItem();
       
                temp =  temp.replaceAll(" ", "");
                temp = temp.substring(0,3);
                temp1 = temp1.replaceAll(" ", "");
                temp2 = temp2.replaceAll(" ", "");
                
                EncryptedString = encrypt(InputString, temp, temp1, temp2);
                EncryptPane.setText(result_de);
                //System.out.println(temp);
                //System.out.println(temp1);
                //System.out.println(temp2);
                
               } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
              }   
            }
        });
        EncryptedButton.setBounds(230, 278, 130, 50);
        plainPanel.add(EncryptedButton);            
        
        JButton BrowseButton = new JButton("Browse a File");
        
        BrowseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) 
            {
                
                try {
                	getFile a = new getFile();
                    
                    InputString = a.select();
                    //CipherPane.setText(text1);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }   
                }
            });
            
        BrowseButton.setBounds(230, 173, 130, 50);
        plainPanel.add(BrowseButton);
        
        String[] bitCount = { "128-Bit", "192-Bit", "256-Bit" };
        EncryptionBitcount = new JComboBox(bitCount);
        
        EncryptionBitcount.setToolTipText("");
        EncryptionBitcount.setFont(new Font("Tahoma", Font.PLAIN, 16));
        EncryptionBitcount.setBounds(39, 234, 94, 20);
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
        EncryptionType.setBounds(246, 234, 94, 20);
        plainPanel.add(EncryptionType);
        EncryptionType.setModel(new DefaultComboBoxModel(new String[] {"    AES", "    RSA"}));
        EncryptionType.setSelectedIndex(1);
        
        String[] TypeOfPadding = { "Type0","Type1","Type2","Type3","Type4","Type5" };
        PaddingType = new JComboBox(TypeOfPadding);
        
        PaddingType.setFont(new Font("Tahoma", Font.PLAIN, 13));
        PaddingType.setBounds(429, 234, 112, 20);
        plainPanel.add(PaddingType);
        PaddingType.setModel(new DefaultComboBoxModel(new String[] {"  EC13", "  CBC"}));
        PaddingType.setSelectedIndex(0);
    }
    
    public static void main(String args[])
    {
        new MyFrame1();
    }
}