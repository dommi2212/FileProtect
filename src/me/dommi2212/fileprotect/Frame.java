package me.dommi2212.fileprotect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Frame {
	
	private static JTextField file1 = new JTextField();
	private static JTextArea label = new JTextArea();
	private static String path = "";
	private static int folders = 0;
	private static int files = 0;
	public static void createFrame(){
		JFrame frame = new JFrame("FileProtect");
		
		frame.setSize(330, 210);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel fix = new JLabel("");
		fix.setBounds(0, 0, 0, 0);
		
		
		
		file1.setBounds(25, 25, 240, 30);
		
		
		JButton buttonch1 = new JButton("folder");
		buttonch1.setBounds(267, 25, 30, 30);
		buttonch1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				fChooser1();
			}
		});
		
		
		
		JPasswordField pw = new JPasswordField();
		pw.setBounds(25, 57, 120, 30);
		
		JButton crypt = new JButton("encode");
		crypt.setBounds(25, 87, 120, 30);
		crypt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == crypt){
					JButton cButton = (JButton) e.getSource();
					if(cButton.getText().equalsIgnoreCase("encode")){
						cButton.setText("decode");
					} else {
						cButton.setText("encode");
					}
				}
				
			}
		});
		
		JButton perform = new JButton("GO!");
		perform.setBounds(25, 120, 120, 30);
		perform.addActionListener(new ActionListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				folders = 0;
				files = 0;
				File DIR = new File(file1.getText().replaceAll("\"", "/"));
				
				path = DIR.getAbsolutePath() + " C";
				Long time = System.currentTimeMillis();
				if(crypt.getText().equalsIgnoreCase("encode")) {
					
					encfolder(DIR, pw.getText(), path);
					
				} else {
					defolder(DIR, pw.getText(), path);
					
				}
				System.out.println("Done!");
				System.out.println("folder :" + folders);
				System.out.println("files: " + files);
				System.out.println("Time: " + ((System.currentTimeMillis() - time) / 1000) + " sec");
				
				
			}
		});
		
		frame.add(crypt);
		frame.add(file1);
		frame.add(pw);
		frame.add(perform);
		frame.add(buttonch1);
		
		frame.add(fix);
		
		
		frame.setVisible(true);
		
	}
	
	private static void fChooser1(){
		JFrame frame = new JFrame("Input folder");
		frame.setSize(700, 500);
		frame.setResizable(false);
		
		JLabel fix = new JLabel("");
		fix.setBounds(0, 0, 0, 0);
		
		JFileChooser chooser = new JFileChooser();
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		chooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					Frame.file1.setText(chooser.getSelectedFile().getAbsolutePath());
				} catch(NullPointerException ex){
					
				}
				frame.setVisible(false);
				
			}
		});
		
		frame.add(chooser);
		
		frame.setVisible(true);
	}
	
	private static void encfolder(File DIR, String pw, String path){
		cPath nPath = new cPath(path);
		File destDir = new File(nPath.getPath());
		if(!destDir.exists()) {
			destDir.mkdir();
		}
		label.setText(label.getText() + " "+ path);
		for(File file : DIR.listFiles()) {
			if(file.isFile()){
				files = files + 1;
				
				try {
					byte[] data = Files.readAllBytes(file.toPath());
					FileOutputStream out = new FileOutputStream(destDir.getAbsolutePath() + File.separator + file.getName() + ".enc");		
					byte[] encoded = EncryptionUtil.encode(data, pw);
					out.write(encoded);
					out.flush();
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InvalidKeyException e1) {
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {
					e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {
					e1.printStackTrace();
				} catch (BadPaddingException e1) {
					e1.printStackTrace();
				}
			} else {
				cPath newPath = new cPath(nPath.getPath() + "/" + file.getName());
				folders = folders + 1;
				encfolder(file, pw, newPath.getPath());
			}
		}
	}
	
	private static void defolder(File DIR, String pw, String path){
		cPath nPath = new cPath(path);
		File destDir = new File(nPath.getPath());
		if(!destDir.exists()) {
			destDir.mkdir();
		}
		label.setText(label.getText() + " "+ path);
		for(File file : DIR.listFiles()) {
			if(file.isFile()){
				files = files + 1;
				try {
					byte[] data = Files.readAllBytes(file.toPath());
					try {
						byte[] decoded = EncryptionUtil.decode(data, pw);					
						FileOutputStream out = new FileOutputStream(destDir.getAbsolutePath() + File.separator + file.getName().replace(".enc", ""));	
						out.write(decoded);
						out.flush();
						out.close();
					} catch (InvalidKeyException | NoSuchAlgorithmException
							| NoSuchPaddingException
							| IllegalBlockSizeException e1) {
						e1.printStackTrace();
					} catch(BadPaddingException e1) {
						JOptionPane.showMessageDialog(null, file.getAbsolutePath() + " has been encoded with a different password!\nOperation has been aborted!");
						return;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}						
			} else {
				cPath newPath = new cPath(nPath.getPath() + "/" + file.getName());
				folders = folders + 1;
				defolder(file, pw, newPath.getPath());
			}
		}
	}

}
