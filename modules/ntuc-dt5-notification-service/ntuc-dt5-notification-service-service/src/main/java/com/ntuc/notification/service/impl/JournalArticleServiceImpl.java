package com.ntuc.notification.service.impl;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.CourseArticleConfig;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.audit.util.MdcUtil;
import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.journal.api.DynamicElementValueExtractor;
import com.ntuc.notification.model.CourseResponse;
import com.ntuc.notification.service.JournalArticleService;
import com.ntuc.notification.service.internal.journal.JournalArticleCandidateSelector;
import com.ntuc.notification.service.internal.journal.XmlDynamicElementValueExtractor;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = JournalArticleService.class)
public class JournalArticleServiceImpl implements JournalArticleService {

    private static final Log _log = LogFactoryUtil.getLog(JournalArticleServiceImpl.class);

    @Reference
    private JournalArticleLocalService _journalArticleLocalService;

    @Reference
    private LayoutLocalService _layoutLocalService;

    @Reference
    private AuditEventWriter _auditEventWriter;

    @Reference
    private Reindexer _reindexer;

    // Plain Java helpers (unit-testable)
    private final DynamicElementValueExtractor _xmlExtractor = new XmlDynamicElementValueExtractor();
    private final JournalArticleCandidateSelector _candidateSelector = new JournalArticleCandidateSelector(_xmlExtractor);

    @Activate
    protected void activate() {
        // No param preload here. JournalArticleService must not own alerting config.
    }

    @Override
    public JournalArticle processFields(
            CourseEventContext eventCtx,
            CourseArticleConfig articleConfig,
            CourseResponse courseResponse,
            String xmlContent,
            boolean isNew,
            long articleId,
            JournalArticle existingArticle) {

        final String corrId = corrId();
        final Ids ids = Ids.from(eventCtx);
        final long startMs = System.currentTimeMillis();

        writeAudit(AuditEvent.builder()
                .companyId(ids.companyId)
                .groupId(ids.groupId)
                .userId(ids.userId)
                .courseCode(ids.courseCode)
                .ntucDTId(ids.ntucDTId)
                .correlationId(corrId)
                .startTimeMs(startMs)
                .endTimeMs(0)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.STARTED)
                .step(AuditStep.JA_PROCESS_FIELDS)
                .category(AuditCategory.JOURNAL_ARTICLE)
                .message("JA processFields started")
                .detail("isNew", String.valueOf(isNew))
                .detail("xmlBytes", String.valueOf(len(xmlContent)))
                .detail("existingArticleId", String.valueOf(articleId))
                .build());

        try {
            JournalArticle article;
            if (isNew) {
                article = saveJournalArticle(eventCtx, articleConfig, xmlContent, courseResponse, corrId);
            } else {
                article = updateJournalArticle(eventCtx, articleConfig, xmlContent, existingArticle, courseResponse, corrId);
            }

            long endMs = System.currentTimeMillis();
            long id = (article != null) ? article.getId() : 0;

            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(endMs)
                    .severity(id > 0 ? AuditSeverity.INFO : AuditSeverity.ERROR)
                    .status(id > 0 ? AuditStatus.SUCCESS : AuditStatus.FAILED)
                    .step(AuditStep.JA_PROCESS_FIELDS)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(id > 0 ? AuditErrorCode.NONE : AuditErrorCode.JA_PERSIST_FAILED)
                    .message(id > 0 ? "JA processFields completed" : "JA processFields failed: returned id=0")
                    .detail("resultId", String.valueOf(id))
                    .detail("durationMs", String.valueOf(endMs - startMs))
                    .build());

