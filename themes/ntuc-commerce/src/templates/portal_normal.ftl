<!DOCTYPE html>
<#include init />
<#assign scope_group = theme_display.getScopeGroup() />
<#assign autoAppendTitle = scope_group.getExpandoBridge().getAttribute("Auto_Append_Title") />
<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">
	<head>
	<title>${the_title} ${autoAppendTitle}</title>
<!-- Google Tag Manager 11/11/2022-->
		 <#if gtmBot == true>
			<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
		new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
		j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
		'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
		})(window,document,'script','dataLayer','GTM-WNKRHD');</script>
		</#if>
	
	<!-- End Google Tag Manager -->
	<#if noIndex>
		<meta name="robots" content="noindex">
		<meta name="googlebot" content="noindex">
	</#if>
		<meta content="initial-scale=1.0, width=device-width, maximum-scale=1" name="viewport" />
		<meta name="loadforge-site-verification" content="df2396aea48a6cb9040bc409f83d92bf5d36d22b6b3e5ab60b43064e29b8e7f1608f41d1fd328dd62e06a11b3be71b379d7115e5837dcc8ceb5192533376ad9e" />

		<link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700&display=swap" rel="stylesheet">
		<script type="text/javascript" src="${javascript_folder}/standalone/SpeedwellSlider.js" defer></script>
		<@liferay_util["include"] page=top_head_include />
	</head>
		<#if navTransparent == true>
			<#assign nav_cls = "transparent-header"/>
		<#else>
			<#assign nav_cls = " "/>
		</#if>
		<!--End-->
		<#if navWhite == true>
			<#assign nav_white = "white-header"/>
		<#else>
			<#assign nav_white = " "/>
		</#if>
		<!--End-->
		<#if navBlack == true>
			<#assign nav_black = "black-header"/>
		<#else>
			<#assign nav_black = " "/>
		</#if>
		<!--End-->
		<#if showControlMenu> 
			<#assign control_name = "admin_control"/>
		<#else>
			<#assign control_name = "user_control"/>
		</#if>
		<#assign user_portrait = user.getPortraitURL(themeDisplay)>
		<#assign titleclass = the_title?replace(" ", "")?replace("-", " ") /> 
	<body class="speedwell ${css_class}  ${nav_cls} ${nav_white} ${nav_black} ${control_name} content-notice-header ${titleclass}" id="content">
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
			<label for="settings-btn" class="settings-box-element"><i class="fa fa-cogs" aria-hidden="true"></i></label>
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
            			<a data-senna-off="true" class="lfr-icon-item taglib-icon btn cx-btn btn-primary btn-sm" href="${logout_url}">Logout</a>
					</div>
				</div>
				
			</div>
		</div>	
	</#if>
	<#--  End Service Panel  -->



		<@liferay.control_menu />
<#include "${full_templates_path}/header.ftl" />
		<div id="wrapper">
			<div class="liferay-top">
				<@liferay_ui["quick-access"] contentId="#main-content" />
				<@liferay_util["include"] page=body_top_include />
			</div>

			<main class="speedwell-frame" id="speedwell">
				<div class="speedwell-frame__topbar">
					<#--  <#include "${full_templates_path}/topbar.ftl" />  -->
				</div>

				<#if speedwell_content_css_class?contains("wide")>
				<div class="speedwell-frame speedwell-frame__content--wide">
				<#else>
				<div class="speedwell-frame speedwell-frame__content">
				</#if>
					<a name="speedwell-top"></a>

					<#--  <div class="container-fluid ${speedwell_content_css_class}">  -->
					<div class="content-ntuc">
						<#if selectable>
							<@liferay_util["include"] page=content_include />
						<#else>
							${portletDisplay.recycle()}
							${portletDisplay.setTitle(the_title)}

							<@liferay_theme["wrap-portlet"] page="portlet.ftl">
								<@liferay_util["include"] page=content_include />
							</@>
						</#if>
					</div>
				</div>
<#include "${full_templates_path}/footer.ftl" />

			</main>

			<div class="liferay-bottom">
				<@liferay_util["include"] page=body_bottom_include />
				<@liferay_util["include"] page=bottom_include />
			</div>
		</div>
		<div id="loading">
		<img src="${images_folder}/loading.svg" class="img-fluid" />
		</div>
