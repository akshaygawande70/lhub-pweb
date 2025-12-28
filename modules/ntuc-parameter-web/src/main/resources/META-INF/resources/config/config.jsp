<%@page import="web.ntuc.nlh.parameter.config.ParameterConfig"%>
<%@include file="../init.jsp"%>

<liferay-portlet:actionURL portletConfiguration="<%=true%>"
	var="configActionURL" />

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@ page import="com.liferay.portal.kernel.util.Validator"%>

<%
	String groupId = "";

	groupId = portletPreferences.getValue(ParameterConfig.GROUP_ID, "0");
%>

<c:set var="groupId" value="<%=groupId%>" />

<aui:form action="${configActionURL}" method="post" name="fm">
	<liferay-ui:panel-container extended="true" id="scope">
		<aui:input type="hidden" name="<%=Constants.CMD%>"
			value="<%=Constants.UPDATE%>" />

		<liferay-ui:panel title="" collapsible="false" extended="true"
			defaultState="expanded">
			<aui:fieldset label="">
				<div class="row">
					<aui:select name="groupId" value="${groupId}" label="Group Site">
						<aui:option value="0">All</aui:option>
						<c:forEach items="${groups}" var="c">
							<aui:option value="${c.groupId }">${c.groupId } - ${c.name } - ${c.friendlyURL }</aui:option>
						</c:forEach>
					</aui:select>
				</div>
			</aui:fieldset>
		</liferay-ui:panel>

	</liferay-ui:panel-container>
	<aui:button-row>
		<aui:button type="submit" value="save"></aui:button>
	</aui:button-row>
</aui:form>