'use strict';

$(document).ready(function () {

	if(window.localStorage){

		$("#LocalStorage").removeClass("badge-danger").addClass("badge-success");

		Forms.restore('#inputArea');

		setInterval(function(){

			Forms.store('#inputArea');
			notifyAlert('info', 'saved.', 3000);
		},3000)
	}
});
