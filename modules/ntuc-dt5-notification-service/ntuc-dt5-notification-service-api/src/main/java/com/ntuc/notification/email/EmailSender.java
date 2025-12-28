package com.ntuc.notification.email;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface EmailSender {

    final class EmailAttachment {

        private final String fileName;
        private final byte[] bytes;

        public EmailAttachment(String fileName, byte[] bytes) {
            this.fileName = fileName;
            this.bytes = bytes == null ? null : Arrays.copyOf(bytes, bytes.length);
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getBytes() {
            return bytes == null ? null : Arrays.copyOf(bytes, bytes.length);
        }
    }

    void send(long companyId, String toEmails, String subject, String body, boolean html);

    void send(
        long companyId,
        String toEmails,
        String ccEmails,
        String bccEmails,
        String subject,
        String body,
        boolean html
    );

    /**
     * Send with attachments without exposing JavaMail types in API.
     * TO/CC/BCC are comma/semicolon separated lists.
     */
    void sendWithAttachments(
        long companyId,
        String toEmails,
        String ccEmails,
        String bccEmails,
        String subject,
        String body,
        boolean html,
        List<EmailAttachment> attachments
    );

    void sendUsingInstanceTemplate(
        long companyId,
        String subjectPropsKey,
        String bodyPropsKey,
        Map<String, Object> context,
        String toEmails,
        boolean html
    );

    void sendUsingInstanceTemplate(
        long companyId,
        String subjectPropsKey,
        String bodyPropsKey,
        Map<String, Object> context,
        String toEmails,
        String ccEmails,
        String bccEmails,
        boolean html
    );

    void sendMinimalAlert(long companyId, Map<String, Object> ctx, String toEmails, boolean html);

    void sendMinimalAlert(
        long companyId,
        Map<String, Object> ctx,
        String toEmails,
        String ccEmails,
        String bccEmails,
        boolean html
    );
}
