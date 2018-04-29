'use strict';

var parsleyValidatorsMessages = function(messageKey){

	return $("#parsleyvalidators_messages").data('messages')[messageKey];
}

var ParsleyValidators = {

	UserIdValidator: {
		validateString: function(value) {
			return /\b[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\b/.test(value);
		},
		messages: {
			en: parsleyValidatorsMessages('en_' + MessageKeys.SYSTEM_ERROR_USERID),
			ja: parsleyValidatorsMessages('ja_' + MessageKeys.SYSTEM_ERROR_USERID)
		}
	},
	PasswordValidator: {
		validateString: function(value) {
			return /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!#$%&'*+/=?^_`{|}~-]).{8,16}/.test(value);
		},
		messages: {
			en: parsleyValidatorsMessages('en_' + MessageKeys.SYSTEM_ERROR_PASSWORD),
			ja: parsleyValidatorsMessages('ja_' + MessageKeys.SYSTEM_ERROR_PASSWORD)
		}
	},
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
