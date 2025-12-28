package api.ntuc.common.util;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.codec.binary.Base64;

public class EmailUtil {
	private static final Log log = LogFactoryUtil.getLog(EmailUtil.class);
	
	public static void sendMailWithHTMLFormat(String fromName, String fromAddress, ArrayList<String> to, String subject, String content){
		try{
			InternetAddress fromInternetAddress = new InternetAddress(fromAddress, fromName);
			InternetAddress[] toAddresses = new InternetAddress[to.size()];
			for(int i=0;i<to.size();i++){
				toAddresses[i] = new InternetAddress(to.get(i));
			}
		
			MailMessage mailMessage = new MailMessage();
			mailMessage.setFrom(fromInternetAddress);
			mailMessage.setTo(toAddresses);
			mailMessage.setSubject(subject);
			mailMessage.setBody(content);
			mailMessage.setHTMLFormat(true);
			MailServiceUtil.sendEmail(mailMessage);
		}catch (AddressException e) {
			log.error("Error while send email : "+e.getMessage() );
		} catch (UnsupportedEncodingException e) {
			log.error("Error while send email : "+e.getMessage());
		}
	}
	
	public static void sendMailWithHTMLFormatAndAttachment(String fromName, String fromAddress, String cc, ArrayList<String> to, String subject, String content, Map<String, String> attachmentMaps){
		try{
			InternetAddress fromInternetAddress = new InternetAddress(fromAddress,fromName);
			InternetAddress ccInternetAddress = new InternetAddress(cc);
			InternetAddress[] toAddresses = new InternetAddress[to.size()];
			for(int i=0;i<to.size();i++){
				toAddresses[i] = new InternetAddress(to.get(i));
			}
			
			MailMessage mailMessage = new MailMessage();
			mailMessage.setFrom(fromInternetAddress);
			mailMessage.setCC(ccInternetAddress);
			mailMessage.setTo(toAddresses);
			mailMessage.setSubject(subject);
			mailMessage.setBody(content);
			mailMessage.setHTMLFormat(true);
			
			for(Map.Entry<String, String> attachment : attachmentMaps.entrySet()) {
				Properties properties=PortalUtil.getPortalProperties();
				String serverHome = properties.getProperty("liferay.home");
				String uploadPath = serverHome+File.separator+"uploaded"+File.separator;
				File file = new File(uploadPath+attachment.getKey());
				
				try (OutputStream stream = new FileOutputStream(file)) {
					byte[] data = Base64.decodeBase64(attachment.getValue());
					stream.write(data);
				}
				mailMessage.addFileAttachment(file);
			}
			
			MailServiceUtil.sendEmail(mailMessage);
		}catch (AddressException e) {
			log.error("Error while send email : "+e.getMessage() );
		} catch (Exception e) {
			log.error("Error while send email : "+e.getMessage() );
		}
	}
}
