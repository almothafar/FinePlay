'use strict';

var CKEDITORMessages = function(messageKey){

	return $("#ckeditor_messages").data('messages')[messageKey];
}

CKEDITOR.config.language = CKEDITORMessages(MessageKeys.CKEDITOR_LANG);
CKEDITOR.config.uiColor = $.Color(getTheme().primary.disabledBackgroundColor).toHexString();

