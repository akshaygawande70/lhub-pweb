<%@page import="com.liferay.portal.kernel.servlet.SessionErrors"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="web.ntuc.eshop.myaccount.constants.MyAccountMessagesKeys"%>
<%@page import="com.liferay.portal.kernel.service.PasswordPolicyLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.PasswordPolicy"%>
<%@page import="web.ntuc.eshop.myaccount.constants.MVCCommandNames"%>
<%@ include file="../init.jsp"%>

<%
PasswordPolicy passwordPolicy = PasswordPolicyLocalServiceUtil.getDefaultPasswordPolicy(themeDisplay.getCompanyId());
%>
	
<portlet:actionURL name="<%=MVCCommandNames.UPDATE_PASSWORD_ACTION%>"
	var="updatePasswordURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<section class="general-info myaccount-cxrus">
	<div class="container">
		<div class="row">
			<div class="breadcrumb">
				<ul>
					<li><a href="/home" class="text-dark">Home</a></li>
					<li><a href="/account/profile" class="text-dark">My Account</a></li>
					<li>Change Password</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="text-center w100">
				<h1 class="h1-commerce">MY ACCOUNT</h1>
			</div>
		</div>
	</div>
	<c:forEach items="${accountList}" var="account">
		<div class="container">
			<div class="row">
				<div class="box-name">
					<div class="name" id="capital-name">HI, ${fullNameCap}</div>
					<div class="email">${account.email}</div>
				</div>
				<hr class="hr-bold" />
			</div>
		</div>
	</c:forEach>
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<div class="heading-title" data-id="#myacc">MY ACCOUNT</div>
				<div class="isi-account active" id="myacc">
					<ul>
						<li class="list-item" data-id="#pi"><a
							href="/account/profile" class="text-dark">Personal Info</a></li>
						<li class="list-item active" data-id="#cp"><a
							href="/account/change_password">Change Password</li>
					</ul>
				</div>
				<div class="heading-title" data-id="#register">
					<a href="/account/course_listing" class="text-dark">REGISTERED
						COURSE </a>
				</div>
				<div class="heading-title" data-id="#invoices">INVOICES</div>
				<div class="isi-account" id="invoices">
					<ul>
						<li class="list-item" data-id="#course-order-history"><a
							href="/account/courseinvoices" class="text-dark">Course Order
								History</a></li>

						<li class="list-item" data-id="#exam-order-history"><a
							href="/account/examinvoices" class="text-dark">Exam Order History</a></li>
						<!-- <li class="list-item" data-id="#merchandize-order-history"><a
							href="/account/merchandizeinvoices" class="text-dark">Merchandize Order
								History</a></li>
						<li class="list-item" data-id="#exam-merchandize-order-history"><a
							href="/account/exammerchandizeinvoices" class="text-dark">Exam & Merchandize Order History</a></li> -->
					</ul>
				</div>
			</div>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="pi">
					<liferay-ui:error
						key="<%=MyAccountMessagesKeys.INCORRECT_PASSWORD%>"
						message="current-password-not-match"
					/>
					
					<liferay-ui:error
						key="<%=MyAccountMessagesKeys.PASSWORD_TOO_OFTEN%>"
						message="password-too-often"
					/>
					
					<c:if test="<%=SessionMessages.contains(renderRequest, MyAccountMessagesKeys.PASSWORD_UPDATE_SUCCESS)%>">
						<clay:alert
							message="<%=MyAccountMessagesKeys.PASSWORD_UPDATE_SUCCESS %>"
							style="success"
							closeable="true"
							dismissible="true"
							title="Success"
						/>
					</c:if>
					
					<aui:form name="fm" method="post" action="${updatePasswordURL}"
						enctype="multipart/form-data">
						<div class="row">
							<div class="col-md-10">
								<div class="heading-content">Change Password</div>
								<div class="box-info">
									<div class="box-input pswd">
										<label for="current-password">Current Password
										<span style="color: red;">*</span>
										</label>
										<aui:input cssClass="clearable cs-form" type="password"
											id="current-password"
											name="current-password" label="" value="" showRequiredLabel="false">
											<aui:validator name="required" />
										</aui:input>
									</div>
								</div>
								<div class="box-info">
									<div class="box-input pswd" id="spanPassword">
										<span class="info-required"><img alt=""
											src="<%=request.getContextPath()%>/img/Info_Icon.png"
										>
											<div class="box-info-required">
												Password must contains:
												<ul>
													<li>At least 12 characters</li>
													<li>Both upper and lower cases</li>
													<li>At least one number</li>
													<li>Must not reuse any of your last three passwords</li>
												</ul>
											</div>
										</span>
										<label for="new-password">New Password
										<span style="color: red;">*</span>
										</label>
										<aui:input cssClass="clearable cs-form" type="password" id="password1"
											 name="password1" label=""
											value="" showRequiredLabel="false">
											<aui:validator name="required" />
											<aui:validator
											errorMessage="ntuc-password-policy"
											name="custom">
								           			function(val, fieldNode, ruleValue) {
								           				var strRegex = /<%=passwordPolicy.getRegex()%>/g
								                    	var regex = new RegExp(strRegex);
								                        var flag = regex.test(val);
								                    	if(flag) {
								                    		addValidationClass("has-error","has-success");
								                    	} else {
								                    		addValidationClass("has-success","has-error");
								                    	}
								                        return flag;
								                	}
										</aui:validator>
											
										</aui:input>
									</div>
								</div>
								<div class="box-info">
									<div class="box-input pswd">
										<label for="confirm-password">Confirm New Password
										<span style="color: red;">*</span>
										</label>
										<aui:input cssClass="clearable cs-form" type="password"
											id="password2" 
											name="password2" label="" value=""  showRequiredLabel="false" >
											<aui:validator name="required" />
											<aui:validator name="equalTo">
									        	'#<portlet:namespace />password1'
											</aui:validator>		
										</aui:input>
									</div>
								</div>
								<span id="<portlet:namespace />passwordCapsLockSpan"
									style="display: none;"
								><liferay-ui:message key="caps-lock-is-on" /> </span>
								<div class="btn-box-right">
									<aui:button type="submit" id="submit" name="submit"
										cssClass="btn-blue text-capitalize" value="Update Password" />
									<a class="btn-white" href="#">Cancel</a>
								</div>
							</div>
						</div>
					</aui:form>
				</div>
				<!--End content-->
			</div>
		</div>
	</div>
</section>

<script type="text/javascript">
	 $("#<portlet:namespace/>fm div > .field")
			.each(
					function(idx) {
						var b_pass = $(this);
						var act_eye = '<span class="act_show" id="eye_show_'+idx+'"><i class="far fa-eye"></i></span>';
						var act_eye_slash = '<span class="act_hide" id="eye_hide_'+idx+'"><i class="far fa-eye-slash"></i></span>';

						$(act_eye).insertAfter(b_pass);
						$(act_eye_slash).insertAfter(b_pass);

						$("#eye_show_"+idx).click(function() {
							$(b_pass).attr("type", "password");
							$(this).fadeOut();
							$("#eye_hide_"+idx+".act_hide").fadeIn();
						});

						$("#eye_hide_"+idx).click(function() {
							$(b_pass).attr("type", "text");
							$(this).fadeOut();
							$("#eye_show_"+idx+".act_show").fadeIn();
						});

					}); 
	 
	 function addValidationClass(beforeClass, afterClass) {
			$("#spanPassword").removeClass(beforeClass).addClass(afterClass);
		}
</script>