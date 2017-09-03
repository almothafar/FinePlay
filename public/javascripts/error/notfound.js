'use strict';

$('#notfoundModal').modal({backdrop:'static'});

$('#notfoundModalOk').on('click', function (e) {

	window.location.href = Routes.controllers.home.Home.index().url;
});

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		if($("#notfoundModal").is(".show")){

			$('#notfoundModalOk').trigger("click");
		}else{

		}

		e.preventDefault();
	}
})
