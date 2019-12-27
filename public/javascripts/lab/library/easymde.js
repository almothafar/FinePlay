'use strict';

$(document).ready(function() {

	var editor = new EasyMDE({
		element: document.getElementById('easymde'),
		minHeight: "100px"
	});

	editor.codemirror.on("change", function(){

		console.log(editor.value());
	});

	$('#previewButton').on('click', function(){

		var html = editor.markdown(editor.value());
		$('#codeArea').text(html);

		$('#previewArea').html(html);

		markedstrap('#previewArea');
	});
});