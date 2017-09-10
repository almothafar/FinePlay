'use strict';

$('#callReportErrorButton').on('click', function(){

	reportError({key: 'value'});
});

$('#throwErrorButton').on('click', function(){

	throw new Error("Client error.");
});
