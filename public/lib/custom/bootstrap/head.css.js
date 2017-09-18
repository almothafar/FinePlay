'use strict';

var bootstrapMessages = function(messageKey){

	return JSON.parse(document.getElementById('bootstrap_messages').getAttribute('data-messages'))[messageKey];
//	return $("#bootstrap_messages").data('messages')[messageKey];
}

injectStyle('label.required:after', {
	'content': '"' + bootstrapMessages(MessageKeys.REQUIRED) + '"'
});

injectStyle('.custom-file-control:empty::after', {
	'content': '"' + bootstrapMessages(MessageKeys.SELECT__FILE) + '" !important'
});
injectStyle('.custom-file-control::before', {
	'content': '"' + bootstrapMessages(MessageKeys.BROWSE) + '" !important'
});
