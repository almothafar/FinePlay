'use strict';

insertStyle('.bootbox.modal .btn-default', {
	'border-color': getTheme().primary.backgroundColor,
	'color': getTheme().primary.backgroundColor
});

var bootboxMessages = function(messageKey){

	return $("#bootbox_messages").data('messages')[messageKey];
}

bootbox.setDefaults({locale: bootboxMessages(MessageKeys.BOOTBOX_LANG)});
