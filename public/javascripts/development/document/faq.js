'use strict';

var highlight = function(text, searchText){

	var result = text.match(new RegExp('('+searchText+')', 'gi'));
	var highlighted = text;
	if(result){
		for(var i=0;i < result.length;i++){

			highlighted = highlighted.replace(new RegExp(result[i]), '<mark class="px-0">' + result[i] + '</mark>');
		}
	}
	return highlighted;
}

var keyupEvents = [];
$("#searchField").on('keyup', function () {

	keyupEvents.push('keyup');

	var searchText = $(this).val();
	setTimeout(function () {

		keyupEvents.pop();

		if (0 == keyupEvents.length) {

			$('.thing').each(function(){

				var thing = $(this);
				var thingSource = thing.data('thing');

				var titleElement = thing.find('.card-title');
				var titleSource = thingSource['title'];

				var textElement = thing.find('.card-text');
				var textSource = thingSource['text'];

				var isShow = true;
				var title;
				var text;
				if(0 == searchText.length){

					title = titleSource;
					text = textSource;

					isShow = true;
				}else{

					title = highlight(titleSource, searchText);
					text = highlight(textSource, searchText);

					var isTitleMatched = titleSource != title;
					var isTextMatched = textSource != text;

					var isMatched = isTitleMatched || isTextMatched;

					isShow = isMatched;
				}

				var thingWrapper = thing.parent();

				if(!thingWrapper.is(':visible') && isShow){

					thingWrapper.css({
						"display": "inline"
					}).animate({
						"opacity": 1
					}, 'fast');
				}else if(thingWrapper.is(':visible') && !isShow){

					thingWrapper.animate({
						"opacity": 0
					}, 'fast', function() {
						$(this).css('display','none');
					});
				}

				titleElement.html(title);
				textElement.html(text);
			});
		}
	}, 500);
});
