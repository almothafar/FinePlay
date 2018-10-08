'use strict';

$(document).ready(function() {

	$('.md.diagram-frame').each(function(){

		var diagramFrame = $(this);
		diagramFrame.html(markdeep.formatDiagram(diagramFrame.text(), undefined));
		diagramFrame.addClass('table-responsive')
	});
});