'use strict';

$(document).ready(function() {

	var viewer = $3Dmol.createViewer($("#viewer"));
	viewer.setBackgroundColor(0x00000000, 0.2);
	viewer.addModel( $('#pdb').text(), "pdb" );
	viewer.setStyle({}, {cartoon:{color:"spectrum"}});
	viewer.render();

	setInterval(function() {

		if(viewer) {

			viewer.rotate(1);
		}
	}, 50);

});