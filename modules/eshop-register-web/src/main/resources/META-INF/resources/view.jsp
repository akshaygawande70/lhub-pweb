<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="web.ntuc.eshop.register.constants.MVCCommandNames"%>
<%@ include file="/init.jsp" %>

<liferay-ui:error key="error-validate" message="corporate-validate-fail" />
<liferay-ui:success key="success-sign-up" message="adding-user-success" />
<liferay-ui:success key="please-login-checkout" message="please-login-checkout" />
<liferay-ui:success key="please-login-invoice" message="please-login-invoice" />
<liferay-ui:error key="nric-fail" message="nric-fail" />
<liferay-ui:error key="email-fail" message="email-fail" />
<liferay-ui:error key="password-fail" message="password-fail" />
<liferay-ui:error key="password-confirmation-fail" message="password-confirmation-fail" />
<liferay-ui:error key="dob-fail" message="dob-fail" />
<liferay-ui:error key="fullname-fail" message="fullname-fail" />
<liferay-ui:error key="dob-null" message="dob-null" />
<%
	String backURL = themeDisplay.getPortalURL() + "/registration";
%>

<portlet:renderURL var="registerIndividuViewURL"
	windowState="<%=LiferayWindowState.MAXIMIZED.toString()%>">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.REGISTER_INDIVIDUAL_RENDER%>" />
	<portlet:param name="code"
		value="${code}" />
	<portlet:param name="redirect" value="<%=backURL %>"></portlet:param>
		
</portlet:renderURL>

<portlet:renderURL var="registerIndividuViewURL"
	windowState="<%=LiferayWindowState.MAXIMIZED.toString()%>">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.REGISTER_INDIVIDUAL_RENDER%>" />
	<portlet:param name="redirect" value="<%=backURL %>"></portlet:param>
</portlet:renderURL>

<portlet:renderURL var="registerCorporateViewURL"
	windowState="<%=LiferayWindowState.MAXIMIZED.toString()%>">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="mvcRenderCommandName"
		value="<%=MVCCommandNames.REGISTER_CORPORATE_RENDER%>" />
	<portlet:param name="redirect" value="<%=backURL %>"></portlet:param>
</portlet:renderURL>

<section class="create-account">
	<div class="container">
		<div class="row">
			<div class="col-md-6">
				<div class="box-new">
					<div class="title">Create New Account</div>

					<div class="small">I am registering for this account as a/an</div>
					<form id="registerForm">
						<div class="box-radio-login">
							<label class="radio-inline">
								<input checked="checked" name="optradio" type="radio" id="individual" value="individual" />Individual
							</label> 
							<label class="radio-inline">
								<input name="optradio" type="radio" id="corporate" value="corporate"/>Company
							</label>
						</div>
						<a class="btn-grey w-100" href="#">Create An Account</a>
					</form>
				</div>
			</div>
		</div>
	</div>
</section>

<script type="text/javascript">

$(document).ready(function() {
	radioChange();
	var url_string = window.location.href;
	var url = new URL(url_string);
	var code = url.searchParams.get('code');
	if(code !== null && code !== undefined) {
		setTimeout(function(){ 
			landingPage("${registerIndividuViewURL}");
		});
	}
});

$('#registerForm input[type="radio"]').click(function() {
	radioChange();
});

function radioChange() {
	var individual = $("#individual").is(":checked");
    var corporate = $("#corporate").is(":checked");

    var url = "";
    if (individual) {
       addr = "${registerIndividuViewURL}";
    } else {
		addr = "${registerCorporateViewURL}";
    }
    //$("#registerForm a").attr('href', 'javascript:showPopup("'+addr+'")');
    $("#registerForm a").attr('href', 'javascript:landingPage("'+addr+'")');
}

function landingPage(url) {
	window.location.href = url;
	
}
</script>



<aui:script use="aui-base,aui-io-request">
AUI().use('aui-base',
		'aui-io-plugin-deprecated',
		'liferay-util-window',
		function(A) {
		var popUpWindow=Liferay.Util.Window.getWindow(
			{
			dialog: { 
			centered: true,
			constrain2view: true,
			modal: true,
			resizable: false,
			width: 1000,
			height : 700
			},
			title : 'Registration form',
			id: 'dialog'
			}
			).plug(
			A.Plugin.IO,
			{
			autoLoad: false
			}).hide();
	
		window.showPopup = function(url){
		popUpWindow.show();
		popUpWindow.io.set('uri',url);
		popUpWindow.io.start();

		};
		
		window.closeDialog = function(){
			popUpWindow.hide();
		}
		});

</aui:script>
