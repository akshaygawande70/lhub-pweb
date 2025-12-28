<%@page import="web.ntuc.nlh.parameter.constants.MVCCommandNames"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<portlet:renderURL var="updateGroupURL">
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.UPDATE_GROUP_RENDER%>" />
	<portlet:param name="authToken" value="${authToken}" />
</portlet:renderURL>

<portlet:resourceURL id="<%=MVCCommandNames.GROUP_DATA_RESOURCES%>"
	var="searchGroupURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:renderURL var="parameterGroupViewURL"
	windowState="<%=LiferayWindowState.POP_UP.toString()%>">
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.VIEW_GROUP_RENDER%>" />
	<portlet:param name="redirect"
		value="<%=themeDisplay.getURLCurrent()%>" />
</portlet:renderURL>

<div class="box-dynamic-item" id="dynamic-parameter-group">
	<div class="box-unit-inner">
		<div class="box-unit-header">
			<div class="box-unit-filter full">
				<label><liferay-ui:message key="parameter-group" /></label>
			</div>
		</div>
		<div class="box-unit-content">
			<div class="box-unit-content-inner" style="display: block;">
				<div class="box-act" style="margin-bottom: 3rem;">
					<c:if test="${addGroup || isAdmin}">
						<a href="${updateGroupURL}" class="btn btn-md btn-primary"><liferay-ui:message
								key="add-group" /><em class="fas fa-plus"></em> </a>
					</c:if>
				</div>
				<table class="unit-table CX-Tables mt-3"
					id="<portlet:namespace/>parameterGroup" aria-label="Parameter Group Admin">
					<thead>
						<tr>
							<th id="group-name"><liferay-ui:message key="group-name" /></th>
							<th id="group-code"><liferay-ui:message key="group-code" /></th>
							<th id="created-date"><liferay-ui:message key="created-date" /></th>
							<th id="modified-date"><liferay-ui:message key="modified-date" /></th>
							<c:if
								test="${editGroup || deleteGroup || isAdmin || viewParameter}">
								<th style="text-align: center !important;" id="action"><liferay-ui:message
										key="action" /></th>
							</c:if>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>