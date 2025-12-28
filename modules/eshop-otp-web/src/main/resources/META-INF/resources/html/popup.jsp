<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="web.ntuc.eshop.otp.constants.MVCCommandNames"%>
<%@ include file="/init.jsp" %>

<portlet:actionURL var="resendOtpURL"
	name="<%=MVCCommandNames.OTP_RESEND_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<section class="opps-wrong">
	<div class="container">
		<div class="row">
			<div class="col-md-6 box-auto">
				<div class="title">Opps</div>

				<div class="desc">Something went wrong</div>

				<div class="row  box-btn">
					<div class="col-md-6">
						<button class="btn-blue w100" onclick="javascript:closeDialog();">Try Again</button>
					</div>

					<div class="col-md-6">
						<a class="btn-blue-no-block" href="<%=resendOtpURL.toString()%>">Resend Code</a>
					</div>
				</div>
			</div>

			<div class="col-md-6">
				<img alt="" class="img-fluid"
					src="/o/ntuclearninghub-commerce-theme/images/asset_commerce/people-help.jpg" />
			</div>
		</div>
	</div>
</section>
