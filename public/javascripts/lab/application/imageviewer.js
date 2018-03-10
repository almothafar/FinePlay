'use strict';

$(document).ready(function() {

	$('#single img').on('click', function(){

		var imageUrl = $(this).attr('src');

		$('#singleViewer>.modal-dialog>.modal-content img').attr('src', imageUrl);
		$('#singleViewer').modal('show');
	});

	$('#multi img').on('click', function(){

		$('#multiViewer').modal('show');

		$('#multiViewer').on('shown.bs.modal', function (e) {

			if(!$('.slick-container').hasClass('slick-initialized')){

				$('.slick-container').slick({
					dots: true
				});
			}
		})
	});
});
