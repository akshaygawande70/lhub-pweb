function respon() {
    var hfoot = $('.footer-container').outerHeight(),
            hhead = $('.header-container').outerHeight(),
            hscreen = $(window).outerHeight(),
            wcontainer = $('.footer-container .container').outerWidth(),
            wscreen = $(window).outerWidth();
    hbox = $('.visit-form .content').outerHeight() - 20;


    $('.introhome-3 .grid-5 .content').css({minHeight: hbox});
    $('.mm-page').css({marginBottom: -hfoot});
    $('.main-wrap').css({paddingBottom: hfoot});
    $('.spleft').css({paddingLeft: (wscreen - wcontainer) / 2 + 15});
    $('.spright').css({paddingRight: (wscreen - wcontainer) / 2 + 15});
    $('.banner .item').css({height: hscreen, paddingTop: hhead});
    $('.bn-inner .captions').css({paddingTop: hhead});
    $('.search-sub .hscroll').css({maxHeight: hscreen - hhead - 30});
    $('.no-banner .main-wrap').css({paddingTop: hhead});
    $('.hcontent').css({minHeight: hscreen - hhead - hfoot});
    $('.course-details-wrap .sidebar').css({top: hhead});
    $('.sumw').each(function () {
        var smw = 0;
        var item = $(this).find('.item');
        $(item).each(function () {
            smw += $(this).outerWidth(true);
        });
        $(this).css({width: smw});
    });

    /*var stickySidebar = new StickySidebar('#sidebar', {
     topSpacing: hhead+10,
     bottomSpacing: 20,
     containerSelector: '.course-details-wrap',
     innerWrapperSelector: '.sidebar-inner'
     });*/


    $('#sidebar').stickySidebar({
        topSpacing: hhead + 10,
        container: '.course-details-wrap',
        sidebarInner: '.sidebar-inner',
        side: 'right',
        disableAt: 767,
    });

    $('#dashsidebar').stickySidebar({
        topSpacing: hhead,
        container: '.acc-wrap',
        sidebarInner: '.sidebar-inner',
        disableAt: 767,
    });


}



function myScroll(e) {
    var topScroll = $(window).scrollTop();
    var totalScroll = topScroll + $(window).height();
    var offsetTop = $(e).offset().top;
    return totalScroll >= (offsetTop)
}
$(document).ready(function () {
    $(".object").each(function () {
        myScroll(this) === !0 && $(this).find('.animated').addClass("go")
    });
});

$(window).scroll(function () {
    $(".object").each(function () {
        myScroll(this) === !0 && $(this).find('.animated').addClass("go")
    });
    if ($(window).scrollTop() > 0) {
        $('.gotop').css({opacity: 1});
        $('.header-container').addClass('fixhead');
    } else {
        $('.gotop').css({opacity: 0});
        $('.header-container').removeClass('fixhead');
    }
});



var jump = function (e) {
    if (e) {
        e.preventDefault();
        var target = $(this).attr("href");
    } else {
        var target = location.hash;
    }

    $('html,body').animate(
            {
                scrollTop: $(target).offset().top -250
            }, 1000, function ()
    {
        location.hash = target;
    });

}


