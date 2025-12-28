package com.ntuc.webcontent.structure.panel.util;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.Validator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.internet.InternetAddress;

/**
 * The type Webcontent modify util.
 *
 * @author Dhivakar Sengottaiyan The type Webcontent modify util.
 */
public class WebcontentModifyUtil {

    /**
     * Send email.
     *
     * @param subject          the subject
     * @param fromAddress      the from address
     * @param toAddressSucess  the to address sucess
     * @param toAddressFailure the to address failure
     * @param successBody      the success body
     * @param failureBody      the failure body
     * @param message          the message
     * @param errorCode        the error code
     */
    public static void sendEmail(String subject, String fromAddress, String toAddressSucess, String toAddressFailure,
                                 String successBody, String failureBody, Object message, int errorCode) {

        log.info("mail message : " + message);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd G  HH:mm:ss z");
        Date date = new Date();
        sd.setTimeZone(TimeZone.getTimeZone("IST"));
        try {
            InternetAddress fromAddressmail = new InternetAddress(fromAddress);
            String body = successBody;
            InternetAddress[] internetAddresses = getToAddress(toAddressSucess);
            if(errorCode == 400){
                body = failureBody;
                internetAddresses = getToAddress(toAddressFailure);
            }
            MailMessage mailMessage = new MailMessage();
            mailMessage.setFrom(fromAddressmail);
            mailMessage.setTo(internetAddresses);

            mailMessage.setSubject(subject);
            body = getBody(body, message, errorCode);
            mailMessage.setBody(body);
            mailMessage.setHTMLFormat(Boolean.TRUE);

            log.info("Attempting to send mail to " + internetAddresses[0].getAddress() + " from address " + fromAddressmail + " body is " + body + " at " + sd.format(date));
            MailServiceUtil.sendEmail(mailMessage);

        } catch (Exception e) {
            log.error("Error in sending mail : " + e);
        }
    }

    private static InternetAddress[] getToAddress(String toAddress){
        List<InternetAddress> internetAddresses = null;
        try{
            String[] toAddresses = toAddress.split(";");
            for(String toAddr : toAddresses){
                if(Validator.isEmailAddress(toAddr)){
                    if (internetAddresses == null){
                        internetAddresses = new ArrayList<InternetAddress>();
                    }
                    internetAddresses.add(new InternetAddress(toAddr));
                }
            }
        }
        catch(Exception ex){
            log.error("Error while spliting toaddress : "+ex);
        }
        InternetAddress[] internetAddressArr = new InternetAddress[internetAddresses.size()];
        return internetAddresses.toArray(internetAddressArr);
    }

    /**
     * Gets print stack trace.
     *
     * @param e the e
     * @return the print stack trace
     */
    public static String getPrintStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Gets body.
     *
     * @param body      the body
     * @param message   the message
     * @param errorCode the error code
     * @return the body
     */
    public static String getBody(String body, Object message, int errorCode) {
        Map<String, String> countMap = (Map<String, String>) message;
        if (errorCode == 400) {
            body = body.replace("[$STATUS$]", "<p>Webcontent labels update : <span style='color:red'>Failure</span></p>" +
                            "<p>Structure ID : " + countMap.get("webContentIDs") + "</p><p>Field Name : " + countMap.get("fieldNameCurrents") + "</p>" +
                            "<p>Content : " + countMap.get("contents") + "</p>")
                    .replace("[$MESSAGE$]", "<p>" + countMap.get("message") + "</p>");
        } else if (errorCode == 200) {

            String successMessage = "<table><tr><th>Content</th><th>Count</th></tr><tr><tr><td>Webcontent</td><td>" + countMap.get("journal") + "</td></tr></table>";
            body = body.replace("[$STATUS$]", "<p>Webcontent labels update : <span style='color:green'>Success</span></p>" +
                            "<p>Structure ID : " + countMap.get("webContentIDs") + "</p><p>Field Name : " + countMap.get("fieldNameCurrents") + "</p>" +
                            "<p>Content : " + countMap.get("contents") + "</p>")
                    .replace("[$MESSAGE$]", "<p>" + successMessage + "</p>");
        }
        return body;
    }

    /**
     * Is admin role boolean.
     *
     * @return the boolean
     */
    public static boolean isAdminRole(){
        PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();
        return permissionChecker.isOmniadmin();
    }

    private static final Log log = LogFactoryUtil.getLog(WebcontentModifyUtil.class.getName());
}
