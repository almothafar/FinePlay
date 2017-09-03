'use strict';

var initEraceableField = function(selector){

	var updateIconState = function(input){

		var addOn = input.next(".input-group-addon");
		var icon = addOn.children().eq(0);

		if(0 == input.val().length){

			icon.css("display", "none");
		}else{

			icon.css("display", "inline");
		}
	}

	var input = $(selector + ">input");
	input.css("border-right-width","0px");
	input.on('change', function(event){

		var currentInput = $(this);
		updateIconState(currentInput);
	});

	input.each(function(){

		var input = $(this);
		var addOn = input.next(".input-group-addon");
		addOn.css({"border-left-width": "0px", "background-color": "transparent", "color": "#b3b3b3"});

		updateIconState(input);

		input.focus(function(){

			addOn.css({"border-color": "#66afe9", "outline": "0"});
		}).blur(function(){

			addOn.css({"border-color": "#ccc"});
		});

		var icon = addOn.children().eq(0);
		icon.on('click', function(event){

			var currentIcon = $(this);
			var currentAddOn = currentIcon.parent();
			var currentInput = currentAddOn.prev(".form-control");

			currentInput.val("");
			currentIcon.css("display", "none");
		});
	});
}

initEraceableField(".eraceable");
