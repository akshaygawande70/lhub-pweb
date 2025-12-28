package com.ntuc.notification.email.internal;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.ntuc.notification.email.AlertEmailTemplates;
import com.ntuc.notification.email.EmailSender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = EmailSender.class)
public class EmailSenderImpl implements EmailSender {

    private static final Log _log = LogFactoryUtil.getLog(EmailSenderImpl.class);

    @Reference
    private MailService _mailService;

    // ========== PUBLIC API ==========

    @Override
    public void send(long companyId, String toEmails, String subject, String body, boolean html) {
        long t0 = System.currentTimeMillis();
        try {
            InternetAddress from = resolveFrom(companyId);
            InternetAddress[] toArr = parseAddrs(toEmails);

            if (toArr.length == 0) {
                throw new IllegalArgumentException("No valid TO recipient in: " + safe(toEmails));
            }

            sendCore(companyId, from, toArr, null, null, null, subject, body, html, null);
        }
        catch (Exception e) {
            long took = System.currentTimeMillis() - t0;
            _log.error("Email send(simple) failed: to=" + safe(toEmails) + ", subject=" + safe(subject) +
                ", tookMs=" + took + ", err=" + rootMsg(e), e);

            // CRITICAL: do NOT swallow. Trigger must audit EMAIL_SEND_FAILED.
            throw new RuntimeException("Email send failed", e);
        }
    }

    @Override
    public void send(
        long companyId, String toEmails, String ccEmails, String bccEmails,
        String subject, String body, boolean html) {

        long t0 = System.currentTimeMillis();
        try {
            InternetAddress from = resolveFrom(companyId);

            InternetAddress[] toArr = parseAddrs(toEmails);
            InternetAddress[] ccArr = parseAddrs(ccEmails);
            InternetAddress[] bccArr = parseAddrs(bccEmails);

            if (toArr.length == 0) {
                throw new IllegalArgumentException("No valid TO recipient in: " + safe(toEmails));
            }

            List<InternetAddress> ccList = (ccArr.length == 0) ? null : java.util.Arrays.asList(ccArr);
            List<InternetAddress> bccList = (bccArr.length == 0) ? null : java.util.Arrays.asList(bccArr);

            sendCore(companyId, from, toArr, ccList, bccList, null, subject, body, html, null);
        }
        catch (Exception e) {
            long took = System.currentTimeMillis() - t0;
            _log.error("Email send(with cc/bcc) failed: to=" + safe(toEmails) + ", subject=" + safe(subject) +
                ", tookMs=" + took + ", err=" + rootMsg(e), e);

            throw new RuntimeException("Email send failed", e);
        }
    }

    @Override
    public void sendWithAttachments(
        long companyId, String toEmails, String ccEmails, String bccEmails,
        String subject, String body, boolean html, List<EmailAttachment> attachments) {

        long t0 = System.currentTimeMillis();
        try {
            InternetAddress from = resolveFrom(companyId);

            InternetAddress[] toArr = parseAddrs(toEmails);
            InternetAddress[] ccArr = parseAddrs(ccEmails);
            InternetAddress[] bccArr = parseAddrs(bccEmails);

            if (toArr.length == 0) {
                throw new IllegalArgumentException("No valid TO recipient in: " + safe(toEmails));
            }

            List<InternetAddress> ccList = (ccArr.length == 0) ? null : java.util.Arrays.asList(ccArr);
            List<InternetAddress> bccList = (bccArr.length == 0) ? null : java.util.Arrays.asList(bccArr);

            sendCore(companyId, from, toArr, ccList, bccList, null, subject, body, html, attachments);
        }
        catch (Exception e) {
            long took = System.currentTimeMillis() - t0;
            _log.error("Email sendWithAttachments failed: to=" + safe(toEmails) + ", subject=" + safe(subject) +
                ", tookMs=" + took + ", err=" + rootMsg(e), e);

            throw new RuntimeException("Email send failed", e);
        }
    }

