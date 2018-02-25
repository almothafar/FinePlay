'use strict';

$(window).on('load', function () {

	var colors = ["#ff6666", "#ffcc66", "#66ff66", "#66ccff", "#6666ff", "#cc66ff", "#ff66ff"];
	$('.icon_body').each(function(){

		$(this).css('color', colors[0|Math.random() * colors.length]);
	});
});

var faIconList = $('#fa>.icon_list');
var faIconResultList = $('#fa>.icon_result_list');
var miIconList = $('#materialicons>.icon_list');
var miIconResultList = $('#materialicons>.icon_result_list');
var icoFontIconList = $('#icofont>.icon_list');
var icoFontIconResultList = $('#icofont>.icon_result_list');
var emojiOneIconList = $('#emojione>.icon_list');
var emojiOneIconResultList = $('#emojione>.icon_result_list');

var keyupEvents = [];
$("#searchField").on('keyup', function () {

	keyupEvents.push('keyup');

	var searchText = $(this).val();
	setTimeout(function () {

		keyupEvents.pop();

		if (0 == keyupEvents.length) {

			faIconList.show();
			miIconList.show();
			icoFontIconList.show();
			emojiOneIconList.show();

			if(0 == searchText.length){

				faIconResultList.hide();
				miIconResultList.hide();
				icoFontIconResultList.hide();
				emojiOneIconResultList.hide();
			}else{

				faIconList.hide();
				miIconList.hide();
				icoFontIconList.hide();
				emojiOneIconList.hide();

				faIconResultList.empty();
				miIconResultList.empty();
				icoFontIconResultList.empty();
				emojiOneIconResultList.empty();

				$("#fa .icon_caption:contains('" + searchText + "')").each(function(){faIconResultList.append($(this).parent().clone());});
				$("#materialicons .icon_caption:contains('" + searchText + "')").each(function(){miIconResultList.append($(this).parent().clone());});
				$("#icofont .icon_caption:contains('" + searchText + "')").each(function(){icoFontIconResultList.append($(this).parent().clone());});
				$("#emojione .icon_caption:contains('" + searchText + "')").each(function(){emojiOneIconResultList.append($(this).parent().clone());});

				faIconResultList.show();
				miIconResultList.show();
				icoFontIconResultList.show();
				emojiOneIconResultList.show();
			}
		}
	}, 500);
});
