function initFormJS(){

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

}



$('li.quick-link').click(function(){

    let target = '#'+$(this).data('target');

    let header_height = $('header').first().height();



    $('html, body').animate({ scrollTop: ( $(target).offset().top - header_height - 50 ) }, 'slow');

});

$(document).on('change', '#batchid-search', function(){
    let keyword = $(this).val();
    $('#schedule-list tr').show();

    if( keyword.length > 0 ){
        $('#schedule-list tbody tr').each(function(){
            let found = false;
            $(this).find('td').each(function(){
                if( $(this).html().search(keyword) !== -1 ){
                    found = true;
                }
            });

            if(found === false){
                $(this).hide();
            }
        });
    }
});


