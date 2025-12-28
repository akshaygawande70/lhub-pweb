package com.ntuc.notification.model;

import java.io.Serializable;

/**
 * API-safe processing result object.
 * Do NOT put any Liferay model types in API.
 */
public class CourseProcessResult implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean success;

    private long journalArticleId;
    private String articleId;
    private String articleUuid;
    private double version;
    private String articleStatus;
    
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getJournalArticleId() {
        return journalArticleId;
    }

    public void setJournalArticleId(long journalArticleId) {
        this.journalArticleId = journalArticleId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleUuid() {
        return articleUuid;
    }

    public void setArticleUuid(String articleUuid) {
        this.articleUuid = articleUuid;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

	public String getArticleStatus() {
		return articleStatus;
	}

	public void setArticleStatus(String articleStatus) {
		this.articleStatus = articleStatus;
	}

	
}
