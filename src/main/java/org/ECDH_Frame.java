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
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JSlider;    
public class ECDH_Frame extends JFrame
{


private String Alice_key;
private String Bob_key;
private JTextField textField;
private JTextField textField_1;
private JTextField textField_2;
private JTextField textField_3;
private JTextField textField_4;
private JTextField textField_5;
private JTextField textField_6;

    public ECDH_Frame()
    {
        super("Welcome to 'ECDH'");
        setBounds(100, 100, 421, 601);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);    
        // PlainText Panel
       
        
        JPanel plainPanel = new JPanel();
        plainPanel.setBounds(0, 0, 404, 164);
        getContentPane().add(plainPanel);
        plainPanel.setLayout(null);
        
        JLabel lblProvideAPrime = new JLabel("Provide a Prime Number:");
        lblProvideAPrime.setBounds(45, 38, 157, 25);
        plainPanel.add(lblProvideAPrime);
        
        JLabel lblProvideValueOf = new JLabel("Provide Value of a:");
        lblProvideValueOf.setBounds(45, 83, 116, 20);
        plainPanel.add(lblProvideValueOf);
        
        JLabel lblProvideValueOf_1 = new JLabel("Provide Value of b:");
        lblProvideValueOf_1.setBounds(45, 139, 128, 14);
        plainPanel.add(lblProvideValueOf_1);
        
        textField = new JTextField();
        textField.setBounds(225, 40, 144, 20);
        plainPanel.add(textField);
        textField.setColumns(10);
       
        textField_1 = new JTextField();
        textField_1.setBounds(225, 83, 144, 20);
        plainPanel.add(textField_1);
        textField_1.setColumns(10);
        
        textField_2 = new JTextField();
        textField_2.setBounds(225, 133, 144, 20);
        plainPanel.add(textField_2);
        textField_2.setColumns(10);
        //Decipher Panel
        JPanel decipherPanel = new JPanel();
        decipherPanel.setBounds(0, 285, 404, 277);
        getContentPane().add(decipherPanel);
        decipherPanel.setLayout(null);
        
        JLabel lblPrivateKeyFor = new JLabel("Private key for Alice:");
        lblPrivateKeyFor.setBounds(45, 31, 130, 21);
        decipherPanel.add(lblPrivateKeyFor);
        
        JLabel lblPrivateKeyFor_1 = new JLabel("Private Key for Bob:");
        lblPrivateKeyFor_1.setBounds(45, 74, 130, 21);
        decipherPanel.add(lblPrivateKeyFor_1);
        
        JLabel lblResult = new JLabel("Result:");
        lblResult.setVerticalAlignment(SwingConstants.TOP);
        lblResult.setBounds(45, 159, 46, 14);
        decipherPanel.add(lblResult);
        
        JLabel lblErrorMessage = new JLabel("Error Message:");
        lblErrorMessage.setBounds(45, 208, 115, 21);
        decipherPanel.add(lblErrorMessage);
        
               
        JButton btnNewButton = new JButton("Browse a File");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		try {
                	getFile a = new getFile();
                    
                    Alice_key = a.select();
                    //CipherPane.setText(text1);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } 
        	}
        });
        btnNewButton.setBounds(225, 30, 145, 23);
        decipherPanel.add(btnNewButton);
        
        JButton btnBrowse = new JButton("Browse a File");
        btnBrowse.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
                	getFile a = new getFile();
                    
                    Bob_key = a.select();
                    //CipherPane.setText(text1);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } 
        		        	}
        });
        btnBrowse.setBounds(225, 73, 145, 23);
        decipherPanel.add(btnBrowse);
        
        textField_4 = new JTextField();
        textField_4.setBounds(226, 159, 144, 20);
        decipherPanel.add(textField_4);
        textField_4.setColumns(10);
        
        textField_5 = new JTextField();
        
        textField_5.setBounds(226, 208, 144, 20);
        decipherPanel.add(textField_5);
        textField_5.setColumns(10);
        
        JButton button = new JButton("Submit");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       // Call your function after browsing alice key and bob key. Catch the return in String Final_Result;
        		
        		textField_4.setText(Final_Result);
        		textField_5.setText(Error_msg);   
///////////////////////////         		
        		
        	}
        });
        button.setBounds(161, 113, 89, 23);
        decipherPanel.add(button);
        
        JLabel lblTheEcPoint = new JLabel("X Point :");
        lblTheEcPoint.setBounds(45, 175, 159, 26);
        getContentPane().add(lblTheEcPoint);
        
        textField_3 = new JTextField();
        textField_3.setBounds(224, 178, 144, 20);
        getContentPane().add(textField_3);
        textField_3.setColumns(10);
        
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(157, 251, 89, 23);
        getContentPane().add(btnSubmit);
        
        JLabel lblNewLabel = new JLabel("Y Point :");
        lblNewLabel.setBounds(45, 228, 46, 14);
        getContentPane().add(lblNewLabel);
        
        textField_6 = new JTextField();
        textField_6.setBounds(224, 220, 144, 20);
        getContentPane().add(textField_6);
        textField_6.setColumns(10);
        btnSubmit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int Prime_number = Integer.parseInt(textField.getText());
        		int a = Integer.parseInt(textField_1.getText());
        		int b = Integer.parseInt(textField_2.getText());
           		int X_point = Integer.parseInt(textField_3.getText());
        		int Y_point = Integer.parseInt(textField_6.getText());
        		 		
        		
        	}
        });
        setVisible(true);
        
        
    }
    
    public static void main(String args[])
    {
        new ECDH_Frame();
    }
}
