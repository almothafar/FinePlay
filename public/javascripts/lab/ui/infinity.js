'use strict';

$(document).ready(function () {

	var lastItem;
	getContent().scroll(function(e){

		if(lastItem){

			var appendY = $('body').outerHeight();
			var lastY = lastItem.offset().top;
			if(lastY < appendY){

				appndItems(document.createDocumentFragment());
			}
		}
	});

	var appndItems = function(fragment){

		for(var i=0; i<10; i++){

			var newItem = $('' +
				'<li class="media mb-4 infinityItem">' +
					'<img src="' + messages("img") + '" class="d-flex mr-3" alt="Picture"></img>' +
					'<div class="media-body draft">' +
						'<h5 class="mt-0 mb-1"><span class="draft-text">List-based media object</span></h5>' +
						'<span class="draft-text">Cras sit amet nibh libero, in gravida nulla.' +
						'Nulla vel metus scelerisque ante sollicitudin.' +
						'Cras purus odio, vestibulum in vulputate at, tempus viverra turpis.' +
						'Fusce condimentum nunc ac nisi vulputate fringilla.' +
						'Donec lacinia congue felis in faucibus.</span>' +
					'</div>' +
				'</li>');
			fragment.appendChild(newItem.get(0));
		}
		$('#infinityItems')[0].appendChild(fragment);

		lastItem = $('#infinityItems>.infinityItem').last();
	}

	appndItems(document.createDocumentFragment());
});
