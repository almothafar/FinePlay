'use strict';

$(document).ready(function() {

	Split(['#oldPane', '#newPane']);

	var updateTabState = function(){

		var maxHeight = Math.max($('#oldPane').height(), $('#newPane').height());
		$('#h-split-pane').height(maxHeight);
	};

	updateTabState();

	var timer = false;
	$('#lib-content').on('mouseup touchend pointerup', function() {

		if (timer !== false) {

			clearTimeout(timer);
		}
		timer = setTimeout(function() {

			updateTabState();
		}, 100);
	});

	$('#diffButton').on('click', function(){

		var jsDiff= Diff.createPatch("", $('#oldPane>.jsPane>.code').val(), $('#newPane>.jsPane>.code').val(), $('#oldPane>.jsPane>.fileName').val(), $('#newPane>.jsPane>.fileName').val());
		var javaDiff= Diff.createPatch("", $('#oldPane>.javaPane>.code').val(), $('#newPane>.javaPane>.code').val(), $('#oldPane>.javaPane>.fileName').val(), $('#newPane>.javaPane>.fileName').val());

		var lineDiffExample = jsDiff + javaDiff;

		var diff2htmlUi = new Diff2HtmlUI({diff: lineDiffExample});

		diff2htmlUi.draw('#line-by-line', {
			inputFormat: 'json',
			showFiles: true,
			matching: 'lines'
		});
		diff2htmlUi.fileListCloseable('#line-by-line', false);
		diff2htmlUi.highlightCode('#line-by-line');

		diff2htmlUi.draw('#side-by-side', {
			inputFormat: 'json',
			showFiles: true,
			matching: 'lines',
			outputFormat: 'side-by-side',
			synchronisedScroll: true
		});
		diff2htmlUi.fileListCloseable('#side-by-side', false);
		diff2htmlUi.highlightCode('#side-by-side');
	});
});
