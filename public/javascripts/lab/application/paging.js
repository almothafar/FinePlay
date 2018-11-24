'use strict';

$( "#pageSizeSelect>select" ).change(function(e) {

	var pageSize = $(this).children(':selected').attr('value');
	location.href = messages("path") + "?size="+pageSize+"&page=0";
});
