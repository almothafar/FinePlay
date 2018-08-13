'use strict';

$(document).ready(function() {

	$('.i_task').on('click', function(e) {

		var isDetailOfThisTask = $(this).attr("href") == (window.location.pathname + window.location.search);
		if(isDetailOfThisTask){

			$('#i_content').addClass('detail');
			e.preventDefault();
		}
	});

	$('.detail #backButton').on('click', function() {

		hideToRight('#i_detail', function(e){

			$('#i_content').removeClass('detail');
			$('#i_detail').css('display', '');
		});
	});
});
