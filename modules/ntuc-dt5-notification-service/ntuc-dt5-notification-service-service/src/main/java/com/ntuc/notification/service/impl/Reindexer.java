package com.ntuc.notification.service.impl;

import com.liferay.journal.model.JournalArticle;

/**
 * Wrapper used to isolate static IndexerRegistryUtil access.
 * JournalArticleServiceImpl can be unit-tested by mocking this interface.
 */
public interface Reindexer {

    void reindex(JournalArticle article);
}
