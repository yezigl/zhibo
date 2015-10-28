/**
 * 
 */
function ajaxDelete(url, data, success, error) {
    jQuery.ajax({
        'url': url,
        'type': 'DELETE',
        'data': data,
        'success': success,
        'error': error,
    });
}