$(document).ready(function () {
    respon();

    stickyHeader();
    $('[data-toggle="tooltip"]').tooltip();

    if (location.hash) {
        setTimeout(function () {
            $('html, body').scrollTop(0).css({opacity: 1});
            jump()
        }, 300);

    } else {
        $('html, body').css({opacity: 1});
    }
    $(".menu ul a").on("click", function () {
        var xhref = $(this).attr("href");
        var arrHref = xhref.split("#");
        if (arrHref.length > 1) {
            //alert(arrHref[1]);
            $("#" + arrHref[1]).trigger("click");
        }
    })


    var md = new MobileDetect(window.navigator.userAgent);
    if (md.mobile() == "iPad") {
        $("body").addClass("touch");
    } else if (md.mobile() == "" || md.mobile() == null) {

        $("body").addClass("notouch");
    } else {

        $("body").addClass("touch");
    }


    $(".link-wrap li, .nav-1 li").find("ul").prev().addClass("hasSub").append("<span class='subarrow'></span>");
    $(".menu li").find(".mn-sub").prev().addClass("hasSub").append("<span class='subarrow'></span>");

    $('.btn-search').each(function () {
        var currentid = $(this).attr('href');
        $(this).click(function (e) {
            e.preventDefault();
            $(this).parent().toggleClass('open');
        });
        $(document).on("click", ".btn-search, .search-sub, #resultsearch", function (e) {
            e.stopPropagation();
        });

        $(document).click(function () {
            $('.quick-search').removeClass('open');
        });
    });

    $('#txsearch').focus(function () {
        $('#resultsearch').slideDown(500);
    }).focusout(function () {
        $('#resultsearch').slideUp(500);
    });

    // $('.menu .subarrow, .link-wrap .subarrow').each(function () {
    //     var currentid = $(this).attr('href');
    //     $(this).click(function (e) {
    //         e.preventDefault();
    //         $(this).parent().next().toggleClass('open');
    //         $(this).parent().toggleClass('open');
    //     });
    // });
    $('.menu .subarrow').each(function () {
        var currentid = $(this).attr('href');
        $(this).click(function (e) {
            e.preventDefault();
            //  $('.nav-link').toggleClass('open');
            //   $('.mn-sub.colsub-1').toggleClass('open');
              $(this).parent().next().toggleClass('open');
        });
    });

    $('.control-page').each(function () {
        var currentid = $(this).attr('href');
        $(this).click(function (e) {
            e.preventDefault();
            $(this).toggleClass('active-burger');
            $(currentid).toggleClass('open-sub');
            $('body').toggleClass('open-page');
        });
    });


    $('.btn-control').each(function () {
        var currentid = $(this).attr('href'),
                lnk = $(currentid).find('a'),
                str = $(this).next().find('.active').text();
        if (str != "") {
            $(this).html(str);
        }
        /*$(lnk).each(function(){	       
         $(this).click(function(e){
         e.preventDefault();
         var str2 = $(this).text();
         $(this).parent().parent().prev().html(str2);	
         });				
         });	*/
        $(this).click(function (e) {
            e.preventDefault();
            $(this).toggleClass('open');
            $(currentid).toggle().toggleClass('opensub');
        });
        /*$('body').on('click',function(event){
         if(!$(event.target).is('.btn-control')){
         $(this).removeClass('open');
         $(currentid).hide().removeClass("opensub");
         }
         });*/
    });

    $('.show-control').each(function () {
        var btn = $(this).find('.control');
        $(btn).click(function (e) {
            e.preventDefault();
            $(btn).toggleClass('open');
            $(btn).next().toggleClass('opensub');
        })

        $(document).on("click", ".control, .sub-content", function (e) {
            e.stopPropagation();
        });

        $(document).click(function () {
            $(btn).removeClass('open');
            $(btn).next().removeClass("opensub");
        });
    });

    $('.touch .btn-show-wrap').each(function () {
        $(this).click(function () {
            if ($(this).hasClass()) {
                $(this).removeClass('open');
            } else {
                $('.touch .btn-show-wrap').removeClass('open');
                $(this).addClass('open');
            }
        });

        $(document).on("click", ".touch .btn-show-wrap, .touch .btn-show-wrap .descript", function (e) {
            e.stopPropagation();
        });

        $(document).click(function () {
            $('.touch .btn-show-wrap').removeClass('open');
        });
    });
    $('.notouch .btn-show-wrap').each(function () {
        $(this).hover(function () {
            $(this).toggleClass('open');
        });
    });

    $('.btn-drop').each(function () {
        var currentid = $(this).attr('href'),
                str = $(this).next().find('.active').text();
        $(this).html(str);
        $(this).click(function (e) {
            e.preventDefault();
            e.stopPropagation();
            if ($(this).hasClass('open')) {
                $(this).removeClass('open');
                $(currentid).removeClass('opensub');
            } else {
                $(".drop-wrap .drop-content").removeClass('opensub');
                $('.btn-drop').removeClass('open');
                $(this).addClass('open');
                $(currentid).addClass('opensub');
            }
        });
        /*$(currentid).click(function(e){
         e.stopPropagation();
         });*/

        $(document).click(function () {
            $(".drop-wrap .drop-content").removeClass('opensub');
            $('.btn-drop').removeClass('open');
        });
    });

    $('.nav-1 .hasSub').each(function () {
        $(this).click(function (e) {
            e.preventDefault();
            e.stopPropagation();
            var item = $(this).parent();
            if ($(item).hasClass('open')) {
                $(item).removeClass('open');
            } else {
                $('.nav-1 li').removeClass('open');
                $(item).addClass('open');
            }
        });
    });


    /* bg appointment */
    // $('.bg').each(function () {
    //     var imgUrl1 = $(this).find('.bgimg').attr('src');
    //     $(this).fixbg({srcimg: imgUrl1});
    // });


    /* btn scroll to element */
    $('.smoothscroll').bind('click', function (e) {
        e.preventDefault();
        var target = this.hash,
                $target = $(target);
        $('html, body').stop().animate({
            'scrollTop': $target.offset().top
        }, 1000, 'swing', function () {
            window.location.hash = target;
        });
    });


    $('.date-wrap .nogetdate').datetimepicker({
        format: 'DD MMM YYYY',

    })
            .on("dp.change", function (e) {
                if ($(this).attr('id') == 'specifieddate')
                {
                    $('#search-blog-year').val('');
                    $('#search-blog-month').val('');
                    $('#blog-search').submit();
                }
            });
    ;

    $('.date-wrap .getdate').datetimepicker({
        defaultDate: new Date(),
        format: 'DD MMMM YYYY'
    });

    $('.time-wrap input').datetimepicker({
        format: 'LT',
        icons: {
            up: "fas fa-chevron-up",
            down: "fas fa-chevron-down"
        }
    });

    /*
     $('.form-ani .form-control, .comment-form .form-control .hs-form').each(function () {
     var tmpval = $(this).val();
     if (tmpval == "") {
     $(this).parent().parent().addClass('empty');
     $(this).parent().parent().removeClass('not-empty');
     } else {
     $(this).parent().parent().addClass('not-empty');
     $(this).parent().parent().removeClass('empty');
     }
     });
     $('.form-ani .form-control, .comment-form .form-control').focus(function () {
     $(this).parent().parent().addClass('focus');
     });
     $('.form-ani .form-control, .comment-form .form-control').focusout(function () {
     $(this).parent().parent().removeClass('focus');
     });
     $('.form-ani .form-control, .comment-form .form-control').blur(function () {
     var tmpval = $(this).val();
     if (tmpval == "") {
     $(this).parent().parent().addClass('empty');
     $(this).parent().parent().removeClass('not-empty');
     } else {
     $(this).parent().parent().addClass('not-empty');
     $(this).parent().parent().removeClass('empty');
     }
     });*/



    $('.form-ani .form-control, .comment-form .form-control').each(function () {
        var tmpval = $(this).val();
        if (tmpval == "") {
            $(this).parents('.inrow').addClass('empty');
            $(this).parents('.inrow').removeClass('not-empty');
        } else {
            $(this).parents('.inrow').addClass('not-empty');
            $(this).parents('.inrow').removeClass('empty');
        }
    });
    $('.form-ani .form-control, .comment-form .form-control').focus(function () {
        $(this).parents('.inrow').addClass('focus');
    });
    $('.form-ani .form-control, .comment-form .form-control').focusout(function () {
        $(this).parents('.inrow').removeClass('focus');
    });
    $('.form-ani .form-control, .comment-form .form-control').blur(function () {
        var tmpval = $(this).val();
        if (tmpval == "") {
            $(this).parents('.inrow').addClass('empty');
            $(this).parents('.inrow').removeClass('not-empty');
        } else {
            $(this).parents('.inrow').addClass('not-empty');
            $(this).parents('.inrow').removeClass('empty');
        }
    });


    $(".toggle-password").click(function () {
        $(this).toggleClass("fa-eye fa-eye-slash");
        var input = $($(this).attr("toggle"));
        if (input.attr("type") == "password") {
            input.attr("type", "text");
        } else {
            input.attr("type", "password");
        }
    });


    $('.control-row .control').each(function () {
        var element = $(this).parent().parent().parent(),
                current = $(element).attr('data-classub');
        $(this).click(function (e) {
            e.preventDefault();
            if ($(element).hasClass('open')) {
                $(element).removeClass('open');
                $(current).removeClass('open-row');
            } else {
                //$('.control-row').removeClass('open');
                //$('.sub-row').removeClass('open-row');
                $(element).addClass('open');
                $(current).addClass('open-row');

            }
        });
    });

    $('ul.tabs li').each(function () {
        $(this).click(function () {
            var tab_id = $(this).attr('data-tab'),
                    tab_nav = $(this).attr('data-nav'),
                    tab_content = $(this).attr('data-content');

            $(tab_nav).removeClass('active');
            $(tab_content).removeClass('current');

            $(this).addClass('active');
            $(tab_id).addClass('current');
        });
    });
	$('ul.hashtab li').each(function () {
		var tab_id = $(this).attr('data-tab'),
				tab_nav = $(this).attr('data-nav'),
				tab_content = $(this).attr('data-content');
		var hash = window.location.hash,
		number = $(hash).index();
            $(tab_nav).removeClass('active');
            $(tab_content).removeClass('current');

	   if (hash != "") {
		   $(hash).addClass('current');
		   $('ul.hashtab li').eq(number).addClass('active');
	   } else {
		   $('ul.hashtab li:first').addClass('active');
		   $('ul.hashtab').next().find('.tabcontent:first-child').addClass('current');
	   }
    });

    $('.inputfile').on("change", function () {
        let filenames = [];
        let files = document.getElementById("customFile").files;
        if (files.length > 1) {
            filenames.push("Total Files (" + files.length + ")");
        } else {
            for (let i in files) {
                if (files.hasOwnProperty(i)) {
                    filenames.push(files[i].name);
                }
            }
        }
        $(this).parent().next(".custom-file-label").html(filenames.join(","));
    });

    $(".d-readmore").dReadmore({
        moreText: "read more <i class='fas fa-caret-down'></i>",
        lessText: "read less <i class='fas fa-caret-up'></i>",
        beforeToggle: function ($element, expanded) {
            if (!expanded) {
                console.log("true")
            }
        },
        afterToggle: function ($element, expanded) {
            if (expanded) {
                console.log("true")
            }
        }
    });

    $(".d-showmore").dReadmore({
        moreText: "show more",
        lessText: "show less",
        beforeToggle: function ($element, expanded) {
            if (!expanded) {
                console.log("true")
            }
        },
        afterToggle: function ($element, expanded) {
            if (expanded) {
                console.log("true")
            }
        }
    });

    $('.hoverpp').each(function (index, el) {
        var content = $(el).find('.hidden-pp').html();
        $(el).followPopup({
            html: content
        });
    });

    $('#calendarview').change(function () {
        if ($(this).is(":checked")) {
            $(this).parent().parent().find('.date-wrap').show();
            $(this).parent().parent().find('.yearmonth').hide();
        } else {
            $(this).parent().parent().find('.yearmonth').show();
            $(this).parent().parent().find('.date-wrap').hide();
        }
    });



    $('#specifieddate').change(function () {
        $('#blog-search').submit();

    });

    $('#search-blog-year').change(function () {
        $('#blog-search').submit();
    });

    $('#search-blog-month').change(function () {
        $('#blog-search').submit();
    });

    $('#category_filter').change(function () {
        $('#blog-search').submit();
    });


    $('.downloadreport').click(function (e) {
        e.preventDefault();
        var downloadId = $(this).attr("ref-data");
        $('#download-id').val(downloadId);
        $('#download-pp').modal('show');
    })

    $('.checkcontrol').each(function (index, el) {
        $(this).change(function () {
            var current = '#' + $(this).val();
            if ($(this).is(":checked")) {
                $(current).show();
            } else {
                $(current).hide();
            }
        });
    });


    $('#quicksearch').find('input').keypress(function (e) {
        // Enter pressed?
        if (e.which == 10 || e.which == 13) {
            $('#search_course').submit();
        }
    });


});


