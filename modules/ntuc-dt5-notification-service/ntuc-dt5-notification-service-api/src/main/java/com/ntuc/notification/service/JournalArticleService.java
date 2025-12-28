package com.ntuc.notification.service;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.model.Layout;
import com.ntuc.notification.audit.api.CourseArticleConfig;
import com.ntuc.notification.audit.api.CourseEventContext;
import com.ntuc.notification.model.CourseResponse;

/**
 * API contract for JournalArticle operations used by course sync flows.
 *
 * NOTE:
 * - No OSGi annotations here.
 * - No Liferay service references here.
 */
public interface JournalArticleService {

    JournalArticle processFields(
            CourseEventContext eventCtx,
            CourseArticleConfig articleConfig,
            CourseResponse courseResponse,
            String xmlContent,
            boolean isNew,
            long articleId,
            JournalArticle existingArticle);

    JournalArticle updateStatus(
            CourseEventContext eventCtx,
            CourseArticleConfig articleConfig,
            JournalArticle article,
            int status);

    void updateStatusForAllVersions(CourseEventContext eventCtx, JournalArticle latest, int status);

    JournalArticle fetchJournalArticleByUuidAndGroupId(String uuid, long groupId);

    JournalArticle findExistingArticle(long groupId, long companyId, String courseCode, CourseEventContext eventCtx);

    Layout getLayoutByName(long groupId, String layoutName, boolean privateLayout);
}
