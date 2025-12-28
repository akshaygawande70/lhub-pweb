<!DOCTYPE html>

<#include init />
<#assign scope_group = theme_display.getScopeGroup() />
<#assign autoAppendTitle = scope_group.getExpandoBridge().getAttribute("Auto_Append_Title") />
<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
	<!-- Google Tag Manager 11/11/2022-->
	    <#if gtmBot == true>
			<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
		new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
		j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
		'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
		})(window,document,'script','dataLayer','GTM-WNKRHD');</script>
		</#if>
	<!-- End Google Tag Manager -->
	
	<title>${the_title} ${autoAppendTitle}</title>
<meta name="loadforge-site-verification" content="df2396aea48a6cb9040bc409f83d92bf5d36d22b6b3e5ab60b43064e29b8e7f1608f41d1fd328dd62e06a11b3be71b379d7115e5837dcc8ceb5192533376ad9e" />
	<meta content="initial-scale=1.0, width=device-width" name="viewport" />
	<#if noIndex>
		<meta name="robots" content="noindex">
		<meta name="googlebot" content="noindex">
	</#if>

	<@liferay_util["include"] page=top_head_include />
</head>
<#if navTransparent == true>
  <#assign nav_cls = " "/>
<#else>
  <#assign nav_cls = "no-banner"/>
</#if>
<#if navBlack == true>
  <#assign nav_black = "show-nav-black"/>
<#else>
  <#assign nav_black = " "/>
</#if>
<#if navBlog == true>
  <#assign nav_blog = "show-nav-blog"/>
<#else>
  <#assign nav_blog = " "/>
</#if>
<#if hideHeader == true>
  <#assign hide_header = "hide-header"/>
<#else>
  <#assign hide_header = " "/>
</#if>
<#if hideFooter == true>
  <#assign hide_footer = "hide-footer"/>
<#else>
  <#assign hide_footer = " "/>
</#if>
<#if pswdProtect>
	<#assign clsPswdProtect = "content-use-password" />
<#else>
	<#assign clsPswdProtect = "" />
</#if>
<#if showControlMenu> 
			<#assign control_name = "admin_control"/>
		<#else>
			<#assign control_name = "user_control"/>
		</#if>
	<#assign user_portrait = user.getPortraitURL(themeDisplay)> 


	<#assign titleclass = the_title?replace(" ", "")?replace("-", " ") />
<body class="${css_class} ${nav_cls}  ${nav_black} ${hide_header} ${hide_footer} content-notice-header ${titleclass} ${control_name} ${userRoleName}">

 <!-- Google Tag Manager (noscript) 11/11/2022-->
	    <#if gtmBot == true>
            <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-WNKRHD"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
        </#if>
<!-- End Google Tag Manager (noscript) -->


<#--  Service Panel  -->
	<#if is_signed_in>
		<div class="styleswitcher">
			<input id="settings-btn" class="settings-btn" type="checkbox">
			<label for="settings-btn" class="settings-box-element"><i class="fa-2x fas fa-cogs"></i></label>
			<div class="buttons-wrapper settings-box-element">
				<h4 class="text-uppercase title-panel">Service Panel</h4>
				<div class="item-panel">
					<span class="label">Show Control Panel</span>
					<label class="rocker rocker-small">
						<input type="checkbox" name="show-control-panel" class="check-control-panel">
						<span class="switch-left">On</span>
						<span class="switch-right">Off</span>
					</label>
				</div>
				<div class="item-bottom">
					<#--  <p class="label">Deploy</p>
					<a href="javascript:;" target="_self" class="lfr-icon-item taglib-icon btn cx-btn btn-primary btn-sm" id="_com_liferay_marketplace_app_manager_web_portlet_MarketplaceAppManagerPortlet_kldx______menu__upload" onclick="_com_liferay_marketplace_app_manager_web_portlet_MarketplaceAppManagerPortlet_uploadUrlLink();" role="menuitem" tabindex="0"> <span class="taglib-text-icon">Upload</span> </a>
					<div class="col-sm-12 mt-3">  -->
            			<a data-senna-off="true" class="lfr-icon-item taglib-icon btn cx-btn btn-primary btn-sm" href="${logout_url}">Logout</a>

						<!--user-->
					<div class="box-user mt-3">
						<div class="dropdown">
							<button class="dropbtn"><i class="fas fa-user"></i></button>
							<div class="dropdown-content">
								<a class="mt-3" href="/group/guest/~/control_panel/manage?p_p_id=com_liferay_my_account_web_portlet_MyAccountPortlet&p_p_lifecycle=0">Account</a>
								 <hr>
								<a href="/group/guest/~/control_panel/manage?p_p_id=com_liferay_notifications_web_portlet_NotificationsPortlet&p_p_lifecycle=0">Notifications</a>
								<a href="/group/guest/~/control_panel/manage?p_p_id=com_liferay_sharing_web_portlet_SharedAssetsPortlet&p_p_lifecycle=0">Shared Content</a>
								<a href="/group/guest/~/control_panel/manage?p_p_id=com_liferay_portal_workflow_web_internal_portlet_UserWorkflowPortlet&p_p_lifecycle=0">My Submissions</a>
								<a href="/group/guest/~/control_panel/manage?p_p_id=com_liferay_portal_workflow_task_web_portlet_MyWorkflowTaskPortlet&p_p_lifecycle=0">My Workflow Task</a>
								<hr>
								<#--  <a href="/group/guest/~/control_panel/manage?p_p_id=com_liferay_my_account_web_portlet_MyAccountPortlet&p_p_lifecycle=0&p_p_auth=GpqiQfsV">Account Settings</a>  -->
								<a href="/group/guest/~/control_panel/manage?p_p_id=com_liferay_users_admin_web_portlet_MyOrganizationsPortlet&p_p_lifecycle=0">My Connected Applications</a>
								<a href="/group/guest/~/control_panel/manage?p_p_id=com_liferay_oauth2_provider_web_internal_portlet_OAuth2ConnectedApplicationsPortlet&p_p_lifecycle=0">My Organizations</a>
								<hr>
								<a class="mb-3" href="${logout_url}">Sign Out</a>
							</div>
						</div>
					</div>
					<!--end-->

					</div>
				</div>
				
			</div>
		</div>	
	</#if>
	<#--  End Service Panel  -->
