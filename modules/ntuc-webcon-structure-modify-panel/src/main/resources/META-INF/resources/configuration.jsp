<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.petra.string.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.UnicodeFormatter"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@ include file="/init.jsp" %>

<liferay-portlet:actionURL name="emailConfiguration" var="configurationURL" />

<%  
String fromAddress = GetterUtil.getString(portletPreferences.getValue("fromAddress", StringPool.BLANK));
String toAddressSuccess = GetterUtil.getString(portletPreferences.getValue("toAddressSuccess", StringPool.BLANK));
String toAddressFailure = GetterUtil.getString(portletPreferences.getValue("toAddressFailure", StringPool.BLANK));
String successBody = GetterUtil.getString(portletPreferences.getValue("successBody", StringPool.BLANK));
String failureBody = GetterUtil.getString(portletPreferences.getValue("failureBody", StringPool.BLANK));
String subject = GetterUtil.getString(portletPreferences.getValue("subject", StringPool.BLANK));
%>

<div class="container">
	<h4>Configure Mailers</h4>
	<aui:form action="<%= configurationURL %>" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
		
		<aui:input name="fromAddress" type="text" value="<%= fromAddress %>" />
		<aui:input name="subject" type="text" value="<%= subject %>" />
		<div class="form-group" style="padding:15px; border-style: ridge; border-color:green">
            <h4>Success Case:</h4>
            <aui:input name="toAddressSuccess" label="To Address (Add multiple email-ids with semicolon[;] seperated)" type="text" value="<%= toAddressSuccess %>" />
            <aui:field-wrapper label="Email Body">
                <liferay-ui:input-editor name="successBody" toolbarSet="slimmed" initMethod="initEditor" width="200" />
                <script type="text/javascript">
                    function <portlet:namespace />initEditor() { return "<%= UnicodeFormatter.toString(successBody) %>"; }
                </script>
            </aui:field-wrapper>
            </div>
            <div class="form-group"style="padding:15px; border-style: ridge; border-color:red">
                    <h4>Failure Case:</h4>
                    <aui:input name="toAddressFailure" label="To Address (Add multiple email-ids with semicolon[;] seperated)" type="text" value="<%= toAddressFailure %>" />
                    <aui:field-wrapper label="Email Body">
                        <liferay-ui:input-editor name="failureBody" toolbarSet="slimmed" initMethod="initEditor" width="200" />
                        <script type="text/javascript">
                            function <portlet:namespace />initEditor() { return "<%= UnicodeFormatter.toString(failureBody) %>"; }
                        </script>
                    </aui:field-wrapper>
            </div>
		<aui:button-row>
			<aui:button type="submit" />
		</aui:button-row>
	</aui:form>
</div>