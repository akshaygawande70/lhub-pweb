package com.ntuc.notification.service.impl;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;

import org.osgi.service.component.annotations.Component;

@Component(service = Reindexer.class)
public class ReindexerImpl implements Reindexer {

    private static final Log _log = LogFactoryUtil.getLog(ReindexerImpl.class);

    @Override
    public void reindex(JournalArticle article) {
        if (article == null) {
            return;
        }
        try {
            Indexer<JournalArticle> indexer = IndexerRegistryUtil.nullSafeGetIndexer(JournalArticle.class);
            indexer.reindex(article);
        } catch (SearchException se) {
            _log.error("Failed to reindex JournalArticle id=" + article.getId(), se);
        }
    }
}
