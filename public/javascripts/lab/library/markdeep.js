'use strict';

$(document).ready(function() {

	$('.md.diagram-frame').each(function(){

		var diagramFrame = $(this);
		diagramFrame.html(markdeep.formatDiagram(diagramFrame.text(), undefined));
		diagramFrame.addClass('table-responsive')
	});

	$('#test-document').one('show.bs.collapse', function(){

		$('.md.document-frame').each(function(){

			var documentFrame = $(this);
			documentFrame.html(markdeep.format(documentFrame.text(), false));
		});
		$('.md svg.diagram').parent().filter('p').addClass('table-responsive');
//		$('link[href*="markdeep"]').before(markdeep.stylesheet());
	});
});