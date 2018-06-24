'use strict';

var summernoteMessages = function(messageKey) {

	return $("#summernote_messages").data('messages')[messageKey];
}

var lang = summernoteMessages(MessageKeys.SUMMERNOTE_LANG)
if ('' == lang) {
} else {

	$.summernote.options.lang = lang;
}
