<%@ include file="../init.jsp"%>

<div class="cx-detail-view">
	<div class="detail-row">
		<label><liferay-ui:message key="group-code" /> :</label>
		<div class="value">${p.groupCode}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="group-name" /> :</label>
		<div class="value">${p.groupName}</div>
	</div>
	<div class="detail-row">
		<label><liferay-ui:message key="descrition" /> :</label>
		<div class="value">${p.desc}</div>
	</div>
</div>