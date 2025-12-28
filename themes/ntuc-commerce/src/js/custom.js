AUI().ready(function () {
    showControlMenu();
    toggleMenu();
    eyePswd();
    circleProgress();
    menuCart();
    citredToogle();
    $("#loading").fadeOut("slow");
    $("#loading-buy").fadeOut("fast");
    setTimeout(function() { 
        $('.btn-buy').addClass('show');
    }, 500);
});
// Switch Function
function showControlMenu() {
    $('.check-control-panel').click(function () {
        if ($(this).prop("checked") == true) {
            $.cookie('show-control-menu', 'true', {
                expires: 365,
                path: '/'
            });
            $('body').addClass('show_c_menu');
        } else if ($(this).prop("checked") == false) {
            $.cookie('show-control-menu', 'false', {
                expires: 365,
                path: '/'
            });
            $('body').removeClass('show_c_menu');
        }
    });

    if ($.cookie('show-control-menu') == "true") {
        $('body').addClass('show_c_menu');
        $('.check-control-panel').prop('checked', true);
    } else {
        $('body').removeClass('show_c_menu');
        $('.check-control-panel').prop('checked', false);
    }
}
function eyePswd() {
    $(document).ready(function(){
        $("#toggle_pwd").click(function () {
            $(this).toggleClass("fa-eye fa-eye-slash");
            var type = $(this).hasClass("fa-eye-slash") ? "text" : "password";
            $("#txtPassword").attr("type", type);
        });
    });
}

function menuCart() {


  $(".pswd").click(function(){
    $(".box-info-required").addClass("dshow");
    $(".box-info-required").removeClass("dnone");
  });
  $(".pswd input").keyup(function(){
    $(".box-info-required").addClass("dnone");
    $(".box-info-required").removeClass("dshow");
  });

  $(".btn-cart-custom").click(function(){
    $(".list-cart-custom").addClass("active");
    $(".overlay-cart").addClass("active");
    });

    var count_element = $(".list-cart-custom .ball1px").length
    $( ".text-count" ).html( count_element );
  
    $(".btn-close-cart").click(function(){
        $(".list-cart-custom").removeClass("active");
        $(".overlay-cart").removeClass("active");
    });
    $(".overlay-cart").click(function(){
        $(".list-cart-custom").removeClass("active");
        $(this).removeClass("active");
    });
    
    
}
// function loadingCategoriesMenu() {
    
    // $("<div class='loadingct'>Loading ...</div>").insertBefore(".portlet-commerce-product-asset-category-navigation #portlet_com_liferay_commerce_product_asset_categories_navigation_web_internal_portlet_CPAssetCategoriesNavigationPortlet_INSTANCE_FXCboM1mT9Qe");
    
    // setTimeout(function() {
    //     $(".loadingct").hide();
    // }, 2000);
//     $("#loading").fadeOut("slow");
// }
function citredToogle(){
    $(".citredshowhide").click(function(){
        $(".box-billing-address").toggleClass("active");
      });
}
function toggleMenu() {
$(document).ready(function(){
        $(".heading-title").click(function(){
            var id =$(this).attr("data-id");
            $(id).toggle();
        });
        $(".toggle-select").click(function() {
	        var id =$(this).attr("data-id");
            var idkey=id.replace('#','');
            if ($(id).hasClass("active")) {
                $.cookie('toggle-select-'+idkey, 'false', {
                    expires: 365,
                    path: '/'
                });
                $(id).removeClass("active");
                $(this).removeClass("active");
            }else{
                $.cookie('toggle-select-'+idkey, 'true', {
                    expires: 365,
                    path: '/'
                });
	            
                $(id).addClass("active");
                $(this).addClass("active");
            }
        });

        if ($.cookie('toggle-select-one') == "true") {
            $('#one').addClass("active");
            $('#one').parent().find('.toggle-select').addClass("active");
        } else {
            $('#one').removeClass("active");
            $('#one').parent().find('.toggle-select').removeClass("active");
        }
        //End
        if ($.cookie('toggle-select-two') == "true") {
            $('#two').addClass("active");
            $('#two').parent().find('.toggle-select').addClass("active");
        } else {
            $('#two').removeClass("active");
            $('#two').parent().find('.toggle-select').removeClass("active");
        }
        //End
        if ($.cookie('toggle-select-three') == "true") {
            $('#three').addClass("active");
            $('#three').parent().find('.toggle-select').addClass("active");
        } else {
            $('#three').removeClass("active");
            $('#three').parent().find('.toggle-select').removeClass("active");
        }
        //End
        


        $(".close").click(function(){
            var id =$(this).attr("data-id");
            $(id).hide();
        });
        $(".list-item").click(function(){
            $(".content").removeClass("active");
            $(".list-item").removeClass("active");
            var id =$(this).attr("data-id");
            $(id).addClass("active");
            if ($(this).hasClass("active")) {
                $(this).removeClass("active");
            } else {
                $(this).addClass("active");
            }  
        });
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
    });
}
function circleProgress() {
$(".circle_percent").each(function() {
    var $this = $(this),
		$dataV = $this.data("percent"),
		$dataDeg = $dataV * 3.6,
		$round = $this.find(".round_per");
	$round.css("transform", "rotate(" + parseInt($dataDeg + 180) + "deg)");	
	$this.append('<div class="circle_inbox"><span class="percent_text"></span></div>');
	$this.prop('Counter', 0).animate({Counter: $dataV},
	{
		duration: 2000,
		easing: 'swing',
		step: function (now) {
            $this.find(".percent_text").text(Math.ceil(now)+"%");
        }
    });
	if($dataV >= 51){
		$round.css("transform", "rotate(" + 360 + "deg)");
		setTimeout(function(){
			$this.addClass("percent_more");
		},1000);
		setTimeout(function(){
			$round.css("transform", "rotate(" + parseInt($dataDeg + 180) + "deg)");
		},1000);
	}
});
}