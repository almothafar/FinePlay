'use strict';

var taskKey = "firstAccess";
var documentId = "guided";

if(window.localStorage){

	$("#LocalStorage").removeClass("badge-danger").addClass("badge-success");

	var savedDataJson = window.localStorage.getItem(taskKey);
	if(!savedDataJson){

		savedDataJson = JSON.stringify({})
		window.localStorage.setItem(taskKey, savedDataJson);
	}

	var savedData = JSON.parse(savedDataJson);
	var isGuided = savedData[documentId];

	if(!isGuided){

		$('#firstDialog').modal('show');

		$('#firstDialog').on('shown.bs.modal', function (e) {

			$('.slick-container').slick({
				dots: true,
				rtl: $('html').hasClass('dir-rtl')
			});
		})

		savedData[documentId] = true;

		var saveDataJson = JSON.stringify(savedData);
		window.localStorage.setItem(taskKey, saveDataJson);
	}

	$('#resetButton').prop('disabled', false);

	$('#resetButton').on('click', function(){

		savedData[documentId] = false;
		var saveDataJson = JSON.stringify(savedData);
		window.localStorage.setItem(taskKey, saveDataJson);
	});
}
