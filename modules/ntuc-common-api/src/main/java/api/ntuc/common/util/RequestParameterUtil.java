package api.ntuc.common.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;

public class RequestParameterUtil {
	
	private static Log log = LogFactoryUtil.getLog(RequestParameterUtil.class);
	
	public static String encryptParam(Object paramValue) {
		try {
			return StringUtil.bytesToHexString(paramValue.toString().getBytes());
		}catch(Exception e) {
			log.error("Error encryptParam: " + e.getMessage(), e);
		}
		
		return "";
	}
	
	public static String decryptParam(Object paramValue) {
		try {
			return new String(fromHexString(paramValue.toString()),"UTF8");
		}catch(Exception e) {
			log.error("Error decryptParam: " + e.getMessage(), e);
		}
		
		return "";
	}
	
	public static byte[] fromHexString(String s) {
		int length = s.length() / 2;
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = (byte) ((Character.digit(s.charAt(i * 2), 16) << 4)
				| Character.digit(s.charAt((i * 2) + 1), 16));
		}
		
		return bytes;
	}

}