<@liferay_ui["quick-access"] contentId="#main-content" />

<@liferay_util["include"] page=body_top_include />

<div class="d-flex flex-column min-vh-100">
	<@liferay.control_menu />

		<#include "${full_templates_path}/header.ftl" />

	<div class="d-flex flex-column flex-fill">
		<#--  <#if show_header>
			<header id="banner">
				<div class="navbar navbar-classic navbar-top py-3">
					<div class="container user-personal-bar">
						<div class="align-items-center autofit-row">
							<a class="${logo_css_class} align-items-center d-md-inline-flex d-sm-none d-none logo-md" href="${site_default_url}" title="<@liferay.language_format arguments="" key="go-to-x" />">
								<img alt="${logo_description}" class="mr-2" height="56" src="${site_logo}" />

								<#if show_site_name>
									<h2 class="font-weight-bold h2 mb-0 text-dark" role="heading" aria-level="1">${site_name}</h2>
								</#if>
							</a>

							<#assign preferences = freeMarkerPortletPreferences.getPreferences({"portletSetupPortletDecoratorId": "barebone", "destination": "/search"}) />

							<div class="autofit-col autofit-col-expand">
								<#if show_header_search>
									<div class="justify-content-md-end mr-4 navbar-form" role="search">
										<@liferay.search_bar default_preferences="${preferences}" />
									</div>
								</#if>
							</div>

							<div class="autofit-col">
								<@liferay.user_personal_bar />
							</div>
						</div>
					</div>
				</div>

				<div class="navbar navbar-classic navbar-expand-md navbar-light pb-3">
					<div class="container">
						<a class="${logo_css_class} align-items-center d-inline-flex d-md-none logo-xs" href="${site_default_url}" rel="nofollow">
							<img alt="${logo_description}" class="mr-2" height="56" src="${site_logo}" />

							<#if show_site_name>
								<h2 class="font-weight-bold h2 mb-0 text-dark">${site_name}</h2>
							</#if>
						</a>

						<#include "${full_templates_path}/navigation.ftl" />
					</div>
				</div>
			</header>
		</#if>  -->
<div id="CX-utama-ntuc" class="CX-utama-ntuc ${clsPswdProtect}">
			<div class="box-utama" id="toppage">
		<section class="${portal_content_css_class} flex-fill" id="content">
			<h2 class="sr-only" role="heading" aria-level="1">${the_title}</h2>

			<#if selectable>
				<@liferay_util["include"] page=content_include />
			<#else>
				${portletDisplay.recycle()}

				${portletDisplay.setTitle(the_title)}

				<@liferay_theme["wrap-portlet"] page="portlet.ftl">
					<@liferay_util["include"] page=content_include />
				</@>
			</#if>
		</section>
