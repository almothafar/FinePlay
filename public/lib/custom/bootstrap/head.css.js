'use strict';

var bootstrapMessages = function(messageKey){

	return JSON.parse(document.getElementById('bootstrap_messages').getAttribute('data-messages'))[messageKey];
}

injectStyle('label.required:after', {
	'content': '"' + bootstrapMessages(MessageKeys.REQUIRED) + '"'
});

injectStyle('.custom-file-label:empty::after', {
	'content': '"' + bootstrapMessages(MessageKeys.SELECT__FILE) + '" !important'
});
injectStyle('.custom-file-label::before', {
	'content': '"' + bootstrapMessages(MessageKeys.BROWSE) + '" !important'
});
