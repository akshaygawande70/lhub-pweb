<%@ include file="../init.jsp"%>

<c:set var="seoBulkUpload" value="${SEARCH_CONTAINER_RESULT_ROW.object}" />
<%
	ResultRow row = (ResultRow) request.getAttribute( WebKeys.SEARCH_CONTAINER_RESULT_ROW );
	NtucBulkUpload ntucBulkData = (NtucBulkUpload)row.getObject();
	String status = StatusCommonUtil.getStatus(ntucBulkData.getRowData()); 
%>

<liferay-ui:icon-menu markupView="lexicon">
	<%if("Fail".equalsIgnoreCase(status)) {%>
 		<liferay-ui:icon message="Download" onClick="failedDownload('${seoBulkUpload.ntucBulkUploadId}')" url="javascript:void(0)">
		</liferay-ui:icon>
		
	<%} %>
		<portlet:actionURL name="<%=MVCCommandNames.DELETE_SEO%>" var="deleteSeoBulkUploadURL">
			<portlet:param name="redirect" value="${currentURL}" />
			<portlet:param name="ntucBulkUploadId" value="${seoBulkUpload.ntucBulkUploadId}" />
		</portlet:actionURL>
		<liferay-ui:icon-delete url="${deleteSeoBulkUploadURL}" />
</liferay-ui:icon-menu>


