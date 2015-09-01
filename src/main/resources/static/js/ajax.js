/**
 * 
 */
function ajaxPost(url, ids, success, error) {
    var data = {};
    jQuery.each(ids, function(i, id) {
        data[id] = jQuery('#' + id).val();
    });
    jQuery.ajax({
        'url': url,
        'type': 'POST',
        'data': data,
        'success': success,
        'error': error,
    });
}

function ajaxGet(url, ids, success, error) {
    var data = {};
    jQuery.each(ids, function(i, id) {
        data[id] = jQuery('#' + id).val();
    });
    jQuery.ajax({
        'url': url,
        'type': 'GET',
        'data': data,
        'success': success,
        'error': error,
    });
}