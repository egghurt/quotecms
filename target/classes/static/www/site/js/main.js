$(function() {
    $('#tab_header ul li.item').on('mouseover', function() {
        var number = $(this).data('option');
        $('#tab_header ul li.item').removeClass('is-active');
        $(this).addClass('is-active');
        $('#tab_container .container_item').removeClass('is-active');
        $('div[data-item="' + number + '"]').addClass('is-active');
    });

    $(".skitter-large").skitter({
        dots: false,
        numbers_align: 'right',
        numbers: true
    });
});