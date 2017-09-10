'use strict';

$( "#pageSizeSelect>select" ).change(function(e) {

	var pageSize = $(this).children(':selected').attr('value');
	location.href = Messages("path") + "?size="+pageSize+"&page=0";
});
