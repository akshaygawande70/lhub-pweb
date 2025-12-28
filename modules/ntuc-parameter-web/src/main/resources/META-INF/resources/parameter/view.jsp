<%@page import="web.ntuc.nlh.parameter.constants.MVCCommandNames"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<portlet:renderURL var="updateParameterURL">
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.UPDATE_PARAMETER_RENDER%>" />
	<portlet:param name="authToken" value="${authToken}" />
</portlet:renderURL>

<portlet:resourceURL id="<%=MVCCommandNames.PARAMETER_DATA_RESOURCES%>"
	var="searchParameterURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:renderURL var="parameterViewURL"
	windowState="<%=LiferayWindowState.POP_UP.toString()%>">
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.VIEW_PARAMETER_RENDER%>" />
	<portlet:param name="redirect"
		value="<%=themeDisplay.getURLCurrent()%>" />
</portlet:renderURL>

<div class="box-dynamic-item" id="dynamic-parameter">
	<div class="box-unit-inner">
		<div class="box-unit-header">
			<div class="box-unit-filter">
				<label><liferay-ui:message key="parameter" /></label>
			</div>
		</div>
		<div class="box-unit-content">
			<div class="box-unit-content-inner" style="display: block;">
				<div class="box-act" style="margin-bottom: 3rem;">
					<c:if test="${addParameter || isAdmin}">
						<a href="${updateParameterURL}" class="btn btn-md btn-primary"><liferay-ui:message
								key="add-parameter" /><em class="fas fa-plus"></em> </a>
					</c:if>
				</div>
				<table class="unit-table CX-Tables mt-3" id="<portlet:namespace/>parameter" aria-label="Parameter Admin">
					<thead>
						<tr>
							<th id="group-name"><liferay-ui:message key="group-name" /></th>
							<th id="param-code"><liferay-ui:message key="param-code" /></th>
							<th id="param-name"><liferay-ui:message key="param-name" /></th>
							<th id="param- value"><liferay-ui:message key="param-value" /></th>
							<th id="created-date"><liferay-ui:message key="created-date" /></th>
							<th id="modifiedDate"><liferay-ui:message
									key="modified-date" /></th>
							<c:if
								test="${editParameter || deleteParameter || isAdmin || viewParameter}">
								<th style="text-align: center !important;" id="action"><liferay-ui:message
										key="action" /></th>
							</c:if>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>