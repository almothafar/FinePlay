'use strict';

$(document).ready(function() {

	$('#summernote').summernote({
		height: 300,
		minHeight: null,
		maxHeight: null,
		focus: true
	});

	$('#previewButton').on('click', function(){

		var code = $('#summernote').summernote('code');
		$('#codeArea').text(code);

		var preview = $('#summernote + .note-editor > .note-editing-area > .note-editable').clone().children();
		$('#previewArea').empty().append(preview);
	});
});