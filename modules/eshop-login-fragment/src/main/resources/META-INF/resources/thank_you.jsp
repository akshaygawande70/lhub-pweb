<%@ include file="/init.jsp" %>
<%
//String login = (String)portletSession.getAttribute(WebKeys.FORGOT_PASSWORD_REMINDER_USER_EMAIL_ADDRESS);
//String login = ParamUtil.getString(request, "email");
%>
<section class="otp-cxrus-default">
	<div class="container">
		<div class="row justify-content-center align-items-center">
			<div class="position-relative">
				<div class="card p-2 text-center">
					<h6 class="title">Forgot Password</h6>
					<div class="small-abstrak">
						<span>Password reset link has been sent to ${HtmlUtil.escapeAttribute(login)}.</span>
						<p>Please follow the steps in the email to reset your
							password.</p>
					</div>
					<div class="small-desc">
						Don't receive the email?<a href="javascript:history.back();">Click here.</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>