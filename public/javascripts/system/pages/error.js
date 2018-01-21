'use strict';

if(Messages("isPlayException")){
}else{

	$('#errorModal .modal-content').addClass('bg-dark').addClass('text-white');
}

$('#errorModal').modal({backdrop:'static'});

$('#errorModalOk').on('click', function (e) {

	window.location.href = Routes.controllers.user.User.index().url;
});

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		if($("#errorModal").is(".show")){

			$('#errorModalOk').trigger("click");
		}else{

		}

		e.preventDefault();
	}
})

$('#searchServerStackOverflowButton').on('click', function(){ window.open('https://stackoverflow.com/search?q=[java]' + $('#searchServerErrorMessage').val()); });
$('#searchServerStackOverflowJaButton').on('click', function(){ window.open('https://ja.stackoverflow.com/search?q=[java]' + $('#searchServerErrorMessage').val()); });
$('#searchServerQiitaButton').on('click', function(){ window.open('https://qiita.com/search?q=' + $('#searchServerErrorMessage').val()); });
$('#searchServerTeratailButton').on('click', function(){ window.open('https://teratail.com/questions/search?q=' + $('#searchServerErrorMessage').val()); });