    @Override
    public void sendMinimalAlert(long companyId, Map<String, Object> ctx, String toEmails, boolean html) {
        try {
            AlertEmailTemplates.normalizeCommon(ctx);
            String subject = AlertEmailTemplates.render(AlertEmailTemplates.SUBJECT, ctx);
            String body = AlertEmailTemplates.render(AlertEmailTemplates.BODY, ctx);
            send(companyId, toEmails, subject, body, html);
        }
        catch (Exception e) {
            _log.error("Minimal alert send failed: to=" + safe(toEmails) + ", err=" + rootMsg(e), e);
            throw (e instanceof RuntimeException) ? (RuntimeException)e : new RuntimeException(e);
        }
    }

    @Override
    public void sendMinimalAlert(
        long companyId, Map<String, Object> ctx, String toEmails, String ccEmails, String bccEmails, boolean html) {

        try {
            AlertEmailTemplates.normalizeCommon(ctx);
            String subject = AlertEmailTemplates.render(AlertEmailTemplates.SUBJECT, ctx);
            String body = AlertEmailTemplates.render(AlertEmailTemplates.BODY, ctx);
            send(companyId, toEmails, ccEmails, bccEmails, subject, body, html);
        }
        catch (Exception e) {
            _log.error("Minimal alert send(cc/bcc) failed: to=" + safe(toEmails) + ", err=" + rootMsg(e), e);
            throw (e instanceof RuntimeException) ? (RuntimeException)e : new RuntimeException(e);
        }
    }

    @Override
    public void sendUsingInstanceTemplate(
        long companyId, String subjectPropsKey, String bodyPropsKey,
        Map<String, Object> context, String toEmails, boolean html) {

        try {
            String subjectTpl = PrefsPropsUtil.getContent(companyId, subjectPropsKey);
            String bodyTpl = PrefsPropsUtil.getContent(companyId, bodyPropsKey);

            String subject = renderFtl(subjectTpl == null ? "" : subjectTpl, context);
            String body = renderFtl(bodyTpl == null ? "" : bodyTpl, context);

            send(companyId, toEmails, subject, body, html);
        }
        catch (Exception e) {
            _log.error("Template email failed: subjectKey=" + safe(subjectPropsKey) +
                ", bodyKey=" + safe(bodyPropsKey) + ", to=" + safe(toEmails) + ", err=" + rootMsg(e), e);

            throw new RuntimeException("Email send failed", e);
        }
    }

    @Override
    public void sendUsingInstanceTemplate(
        long companyId, String subjectPropsKey, String bodyPropsKey,
        Map<String, Object> context, String toEmails, String ccEmails, String bccEmails, boolean html) {

        try {
            String subjectTpl = PrefsPropsUtil.getContent(companyId, subjectPropsKey);
            String bodyTpl = PrefsPropsUtil.getContent(companyId, bodyPropsKey);

            String subject = renderFtl(subjectTpl == null ? "" : subjectTpl, context);
            String body = renderFtl(bodyTpl == null ? "" : bodyTpl, context);

            send(companyId, toEmails, ccEmails, bccEmails, subject, body, html);
        }
        catch (Exception e) {
            _log.error("Template email(cc/bcc) failed: subjectKey=" + safe(subjectPropsKey) +
                ", bodyKey=" + safe(bodyPropsKey) + ", to=" + safe(toEmails) + ", err=" + rootMsg(e), e);

            throw new RuntimeException("Email send failed", e);
        }
    }

    // ========== CORE SENDER ==========

