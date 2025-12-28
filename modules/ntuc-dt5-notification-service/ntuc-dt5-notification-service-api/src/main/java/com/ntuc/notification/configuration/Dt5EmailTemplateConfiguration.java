package com.ntuc.notification.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * System configuration for alert email templates.
 *
 * <p>UI edits these values via Configuration Admin.</p>
 * <p>SERVICE reads these templates for rendering alert emails (audit-driven only).</p>
 *
 * <p>SECURITY:</p>
 * <ul>
 *   <li>Templates must not include secrets or raw payload dumps.</li>
 *   <li>Only whitelisted tokens (AlertEmailToken) must be supported.</li>
 * </ul>
 */
@Meta.OCD(
    id = "com.ntuc.notification.configuration.Dt5EmailTemplateConfiguration",
    localization = "content/Language",
    name = "dt5-email-templates-configuration-name"
)
public interface Dt5EmailTemplateConfiguration {

    @Meta.AD(
        deflt = "[DT5] Failure - [$ERROR_CODE$] - Correlation [$CORRELATION_ID$]",
        required = false
    )
    String dt5FailureSubject();

    @Meta.AD(
        deflt = "<p><b>DT5 Failure</b></p><p><b>Time:</b> [$TIMESTAMP$]</p><p><b>Correlation:</b> [$CORRELATION_ID$]</p><p><b>Audit ID:</b> [$AUDIT_ID$]</p><p><b>Step:</b> [$STEP$]</p><p><b>Error Code:</b> [$ERROR_CODE$]</p><p><b>Message:</b> [$ERROR_MESSAGE$]</p><hr/><p><b>Business Keys</b></p><ul><li>Course Code: [$COURSE_CODE$]</li><li>NTUC DT ID: [$NTUC_DT_ID$]</li></ul><p>Refer to Audit Log using correlation/audit id.</p>",
        required = false
    )
    String dt5FailureBody();

    @Meta.AD(
        deflt = "[DT5] CLS Failure - [$ERROR_CODE$] - Correlation [$CORRELATION_ID$]",
        required = false
    )
    String clsFailureSubject();

    @Meta.AD(
        deflt = "<p><b>CLS Failure</b></p><p><b>Time:</b> [$TIMESTAMP$]</p><p><b>Endpoint:</b> [$ENDPOINT$]</p><p><b>Correlation:</b> [$CORRELATION_ID$]</p><p><b>Audit ID:</b> [$AUDIT_ID$]</p><p><b>Error Code:</b> [$ERROR_CODE$]</p><p><b>Message:</b> [$ERROR_MESSAGE$]</p><p>Refer to Audit Log using correlation/audit id.</p>",
        required = false
    )
    String clsFailureBody();

    @Meta.AD(
        deflt = "[DT5] JA Failure - [$ERROR_CODE$] - Correlation [$CORRELATION_ID$]",
        required = false
    )
    String jaFailureSubject();

    @Meta.AD(
        deflt = "<p><b>JA Failure</b></p><p><b>Time:</b> [$TIMESTAMP$]</p><p><b>Correlation:</b> [$CORRELATION_ID$]</p><p><b>Audit ID:</b> [$AUDIT_ID$]</p><p><b>DDM Structure:</b> [$DDM_STRUCTURE_KEY$]</p><p><b>DDM Template:</b> [$DDM_TEMPLATE_KEY$]</p><p><b>Error Code:</b> [$ERROR_CODE$]</p><p><b>Message:</b> [$ERROR_MESSAGE$]</p><p>Refer to Audit Log using correlation/audit id.</p>",
        required = false
    )
    String jaFailureBody();
}
