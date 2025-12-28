<%@page import="web.ntuc.nlh.mediafilter.config.MediaFilterConfig" %>
<%@include file="../init.jsp"%>

<liferay-portlet:actionURL portletConfiguration="<%=true%>"
	var="configActionURL" />

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@ page import="com.liferay.portal.kernel.util.Validator"%>

<aui:form action="${configActionURL}" method="post" name="fm">
	<liferay-ui:panel-container extended="true" id="scope">
		<aui:input type="hidden" name="<%=Constants.CMD%>" value="<%=Constants.UPDATE %>"/>
		
		<liferay-ui:panel title="" collapsible="false" extended="true" defaultState="expanded">
			<aui:fieldset label="">
				<div class="row">
					<aui:select name="topics" value="${topics}"
						label="Topic Categories">
						<aui:option value="0">Select Topic Purposes</aui:option>
						<c:forEach items="${topics}" var="t">
							<aui:option value="${t.topicId }">${t.topicId} - ${t.topicName}</aui:option>
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