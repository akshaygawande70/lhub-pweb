<%@ include file="init.jsp"%>

<liferay-ui:error key="no-group-parameter-available" message="no-group-parameter-available" />
<liferay-ui:error key="you-dont-have-permission-or-your-session-is-end" message="you-dont-have-permission-or-your-session-is-end" />
<liferay-ui:error key="your-input-not-pass-xss" message="your-input-not-pass-xss" />


<section class="CX-main CX-admin-content">
	<div class="container CX-wrapper">
		<div class="box-unit">
			<ul class="control-unit tab-slide">
				<li class="active"><a href="javascript:void(0)" data-slide="1"><liferay-ui:message
							key="parameter-group" /></a></li>
				<li><a href="javascript:void(0)" data-slide="2"><liferay-ui:message
							key="parameter" /></a></li>
			</ul>
			<div class="box-dynamic-slider cx-tab-slider">
				<%@include file="parameter-group/view.jsp"%>

				<%@include file="parameter/view.jsp"%>
			</div>
		</div>
	</div>
</section>

<script type="text/javascript">
	function confirmDelete() {
		var m = '<liferay-ui:message key="delete-pop-up"/>';
		return confirm(m);
	}

	$(document).ready(function() {
		var groupTableId = '#<portlet:namespace/>parameterGroup';
		var paramGroupDataTable = $(groupTableId).dataTable({
			"bFilter" : true,
			"responsive" : true,
			"bServerSide" : true,
			"bAutowidth" : false,
			"bLengthChange" : false,
			"bInfo" : true,
			"sAjaxSource" : '${searchGroupURL}',
			"aoColumnDefs" : [ {
				"mDataProp" : "groupName",
				"bSortable" : true,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "groupCode",
				"bSortable" : true,
				"aTargets" : [ 1 ]
			}, {
				"mDataProp" : "createDate",
				"bSortable" : true,
				"aTargets" : [ 2 ]
			}, {
				"mDataProp" : "modifiedDate",
				"bSortable" : true,
				"aTargets" : [ 3 ]
			}, {
				"aTargets" : [ 4 ],
				"bSortable" : false,
				"mData" : null,
				"mRender" : function(data, type, full) {
					return parameterGroupActionButton(data);
				}
			} ]
		});

		var parameterTableId = '#<portlet:namespace/>parameter';
		var paramDataTable = $(parameterTableId).dataTable({
			"bFilter" : true,
			"responsive" : true,
			"bServerSide" : true,
			"bAutowidth" : false,
			"bLengthChange" : false,
			"bInfo" : true,
			"sAjaxSource" : '${searchParameterURL}',
			"aoColumnDefs" : [ {
				"mDataProp" : "groupName",
				"bSortable" : true,
				"aTargets" : [ 0 ]
			}, {
				"mDataProp" : "paramCode",
				"bSortable" : true,
				"aTargets" : [ 1 ]
			}, {
				"mDataProp" : "paramName",
				"bSortable" : true,
				"aTargets" : [ 2 ]
			}, {
				"aTargets" : [ 3 ],
				"bSortable" : true,
				"mData" : null,
				"mRender" : function(data, type, full) {
					return formatParamValue(data);
				}
			}, {
				"mDataProp" : "createDate",
				"bSortable" : true,
				"aTargets" : [ 4 ]
			}, {
				"mDataProp" : "modifiedDate",
				"bSortable" : true,
				"aTargets" : [ 5 ]
			}, {
				"aTargets" : [ 6 ],
				"bSortable" : false,
				"mData" : null,
				"mRender" : function(data, type, full) {
					return parameterActionButton(data);
				}
			} ]
		});
	});

	function parameterGroupActionButton(data) {
		var btn = "<div class='box-action text-center'>";
		<c:if test="${isAdmin || viewParameter}">
		btn += '<a class="btn btn-xs btn-border" href="javascript:showPopupGroup('
				+ data.parameterGroupId + ');">';
		btn += '<liferay-ui:message key="view"/><i class="fa fa-eye"></i></a>';
		</c:if>
		<c:if test="${isAdmin || editGroup}">
		btn += '<a href=\"'+ data.editUrl+ '\" class="btn btn-xs btn-border">';
		btn += '<liferay-ui:message key="edit"/><i class="far fa-pencil-alt"></i></a>';
		</c:if>
		<c:if test="${isAdmin || deleteGroup}">
		btn += '&nbsp;';
		btn += '<a href=\"'
				+ data.deleteUrl
				+ '\" onclick="return confirmDelete()" class="btn btn-xs btn-border">';
		btn += '<liferay-ui:message key="delete"/><i class="far fa-trash-alt"></i></a>';
		</c:if>
		return btn + "</div>";
	}

	function showPopupGroup(id) {
		var popupURL = '${parameterGroupViewURL}';
		popupURL = encodeURI(popupURL) + "&<portlet:namespace/>id=" + id;

		Liferay.Util.openWindow({
			cache : false,
			dialog : {
				align : Liferay.Util.Window.ALIGN_CENTER,
				destroyOnHide : true,
				modal : true,
				width : 1000,
				height : 700,
			},
			id : 'parameter-group-' + id,
			title : 'Parameter Group Detail',
			uri : popupURL
		});
	}

	function formatParamValue(data) {
		var text = new String(data.paramValue);
		if (text.length > 27) {
			newText = text.slice(0, 27);
			newText += "<span role=\"button\" data-toggle=\"tooltip\" ";
			newText	+= "data-placement=\"right\" data-content=\"" + text; 
			newText+= "\"data-original-title=\""+ text;
			newText+= "\"title=\""+ text + "\">...</span>";

			text = newText;
		}
		return text;
	}

	function parameterActionButton(data) {
		var btn = "<div class='box-action text-center'>";
		<c:if test="${isAdmin || viewParameter}">
		btn += '<a class="btn btn-xs btn-border" href="javascript:showPopup('
				+ data.parameterId + ');">';
		btn += '<liferay-ui:message key="view"/><i class="fa fa-eye"></i></a>';
		</c:if>
		<c:if test="${isAdmin || editParameter}">
		btn += '<a href=\"'+ data.editUrl + '\" class="btn btn-xs btn-border">';
		btn += '<liferay-ui:message key="edit"/><i class="far fa-pencil-alt"></i></a>';
		</c:if>
		<c:if test="${isAdmin || deleteParameter}">
		btn += '&nbsp;';
		btn += '<a href=\"'
				+ data.deleteUrl
				+ '\" onclick="return confirmDelete()" class="btn btn-xs btn-border">';
		btn += '<liferay-ui:message key="delete"/><i class="far fa-trash-alt"></i></a>';
		</c:if>
		return btn + "</div>";
	}

	function showPopup(id) {
		var popupURL = '${parameterViewURL}';
		popupURL = encodeURI(popupURL) + "&<portlet:namespace/>id=" + id;

		Liferay.Util.openWindow({
			cache : false,
			dialog : {
				align : Liferay.Util.Window.ALIGN_CENTER,
				destroyOnHide : true,
				modal : true,
				width : 1000,
				height : 700,
			},
			id : 'parameter-' + id,
			title : 'Parameter Detail',
			uri : popupURL
		});
	}
</script>