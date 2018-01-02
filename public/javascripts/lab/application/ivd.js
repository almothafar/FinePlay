'use strict';

var initInput = function(){

	$('input[name=searchType]').eq(0).prop('checked', true);
	$('input[name=searchText]').val('');
};
var initResult = function(){

	$('#character').empty();

	$('#variations').empty();
	emptyVariations();

	$('#ajaxMessage').text('');
	$('#ajaxDescription').text('-');
	$('#ajaxProgress>.progress-bar').removeClass('bg-danger').css('width', '0%');
	$('#ajaxCancel').prop('disabled', false);
};

var emptyVariations = function(){

	$('#variations').append('<div class="mx-auto font-weight-bold text-muted">' + Messages("variationEmpty") + '</div>');
};

var adobeJapan1Position;
var hanyoDenshiPosition;
$('#pickerWindow').on('shown.bs.modal', function (e) {

	adobeJapan1Position = getPosition('#adobe-japan1Text');
	hanyoDenshiPosition = getPosition('#hanyo-denshiText');

	initInput();
	initResult();

	$('input[name=searchText]').focus();
})

var search = function(){

	var type = $('input[name=searchType]:checked').val();
	var text = $('input[name=searchText]').val();

	if(0 == text.length){

		initResult();
		return;
	}

	initResult();
	$('#ajaxCancel').prop('disabled', true);
	$('#ajaxMessage').text(Messages(MessageKeys.PLEASE__WAIT));
	$('#ajaxProgress>.progress-bar').addClass('bg-primary').css('width', '100%')

	var timeout = "10000";
	$.ajax({
		method:"GET",
		url: Routes.apis.character.Character.character().url + "?" + getToken(),
		data: $.param({
				type: type,
				text: text
		}),
		contentType: 'text/plain',
		dataType: "json",
		timeout: timeout
	})
	.then(
		function (responseJson) {

			initResult();

			var codePoint = responseJson["codePoint"];
			var hex = responseJson["hex"];
			var character = responseJson["character"];
			var variations = responseJson["variations"];

			$('#character').html(character + '<small class="text-muted">' + '&nbsp;&nbsp;' + Messages(MessageKeys.CODEPOINT) + ':' + codePoint + '&nbsp;&nbsp;' + Messages(MessageKeys.HEX) + ':' + hex + '</small>');

			if(0 == variations.length){

				$('#variations').empty();
				emptyVariations();
			}else{

				$('#variations').empty();
				$.each(variations, function(i, key) {

					var variation = variations[i];

					var variationCodePoint = variation["codePoint"];
					var variationHex = variation["hex"];
					var variationSequence = variation["character"];
					var variationCollections = variation["collections"];

					var variationCharacter = (character+variationSequence);

					var cssClass = "";
					var idTip = "";
					$.each(variationCollections, function(key, val) {

						var variationCollection = variationCollections[key];

						var variationCollectionName = variationCollection["name"];
						cssClass = cssClass + " " + variationCollectionName.toLowerCase();

						var variationCollectionId = variationCollection["id"];
						idTip = idTip + variationCollectionId + '<br>';
					});

					var variation = '<div class="variation' + cssClass + '">' +
									'<div class="marker">' +
										'<i id="" class="fa fa-circle"></i><i id="" class="fa fa-circle "></i><i id="" class="fa fa-circle "></i><i id="" class="fa fa-circle "></i><i id="" class="fa fa-circle "></i>' +
									'</div>' +
									'<div class="character">' + variationCharacter + '</div>' +
									'<small class="text-muted">' + variationHex +
										'<button id="" type="button" class="btn btn-link btn-sm p-0" title="' + idTip + '" data-toggle="tooltip" data-placement="top"><i id="" class="fa fa-info-circle"></i></button>' +
									'</small>' +
								'</div>';
					$('#variations').append(variation);
				});
			}

			$('[data-toggle="tooltip"]').tooltip({html: true});
		},
		function (jqXHR, textStatus, errorThrown) {

			$('#ajaxMessage').text(Messages(MessageKeys.FAILURE));
			$('#ajaxCancel').prop('disabled', false);
			var errorMessage = errorThrown;
			if(jqXHR.responseJSON){errorMessage = jqXHR.responseJSON['error'];}
			$('#ajaxDescription').html(Messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + Messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorMessage+'</strong>');
			$('#ajaxProgress>.progress-bar').removeClass('bg-primary').addClass('bg-danger');
		}
	);
}

$('input[name=searchType]').on('change', function () {

	search();
});

var keyupEvents = [];
$('input[name=searchText]').on('keyup', function () {

	keyupEvents.push('keyup');

	var searchText = $(this).val();
	setTimeout(function () {

		keyupEvents.pop();

		if (0 == keyupEvents.length) {

			search();
		}
	}, 500);
});

$('#variations').on('click', '.variation', function (e) {

	$('#variations>.variation.bg-primary').removeClass('bg-primary');
	var variation = $(this);
	variation.addClass('bg-primary');
});

$('#okButton').on('click', function (e) {

	var button = $(this);
	var selectedCharacter = $('#variations>.variation.bg-primary').children('.character').text();

	// possibility of dependant browser
	insertText('#adobe-japan1Text', adobeJapan1Position[0], adobeJapan1Position[1], selectedCharacter);
	insertText('#hanyo-denshiText', hanyoDenshiPosition[0], hanyoDenshiPosition[1], selectedCharacter);
})

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		if($("#pickerWindow").is(".show")){

			$('#okButton').trigger("click");
		}

		e.preventDefault();
	}
});

var getPosition = function(selector){

	var element = document.querySelector(selector);
	var start = element.selectionStart;
	var end = element.selectionEnd;
	return [start, end];
}

var insertText = function(selector, start, end, text){

	var element = document.querySelector(selector);
	element.value = replaceText(element.value, start ,end ,text);
}

var replaceText = function(source, start, end, text){

	var right = source.substring(0, start);
	var middle = source.substring(start, end);
	var left = source.substring(end, source.length);

	return right + text + left;
}

