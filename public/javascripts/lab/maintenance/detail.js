'use strict';

showFromEnd('#detailContent');

$('#previousButton').on('click', function(e){

	e.preventDefault();

	hideToEnd('#detailContent', function(e){

		window.location.href = Routes.controllers.lab.maintenance.Maintenance.master().url;
	});
});
