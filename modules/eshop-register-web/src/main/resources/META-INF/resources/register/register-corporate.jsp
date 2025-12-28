<%@ include file="../init.jsp"%>

<%
	PasswordPolicy passwordPolicy = PasswordPolicyLocalServiceUtil
			.getDefaultPasswordPolicy(themeDisplay.getCompanyId());
%>
<portlet:actionURL var="registerCorporateURL"
	name="<%=MVCCommandNames.REGISTER_CORPORATE_ACTION%>"
>
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>
<portlet:resourceURL id="<%=MVCCommandNames.CHECK_EMAIL_RESOURCE%>"
	var="checkEmailResourceURL"
>
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>
<portlet:resourceURL
	id="<%=MVCCommandNames.CHECK_UEN_AND_COMPANY_RESOURCE%>"
	var="checkUenAndCompanyResourceURL"
>
<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<style>
span#qfkd____::before {
    content: 'Back';
    position: absolute;
    margin-left: 25px;
}
span#qfkd____ {
    color: #000;
    margin-top: 20px;
    display: block;
    font-weight: bold;
    font-size: 1rem;
}
</style>
<section class="create-account create-account-company">
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-md-4 col-xl-6">
				<div class="box-login">
					<div class="title">Sign Up</div>
					<div class="box-tootip">
						<div class="box-icon">
							<i aria-hidden="true" class="fa fa-info-circle"></i>
						</div>
						<div class="box-abstrak">Please key in your company code
							provided by NTUC LearningHub</div>
					</div>
					<div class="login-container-custom">
						<aui:form name="registerForm" class="was-validated box-input"
							method="POST" action="<%=registerCorporateURL.toString()%>"
						>
							<div class="box-input">
								<label for="<portlet:namespace />uen-number"> UEN Number
									<span style="color: red;">*</span>
								</label>
								<aui:input name="uen-number" label="" showRequiredLabel=""
									cssClass="clearable cs-form" type="text" value=""
								>
									<aui:validator name="maxLength">10</aui:validator>
									<aui:validator name="required" />
								</aui:input>
							</div>
							<div class="box-input">
								<label for="<portlet:namespace />company-code"> Company
									Code <span style="color: red;">*</span>
								</label>
								<aui:input name="company-code" label="" showRequiredLabel=""
									cssClass="clearable cs-form" type="text" value=""
								>
									<aui:validator name="maxLength">17</aui:validator>
									<aui:validator name="required" />
									<aui:validator name="custom"
										errorMessage="corporate-validate-fail"
									>
											 function (val, fieldNode, ruleValue) {
											 		var flag = false;
											 		var uenNumber = AUI.$('#<portlet:namespace />uen-number').val();
											 		if(uenNumber == null || uenNumber =='') {
											 			return flag;
											 		} else {
											 			$.ajax({
															url : '<%=checkUenAndCompanyResourceURL.toString()%>',
															async : false,
															data: {
													        	<portlet:namespace />companyCode : val,
													        	<portlet:namespace />uenNumber : uenNumber
													        },
															success : function(result){
																flag = eval(result);
															}
														});	
											 		}
											 		return flag;
											  }
										</aui:validator>
								</aui:input>
							</div>
							<div class="box-input">
								<label for="<portlet:namespace />company-name"> Company
									Name <span style="color: red;">*</span>
								</label>
								<aui:input name="company-name" label="" showRequiredLabel=""
									cssClass="clearable cs-form" type="text" value=""
								>
									<aui:validator name="required" />
									<aui:validator name="maxLength">50</aui:validator>
								</aui:input>
							</div>
							<div class="box-input">
								<label for="<portlet:namespace />contact-person">
									Contact Person <span style="color: red;">*</span>
								</label>
								<aui:input name="contact-person" label="" showRequiredLabel=""
									cssClass="clearable cs-form" type="text" value=""
								>
									<aui:validator name="required" />
									<aui:validator name="maxLength">50</aui:validator>
								</aui:input>
							</div>
							<div class="box-input">
								<label for="<portlet:namespace />contact-person-email-address">
									Contact Person Email Address <span style="color: red;">*</span>
								</label>
								<aui:input name="contact-person-email-address" label=""
									showRequiredLabel="" cssClass="clearable cs-form" type="text"
									value=""
								>
									<aui:validator name="required" />
									<aui:validator name="email" />
									<aui:validator name="maxLength">50</aui:validator>
									<%-- <aui:validator name="custom" errorMessage="invalid-email">
										 function (val, fieldNode, ruleValue) {
												var flag = false;
												$.ajax({
													url : '<%=checkEmailResourceURL.toString()%>',
													async : false,
													data: {
											        	<portlet:namespace />emailAddress : val
											        },
													success : function(result){
														var json = JSON.parse(result);
														if(json.data.includes('Valid')) {
															flag = true;
														}
													}
												});	
												return flag;
										  }
									</aui:validator> --%>
								</aui:input>
							</div>
							<div class="box-input pswd" id="spanPassword">
								<span class="info-required"><img alt=""
									src="/o/web.ntuc.eshop.register/img/Info_Icon.png"
								>
									<div class="box-info-required">
										Password must contains:
										<ul>
											<li>At least 12 characters</li>
											<li>Both upper and lower cases</li>
											<li>At least one number</li>
										</ul>
									</div></span> 
									<label for="<portlet:namespace />password1"> Create
										New Password 
										<span style="color: red;">*</span>
									</label>
								<aui:input cssClass="clearable cs-form" type="password"
									id="password1" name="password1" label="" value=""
									showRequiredLabel="false"
								>
									<aui:validator name="required" />
									<aui:validator errorMessage="ntuc-password-policy"
										name="custom"
									>
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
										<aui:validator name="maxLength">50</aui:validator>
								</aui:input>
							</div>
							<div class="box-input pswd">
								<label for="<portlet:namespace />password2"> Re-type New
									Password <span style="color: red;">*</span>
								</label>
								<aui:input cssClass="clearable cs-form" type="password"
									id="password2" name="password2" label="" value=""
									showRequiredLabel="false"
								>
									<aui:validator name="required" />
									<aui:validator name="equalTo">
									        	'#<portlet:namespace />password1'
											</aui:validator>
									<aui:validator name="maxLength">50</aui:validator>
								</aui:input>
							</div>
							<button type="submit" class="btn-blue w100">
								<liferay-ui:message key="sign-up" />
							</button>
						</aui:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<script type="text/javascript">
	$(document).ready(function(){
		var backUrl = "<%=ParamUtil.get(request, "redirect", "")%>";
		$(".portlet-icon-back").attr("href", backUrl);
	});
	
	function addValidationClass(beforeClass, afterClass) {
		if(beforeClass=="has-success" && afterClass=="has-error"){
			$("#spanPassword").removeClass(beforeClass).addClass(afterClass);
		}else{
			$("#spanPassword").removeClass(beforeClass);
		}
	}

	$("#<portlet:namespace/>registerForm div > .field[id*='password']")
			.each(
					function(idx) {
						var b_pass = $(this);
						var act_eye = '<span class="act_show" id="eye_show_'+idx+'"><i class="far fa-eye"></i></span>';
						var act_eye_slash = '<span class="act_hide" id="eye_hide_'+idx+'"><i class="far fa-eye-slash"></i></span>';

						$(act_eye).insertAfter(b_pass);
						$(act_eye_slash).insertAfter(b_pass);

						$("#eye_show_" + idx).click(function() {
							$(b_pass).attr("type", "password");
							$(this).fadeOut();
							$(".act_hide").fadeIn();
						});

						$("#eye_hide_" + idx).click(function() {
							$(b_pass).attr("type", "text");
							$(this).fadeOut();
							$(".act_show").fadeIn();
						});

					});
</script>