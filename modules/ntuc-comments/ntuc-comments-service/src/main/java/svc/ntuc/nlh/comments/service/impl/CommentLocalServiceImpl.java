/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package svc.ntuc.nlh.comments.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import svc.ntuc.nlh.comments.exception.NoSuchCommentException;
import svc.ntuc.nlh.comments.model.Comment;
import svc.ntuc.nlh.comments.service.base.CommentLocalServiceBaseImpl;

/**
 * The implementation of the comment local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * <code>svc.ntuc.nlh.comments.service.CommentLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CommentLocalServiceBaseImpl
 */
@Component(property = "model.class.name=svc.ntuc.nlh.comments.model.Comment", service = AopService.class)
public class CommentLocalServiceImpl extends CommentLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use
	 * <code>svc.ntuc.nlh.comments.service.CommentLocalService</code> via injection
	 * or a <code>org.osgi.util.tracker.ServiceTracker</code> or use
	 * <code>svc.ntuc.nlh.comments.service.CommentLocalServiceUtil</code>.
	 */

	Log log = LogFactoryUtil.getLog(CommentLocalServiceImpl.class);

	public Comment updateComment(long commentId, String commentText, String commentName, String commentEmail,
			boolean commentOption, boolean commentStatus, ServiceContext serviceContext) throws PortalException {
		log.info("Update Comment - Start");
		
		Comment comment = getComment(commentId);

		comment.setModifiedDate(new Date());
		comment.setCommentText(commentText);
		comment.setCommentName(commentName);
		comment.setCommentEmail(commentEmail);
		comment.setCommentOption(commentOption);
		comment.setCommentStatus(commentStatus);

		log.info("Update Comment - End");
		return comment;
	}

	public List<Comment> getAllComment() {
		log.info("Search All Comment");
		return commentPersistence.findAll();
	}

	public List<Comment> getCommentByName(long groupId, String commentName) {
		log.info("Search Comment by Comment User Name");
		return commentPersistence.findByName(groupId, commentName);
	}

	public Comment getCommentByEmail(long groupId, String commentEmail) throws NoSuchCommentException {
		log.info("Search Comment by User Email");
		return commentPersistence.findByEmail(groupId, commentEmail);
	}
}