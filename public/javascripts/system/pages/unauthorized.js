'use strict';

$('#unauthorizedModal').modal({backdrop:'static'});

$('#unauthorizedModalOk').on('click', function (e) {

	window.location.href = Routes.controllers.home.Home.index().url;
});

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		if($("#unauthorizedModal").is(".show")){

			$('#unauthorizedModalOk').trigger("click");
		}else{

		}

		e.preventDefault();
	}
})
