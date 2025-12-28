AUI().ready(function () {
    showControlMenu();
    modalSchedule();
    slickSlider();
    sosialToogle();
    jsPswd();
    tabCari();
    popularCourse();
    popularCourseNewHtml();
    // navSpesial();
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
function jsPswd() {
    $(".pswd").click(function(){
        $(".box-info-required").addClass("dshow");
        $(".box-info-required").removeClass("dnone");
      });
      $(".pswd input").keyup(function(){
        $(".box-info-required").addClass("dnone");
        $(".box-info-required").removeClass("dshow");
      });

      $(document).ready(function(){
        $("#toggle_pwd").click(function () {
            $(this).toggleClass("fa-eye fa-eye-slash");
            var type = $(this).hasClass("fa-eye-slash") ? "text" : "password";
            $("#txtPassword").attr("type", type);
        });
    });
}
function tabCari(){
    $('.tab-search').slick({
        dots: false,
        infinite: false,
        speed: 300,
        slidesToShow: 5,
        centerMode: false,
        arrows: false,
        responsive: [
            {
                breakpoint: 1024,
                settings: {
                slidesToShow: 2,
                arrows: true,
                slidesToScroll: 2
                }
            },
            {
                breakpoint: 600,
                settings: {
                    arrows: true,
                slidesToShow: 2,
                slidesToScroll: 2
                }
            },
            {
                breakpoint: 480,
                settings: {
                arrows: true,
                slidesToShow: 2,
                slidesToScroll: 2
                }
            }
        ]
    });
    
    $(".box-item").click(function(){
    const id = $(this).data('id');
    if(!$(this).hasClass('active')){
        $(".box-item").removeClass('active');
        $(this).addClass('active');
        
        $('.content-data').hide();
        $(`[data-content=${id}]`).fadeIn();
    }
    });
}
function slickSlider() {
$('.slick-lhub').slick({
    autoplay: true,
    autoplaySpeed: 5000,
    slidesToShow: 1,
    slidesToScroll: 1,
    speed: 500,
    dots: true
});
$('.slider-for').slick({
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
    fade: true,
    asNavFor: '.slider-nav',
    dots:true
});
// $( document ).ready(function() {
//     $('.slider-top').slick({
//         slidesToShow: 3,
//         arrows: true,
//         infinite: false,
//         dots:true
//     });
// });
}
//  function showFilter{
//     $('.show-control').each(function () {
//         var btn = $(this).find('.control');
//         $(btn).click(function (e) {
//             e.preventDefault();
//             $(btn).toggleClass('open');
//             $(btn).next().toggleClass('opensub');
//         })

//         $(document).on("click", ".control, .sub-content", function (e) {
//             e.stopPropagation();
//         });

//         $(document).click(function () {
//             $(btn).removeClass('open');
//             $(btn).next().removeClass("opensub");
//         });
//     });

//  }

// function navSpesial() {
// 	$(".Courses .colsub-1").addClass("colsub-2");
// }

function modalSchedule() {
$(document).ready(function(){
    $(".btn-all-schedules").click(function(){
      $(".modal-all-schedules").addClass("active");
    });
    
    $(".box-close").click(function(){
      $(".modal-all-schedules").removeClass("active");
    });
});
}
function sosialToogle() {
    $('.st-right').click(function() {
        $('.sidebar-sosial').css('opacity', '0');
        $(this).css('display', 'none');
        $('.st-left').css('display', 'block');
    });
    $('.st-left').click(function() {
        $('.sidebar-sosial').css('opacity', '1');
        $(this).css('display', 'none');
        $('.st-right').css('display', 'block');
    });
}

function popularCourse() {
    var $frame  = $('#basic');
    // var $slidee = $frame.children('ul').eq(0);
    var $wrap   = $frame.parent();
    
    // Call Sly on frame
    $frame.sly({
      horizontal: 1,
      itemNav: 'basic',
      smart: 1,
      smart: 1,
      activateOn: 'click',
      mouseDragging: 1,
      touchDragging: 1,
      releaseSwing: 1,
      startAt: 0,
      scrollBar: $wrap.find('.scrollbar-ntuc'),
      scrollBy: 1,
      pagesBar: $wrap.find('.pages'),
      activatePageOn: 'click',
      speed: 300,
      elasticBounds: 1,
      easing: 'easeOutExpo',
      dragHandle: 1,
      dynamicHandle: 1,
      clickBar: 1,
      // Buttons
            
                prev: '.prev',
                next: '.next',
                prevPage: '.prevPage',
                nextPage: '.nextPage'
    });
    
}

function popularCourseNewHtml() {
    $('.slide-cxrus').slick({
        infinite: false,
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
}

$(window).scroll(function () {
    var header = $(".CX-box-header");
    var header_height = $(header).height();
    var scrollPosition = $(this).scrollTop();

    if (scrollPosition >= header_height) {
        // $(".back_to_top").removeClass("hidden");
        // $(".back_to_top").addClass("showup");
        $(header).addClass("scrolled");
    } else {
        // $(".back_to_top").removeClass("showup");
        // $(".back_to_top").addClass("hidden");
        $(header).removeClass("scrolled");
    }
});


Liferay.Portlet.ready(function (portletId, node) {});

Liferay.on(
    "allPortletsReady",

    function () {}
);

// $(window).on("load resize orientationchange", function () {});

Liferay.on("endNavigate", function (event) {});