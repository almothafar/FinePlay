'use strict';

showFromRight('#detailContent');

$('#previousButton').on('click', function(event){

	event.preventDefault();

	hideToRight('#detailContent', function(event){

		window.location.href = Routes.controllers.lab.maintenance.Maintenance.master().url;
	});
});
