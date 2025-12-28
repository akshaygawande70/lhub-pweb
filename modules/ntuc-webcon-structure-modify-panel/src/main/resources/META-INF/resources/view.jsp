<%@page import="com.ntuc.webcontent.structure.panel.util.WebcontentModifyUtil"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/init.jsp" %>
<%@page import="com.liferay.portal.kernel.util.ParamUtil" %>


<portlet:actionURL var="modifyDDMStructureURL" name="modifyDDMStructure">
</portlet:actionURL>

<portlet:renderURL var="redirectPage">
    <portlet:param name="mvcRenderCommandName" value="/configure/email" />
</portlet:renderURL>

<div class="container">
	<div class="dl-btn d-flex justify-content-between">
		<h4>Change Web Content Label</h4>
		<%if(WebcontentModifyUtil.isAdminRole()){ %>
			<a href="<%=redirectPage%>">
				<clay:icon symbol="cog" />
			</a>
		<%} %>
	</div>
	<aui:form id="editDDMForm" action="<%=modifyDDMStructureURL%>" method="post" name="editDDMForm" enctype="multipart/form-data">
	    <aui:input name="webContentStructureID" required="true" type="number" label="Web Content Structure ID"></aui:input>
	    <aui:input name="fieldName" required="true" type="text" label="Structure Field Name"></aui:input>
	    <aui:input name="content" required="true" type="text" label="New Label"></aui:input>
	    <aui:button name="submitBtn" type="submit" value="Submit"></aui:button>
	</aui:form>
</div>

<liferay-ui:success key="success" message="Web content label update started and it will be running in the background, you will get the email notification once it is completed."/>
<style>
.dl-btn .lexicon-icon{
	height: 2em;
    width: 1.4em;
    margin-top: auto;
    color: midnightblue;
}
</style>