</div>
<section class="pswd-protect">
	<div class="container">
		<div class="row">
			<div class="box-protect">
			<h4>PROTECTED AREA</h4>
			<p>This content is password-protected. Please verify with a password to unlock the content.</p>
				<input type="password" autocomplete="off" placeholder="Enter your password.." id="pswd" name="pswd" >
				<button id="pswdBtn" class="passster-submit">Submit</button>
			</div>
		</div>
	</div>
</section>
</div>

		<#include "${full_templates_path}/footer.ftl" />
		<#--  <#if show_footer>
			<footer id="footer" role="contentinfo">
				<div class="container">
					<div class="row">
						<div class="col-md-12 text-center text-md-left">
							<@liferay.language key="powered-by" />

							<a class="text-white" href="http://www.liferay.com" rel="external">Liferay</a>
						</div>
					</div>
				</div>
			</footer>
		</#if>  -->
	</div>
</div>

<@liferay_util["include"] page=body_bottom_include />

<@liferay_util["include"] page=bottom_include />


<#--  
<script type="text/javascript" src="${javascript_folder}/jquery-3.5.1.min.js"></script>  -->
<script type="text/javascript" src="${javascript_folder}/jquery.cookie.min.js"></script>

<script type="text/javascript" src="${javascript_folder}/datatable/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${javascript_folder}/datatable/dataTables.responsive.min.js"></script>
<script type="text/javascript" src="${javascript_folder}/datatable/dataTables.bootstrap4.min.js"></script>

<script type="text/javascript" src="${javascript_folder}/noUi-slider.js"></script>

<!--WP-->
<script type='text/javascript' src='${javascript_folder}/wp/pluginccff.js' id='plugin-js'></script>
<script type='text/javascript' src='${javascript_folder}/wp/mainccff.js' id='main-js'></script>
<script type='text/javascript' src='${javascript_folder}/wp/pagination.minccff.js' id='pagination-js'></script>
<script type='text/javascript' src='${javascript_folder}/wp/jssocials.minccff.js' id='jssocials-js'></script>
<script type='text/javascript' src='${javascript_folder}/wp/holderccff.js' id='holder-js'></script>
<script type='text/javascript' src='${javascript_folder}/wp/customccff.js' id='custom-js'></script>
<script type='text/javascript' src='${javascript_folder}/wp/navigation4a7d.js' id='ntuc-learning-navigation-js'></script>
<script type='text/javascript' src='${javascript_folder}/wp/skip-link-focus-fix4a7d.js' id='ntuc-learning-skip-link-focus-fix-js'></script>
<script type='text/javascript' src='${javascript_folder}/custom.js'></script>
<script>
$(".Courses .colsub-1").addClass("colsub-2");
</script>
<script>
$(document).ready(function() {
  $('.bg').each(function() {
        var imgUrl1 = $(this).find('.bgimg').attr('src');
        $(this).fixbg({srcimg: imgUrl1});
	});
			var input = document.getElementById("pswd");
		input.addEventListener("keyup", function(event) {
		if (event.keyCode === 13) {
			var value = $(this).val();
			if(value == "${pswdText}"){
				$('.CX-utama-ntuc').removeClass('content-use-password');
			} else {
				alert("You have entered an invalid password.");
			}
		}
		
    });
	//
	$("#pswdBtn").click(function(){
		var value = $("#pswd").val();
		if(value == "${pswdText}"){
			$('.CX-utama-ntuc').removeClass('content-use-password');
		} else {
			alert("You have entered an invalid password.");
		}
		
	});
	
	$(".has-edit-mode-menu").find(".CX-utama-ntuc").removeClass("content-use-password");

	$(".user-header").click(function(){
			$(".box-login-user").toggle();
			$(".overlay-login").toggle();
		});
		$(".gotouser").click(function(){
            $(".box-login-user").toggle();
            $(".overlay-login").toggle();
        });
	$(".overlay-login").click(function(){
			$(this).css("display", "none");
			$(".box-login-user").css("display", "none");
		});

		$(".box-user span").html(function(index, currentHTML) {
			//console.log("item", currentHTML.length)
			if (currentHTML.length > 10) {
					return currentHTML.substr(0, 10) + "..";
			}	
		});
		/*
		$(".box-user span").text(function(index, currentText) {
			return currentText.substr(0, 9) + "..";
		});
		*/
//search course
		//var params = new window.URLSearchParams(window.location.search);
		//	$('.box-item').each(function(){
		//	var id =$(this).attr('data-id'); 
		//	console.log(id);
		//	if (params.get('course') == "1") {
		//		console.log(params.get('course'));
		//			if(id == "blogs" || id == "news" || id == "pressreleases") {
		//				$(this).attr('style', 'display: none !important');
		//	console.log("masuk");
		//			}
		//		}
		//	});
//end
});
</script>

</body>

</html>