package me.dommi2212.fileprotect;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

	public static byte[] encode(byte[] input, String pass) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher c = Cipher.getInstance("Blowfish");
		Key k = new SecretKeySpec(pass.getBytes(), "Blowfish");
		c.init(Cipher.ENCRYPT_MODE, k);
		return c.doFinal(input);
	}
	
	public static byte[] decode(byte[] input, String pass) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher c = Cipher.getInstance("Blowfish");
		Key k = new SecretKeySpec(pass.getBytes(), "Blowfish");
		c.init(Cipher.DECRYPT_MODE, k);

		return c.doFinal(input);		
	}

}
