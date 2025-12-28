<%@ include file="../init.jsp"%>
<%
	PasswordPolicy passwordPolicy = PasswordPolicyLocalServiceUtil.getDefaultPasswordPolicy(themeDisplay.getCompanyId());
%>

<portlet:actionURL var="registerIndividualURL"
	name="<%=MVCCommandNames.REGISTER_INDIVIDUAL_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<portlet:resourceURL id="<%=MVCCommandNames.CHECK_EMAIL_RESOURCE %>" var="checkEmailResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.GET_ENV_RESOURCE %>" var="getEnvResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.GET_DATA_RESOURCE %>" var="getDataResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<style>
.create-account .box-fill .title {
	font-size: 1.2rem;
	font-weight: bold;
}
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
.loader {
    position: absolute;
    left: 50%;
    top: 50%;
    z-index: 1;
    width: 120px;
    height: 120px;
    margin: -76px 0 0 -76px;
    border: 16px solid #f3f3f3;
    border-radius: 50%;
    border-top: 16px solid #3498db;
    -webkit-animation: spin 2s linear infinite;
    animation: spin 2s linear infinite;
}
</style>


<section class="create-account create-account-company">
	<div class="container">
		<div class="row justify-content-center">
			<div class="row justify-content-center">
				<div class="col-md-6 col-xl-4">
					<div class="box-fill">
						<div class="row" style="margin-bottom: -20px">
							<div class="title col-md-12">
								Fill up the Form Automatically? <img class="header-tooltip" alt=""
									src="<%=request.getContextPath()%>/img/Info_Icon.png"
									width="30" height="30"
								>
								<div class="header-tooltip-box">
									By proceeding, you will be directed to log into Singpass to retrieve your data from Government agencies.
								</div>
							</div>
						</div>
						<div class="wd-extra">
							<span><small>User Singpass to securely retrieve
									all your personal details instantly.</small></span><br /> <span><small>We
									will only retrieve your identity for verification purposes.</small></span>
						</div>
						<form id="formAuthorise">
							<a href="#" onclick="$(this).closest('form').submit()"><img
								alt="" src="<%=request.getContextPath()%>/img/Primary.svg"
							></a>
						</form>
					</div>
				</div>
			</div>
			<div class="row justify-content-center" style="margin-top:-20px; margin-bottom:-20px;">
				<div class="col-md-8 col-xl-6 ">
					<hr />
				</div>
			</div>
			<div class="row justify-content-center">
				<div class="col-md-6 col-xl-4">
					<div class="box-login">
						<div class="title">Signing up manually</div>
						<div class="login-container-custom" id="individual-form">
							<aui:form name="registerForm" class="was-validated box-input"
								method="POST" action="<%=registerIndividualURL.toString()%>"
							>
								<div class="box-input">
									<label class="control-label"
										for="<portlet:namespace />id-number"
									> ID Number (Last 4 digits NRIC Number, eg : 381D) <span
										style="color: red;"
									>*</span>
									</label>
									<aui:input name="id-number" label="" showRequiredLabel=""
										cssClass="clearable cs-form " type="text" value=""
										required="true" maxlength="4"
									>
										<aui:validator name="required" />
										<aui:validator name="maxLength">4</aui:validator>
										<aui:validator name="minLength">4</aui:validator>
										<aui:validator errorMessage="Please enter a valid NRIC" name="custom">
												function(val, fieldNode, ruleValue) {
													var strRegex = /^\d{3}[a-zA-Z]$/;
													var regex = new RegExp(strRegex);
													var flag = regex.test(val);
													return flag;
																                	}
											</aui:validator>
									</aui:input>
								</div>
								<div class="box-input">
									<label class="control-label"
										for="<portlet:namespace />full-name"
									> Full Name (as NRIC/FIN/Passport) <span style="color: red;">*</span>
									</label>
									<aui:input name="full-name" label="" showRequiredLabel=""
										cssClass="clearable cs-form " type="text" value=""
									>
										<aui:validator name="required" />
										<aui:validator name="maxLength">50</aui:validator>
									</aui:input>
								</div>
								<aui:input name="dobSingpas" type="hidden"></aui:input>
								<div class="box-input date-input">
									<label class="control-label" for="<portlet:namespace />dob">
										Date of Birth <span style="color: red;">*</span>
									</label>
									<aui:input name="dob" label="" showRequiredLabel=""
										cssClass="clearable cs-form date" type="date" value=""
										placeholder="DD/MM/YYYY"
									>
										<aui:validator name="required" />
										<aui:validator name="custom" errorMessage="invalid-dob">
										 function (val, fieldNode, ruleValue) {
										 		console.log('val = '+val);
												var flag = false;
												var date = new Date(val);
												var today = new Date();
												flag = date < today;
												return flag;
										  }
									</aui:validator>
									</aui:input>
								</div>
								<div class="box-input">
									<label class="control-label" for="<portlet:namespace />email">
										Email <span style="color: red;">*</span>
									</label>
									<aui:input name="email" label="" showRequiredLabel=""
										cssClass="clearable cs-form " type="text" value=""
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
											        	<portlet:namespace/>emailAddress : val
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
										</div> </span> <label for="<portlet:namespace />password1"> Create
										New Password <span style="color: red;">*</span>
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
									<label for="<portlet:namespace />password2"> Re-type
										New Password <span style="color: red;">*</span>
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
								<button type="submit" class="btn-grey w100">
									<liferay-ui:message key="sign-up" />
								</button>
							</aui:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<script type="text/javascript">
	var scrollToAppForm = false;
	
	// Variables for API #1 - Authorise API
	var authApiUrl; // URL for authorise API
	var clientId; // your app_id/client_id provided to you during onboarding
	var redirectUrl; //callback url for your application
	
	var attributes; // the attributes you are retrieving for your application to fill the form
	var authLevel; // the auth level, determines the flow
	// the purpose of your data retrieval
	var purpose;
	
	// randomly generated state
	var state;
	var code;
	
	$(document).ready(function(){
		let localCode= "${code}";
		if(localCode !== '' && localCode !== undefined) {
			 $('#individual-form').after('<div class="loader"></div>');
		}
		loadEnv();
		var backUrl = "<%=ParamUtil.get(request, "redirect", "") %>";
		$(".portlet-icon-back").attr("href",backUrl);
		
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

				$("#eye_show_"+idx).click(function() {
					$(b_pass).attr("type", "password");
					$(this).fadeOut();
					$(".act_hide").fadeIn();
				});

				$("#eye_hide_"+idx).click(function() {
					$(b_pass).attr("type", "text");
					$(this).fadeOut();
					$(".act_show").fadeIn();
				});

			}); 
	
	
	function loadEnv(){
		// invoke AJAX call to get the clientId & redirectURL from serverside
		return jQuery.ajax({
			url: "<%=getEnvResourceURL.toString()%>",
			data: {},
			type: "GET", // get from serverside
			dataType: "json",
			async : "false",
			cache : "false",
			success: function(data) {
				// successful response from serverside
				if (data.status == "OK") { // successful
					// fill up the application form
					clientId = data.clientId;
					redirectUrl = data.redirectUrl;
					authApiUrl = data.authApiUrl;
					attributes = data.attributes;
					authLevel = data.authLevel;
					state = data.state;
					purpose = data.purpose;
					
					code = "${code}";
					if(code !== '' && code !== undefined) {
						loadData(code);
					}
				} else {
					// error occured
					alert("ERROR:" + JSON.stringify(data.msg));
				}

			}
		});
	}
	
	function loadData(code){
		return jQuery.ajax({
			url: "<%=getDataResourceURL.toString()%>",
			data: {
				<portlet:namespace/>authorizationCode : code,
				<portlet:namespace/>clientId : clientId,
				<portlet:namespace/>redirectUri : redirectUrl,
				<portlet:namespace/>state : state
			},
			type: "GET", // get from serverside
			dataType: "json",
			async : "false",
			cache : "false",
			beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
				$('#individual-form .loader').removeClass('hidden');
    		},
			success: function(res) {
				console.log(res);
				if (res.status) {
					if(res.data.partialuinfin !== ""){
						$('#<portlet:namespace/>id-number').val(res.data.partialuinfin.substring(res.data.partialuinfin.length - 4)).attr("readOnly","true");	
					}
					if(res.data.name !== ""){
						$('#<portlet:namespace/>full-name').val(res.data.name).attr("readOnly","true");	
					}
					if(isValidDate(new Date(res.data.dob))){
						//using disabled not readOnly because not working on iOS
						$('#<portlet:namespace/>dobSingpas').val(res.data.dob);	
						$('#<portlet:namespace/>dob').val(res.data.dob).attr("disabled","true");	
					}
					if(res.data.email !== ""){
						$('#<portlet:namespace/>email').val(res.data.email);		
					}
				} else {
					// error occured
					alert("ERROR:" + JSON.stringify(data.msg));
				}
			},
			complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
            	$('.loader').hide();
            },
		});
	}
	
	function isValidDate(d) {
		  return d instanceof Date && !isNaN(d);
	}
	
	$(function() {
		$("#formAuthorise").submit(function(event) {
				event.preventDefault();
				callAuthoriseApi();
		});
	});
	
	function callAuthoriseApi() {
		var authoriseUrl = authApiUrl + "?client_id=" + clientId +
			"&attributes=" + attributes +
			"&purpose=" + encodeURI(purpose) +
			"&state=" + encodeURI(state)  +
			"&redirect_uri=" + encodeURI(redirectUrl);
		window.location = authoriseUrl;
	};
	
	
</script>