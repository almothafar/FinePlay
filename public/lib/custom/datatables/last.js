'use strict';

var dataTablesMessages = function(messageKey){

	return $("#datatables_messages").data('messages')[messageKey];
}

$.fn.DataTable.defaults.oLanguage.sUrl = dataTablesMessages("langUrl");

$('table').on('init.dt', function () {

	$('.dataTables_length>label>select').removeClass('form-control').addClass('custom-select');
	$('.dataTables_filter>label').each(function(){

		$(this)[0].firstChild.textContent= dataTablesMessages(MessageKeys.FILTER) +":";
	});
});
