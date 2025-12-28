<!DOCTYPE html>

<#include init />
<#assign scope_group = theme_display.getScopeGroup() />
<#assign autoAppendTitle = scope_group.getExpandoBridge().getAttribute("Auto_Append_Title") />

<html class="${root_css_class}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
	<title>${the_title} ${autoAppendTitle}</title>

	<meta content="initial-scale=1.0, width=device-width" name="viewport" />
	
	<@liferay_util["include"] page=top_head_include />
</head>

<body class="${css_class}">

<@liferay_ui["quick-access"] contentId="#main-content" />

<@liferay_util["include"] page=body_top_include />

<#assign scope_group = theme_display.getScopeGroup() />

<div class="CX-content-panel" id="wrapper">
	<section id="content">
		<#if selectable>
			<@liferay_util["include"] page=content_include />
		<#else>
			${portletDisplay.recycle()}
			<@liferay_theme["wrap-portlet"] page="portlet.ftl">
				<@liferay_util["include"] page=content_include />
			</@>
		</#if>
	</section>

</div>

<@liferay_util["include"] page=body_bottom_include />

<@liferay_util["include"] page=bottom_include />

<script type="text/javascript" src="${javascript_folder}/select2.min.js"></script>

<script>

AUI().ready('aui-module', function(A){ 
   $("#dkdh__").each(function(){
		var col_dynamic = $(this).find(".lfr-ddm-field-group.field-wrapper.form-builder-field.yui3-dd-drop.yui3-dd-draggable");
		var form_group = $(this).find(".form-group");
		var local_change = $(this).find(".input-group-item.input-group-item-shrink.input-localized-content ");
		
		

		
		$(col_dynamic).each(function(){
			//console.log($(this));
			var form_group = $(this).find(".form-group");
			var label = $(form_group).find(".control-label");
			
			if($(label).siblings("span").hasClass("hide_sheet")){
				$("<span class='hide_sheet'></span>").remove();
			}else{
				$("<span class='hide_sheet'></span>").insertAfter(label);
			}


			$(form_group).each(function(){
				var hide_sheet = $(this).find(".hide_sheet");
				var content_child = $(this).siblings(".lfr-ddm-field-group.field-wrapper.form-builder-field");

				//console.log($(this));
				if(content_child.length == 0){
					$(hide_sheet).remove();
					//console.log("ok");
				}
			});

		});


		if($(col_dynamic).length > 0 ){
			$('<div class="box-dynamic-expand text-right"><a class="btn btn-sm btn-primary btn-default cx-collapse">Collapse all child</a></div>').insertAfter(local_change);
		}
		


		$('select.form-control').select2();


		//switching combobox
		$(".form-builder-field").each(function(){
			if ($(this).attr("data-fieldname") == "SwitchingCombobox") { // for dropdown filter name must = "type"
				var box_select = $(this).find(".form-builder-field[data-fieldname*='SwitchType']");
				var select = $(box_select).find("select");
				var selectedCountry = $(select).children("option:selected").val();
				var element = $(".form-builder-field[data-fieldname*='element']");

				console.log($(select).attr("data-fieldname"));
				if(selectedCountry != ""){
					$(element).hide();
					$(".form-builder-field[data-fieldname='"+selectedCountry+"']").fadeIn();
				}

				$(this).find(select).change(function(){
					var selectVal = $(this).val(); // value dropdown name must same with name field will be filter
					$(element).hide(); // for element will be filter first name must element eg: "elementCarousel"
					$(".form-builder-field").each(function(){
						if($(this).attr("data-fieldname") ==  selectVal) {
							$(this).fadeIn();
						}
					});
				});
			}
		});
		
		
	});

	$(".hide_sheet").click(function (e) {
		e.stopPropagation();

		var content = $(this).parent().siblings(".lfr-ddm-field-group.field-wrapper.form-builder-field");

		if (!$(this).hasClass("active")) {
			$(content).addClass("perkecil");
			$(this).addClass("active");
		} else {
			$(content).removeClass("perkecil");
			$(this).removeClass("active");
		}
	});

	$(".cx-collapse").click(function(e){
		e.stopPropagation();

		var content = $(".lfr-ddm-field-group.field-wrapper.form-builder-field");

		if (!$(this).hasClass("active")) {
			$(content).addClass("perkecil");
			$(this).addClass("active");
			$(".hide_sheet").addClass("active");
			$(this).html("Expand all child");
		} else {
			$(content).removeClass("perkecil");
			$(this).removeClass("active");
			$(".hide_sheet").removeClass("active");
			$(this).html("Collapse all child");
		}
	});




});



</script>
<script type='text/javascript'>
    $(document).ready(function(){
		$('input[name*="batchID"]').attr('disabled','true');
		$('input[name*="courseCode"]').attr('disabled','true');
		$('input[name*="startDate"]').attr('disabled','true');
		$('input[name*="endDate"]').attr('disabled','true');
		$('input[name*="venue"]').attr('disabled','true');
		$('input[name*="courseDescription"]').attr('disabled','true');
		$('input[name*="CourseDuration"]').attr('disabled','true');
		$('input[name*="noStudentEnrolled"]').attr('disabled','true');
		$('input[name*="popular"]').attr('disabled','true');
		$('input[name*="funded"]').attr('disabled','true');
		$('input[name*="price"]').attr('disabled','true');
	});
</script>


</body>

</html>