<script type="text/javascript" src="${javascript_folder}/jquery.cookie.min.js"></script>


<script type="text/javascript" src="${javascript_folder}/datatable/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${javascript_folder}/datatable/dataTables.responsive.min.js"></script>
<script type="text/javascript" src="${javascript_folder}/datatable/dataTables.bootstrap4.min.js"></script>

		<script type="text/javascript" src="${javascript_folder}/intersection-observer.js"></script>
		<script type="text/javascript" src="${javascript_folder}/features/accessibility.js"></script>
		<script type="text/javascript" src="${javascript_folder}/features/scrollHandler.js"></script>
		<script type="text/javascript" src="${javascript_folder}/features/topbar.js"></script>
		<script type="text/javascript" src="${javascript_folder}/features/categoryMenu.js"></script>
		<script type="text/javascript" src="${javascript_folder}/features/mobile.js"></script>
		<script type="text/javascript" src="${javascript_folder}/custom.js"></script>

		<!--WP-->
<script type='text/javascript' src='${javascript_folder}/wp/pluginccff.js' id='plugin-js'></script>
<script type='text/javascript' src='${javascript_folder}/wp/mainccff.js' id='main-js'></script>
 <script type='text/javascript' src='${javascript_folder}/wp/pagination.minccff.js' id='pagination-js'></script>  
<script type='text/javascript' src='${javascript_folder}/wp/jssocials.minccff.js' id='jssocials-js'></script>  
<script type='text/javascript' src='${javascript_folder}/wp/holderccff.js' id='holder-js'></script>
<script type='text/javascript' src='${javascript_folder}/wp/customccff.js' id='custom-js'></script>  
<script type='text/javascript' src='${javascript_folder}/wp/navigation4a7d.js' id='ntuc-learning-navigation-js'></script>  
<script type='text/javascript' src='${javascript_folder}/wp/skip-link-focus-fix4a7d.js' id='ntuc-learning-skip-link-focus-fix-js'></script>  

<script>
$(".Courses .colsub-1").addClass("colsub-2");
</script>
	<script>
	AUI().ready(function () {
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

	//	 $(".cs-holder + input").keyup(function() {
	//		if($(this).val().length) {
	//			$(this).prev('.cs-holder').hide();
	//		} else {
	//			$(this).prev('.cs-holder').show();
	//		}
	//		console.log($(this));
	//	});
	//	$(".cs-holder").click(function() {
	//		$(this).next().focus();
	//	});
		//---hide label form--->
		$('.cs-holder').next('.form-group').children('input').keyup(function() {
			if($(this).val().length) {
				$(this).parent().prev('.cs-holder').hide();
			} else {
				$(this).parent().prev('.cs-holder').show();
				}
			});
		$(".cs-holder").click(function() {
			$(this).next('.form-group').children('input').focus();
		});
//end//
$(".mobile-cart .mini-cart-opener").click(function(){
    $(".btn-menu").css("display", "none");
  });
  
  $(".mobile-cart .mini-cart-close").click(function(){
    $(".btn-menu").css("display", "block");
  });


  //Menu Mobile//
  $(".menu-mobile").click(function(){
	$(".mn-wrap").addClass("open-sub");
	$("body").addClass("open-page");
	});

	$(".close-menu-mobile").click(function(){
		$(".mn-wrap").removeClass("open-sub");
		$("body").removeClass("open-page");
	});
//end menu//
		$('.slick-carousel-date').slick({
			infinite: false,
			slidesToShow: 1, // Shows a three slides at a time
			slidesToScroll: 1, // When you click an arrow, it scrolls 1 slide at a time
			arrows: true, // Adds arrows to sides of slider
			dots: false // Adds the dots on the bottom
		});
		$('.slick-carousel-session').slick({
			infinite: false,
			//centerMode: true,
			//centerPadding: '60px',
			slidesToShow: 5, // Shows a three slides at a time
			slidesToScroll: 5, // When you click an arrow, it scrolls 1 slide at a time
			arrows: false, // Adds arrows to sides of slider
			dots: false // Adds the dots on the  
			
		});
		$('.tab-search').slick({
			dots: false,
			infinite: false,
			speed: 300,
			slidesToShow: 5,
			centerMode: false,
			arrows: false,
		});


      
	});
	</script>
	</body>
</html>