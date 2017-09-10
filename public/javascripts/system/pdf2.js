'use strict';

var pdfjsMessages = function(messageKey){

	return $("#pdfjs_messages").data('messages')[messageKey];
}

PDFJS.locale = pdfjsMessages(MessageKeys.PDFJS_LANG);