    private void sendCore(
        long companyId,
        InternetAddress from,
        InternetAddress[] toArr,
        List<InternetAddress> cc,
        List<InternetAddress> bcc,
        InternetAddress replyTo,
        String subject,
        String body,
        boolean html,
        List<EmailSender.EmailAttachment> attachments) throws Exception {

        long t0 = System.currentTimeMillis();

        MailMessage msg = new MailMessage();
        msg.setFrom(from);

        if (toArr != null && toArr.length > 0) {
            msg.setTo(toArr);
        }
        if (cc != null && !cc.isEmpty()) {
            msg.setCC(cc.toArray(new InternetAddress[0]));
        }
        if (bcc != null && !bcc.isEmpty()) {
            msg.setBCC(bcc.toArray(new InternetAddress[0]));
        }
        if (replyTo != null) {
            msg.setReplyTo(new InternetAddress[]{replyTo});
        }

        msg.setSubject(subject);
        msg.setBody(body);
        msg.setHTMLFormat(html);

        if (attachments != null && !attachments.isEmpty()) {
            for (EmailSender.EmailAttachment a : attachments) {
                File tmp = File.createTempFile("email-attachment-", "-" + safeFileName(a.getFileName()));
                try (FileOutputStream fos = new FileOutputStream(tmp)) {
                    fos.write(a.getBytes());
                }
                msg.addFileAttachment(tmp, a.getFileName());
                tmp.deleteOnExit();
            }
        }

        _mailService.sendEmail(msg);

        long took = System.currentTimeMillis() - t0;

        if (_log.isInfoEnabled()) {
            _log.info("Email sent: to=" + joinAddrs(toArr) +
                ", subject=" + safe(subject) + ", html=" + html + ", tookMs=" + took);
        }
    }

    // ---------- helpers ----------

    private InternetAddress resolveFrom(long companyId) throws Exception {
        String fromName = PrefsPropsUtil.getString(companyId, PropsKeys.ADMIN_EMAIL_FROM_NAME);
        String fromAddr = PrefsPropsUtil.getString(companyId, PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);

        if (isBlank(fromAddr)) {
            long defaultCompanyId = PortalUtil.getDefaultCompanyId();
            fromName = PrefsPropsUtil.getString(defaultCompanyId, PropsKeys.ADMIN_EMAIL_FROM_NAME);
            fromAddr = PrefsPropsUtil.getString(defaultCompanyId, PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
        }

        return new InternetAddress(fromAddr, fromName);
    }

    private String renderFtl(String tplString, Map<String, Object> ctx) throws Exception {
        Template tpl = TemplateManagerUtil.getTemplate(
            TemplateConstants.LANG_TYPE_FTL,
            new StringTemplateResource("inline-" + System.nanoTime(), tplString),
            false
        );

        if (ctx != null) {
            for (Map.Entry<String, Object> e : ctx.entrySet()) {
                tpl.put(e.getKey(), e.getValue());
            }
        }

        StringWriter out = new StringWriter();
        tpl.processTemplate(out);
        return out.toString();
    }

    private static InternetAddress[] parseAddrs(String s) throws Exception {
        if (s == null) return new InternetAddress[0];
        String trimmed = s.trim();
        if (trimmed.isEmpty()) return new InternetAddress[0];
        String normalized = trimmed.replace(';', ',');
        InternetAddress[] arr = InternetAddress.parse(normalized, true);
        return (arr == null) ? new InternetAddress[0] : arr;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String rootMsg(Throwable t) {
        if (t == null) return "";
        Throwable r = t;
        while (r.getCause() != null) r = r.getCause();
        String m = r.getMessage();
        return r.getClass().getName() + (m == null ? "" : (": " + m));
    }

    private static String safe(String s) {
        if (s == null) return "";
        String trimmed = s.trim();
        return trimmed.length() > 250 ? (trimmed.substring(0, 247) + "...") : trimmed;
    }

    private static String safeFileName(String s) {
        if (s == null || s.trim().isEmpty()) return "file";
        return s.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private static String joinAddrs(InternetAddress[] arr) {
        if (arr == null || arr.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[i].toUnicodeString());
        }
        return sb.toString();
    }
}
