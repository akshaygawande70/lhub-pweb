package web.ntuc.nlh.comments.admin.dto;

import javax.portlet.ActionURL;

public class CommentDto {

	private long commentId;
	private String comment;
	private String name;
	private String email;
	private boolean option;
	private String createDate;
	private String modifiedDate;
	private boolean status;
	private ActionURL deleteUrl;

	public CommentDto() {
		super();
	}
	
	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getOption() {
		return option;
	}

	public void setOption(boolean option) {
		this.option = option;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public ActionURL getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(ActionURL deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	@Override
	public String toString() {
		return "CommentDto [commentId=" + commentId + ", comment=" + comment + ", name=" + name + ", email=" + email
				+ ", option=" + option + ", createDate=" + createDate + ", modifiedDate=" + modifiedDate + ", status="
				+ status + ", deleteUrl=" + deleteUrl + "]";
	}

}
