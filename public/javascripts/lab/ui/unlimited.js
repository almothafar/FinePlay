'use strict';

var elementHtml=
	'<div class="row">'+
		'<div class="form-group col-sm-8">'+
			'<label for="inputElement" class="sr-only">Element</label>'+
			'<input type="text" class="form-control" id="inputElement" placeholder="Element">'+
		'</div>'+
		'<div class="form-group col-sm-4">'+
			'<button id="speakButton" type="button" class="btn btn-outline-primary minusButton"><i class="fa fa-minus" ></i></button>'+
			'<button id="speakButton" type="button" class="btn btn-outline-primary plusButton"><i class="fa fa-plus" ></i></button>'+
		'</div>'+
	'</div>';

var minusClick = function(e){

	var element =$(e.currentTarget).parent().parent();
	element.remove();
}

var plusClick = function(e){

	var element =$(e.currentTarget).parent().parent();;
	element.after(elementHtml);
	var nextElement = element.next();
	nextElement.find('.minusButton').on('click',minusClick);
	nextElement.find('.plusButton').on('click',plusClick);
}

$('#unlimitedContainer').append(elementHtml);
var firstElement = $('#unlimitedContainer>.row').eq(0);
firstElement.find('.minusButton').attr("disabled", "disabled");
firstElement.find('.plusButton').on('click',plusClick);
