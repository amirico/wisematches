$(document).ready(function () {
    var timeoutID;

    $("#language-combobox .ui-state-default").hover(function () {
        $(this).addClass('ui-state-active').removeClass('ui-state-default');
    }, function () {
        $(this).addClass('ui-state-default').removeClass('ui-state-active');
    });

    $('.dropdown').mouseenter(function () {
        var submenu = $('.sublinks').stop(false, true).hide();
        window.clearTimeout(timeoutID);

        submenu.css({
            width:$(this).width() + 20 + 'px',
            top:$(this).offset().top + $(this).height() + 7 + 'px',
            left:$(this).offset().left + 'px'
        });

        submenu.stop().slideDown(300);

        submenu.mouseleave(function () {
            $(this).slideUp(300);
        });

        submenu.mouseenter(function () {
            window.clearTimeout(timeoutID);
        });

    });
    $('.dropdown').mouseleave(function () {
        timeoutID = window.setTimeout(function () {
            $('.sublinks').stop(false, true).slideUp(300);
        }, 250);
    });
});
