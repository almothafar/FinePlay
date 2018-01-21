'use strict';

showFromRight('#detailContent');

$('#previousButton').on('click', function(e){

	e.preventDefault();

	hideToRight('#detailContent', function(e){

		window.location.href = Routes.controllers.lab.maintenance.Maintenance.master().url;
	});
});
