'use strict';

$(document).ready(function() {

	var getSummary = function(lang, search){

		var resultJSON = $.ajax({
			type: 'GET',
			url: 'https://' + lang + '.wikipedia.org/api/rest_v1/page/summary/' + search,
			async: false
		}).responseText;

		var result = JSON.parse(resultJSON);
		var summary = result.extract_html;
		if(summary){

			return summary;
		}else{

			var error = result.detail;
			if(error){

				return error;
			}else{

				return "error";
			}
		}
	};

	$('.glanceable a').popover({
		template: '<div class="glance-popover popover" role="tooltip"><div class="arrow"></div><h3 class="popover-header"></h3><div class="popover-body"></div></div>',
		trigger: 'hover',
		placement: 'bottom',
		html: true,
		content: function(){

			var link = $(this);
			var url = link.attr('href');
			var search = url.split('/').pop();
			return getSummary('en', search);
		}
	});

	$('.glanceable a').on('click', function(){

		var isShowWithPress = 1 <= $('.glanceable a.show-with-press').length;
		if(isShowWithPress){

			$('.glanceable a').popover('hide').removeClass('show-with-press');
			return false;
		}
	}).each(function(){

		new Hammer(this).on('tap press', function(e) {

			var link = $(e.target);

			switch (e.type){
			case 'press':

				$('.glanceable a.show-with-press').popover('hide').removeClass('show-with-press');
				link.popover('show').addClass('show-with-press');
				break;
			default:

				break;
			}
		});
	});
});
