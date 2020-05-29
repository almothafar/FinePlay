'use strict';

var bootstrapMessages = function(messageKey){

	return JSON.parse(document.getElementById('bootstrap_messages').getAttribute('data-messages'))[messageKey];
}

injectStyle('label.required:after', {
	'content': '"' + bootstrapMessages(MessageKeys.REQUIRED) + '"'
});

injectStyle('.custom-file-label:after', {
	'content': '"' + bootstrapMessages(MessageKeys.BROWSE) + '" !important'
});