window.ready = new function () {

    respon();

    /*setTimeout(function(){
     $('body').addClass('loaded');
     }, 500);*/


    $('#slider').slick({
        arrows: false,
        autoplay: true,
        autoplaySpeed: 5000,
        dots: true,
        fade: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        speed: 500
    });

    $('.slick-1').slick({
        infinite: false,
        //autoplay: true,
        //autoplaySpeed: 3000,
        speed: 2000,
        slidesToShow: 4,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 1360,
                settings: {
                    slidesToShow: 3
                }
            },
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 2
                }
            },
            {
                breakpoint: 575,
                settings: {
                    arrows: false,
                    slidesToShow: 1
                }
            }
        ]
    });
    $('.slick-2').slick({
        infinite: true,
        autoplay: true,
        autoplaySpeed: 3000,
        dots: true,
        speed: 2000,
        slidesToShow: 3,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 1360,
                settings: {
                    slidesToShow: 2
                }
            },
            {
                breakpoint: 767,
                settings: {
                    slidesToShow: 1
                }
            }
        ]
    });
    $('.slider-top').slick({
        slidesToShow: 3,
        arrows: true,
        infinite: false,
        dots:true,
        autoplay: true,
        autoplaySpeed: 3000
    });
    $('.main-slide-cxrus').slick({
        infinite: true,
        autoplay: true,
        autoplaySpeed: 3000,
        dots: false,
        speed: 2000,
        slidesToShow: 3,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 1360,
                settings: {
                    slidesToShow: 3
                }
            },
            {
                breakpoint: 1000,
                settings: {
                    slidesToShow: 2
                }
            },
            {
                breakpoint: 767,
                settings: {
                    slidesToShow: 1
                }
            }
        ]
    });
    $('.slick-3').slick({
        infinite: false,
        //autoplay: true,
        autoplaySpeed: 3000,
        speed: 2000,
        slidesToShow: 3,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 1360,
                settings: {
                    slidesToShow: 2
                }
            },
            {
                breakpoint: 767,
                settings: {
                    slidesToShow: 1
                }
            }
        ]
    });

    $('.slick-4').slick({
        arrows: false,
        autoplay: true,
        autoplaySpeed: 5000,
        dots: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        speed: 500
    });

    $('.slick-5').slick({
        arrows: false,
        autoplay: true,
        autoplaySpeed: 5000,
        dots: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        speed: 1000
    });

    $('.slick-6').slick({
        autoplay: true,
        autoplaySpeed: 5000,
        slidesToShow: 1,
        slidesToScroll: 1,
        speed: 500
    });

    var itemReponsive = [
        {point: 991, item: 6},
        {point: 767, item: 4},
        {point: 600, item: 2},
        {point: 480, item: 1}
    ];

    /*setTimeout(function(){ 
     $('.multisly').each(function(){   
     slideNum = $(this).data('slide');
     (function () {
     var $wrap = $('.multisly[data-slide="'+slideNum+'"]').parent(),
     number = $('.multisly[data-slide="'+slideNum+'"]').find('.active').index();
     $('.multisly[data-slide="'+slideNum+'"]').sly({
     horizontal: 1,
     itemNav: 'basic',
     smart: 1,
     activateOn: 'click',
     mouseDragging: 1,
     touchDragging: 1,
     releaseSwing: 1,
     startAt: number,
     scrollBar: $wrap.find('.scrollbar'),
     scrollBy: 1,
     activatePageOn: 'click',
     speed: 300,
     elasticBounds: 1,
     easing: 'easeOutExpo',
     dragHandle: 1,
     dynamicHandle: 1,
     clickBar: 1,
     });
     }());
     var wslide = $(this).outerWidth(),
     whandle = $(this).find('.sly-content').outerWidth();
     if (wslide >= whandle) {
     $(this).next().css({opacity:0});
     }
     else {
     $(this).next().css({opacity:1});
     }
     });
     }, 3000);*/

	$(".multisly").imagesLoaded().done( function() {
		$(".multisly").slyReponsive({
			wrapContentInner: ".sly-content",
			item: '.item',
			itemActive: '.active',
			widthAuto: true,
			responsive: null,
			//start options sly
			horizontal: 1,
			itemNav: 'basic',
			smart: 1,
			activateOn: 'click',
			mouseDragging: 1,
			touchDragging: 1,
			releaseSwing: 1,
			startAt: 0,
			scrollBar: '.scrollbar',
			scrollBy: 1,
			pagesBar: '.pages',
			activatePageOn: 'click',
			speed: 300,
			elasticBounds: 1,
			easing: 'easeOutExpo',
			dragHandle: 1,
			dynamicHandle: 1,
			clickBar: 1,
			// Buttons
			forward: '.forward',
			backward: '.backward',
			prev: '.prev',
			next: '.next',
			prevPage: '.prevPage',
			nextPage: '.nextPage'
		});
	});

    



    $('.eheight').each(function () {
        $(this).find('.ecol').matchHeight();
    });
	
