'use strict';

var parsleyValidatorsMessages = function(messageKey){

	return $("#parsleyvalidators_messages").data('messages')[messageKey];
}

var ParsleyValidators = {

	HiraganaValidator: {
		validateString: function(value) {
			return /^[ぁ-ゖ゛゜ゝゞー]*$/.test(value);
		},
		messages: {
			en: parsleyValidatorsMessages('en_' + MessageKeys.SYSTEM_ERROR_HIRAGANA),
			ja: parsleyValidatorsMessages('ja_' + MessageKeys.SYSTEM_ERROR_HIRAGANA)
		}
	},
	KatakanaValidator: {
		validateString: function(value) {
			return /^[ァ-ヶ゛゜ヽヾー]*$/.test(value);
		},
		messages: {
			en: parsleyValidatorsMessages('en_' + MessageKeys.SYSTEM_ERROR_KATAKANA),
			ja: parsleyValidatorsMessages('ja_' + MessageKeys.SYSTEM_ERROR_KATAKANA)
		}
	}
}
