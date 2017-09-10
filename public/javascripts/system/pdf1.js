'use strict';

var pdfjsMessages = function(messageKey){

	return $("#pdfjs_messages").data('messages')[messageKey];
}

var DEFAULT_URL = pdfjsMessages("contentUrl");
