package org.aes.gui;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* Class for getting the file
 *
 * Cryptography Project Fall 2016
 *
 * This contains a method 'SELECT' that will be used to select the file that we will encrypted using both RAS
 * as well as AES.
 *
 * */
public class GetFile {
	public String select() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
	    //JFileChooser fileChooser = new JFileChooser();
	    
	    JFileChooser fileChooser = null;
	    LookAndFeel previousLF = UIManager.getLookAndFeel();
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    fileChooser = new JFileChooser();
	    UIManager.setLookAndFeel(previousLF);
	    String content = "";
	    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	    int result = fileChooser.showOpenDialog(null);
	    if (result == JFileChooser.APPROVE_OPTION) {
	    	//scan.useDelimiter("\\Z");  ---- check the end of the file Sample.txt.txt 
	        //content = new Scanner(fileChooser.getSelectedFile()).nextLine();   --- reads only one line but we want the entire content of the file. 
	    	FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile());
	    	byte[] data = new byte[(int) fileChooser.getSelectedFile().length()];
	    	fis.read(data);
	    	fis.close();
	    	content = new String(data, "UTF-8");
	    }
	    else{
	    	System.out.println("File not choosen! You dumb ASS!");
	    	content = null;
	    }	    
	    return content;
	}	
}