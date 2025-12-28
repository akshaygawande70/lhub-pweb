package api.ntuc.common.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * The Class AESEncryptUtil.
 */
public class AESEncryptUtil {
	
	private static Log log = LogFactoryUtil.getLog(AESEncryptUtil.class);
	
	private static final String ALGO = "AES/ECB/PKCS5Padding";
	private static final String AES = "AES";
	private static final String CHARSET_NAME = "UTF-8";
	private static final String HASH = "SHA-1";
	private static SecretKeySpec SECRET_KEY;
	private static byte[] KEY;
	
	private AESEncryptUtil() {
		 throw new IllegalStateException("Utility class");
	}
	
	public static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			KEY = myKey.getBytes(CHARSET_NAME);
			sha = MessageDigest.getInstance(HASH);
			KEY = sha.digest(KEY);
			KEY = Arrays.copyOf(KEY, 16);
			SECRET_KEY = new SecretKeySpec(KEY, AES);
		} catch (NoSuchAlgorithmException e) {
			log.info("NoSuchAlgorithmException error : "+e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.info("UnsupportedEncodingException error : "+e.getMessage());
		}
	}
	
	public static String encrypt(String strToEncrypt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(CHARSET_NAME)));
		} catch (Exception e) {
			log.error("Error while encrypting: " + e.getMessage());
		}
		return null;
	}

	public static String decrypt(String strToDecrypt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			log.error("Error while decrypting: " + e.getMessage());
		}
		return null;
	}
    
}
