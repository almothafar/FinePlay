'use strict';

var initEraceableField = function(selector){

	var startDirection;
	var endDirection;
	if(!$('html').hasClass('dir-rtl')){

		startDirection = "left";
		endDirection = "right";
	}else{

		startDirection = "right";
		endDirection = "left";
	}

	var updateIconState = function(input){

		var addOn = input.next('.input-group-append').children('.input-group-text');
		var icon = addOn.children().eq(0);

		if(0 == input.val().length){

			icon.addClass("invisible");
		}else{

			icon.removeClass("invisible");
		}
	}

	var input = $(selector + ">input");
	input.css("border-"+endDirection+"-width","0px");
	input.on('change', function(event){

		var currentInput = $(this);
		updateIconState(currentInput);
	});

	input.each(function(){

		var input = $(this);
		var addOn = input.next('.input-group-append').children('.input-group-text');
		var addOnStyle = {"background-color": getTheme().backgroundColor, "color": "#495057"};
		addOnStyle["border-"+startDirection+"-width"] = "0px";
		addOn.css(addOnStyle);

		updateIconState(input);

		input.focus(function(){

			var color = $.Color(getTheme().primary.backgroundColor);
			var focusBorderColor = $.Color({hue: color.hue(), saturation: color.saturation(), lightness: 0.75, alpha: color.alpha()}).toHexString()

//			addOn.css({"border-color": focusBorderColor, "outline": "0"});
		}).blur(function(){

//			addOn.css({"border-color": "rgba(0, 0, 0, .15)"});
		});

		var icon = addOn.children().eq(0);
		icon.on('click', function(event){

			var currentIcon = $(this);
			var currentAddOn = currentIcon.parent().parent();
			var currentInput = currentAddOn.prev(".form-control");

			currentInput.val("");
			currentIcon.addClass("invisible");
		});
	});
}

initEraceableField(".eraceable");
