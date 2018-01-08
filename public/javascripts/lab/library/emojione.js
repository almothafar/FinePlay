'use strict';

$(document).ready(function() {

	emojione.emojiSize = '64';
	$('.convertButton').on('click', function(){

		var inputText = document.getElementById('sourceText').value;
		var outputHtml = emojione.shortnameToImage(inputText);
		notifyAlert('info', outputHtml);
	});
});