            return article;

        } catch (RuntimeException e) {
            long endMs = System.currentTimeMillis();

            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(endMs)
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.JA_PROCESS_FIELDS)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_PROCESS_FIELDS_EXCEPTION)
                    .message("JA processFields failed with exception")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .detail("isNew", String.valueOf(isNew))
                    .detail("existingArticleId", String.valueOf(articleId))
                    .detail("durationMs", String.valueOf(endMs - startMs))
                    .build());

            if (_log.isDebugEnabled()) {
                _log.debug("processFields failed corrId=" + corrId + " courseCode=" + ids.courseCode, e);
            }
            throw e;
        }
    }

    @Override
    public JournalArticle updateStatus(
            CourseEventContext eventCtx,
            CourseArticleConfig articleConfig,
            JournalArticle article,
            int status) {

        final String corrId = corrId();
        final Ids ids = Ids.from(eventCtx);
        final long startMs = System.currentTimeMillis();

        if (article == null) {
            article = findExistingArticle(eventCtx.getGroupId(), eventCtx.getCompanyId(), ids.courseCode, eventCtx, corrId);
        }

        if (article == null) {
            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.WARNING)
                    .status(AuditStatus.SKIPPED) // <-- was NOT_FOUND (invalid)
                    .step(AuditStep.JA_STATUS)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_NOT_FOUND)
                    .message("JA updateStatus skipped: no article found")
                    .detail("requestedStatus", String.valueOf(status))
                    .build());
            return null;
        }

        try {
            updateStatusForAllVersions(eventCtx, article, status, corrId);

            JournalArticle ret = findExistingArticle(
                    eventCtx.getGroupId(),
                    eventCtx.getCompanyId(),
                    ids.courseCode,
                    eventCtx,
                    corrId
            );

            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.INFO)
                    .status(AuditStatus.SUCCESS)
                    .step(AuditStep.JA_STATUS)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.NONE)
                    .message("JA updateStatus completed")
                    .detail("journalArticleId", String.valueOf(article.getId()))
                    .detail("newStatus", String.valueOf(status))
                    .build());

            return ret;

        } catch (RuntimeException e) {
            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.JA_STATUS)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_STATUS_UPDATE_EXCEPTION)
                    .message("JA updateStatus failed with exception")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .detail("journalArticleId", String.valueOf(article.getId()))
                    .detail("requestedStatus", String.valueOf(status))
                    .build());

            if (_log.isDebugEnabled()) {
                _log.debug("updateStatus failed corrId=" + corrId + " articleId=" + article.getId(), e);
            }
            return article;
        }
    }

    @Override
    public void updateStatusForAllVersions(CourseEventContext eventCtx, JournalArticle latest, int status) {
        updateStatusForAllVersions(eventCtx, latest, status, corrId());
    }

    private void updateStatusForAllVersions(CourseEventContext eventCtx, JournalArticle latest, int status, String corrId) {
        final Ids ids = Ids.from(eventCtx);
        final long startMs = System.currentTimeMillis();

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setCompanyId(eventCtx.getCompanyId());
        serviceContext.setScopeGroupId(eventCtx.getGroupId());

        try {
            List<JournalArticle> versions =
                    _journalArticleLocalService.getArticles(latest.getGroupId(), latest.getArticleId());

            for (JournalArticle version : versions) {
                _journalArticleLocalService.updateStatus(
                        eventCtx.getUserId(),
                        version.getPrimaryKey(),
                        status,
                        null,
                        serviceContext
                );
                _reindexer.reindex(version);
            }

            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.INFO)
                    .status(AuditStatus.SUCCESS)
                    .step(AuditStep.JA_STATUS_ALL_VERSIONS)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .message("JA updateStatusForAllVersions completed")
                    .detail("articleId", String.valueOf(latest.getArticleId()))
                    .detail("versionCount", String.valueOf(versions.size()))
                    .detail("newStatus", String.valueOf(status))
                    .build());

        } catch (PortalException e) {
            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.JA_STATUS_ALL_VERSIONS)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_STATUS_UPDATE_EXCEPTION)
                    .message("JA updateStatusForAllVersions failed with exception")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .detail("articleId", String.valueOf(latest.getArticleId()))
                    .detail("requestedStatus", String.valueOf(status))
                    .build());

            if (_log.isDebugEnabled()) {
                _log.debug("updateStatusForAllVersions failed corrId=" + corrId + " articleId=" + latest.getArticleId(), e);
            }
        }
    }

    @Override
    public JournalArticle fetchJournalArticleByUuidAndGroupId(String uuid, long groupId) {
        return _journalArticleLocalService.fetchJournalArticleByUuidAndGroupId(uuid, groupId);
    }

    @Override
    public JournalArticle findExistingArticle(long groupId, long companyId, String courseCode, CourseEventContext eventCtx) {
        return findExistingArticle(groupId, companyId, courseCode, eventCtx, corrId());
    }

    private JournalArticle findExistingArticle(long groupId, long companyId, String courseCode, CourseEventContext eventCtx, String corrId) {
        final long startMs = System.currentTimeMillis();

        try {
            DynamicQuery dq = _journalArticleLocalService.dynamicQuery();

            dq.add(RestrictionsFactoryUtil.eq("groupId", groupId));
            dq.add(RestrictionsFactoryUtil.eq("companyId", companyId));
            dq.add(RestrictionsFactoryUtil.in(
                    "status",
                    new Integer[] {
                            WorkflowConstants.STATUS_APPROVED,
                            WorkflowConstants.STATUS_PENDING,
                            WorkflowConstants.STATUS_INACTIVE,
                            WorkflowConstants.STATUS_EXPIRED
                    }
            ));

            dq.add(RestrictionsFactoryUtil.like("content", "%" + safe(courseCode) + "%"));
            dq.addOrder(OrderFactoryUtil.desc("version"));
            dq.setLimit(0, 50);

            List<JournalArticle> candidates = _journalArticleLocalService.dynamicQuery(dq);

            JournalArticle winner = _candidateSelector.findLatestExactCourseCode(candidates, safe(courseCode));

            writeAudit(AuditEvent.builder()
                    .companyId(companyId)
                    .groupId(groupId)
                    .userId(eventCtx == null ? 0L : eventCtx.getUserId())
                    .courseCode(safe(courseCode))
                    .ntucDTId(0L)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(winner == null ? AuditSeverity.INFO : AuditSeverity.INFO)
                    .status(winner == null ? AuditStatus.SKIPPED : AuditStatus.SUCCESS) // <-- was NOT_FOUND (invalid)
                    .step(AuditStep.JA_LOOKUP)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(winner == null ? AuditErrorCode.JA_NOT_FOUND : AuditErrorCode.NONE)
                    .message(winner == null ? "JA lookup: no latest match" : "JA lookup: latest found")
                    .detail("candidateCount", String.valueOf(candidates == null ? 0 : candidates.size()))
                    .detail("foundArticleId", winner == null ? "" : String.valueOf(winner.getArticleId()))
                    .build());

            return winner;

        } catch (RuntimeException e) {
            writeAudit(AuditEvent.builder()
                    .companyId(companyId)
                    .groupId(groupId)
                    .userId(eventCtx == null ? 0L : eventCtx.getUserId())
                    .courseCode(safe(courseCode))
                    .ntucDTId(0L)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.JA_LOOKUP)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_LOOKUP_EXCEPTION)
                    .message("JA lookup failed with exception")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .build());

            if (_log.isDebugEnabled()) {
                _log.debug("findExistingArticle failed corrId=" + corrId + " courseCode=" + courseCode, e);
            }
            return null;
        }
    }

    @Override
    public Layout getLayoutByName(long groupId, String layoutName, boolean privateLayout) {
        final String corrId = corrId();
        final long startMs = System.currentTimeMillis();

        try {
            List<Layout> layouts = _layoutLocalService.getLayouts(groupId, privateLayout);
            Locale defaultLocale = LocaleUtil.getSiteDefault();

            for (Layout layout : layouts) {
                String resolvedName = layout.getName(defaultLocale);
                if (layoutName.equalsIgnoreCase(resolvedName)) {
                    writeAudit(AuditEvent.builder()
                            .companyId(0L)
                            .groupId(groupId)
                            .userId(0L)
                            .correlationId(corrId)
                            .startTimeMs(startMs)
                            .endTimeMs(System.currentTimeMillis())
                            .severity(AuditSeverity.INFO)
                            .status(AuditStatus.SUCCESS)
                            .step(AuditStep.JA_FIND_LAYOUT)
                            .category(AuditCategory.JOURNAL_ARTICLE)
                            .message("Layout found by name")
                            .detail("layoutId", String.valueOf(layout.getLayoutId()))
                            .detail("layoutUuid", safe(layout.getUuid()))
                            .build());
                    return layout;
                }
            }

            writeAudit(AuditEvent.builder()
                    .companyId(0L)
                    .groupId(groupId)
                    .userId(0L)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.WARNING)
                    .status(AuditStatus.SKIPPED) // <-- was NOT_FOUND (invalid)
                    .step(AuditStep.JA_FIND_LAYOUT)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_LAYOUT_NOT_FOUND)
                    .message("Layout not found by name")
                    .detail("layoutName", safe(layoutName))
                    .build());

        } catch (RuntimeException e) {
            writeAudit(AuditEvent.builder()
                    .companyId(0L)
                    .groupId(groupId)
                    .userId(0L)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.JA_FIND_LAYOUT)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_LAYOUT_LOOKUP_EXCEPTION)
                    .message("Layout lookup failed with exception")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .build());

            if (_log.isDebugEnabled()) {
                _log.debug("getLayoutByName failed corrId=" + corrId + " layoutName=" + layoutName, e);
            }
        }

        return null;
    }

    // ===================== Internal create/update =====================

    protected JournalArticle saveJournalArticle(
            CourseEventContext eventCtx,
            CourseArticleConfig articleConfig,
            String xmlContent,
            CourseResponse courseResponse,
            String corrId) {

        final Ids ids = Ids.from(eventCtx);
        final long startMs = System.currentTimeMillis();

        if (isBlank(xmlContent)) {
            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.WARNING)
                    .status(AuditStatus.SKIPPED)
                    .step(AuditStep.JA_CREATE)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_EMPTY_XML)
                    .message("JA create skipped: empty XML content")
                    .detail("xmlBytes", "0")
                    .build());
            return null;
        }

        long userId = eventCtx.getUserId();
        long groupId = eventCtx.getGroupId();
        long folderId = (articleConfig != null) ? articleConfig.getFolderId() : 0L;

        String defaultLanguageId = (articleConfig != null) ? articleConfig.getDefaultLanguageId() : null;
        Locale defaultLocale = LocaleUtil.fromLanguageId(defaultLanguageId == null ? "en_US" : defaultLanguageId);

        String courseCode = "";
        String courseName = "";
        if (courseResponse != null && courseResponse.getBody() != null) {
            courseCode = safe(courseResponse.getBody().getCourseCode());
            courseName = safe(courseResponse.getBody().getCourseName());
        }

        Map<Locale, String> titleMap = new HashMap<>();
        String title = courseCode + (courseName.isEmpty() ? "" : ": " + courseName);
        titleMap.put(defaultLocale, title);

        Map<Locale, String> friendlyURLMap = new HashMap<>();
        String baseForUrl = !courseName.isEmpty() ? courseName : courseCode;
        if (isBlank(baseForUrl)) {
            baseForUrl = "course";
        }
        String normalized = FriendlyURLNormalizerUtil.normalize(baseForUrl);
        friendlyURLMap.put(defaultLocale, "course/" + normalized);

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setCompanyId(eventCtx.getCompanyId());
        serviceContext.setScopeGroupId(groupId);
        serviceContext.setUserId(userId);

        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int displayMonth = cal.get(java.util.Calendar.MONTH);
            int displayDay = cal.get(java.util.Calendar.DAY_OF_MONTH);
            int displayYear = cal.get(java.util.Calendar.YEAR);
            int displayHour = cal.get(java.util.Calendar.HOUR_OF_DAY);
            int displayMinute = cal.get(java.util.Calendar.MINUTE);

            String ddmStructureKey = (articleConfig != null) ? articleConfig.getDdmStructureKey() : null;
            String ddmTemplateKey = (articleConfig != null) ? articleConfig.getDdmTemplateKey() : null;

            JournalArticle newArticle = _journalArticleLocalService.addArticle(
                    userId, groupId, folderId,
                    0, 0,
                    null,
                    true,
                    1.0,
                    titleMap,
                    Collections.emptyMap(),
                    friendlyURLMap,
                    xmlContent,
                    ddmStructureKey,
                    ddmTemplateKey,
                    null,
                    displayMonth, displayDay, displayYear, displayHour, displayMinute,
                    0, 1, 1970, 0, 0, true,
                    0, 1, 1970, 0, 0, true,
                    true,
                    false,
                    null,
                    null,
                    Collections.emptyMap(),
                    null,
                    serviceContext
            );

            Layout layout = getLayoutByName(groupId, "Detail Courses", false);
            if (layout != null) {
                newArticle.setLayoutUuid(layout.getUuid());
                newArticle.setTitleMap(titleMap);
                newArticle = _journalArticleLocalService.updateJournalArticle(newArticle);
            }

            _reindexer.reindex(newArticle);

            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.INFO)
                    .status(AuditStatus.SUCCESS)
                    .step(AuditStep.JA_CREATE)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .message("JA created")
                    .detail("articleId", String.valueOf(newArticle.getId()))
                    .detail("hadLayout", String.valueOf(layout != null))
                    .build());

            return newArticle;

        } catch (PortalException e) {
            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.JA_CREATE)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_CREATE_EXCEPTION)
                    .message("JA create failed with exception")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .detail("folderId", String.valueOf(folderId))
                    .build());

            return null;
        }
    }

    protected JournalArticle updateJournalArticle(
            CourseEventContext eventCtx,
            CourseArticleConfig articleConfig,
            String xmlContent,
            JournalArticle existingArticle,
            CourseResponse courseResponse,
            String corrId) {

        final Ids ids = Ids.from(eventCtx);
        final long startMs = System.currentTimeMillis();

        if (existingArticle == null) {
            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.WARNING)
                    .status(AuditStatus.SKIPPED) // <-- was NOT_FOUND (invalid)
                    .step(AuditStep.JA_UPDATE)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_NOT_FOUND)
                    .message("JA update skipped: no existing article")
                    .build());
            return null;
        }

        if (isBlank(xmlContent)) {
            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.WARNING)
                    .status(AuditStatus.SKIPPED)
                    .step(AuditStep.JA_UPDATE)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_EMPTY_XML)
                    .message("JA update skipped: empty XML content")
                    .detail("articleId", String.valueOf(existingArticle.getId()))
                    .build());
            return existingArticle;
        }

        existingArticle.setContent(xmlContent);

        if (articleConfig != null) {
            existingArticle.setDDMStructureKey(articleConfig.getDdmStructureKey());
            existingArticle.setDDMTemplateKey(articleConfig.getDdmTemplateKey());
            existingArticle.setFolderId(articleConfig.getFolderId());
        }

        // <-- behind interface now
        String courseCodeFromXml = _xmlExtractor.extract(xmlContent, "courseCode");
        String courseTitleFromXml = _xmlExtractor.extract(xmlContent, "courseTitle");

        String defaultLanguageId =
                (articleConfig != null && articleConfig.getDefaultLanguageId() != null)
                        ? articleConfig.getDefaultLanguageId()
                        : "en_US";

        Locale defaultLocale = LocaleUtil.fromLanguageId(defaultLanguageId);

        String courseCode = safe(courseCodeFromXml);
        String courseName = safe(courseTitleFromXml);

        if (isBlank(courseCode) && courseResponse != null && courseResponse.getBody() != null) {
            courseCode = safe(courseResponse.getBody().getCourseCode());
        }
        if (isBlank(courseName) && courseResponse != null && courseResponse.getBody() != null) {
            courseName = safe(courseResponse.getBody().getCourseName());
        }

        String title = courseCode + (courseName.isEmpty() ? "" : ": " + courseName);

        Map<Locale, String> titleMap = existingArticle.getTitleMap();
        if (titleMap == null) {
            titleMap = new HashMap<>();
        }
        titleMap.put(defaultLocale, title);
        existingArticle.setTitleMap(titleMap);

        String eventType = (articleConfig != null) ? safe(articleConfig.getEventType()) : "";
        if (eventType.equalsIgnoreCase(NotificationType.PUBLISHED)) {
            existingArticle.setStatus(WorkflowConstants.STATUS_APPROVED);
        }

        try {
            JournalArticle updatedArticle = _journalArticleLocalService.updateJournalArticle(existingArticle);
            _reindexer.reindex(updatedArticle);

            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.INFO)
                    .status(AuditStatus.SUCCESS)
                    .step(AuditStep.JA_UPDATE)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .message("JA updated")
                    .detail("articleId", String.valueOf(updatedArticle.getId()))
                    .build());

            return updatedArticle;

        } catch (RuntimeException e) {
            writeAudit(AuditEvent.builder()
                    .companyId(ids.companyId)
                    .groupId(ids.groupId)
                    .userId(ids.userId)
                    .courseCode(ids.courseCode)
                    .ntucDTId(ids.ntucDTId)
                    .correlationId(corrId)
                    .startTimeMs(startMs)
                    .endTimeMs(System.currentTimeMillis())
                    .severity(AuditSeverity.ERROR)
                    .status(AuditStatus.FAILED)
                    .step(AuditStep.JA_UPDATE)
                    .category(AuditCategory.JOURNAL_ARTICLE)
                    .errorCode(AuditErrorCode.JA_UPDATE_EXCEPTION)
                    .message("JA update failed with exception")
                    .errorMessage(safeMsg(e))
                    .exceptionClass(e.getClass().getName())
                    .detail("articleId", String.valueOf(existingArticle.getId()))
                    .build());
            return existingArticle;
        }
    }

    // ===================== internal =====================

    private void writeAudit(AuditEvent e) {
        try {
            _auditEventWriter.write(e);
        } catch (Throwable ignore) {
            // Audit must not break runtime flow.
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static int len(String s) {
        return s == null ? 0 : s.getBytes(StandardCharsets.UTF_8).length;
    }

    private static String corrId() {
        String c = MdcUtil.getCorrId();
        return (c != null && !c.trim().isEmpty()) ? c : UUID.randomUUID().toString();
    }

    private static String safeMsg(Throwable t) {
        if (t == null) return "";
        String m = t.getMessage();
        return m == null ? t.getClass().getSimpleName() : m;
    }

    private static final class Ids {
        final long groupId;
        final long companyId;
        final long userId;
        final String courseCode;
        final long ntucDTId;

        private Ids(long g, long c, long u, String cc, long n) {
            this.groupId = g;
            this.companyId = c;
            this.userId = u;
            this.courseCode = cc;
            this.ntucDTId = n;
        }

        static Ids from(CourseEventContext ctx) {
            long g = (ctx != null) ? ctx.getGroupId() : 0L;
            long c = (ctx != null) ? ctx.getCompanyId() : 0L;
            long u = (ctx != null) ? ctx.getUserId() : 0L;
            String cc = (ctx != null) ? safe(ctx.getCourseCode()) : "";
            long n = 0L;
            return new Ids(g, c, u, cc, n);
        }
    }
}
