<%@ include file="../init.jsp" %>

<liferay-ui:success key="success-msg" message="success-msg" />
<liferay-ui:error key="<%=NtucSeoBulkuploadControlPanelAppPortletKeys.WRONG_EXCEL %>" message="<%=NtucSeoBulkuploadControlPanelAppPortletKeys.WRONG_EXCEL %>" />
<liferay-ui:error key="<%=NtucSeoBulkuploadControlPanelAppPortletKeys.EMPTY_EXCEL %>" message="<%=NtucSeoBulkuploadControlPanelAppPortletKeys.EMPTY_EXCEL %>" />
<liferay-ui:error key="" message="error-msg"/>
<portlet:resourceURL  var="DownloadURL" id="DownloadURL"></portlet:resourceURL>
<portlet:resourceURL  var="validateFileNameURL" id="validateFileName"></portlet:resourceURL>
<portlet:actionURL name='uploadExcel' var="uploadFileURL" />

<div class="container p-0">
	<div class="mb-3 management-bar-light p-3">
		<aui:form action="${uploadFileURL}" id="bulkuploadForm" name="bulkuploadForm" enctype="multipart/form-data">
			<aui:fieldset>
				<div class="bulk-upload-box"> 
					<aui:input required="true" type="file" id="bulkuploadFile" name="fileName" label="Upload" helpMessage="Load Excel file">
						<aui:validator name="acceptFiles">'xls,xlsx'</aui:validator>
						<aui:validator name="required" errorMessage="Please upload file"></aui:validator>
					</aui:input>
					<aui:button type="button" value="Upload" onClick="bulkuploadSubmit()"/>
					<a href="${DownloadURL}" class="btn btn-outline-primary">Download SEO Template</a>
				</div>
			</aui:fieldset>
		</aui:form>
		
	</div>
</div>

<div class="modal fade bulkupload-modal" id="bulkUploadCenter" tabindex="-1" role="dialog"
                			        data-backdrop="static" aria-labelledby="bulkUploadCenterTitle" aria-hidden="true" style="display:none;">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="sol-update-main">
                    <div class="sol-update">
                        <div class="warn-msg">
                        </div>
                        <div class="button-holder">
                            <button class="bulkupload-yes btn btn-primary" data-dismiss="modal"><liferay-ui:message key="yes"/></button>
                            <button class="bulkupload-no btn btn-danger" data-dismiss="modal"><liferay-ui:message key="no"/></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function bulkuploadSubmit(){
     AUI().use('aui-base', function(A){
         var form = A.one("#<portlet:namespace/>bulkuploadForm");
         var liferayForm = Liferay.Form.get(form.attr('id'));
         if (liferayForm) {
             var validator = liferayForm.formValidator;

             if (A.instanceOf(validator, A.FormValidator)) {
                validator.validate();
                hasErrors = validator.hasErrors();
                 if (hasErrors) {
                     validator.focusInvalidField();
                     return;
                 }
                 else{
                     validateFileName();
                 }
             }
         }
      });
}

function buFormSubmit(){
    $("#<portlet:namespace/>bulkuploadForm").submit();
}
$(document).on('click', '.bulkupload-yes', function(){
	buFormSubmit();
});
 
function validateFileName(){
   var bulkuploadFile = document.getElementById("<portlet:namespace/>bulkuploadFile");
   console.log("filename : "+bulkuploadFile.files[0].name);
    AUI().use(
    'aui-io-request',
    function(A) {
        A.io.request(
                '<%=validateFileNameURL%>',
            {
                method : 'POST',
                dataType: 'json',
                data : {
                    <portlet:namespace />fileName : bulkuploadFile.files[0].name
                },
                sync : true,
                on : {
                    success : function() {

                        var data = this
                                .get('responseData');
                        var warningMsg = '';
                        var failedFieldName = '';
                        if(data != null){
                            if(data.isFileNameExist){
                                 warningMsg = `<liferay-ui:message key='bulkupload-file-name-is-already-exist'/>`;
                                 $(".warn-msg").html(warningMsg);
                                 $("#bulkUploadCenter").modal({backdrop: 'static', keyboard: false},'show');
                            }
                            else{
                                buFormSubmit();
                            }
                        }
                    },
                    failure : function() {
                    }
                }
            });
    });
}
</script>
