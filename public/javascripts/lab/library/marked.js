'use strict';

$(document).ready(function() {

	marked.setOptions({
		langPrefix: ''
	});

	var editor = markededit.init('#markededit');

	$('#previewButton').on('click', function(){

		var html = editor.html();
		$('#codeArea').text(html);

		$('#previewArea').html(html);

		markedstrap('#previewArea');
	});
});