<%@page import="web.ntuc.nlh.courses.config.CoursesConfig"%>
<%@include file="../init.jsp"%>

<liferay-portlet:actionURL portletConfiguration="<%=true%>"
	var="configActionURL" />

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@ page import="com.liferay.portal.kernel.util.Validator"%>

<aui:form action="${configActionURL}" method="post" name="fm">
	<liferay-ui:panel-container extended="true" id="scope">
		<aui:input type="hidden" name="<%=Constants.CMD%>"
			value="<%=Constants.UPDATE%>" />

		<liferay-ui:panel title="" collapsible="false" extended="true"
			defaultState="expanded">
			<aui:fieldset label="">
				<div class="row">
					<aui:select name="themes" value="${themes}" label="Themes">
					<aui:option value="0">Select Course Themes</aui:option>
						<c:forEach items="${themes}" var="t">
							<aui:option value="${t.categoryId }">${t.categoryId} - ${t.name}</aui:option>
						</c:forEach>
					</aui:select>
				</div>
			</aui:fieldset>

			<aui:fieldset label="">
				<div class="row">
					<aui:select name="topics" value="${topics}" label="Topics">
						<aui:option value="0">All Related Topics</aui:option>
						<c:forEach items="${topics}" var="s">
							<aui:option value="${s.categoryId }">${s.categoryId} - ${s.name}</aui:option>
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