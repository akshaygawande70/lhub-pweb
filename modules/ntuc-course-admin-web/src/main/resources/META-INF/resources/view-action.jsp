<%@page import="web.ntuc.nlh.course.admin.constants.MVCCommandNames"%>
<%@ include file="/init.jsp" %>

<portlet:actionURL var="generateCourseArticleURL"
	name="<%=MVCCommandNames.GENERATE_COURSE_ARTICLE_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="courseCode" value="${p.courseCode}" />
</portlet:actionURL>


<div class="cx-detail-view">
	<div class="detail-row">
		<label><liferay-ui:message key="course-id" /> :</label>
		<div class="value">${p.courseId}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="venue" /> :</label>
		<div class="value">${p.venue}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="allow-online-payment" /> :</label>
		<div class="value">${p.allowOnlinePayment}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="course-title" /> :</label>
		<div class="value">${p.courseTitle}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="allow-web-registration" /> :</label>
		<div class="value">${p.allowWebRegistration}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="description" /> :</label>
		<div class="value">${p.description}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="course-fee" /> :</label>
		<div class="value">${p.courseFee}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="course-type" /> :</label>
		<div class="value">${p.courseType}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="availability" /> :</label>
		<div class="value">${p.availability}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="batch-id" /> :</label>
		<div class="value">${p.batchId}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="web-expiry" /> :</label>
		<div class="value">${p.webExpiry}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="funded-course-flag" /> :</label>
		<div class="value">${p.fundedCourseFlag}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="course-code" /> :</label>
		<div class="value">${p.courseCode}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="course-duration" /> :</label>
		<div class="value">${p.courseDuration}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="popular" /> :</label>
		<div class="value">${p.popular}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="start-date" /> :</label>
		<div class="value">${p.startDate}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="end-date" /> :</label>
		<div class="value">${p.endDate}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="create-date" /> :</label>
		<div class="value">${p.createDate}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="modified-date" /> :</label>
		<div class="value">${p.modifiedDate}</div>
	</div>
</div>
<div class="row">
	<a href="${generateCourseArticleURL}"
		class="btn btn-success btn-lg top-right-button mr-1"><liferay-ui:message key="generate-course"/></a>
</div>