// AUI().ready(function () {
//     showControlMenu();
// });

// // Switch Function
// function showControlMenu() {
//     $('.check-control-panel').click(function () {
//         if ($(this).prop("checked") == true) {
//             $.cookie('show-control-menu', 'true', {
//                 expires: 365,
//                 path: '/'
//             });
//             $('body').addClass('show_c_menu');
//         } else if ($(this).prop("checked") == false) {
//             $.cookie('show-control-menu', 'false', {
//                 expires: 365,
//                 path: '/'
//             });
//             $('body').removeClass('show_c_menu');
//         }
//     });

//     if ($.cookie('show-control-menu') == "true") {
//         $('body').addClass('show_c_menu');
//         $('.check-control-panel').prop('checked', true);
//     } else {
//         $('body').removeClass('show_c_menu');
//         $('.check-control-panel').prop('checked', false);
//     }
// }

// $(window).scroll(function () {
//     var header = $(".CX-box-header");
//     var header_height = $(header).height();
//     var scrollPosition = $(this).scrollTop();

//     if (scrollPosition >= header_height) {
//         // $(".back_to_top").removeClass("hidden");
//         // $(".back_to_top").addClass("showup");
//         $(header).addClass("scrolled");
//     } else {
//         // $(".back_to_top").removeClass("showup");
//         // $(".back_to_top").addClass("hidden");
//         $(header).removeClass("scrolled");
//     }
// });


// Liferay.Portlet.ready(function (portletId, node) {});

// Liferay.on(
//     "allPortletsReady",

//     function () {}
// );

// // $(window).on("load resize orientationchange", function () {});

// Liferay.on("endNavigate", function (event) {});