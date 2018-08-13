'use strict';

var pdfjsMessages = function(messageKey) {

	return $("#pdfjs_messages").data('messages')[messageKey];
}

var pdfjsOptions = {
	"locale" : pdfjsMessages(MessageKeys.PDFJS_LANG),
	"defaultUrl" : pdfjsMessages("defaultUrl"),
	"imageResourcesPath" : pdfjsMessages("imageResourcesPath"),
	"cMapUrl" : pdfjsMessages("cMapUrl"),
	"workerSrc" : pdfjsMessages("workerSrc")
}
