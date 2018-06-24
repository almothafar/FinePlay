'use strict';

var plotlyjsMessages = function(messageKey){

	return $("#plotlyjs_messages").data('messages')[messageKey];
}

var lang = plotlyjsMessages(MessageKeys.PLOTLYJS_LANG);
if(lang){

	Plotly.setPlotConfig({locale: plotlyjsMessages(MessageKeys.PLOTLYJS_LANG)})
}
