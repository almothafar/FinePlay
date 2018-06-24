'use strict';

var faIconList = $('#fa>.icon_list');
var faIconResultList = $('#fa>.icon_result_list');
var miIconList = $('#materialicons>.icon_list');
var miIconResultList = $('#materialicons>.icon_result_list');
var ioniconsIconList = $('#ionicons>.icon_list');
var ioniconsIconResultList = $('#ionicons>.icon_result_list');
var icoFontIconList = $('#icofont>.icon_list');
var icoFontIconResultList = $('#icofont>.icon_result_list');

var keyupEvents = [];
$("#searchField").on('keyup', function () {

	keyupEvents.push('keyup');

	var searchText = $(this).val();
	setTimeout(function () {

		keyupEvents.pop();

		if (0 == keyupEvents.length) {

			faIconList.show();
			miIconList.show();
			ioniconsIconList.show();
			icoFontIconList.show();

			if(0 == searchText.length){

				faIconResultList.hide();
				miIconResultList.hide();
				ioniconsIconResultList.hide();
				icoFontIconResultList.hide();
			}else{

				faIconList.hide();
				miIconList.hide();
				ioniconsIconList.hide();
				icoFontIconList.hide();

				faIconResultList.empty();
				miIconResultList.empty();
				ioniconsIconResultList.empty();
				icoFontIconResultList.empty();

				$("#fa .icon_caption:contains('" + searchText + "')").each(function(){faIconResultList.append($(this).parent().clone());});
				$("#materialicons .icon_caption:contains('" + searchText + "')").each(function(){miIconResultList.append($(this).parent().clone());});
				$("#ionicons .icon_caption:contains('" + searchText + "')").each(function(){ioniconsIconResultList.append($(this).parent().clone());});
				$("#icofont .icon_caption:contains('" + searchText + "')").each(function(){icoFontIconResultList.append($(this).parent().clone());});

				faIconResultList.show();
				miIconResultList.show();
				ioniconsIconResultList.show();
				icoFontIconResultList.show();
			}
		}
	}, 500);
});
