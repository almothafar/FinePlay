'use strict';

var bootboxMessages = function(messageKey){

	return $("#bootbox_messages").data('messages')[messageKey];
}

bootbox.setDefaults({locale: bootboxMessages(MessageKeys.BOOTBOX_LANG)});
