package me.dommi2212.fileprotect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class FileProtect {
	
	public static final int VERSION = 100;
	
	public static boolean ENCODE;
	public static String PASS;
	public static File DIR;
	
	public static void main(String[] args) {
		if(args.length != 3) {
			System.err.println("Args: Encode|Decode <Password> <Dir>");
			System.exit(-1);
		}
		if(args[0].equalsIgnoreCase("encode")) {
			ENCODE = true;
		} else if(args[0].equalsIgnoreCase("decode")) {
			ENCODE = false;
		} else {
			System.err.println("Args: Encode|Decode <Password> <Dir>");
			System.exit(-1);
		}
		PASS = args[1];
		DIR = new File(args[2]);
		if(!DIR.exists()) {
			System.err.println("This directory doesn't exist!");
			System.exit(-1);
		}
		if(!DIR.isDirectory()) {
			System.err.println("That's not a directory!");
			System.exit(-1);
		}
		if(DIR.listFiles().length == 0) {
			System.err.println("This directory is empty!");
			System.exit(-1);
		}

		if(ENCODE) {
			File destDir = new File(DIR.getAbsoluteFile().getParentFile() + File.separator + "Copy");
			if(!destDir.exists()) {
				destDir.mkdir();
			}
			
			for(File file : DIR.listFiles()) {
				try {
					byte[] data = Files.readAllBytes(file.toPath());
					FileOutputStream out = new FileOutputStream(destDir.getAbsolutePath() + File.separator + file.getName() + ".enc");		
					byte[] encoded = EncryptionUtil.encode(data, PASS);
					out.write(encoded);
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				}
			}
		} else {
			File destDir = new File(DIR.getAbsoluteFile().getParentFile() + File.separator + "CopyCopy");
			if(!destDir.exists()) {
				destDir.mkdir();
			}
			for(File file : DIR.listFiles()) {
				try {
					byte[] data = Files.readAllBytes(file.toPath());
					try {
						byte[] decoded = EncryptionUtil.decode(data, PASS);					
						FileOutputStream out = new FileOutputStream(destDir.getAbsolutePath() + File.separator + file.getName().replace(".enc", ""));	
						out.write(decoded);
						out.flush();
						out.close();
					} catch (InvalidKeyException | NoSuchAlgorithmException
							| NoSuchPaddingException
							| IllegalBlockSizeException e) {
						e.printStackTrace();
					} catch(BadPaddingException e) {
						System.err.println(file.getName() + " has been encoded with a different password!");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}						
			}
		}
		System.out.println("Done!");

	}

}