$('.grid-1').imagesLoaded( function() {
		$('.grid-1').each(function () {
			//$(this).find('figure').matchHeight();
			$(this).find('.title').matchHeight();
			$(this).find('.content').matchHeight();
		});
	});
	
    $('.grid-11').each(function () {
        $(this).find('figure').matchHeight();
        $(this).find('.inner').matchHeight();
    });
    $('.grid-18').each(function () {
        $(this).find('figure').matchHeight();
        $(this).find('.title-1').matchHeight();
        $(this).find('.inner').matchHeight();
    });
    
    $('#course-search-result').each(function () {
        $(this).find('.content').matchHeight();
    });
    
    
    $('.grid-23').each(function () {
        $(this).find('figure').matchHeight();
        $(this).find('.descripts').matchHeight();
    });

	//Added by Shashi
	$('.elementor-widget-grid').each(function () {
        $(this).find('.elementor-widget-title').matchHeight();
        $(this).find('.elementor-widget-content').matchHeight();
    });
	
	//Added by Jayesh
	$('.c-levels-grid').each(function () {
        $(this).find('.c-levels-box').matchHeight();
    });
	$('.azure-box-grid').each(function () {
        $(this).find('.azure-box').matchHeight();
    });
	$('.element-populated-grid').each(function () {
        $(this).find('.element-populated').matchHeight();
    });
	
	
	$('#what-others-also-enrolled-in').each(function () {
        $(this).find('.imgwrap').matchHeight();
        $(this).find('.content').matchHeight();
    });


    $('#recommended_courses_home, #recommended_courses_taxonomy, #course-search-result').each(function () {
        var maxHeight = 0;
        var currentHeight = 0
        $(this).find('.item').each(function () {
            currentHeight = $(this).find('.content').height();

            if (maxHeight < currentHeight)
                maxHeight = currentHeight;
            console.log(currentHeight + " - " + maxHeight);
        })

        $(this).find('.item').each(function () {
            $(this).find('.content').height(maxHeight);
        });
        console.log("Max height: " + maxHeight);
    });

    $('#recommended_courses_home, #recommended_courses_taxonomy').each(function () {
        var maxHeight = 0;
        var currentHeight = 0
        $(this).find('.item').each(function () {
            currentHeight = $(this).find('.imgwrap').height();

            if (maxHeight < currentHeight)
                maxHeight = currentHeight;
            console.log(currentHeight + " - " + maxHeight);
        })

        $(this).find('.item').each(function () {
            $(this).find('.imgwrap').height(maxHeight);
        });
        console.log("Max height: " + maxHeight);
    });

    $('#showroad-home').each(function () {
        var maxHeight = 0;
        var currentHeight = 0
        $(this).find('.item').each(function () {
            currentHeight = $(this).find('.content').height();

            if (maxHeight < currentHeight)
                maxHeight = currentHeight;
            console.log(currentHeight + " - " + maxHeight);
        })

        $(this).find('.item').each(function () {
            $(this).find('.content').height(maxHeight);
        });
        console.log("Max height: " + maxHeight);
    });

    $(".hscroll").mCustomScrollbar({
        axis: "y"
    });
    $(".vscroll").mCustomScrollbar({
        axis: "x"
    });

    //$('#sidebar').fixedsticky();

    if ($('#calendarview').is(":checked")) {
        $('#calendarview').parent().parent().find('.date-wrap').show();
        $('#calendarview').parent().parent().find('.yearmonth').hide();
    } else {
        $('#calendarview').parent().parent().find('.yearmonth').show();
        $('#calendarview').parent().parent().find('.date-wrap').hide();
    }

    document.addEventListener('wpcf7mailsent', function (event) {
        if ('880' == event.detail.contactFormId) {
            $('#download-pp').modal('hide');
            location.href = '/download.php';
        } else if ('611' == event.detail.contactFormId)
        {
            location.href = '/thank-you';
        }
    }, false);
    
    $('.offer-list').each(function() {
		$(this).find('.content').matchHeight();
		$(this).find('.offer-item').matchHeight();
	});
	
	//Added by Shashi
	jQuery('a[href^="#"].aboutpagescroll').click(function(e) {
		var headerHeight = $(".header-container").outerHeight();
        jQuery('html,body').animate({scrollTop: jQuery(this.hash).offset().top-(headerHeight-1)}, 1000);
        e.preventDefault();
        return false;
    });

};

$(window).resize(function () {
    respon();

});

