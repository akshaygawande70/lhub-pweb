<%@page import="com.ntuc.notification.constants.CourseStatusConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ntuc.notification.model.NtucSB"%>
<%@ page import="com.ntuc.notification.service.NtucSBLocalServiceUtil"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>

<%@ include file="/init.jsp"%>

<%
	List<NtucSB> notifications = NtucSBLocalServiceUtil.getNtucSBs(-1, -1);
%>

<!-- Resource & Action URLs -->
<portlet:resourceURL id="/retriggerEntry" var="retriggerResourceURL" />
<portlet:actionURL name="deleteByDateRange" var="deleteActionURL" />

<!-- CSS -->
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap4.min.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />

<!-- JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap4.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

<!-- Bootstrap Toggle (optional) -->
<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>

<!-- <link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.5.0/css/responsive.bootstrap4.min.css" />
<script src="https://cdn.datatables.net/responsive/2.5.0/js/dataTables.responsive.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.5.0/js/responsive.bootstrap4.min.js"></script> -->

<style>
.toast {
	opacity: .95;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, .3);
}

#dt5Table thead th {
	white-space: nowrap;
}
</style>

<div class="px-4">
	<h2 class="text-center mt-4">DT5 Notifications</h2>

	<!-- Non-AJAX messages (e.g., delete form submit) -->
	<liferay-ui:success key="notificationsDeleted"
		message="Notifications successfully deleted." />
	<liferay-ui:error key="errorDeletingNotifications"
		message="Unable to delete notifications." />

	<!-- Toggle Switch for Delete Form -->
	<div class="form-group mb-3">
		<input type="checkbox" id="toggleDeleteForm" data-toggle="toggle"
			data-on="Delete On" data-off="Delete Off" data-onstyle="danger"
			data-offstyle="info">
	</div>

	<!-- DateRange Delete Form (non-AJAX) -->
	<div id="deleteFormContainer" style="display: none;">
		<form method="post" action="${deleteActionURL}"
			class="p-4 bg-light border rounded shadow-sm">
			<h5 class="mb-3 text-danger">Select Notification Date Range</h5>
			<div class="form-row align-items-end">
				<div class="form-group col-md-3">
					<label for="fromDate">From:</label> <input type="text"
						id="fromDate" name="<portlet:namespace/>fromDate"
						class="form-control datepicker" placeholder="YYYY-MM-DD" required
						autocomplete="off" />
				</div>
				<div class="form-group col-md-3">
					<label for="toDate">To:</label> <input type="text" id="toDate"
						name="<portlet:namespace/>toDate" class="form-control datepicker"
						placeholder="YYYY-MM-DD" required autocomplete="off" />
				</div>
				<div class="form-group col-md-3">
					<button type="submit" class="btn btn-danger mt-4 w-100">
						<i class="fas fa-trash-alt mr-2"></i>Delete Notifications
					</button>
				</div>
			</div>
		</form>
	</div>

	<!-- DataTable -->
	<div class="table-responsive">
  		<table id="dt5Table" class="table table-bordered w-100 text-center" width="100%">
			<thead class="thead-light" style="background-color: #bbbfc4;">
				<tr class="text-center">
					<th>Ref ID</th>
					<th>Notification ID</th>
					<th>Course Code</th>
					<th>Course Type</th>
					<th>Notification Date</th>
					<th>System Date</th>
					<th>Event</th>
					<th>Change From</th>
					<th>Processing Status</th>
					<th>Course Status</th>
					<th>Critical Processed</th>
					<th>Non-Critical Processed</th>
					<th>Cron Processed</th>
					<th>Retries Attempted</th>
					<th>Last Retry</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<%
					for (NtucSB entry : notifications) {
				%>
				<tr>
					<td><%=entry.getNtucDTId()%></td>
					<td><%=entry.getNotificationId()%></td>
					<td><%=entry.getCourseCode()%></td>
					<td><%=entry.getCourseType()%></td>
					<td data-order="<%= entry.getNotificationDate() != null ? entry.getNotificationDate().getTime() : 0 %>">
					  <fmt:formatDate value="<%=entry.getNotificationDate()%>"
					                  pattern="dd/MM/yyyy HH:mm:ss" />
					</td>
					<td data-order="<%= entry.getSystemDate() != null ? entry.getSystemDate().getTime() : 0 %>">
					  <fmt:formatDate value="<%=entry.getSystemDate()%>"
					                  pattern="dd/MM/yyyy HH:mm:ss" />
					</td>
					<td><%=entry.getEvent()%></td>
					<td><%=entry.getChangeFrom()%></td>
					<td><%=entry.getProcessingStatus()%></td>
					<td><%=(entry.getCourseStatus().equalsIgnoreCase(CourseStatusConstants.PUBLISHED) || entry.getCourseStatus().equalsIgnoreCase("approved"))? "Approved" : entry.getCourseStatus() %></td>
					<td><%=entry.getIsCriticalProcessed() ? "Yes" : "No"%></td>
					<td><%=entry.getIsNonCriticalProcessed() ? "Yes" : "No"%></td>
					<td><%=entry.getIsCronProcessed() ? "Yes" : "No"%></td>
					<td><%=entry.getTotalRetries()%></td>
					<td><fmt:formatDate value="<%=entry.getLastRetried()%>"
							pattern="dd/MM/yyyy HH:mm:ss" /></td>
					<td>
						<%-- <%
							if (entry.getCanRetry()) {
						%> --%>
						<button type="button" class="btn btn-sm btn-warning retrigger-btn"
							data-entry-id="<%=entry.getNtucDTId()%>">Retrigger</button> <%-- <%
						 	} else {
						 %> --%>
						<!-- <h5>
							<span class="badge badge-secondary font-weight-normal">None</span>
						</h5> --> 
						<%-- <%
						 	}
						 %> --%>
					</td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
</div>

<script>
  (function () {
    const ns = '<portlet:namespace/>';
    const retriggerURL = '${retriggerResourceURL}';

    // UI init
    $(function () {
      $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayHighlight: true,
        orientation: 'bottom right',
        container: 'body'
      });

      $('#dt5Table').DataTable({
    	  paging: true,
    	  searching: true,
    	  lengthChange: true,
    	  pageLength: 10,
    	  order: [[5, 'desc']]   // 5 = System Date column, 'desc' = latest first
    	});


      $('#dt5Table_filter input').attr('autocomplete', 'off');

      $('#toggleDeleteForm').change(function () {
        $('#deleteFormContainer')[$(this).prop('checked') ? 'slideDown' : 'slideUp']();
      });
    });

    // AJAX retrigger
    $(document)
      .off('click.ntuc', '.retrigger-btn')
      .on('click.ntuc', '.retrigger-btn', function (e) {
        e.preventDefault();

        const button = $(this);
        if (button.data('busy')) return;

        const entryId = button.data('entry-id');

        button.data('busy', true)
              .prop('disabled', true)
              .html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Retriggering...');

        $.ajax({
          type: 'POST',
          url: retriggerURL,       // MVCResourceCommand -> JSON
          dataType: 'json',
          data: { [ns + 'entryId']: entryId }
        })
        .done(function (resp) {
          const ok = resp && resp.success === true;
          const msg = ok ? "#" + entryId + ' Retriggered successfully' : 'Failed to retrigger #' + entryId;

          showToast(msg, !ok);

          if (ok) {
            button.html('<i class="fas fa-check-circle mr-1" aria-hidden="true"></i> Done')
                  .removeClass('btn-warning').addClass('btn-success');

            // Optional: lock the action cell after success
            // button.closest('td').html('<h5><span class="badge badge-secondary font-weight-normal">None</span></h5>');
          } else {
            button.prop('disabled', false).html('Retry');
          }
        })
        .fail(function () {
          showToast('Failed to retrigger #' + entryId, true);
          button.prop('disabled', false).html('Retry');
        })
        .always(function () {
          button.data('busy', false);
        });
      });
  })();

  function showToast(message, isError) {
	  // escape message
	  var safeMsg = $('<div>').text(message).html();

	  // build toast markup without template literals
	  var toast = $('<div class="toast" role="alert" aria-live="assertive" aria-atomic="true"></div>')
	    .css({ position: 'fixed', top: '20px', right: '20px', minWidth: '240px', zIndex: 9999 })
	    .addClass(isError ? 'bg-danger text-white' : 'bg-success text-white')
	    .append(
	      '<div class="d-flex">' +
	        '<div class="toast-body">' + safeMsg + '</div>' +
	        // Bootstrap 4 uses data-dismiss, not data-bs-dismiss
	        '<button type="button" class="ml-2 mb-1 close text-white" data-dismiss="toast" aria-label="Close">' +
	          '<span aria-hidden="true">&times;</span>' +
	        '</button>' +
	      '</div>'
	    );

	  $('body').append(toast);

	  // Bootstrap 4 jQuery API
	  toast.toast({ autohide: true, delay: 10000 }); // auto-fade in 10s if untouched
	  toast.on('hidden.bs.toast', function () { $(this).remove(); }); // cleanup after hide
	  toast.toast('show');
	}


</script>
