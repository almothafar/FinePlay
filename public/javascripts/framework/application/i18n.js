'use strict';

$("tbody").append("<tr></tr>");

if(window.SpeechSynthesisUtterance){

	$("tbody > tr:last-child").append('<td>Speech</td><td>' +
		'<button id="speakButton" type="button" class="btn btn-outline-primary"><i class="fa fa-volume-up" ></i>&nbsp;Speech<span class="badge badge-pill badge-secondary"></span></button>'+
	'</td>');
}else{

	$("tbody > tr:last-child").append('<td>Speech</td><td>' +
		'<span class="badge badge-danger">SpeechSynthesis is Unsupported.</span>' +
	'</td>');
}
$('#speakButton').click(function() {

	var synthes = new SpeechSynthesisUtterance(Messages(MessageKeys.WELCOME));
	synthes.lang = Messages(MessageKeys.SPEECH_SYNTHESIS_LANG);
	speechSynthesis.speak(synthes);
});
