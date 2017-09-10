'use strict';

var CKEDITORMessages = function(messageKey){

	return $("#ckeditor_messages").data('messages')[messageKey];
}

CKEDITOR.config.language = CKEDITORMessages(MessageKeys.CKEDITOR_LANG);
var color = $.Color(getTheme().primary.backgroundColor);
CKEDITOR.config.uiColor = $.Color({hue: color.hue(), saturation: color.saturation(), lightness: 0.9, alpha: color.alpha()}).toHexString();
