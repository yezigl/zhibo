/**
 * 
 */
function ajaxDelete(url, data, success, error) {
    var data = params(ids);
    if (ajaxError) {
        ajaxError = false;
        return;
    }
    jQuery.ajax({
        'url': url,
        'type': 'DELETE',
        'data': data,
        'success': success,
        'error': error,
    });
}