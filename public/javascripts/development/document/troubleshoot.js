'use strict';

marked.setOptions({
	langPrefix: ''
});
//var html = `Template literal`;
var html = marked($('#markdown').text());
$('#preview').html(html);
