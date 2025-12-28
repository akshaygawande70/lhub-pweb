<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="web.ntuc.eshop.otp.constants.MVCCommandNames"%>
<%@ include file="/init.jsp" %>

<portlet:resourceURL var="verifyOtpURL"
	id="<%=MVCCommandNames.OTP_VERIFY_RESOURCE%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:resourceURL var="createUserURL"
	id="<%=MVCCommandNames.OTP_CREATE_USER_RESOURCE%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:actionURL var="resendOtpURL"
	name="<%=MVCCommandNames.OTP_RESEND_ACTION%>">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>


<portlet:renderURL var="popupResponseUrl" windowState="<%=LiferayWindowState.EXCLUSIVE.toString()%>">
	<portlet:param name="jspPage" value="/html/popup.jsp" />
</portlet:renderURL>


<section class="otp-cxrus-default">
	<div class="container">
		<div class="row justify-content-center align-items-center">
			<div class="position-relative">
				<div class="card p-2 text-center">
					<img alt="" class="img-small"
						src="<%=request.getContextPath()%>/img/otp-logo.PNG" />
					<h6 class="title">Two Factor Authentication</h6>
					<div class="small-abstrak">A verification code has been sent
						to your email. Please confirm your account by entering the
						authorization code sent to you email.</div>

					<div>
						<form method="get" id="otp" class="inputs d-flex flex-row justify-content-center mt-2" 
						data-group-name="digits" data-autosubmit="false" autocomplete="off">
							<input class="m-2 text-center form-control rounded" id="first"
								maxlength="1" type="text" /> 
							<input
								class="m-2 text-center form-control rounded" id="second"
								maxlength="1" type="text" /> 
							<input
								class="m-2 text-center form-control rounded" id="third"
								maxlength="1" type="text" /> 
							<input
								class="m-2 text-center form-control rounded" id="fourth"
								maxlength="1" type="text" /> 
							<input
								class="m-2 text-center form-control rounded" id="fifth"
								maxlength="1" type="text" /> 
							<input
								class="m-2 text-center form-control rounded" id="sixth"
								maxlength="1" type="text" />
						</form>
					</div>

					<div class="small-desc">
						It may take a minute to receive your code. Haven't received it?<a
							href="<%=resendOtpURL.toString()%>">Resend a new code.</a>
					</div>

					<div class="mt-4">
						<button class="btn-grey w-100" onclick="javascript:sendOTP()">Submit</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</section>

<section class="loader-otp active hide">
	<div class="container">
		<div class="row">
			<div class="box-loader">
				<div class="image">
					<img alt=""
						src="/o/ntuclearninghub-commerce-theme/images/asset_commerce/loading_icon.png"
						srcset=""
					/>
				</div>
				<div class="title">We are creating your individual account...
					Please wait for a few seconds..</div>
				<div class="small-title">
					Page is not loading? <a href="javascript:window.location.reload(true)">Click here</a>
				</div>
			</div>
		</div>
	</div>
</section>
<script>
    document.addEventListener("DOMContentLoaded", function (event) {
        OTPInput();
    });
    
    function OTPInput() {
        const inputs = document.querySelectorAll('#otp > *[id]');
        for (let i = 0; i < inputs.length; i++) {
            inputs[i].addEventListener('keydown', function (event) {
                if (event.key === "Backspace") {
                    inputs[i].value = '';
                    if (i !== 0) inputs[i - 1].focus();
                } else {
                    if (i === inputs.length - 1 && inputs[i].value !== '') {
                        return true;
                    } else if ((event.keyCode > 47 && event.keyCode < 58) || (event.keyCode > 95 && event.keyCode < 106)) {
                        inputs[i].value = event.key;
                        if (i !== inputs.length - 1) inputs[i + 1].focus();
                        event.preventDefault();
                    } else {
                        event.preventDefault();
                        return false;
                    }
                }
            });
        }
    }
    
    function sendOTP() {
	    var dg1 = document.getElementById('first').value;
	    var dg2 = document.getElementById('second').value;
	    var dg3 = document.getElementById('third').value;
	    var dg4 = document.getElementById('fourth').value;
	    var dg5 = document.getElementById('fifth').value;
	    var dg6 = document.getElementById('sixth').value;
	    var dg7 = dg1 + dg2 + dg3 + dg4 + dg5 + dg6;
	    
	    jQuery.ajax({
			url: "<%=verifyOtpURL.toString()%>",
			data: {
				<portlet:namespace/>token: dg7
			},
			type: "GET", // get from serverside
			dataType: "json",
			async : "false",
			cache : "false",
			success: function(data) {
				if(data) {
					//window.location.href = "fandi.com:8080/home";
					createUser("<%=createUserURL.toString()%>");
				} else {
					document.getElementById('first').value = "";
			    	document.getElementById('second').value = "";
			    	document.getElementById('third').value = "";
			    	document.getElementById('fourth').value = "";
			    	document.getElementById('fifth').value = "";
			    	document.getElementById('sixth').value = "";
			    	showPopup("<%=popupResponseUrl.toString()%>");
				}
			}
		});
	}
    
    function createUser(url) {
		return jQuery.ajax({
			url : url,
			type : "POST",
			dataType : "json",
			data : {},
			async : "true",
			cache : "true",
			beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
				$('.loader-otp').removeClass('hide');
				setTimeout(function(){
					
	        	}, 100000)
    		},
	        success : function(data,textStatus,XMLHttpRequest){
	        	window.location.href = "<%=themeDisplay.getPortalURL()%>"+"/registration";
			},
			complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
            	$('.loader-otp').hide();
            },
			error : function(data,textStatus,XMLHttpRequest){
				alert("error");
			}
		});
	}
    
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
    
    
</script>