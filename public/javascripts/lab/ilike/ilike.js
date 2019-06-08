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

		hideToEnd('#i_detail', function(e){

			setTimeout(function(){

				$('#i_content').removeClass('detail');
				$('#i_detail').css('display', '');
			},100);// provisional coped for Bug? of Mobile Safari
		});
	});
});
