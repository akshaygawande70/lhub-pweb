
<%@ include file="init.jsp" %>

<liferay-ui:error
		key="exception_session_msg">
	<% String duplicateError = ((String) SessionErrors.get(liferayPortletRequest, "exception_session_msg")); %>
	<%= duplicateError%>
</liferay-ui:error>
<%@ include file="/seoBulkUpload/missURL.jsp" %>
<%
	long seoBulkUploadCount = (long) request.getAttribute("seoBulkUploadCount");
	int i = 1;
%>

<liferay-ui:success key="seoBulkUploadDeleted" message="SEO-BulkUpload-deleted-successfully" />

<div class="container">
	<h3>SEO Bulk Upload Logs</h3>
	<clay:management-toolbar
		disabled="${seoBulkUploadCount eq 0}"
		displayContext="${panelAppManagementToolbarDisplayContext}"
		itemsTotal="${seoBulkUploadCount}"
		searchContainerId="seoBulkUploadEntries"
		selectable="false"
	/>

	<liferay-ui:search-container id="seoBulkUploadEntries" emptyResultsMessage="no-data" iteratorURL="${portletURL}" total="${seoBulkUploadCount}">
		<liferay-ui:search-container-results results="${seoBulkUploads}"/>
		<liferay-ui:search-container-row className="svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload" modelVar="seoBulkUpload">
			<liferay-ui:search-container-column-text name="No." value="<%= String.valueOf(i++) %>"/>
			<liferay-ui:search-container-column-text name="File Name" value="<%= seoBulkUpload.getFileName()%>"/>
			<%
				User user2 = UserLocalServiceUtil.getUser(seoBulkUpload.getUserId());	
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");  
				dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
				String strDate = dateFormat.format(seoBulkUpload.getCreateDate());  
				String status = StatusCommonUtil.getStatus(seoBulkUpload.getRowData()); 
			%>
			<liferay-ui:search-container-column-text name="Upload Date" value="<%= strDate%>"/>
			<liferay-ui:search-container-column-text name="Uploaded by User" value="<%= user2.getFullName()%>"/>
			<liferay-ui:search-container-column-text name="Upload Status" value="<%= status%>"/>
			<liferay-ui:search-container-column-jsp path="/seoBulkUpload/entry_action.jsp"/>
		</liferay-ui:search-container-row>
		<liferay-ui:search-iterator displayStyle="${panelAppManagementToolbarDisplayContext.getDisplayStyle()}" markupView="lexicon"/>
	</liferay-ui:search-container>
</div>


<div class="modal fade" id="failedDownloadModal" style="display:none;width:40%;left:30%;overflow-y:hidden;" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered vertical-align-center" role="document">
    <div class="modal-content">
      <div class="modal-body">
      		 <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
       		 The excel will be generated, you can download it from <strong>Content & Data > Documents and Media > Failed SEO Upload Excels</strong> folder.
      </div>
    </div>
  </div>
</div>

<portlet:resourceURL  var="exportData"  id="exportData" />
<script type="text/javascript">
	function failedDownload(bulkUploadId){
		console.log("bulkUploadId : "+bulkUploadId);
		 $.ajax({
	            url: '<%= exportData %>',  // Create a Liferay resource URL for the AJAX call
	            method: 'POST',
	            data: {
	            	<portlet:namespace/>bulkUploadId: bulkUploadId
	            },
	            success: function(response) {
	              $("#failedDownloadModal").modal('show');
	            },
	            error: function(xhr, status, error) {
	                //alert('Error: ' + error);
	            }
	        });
	}
</script>
