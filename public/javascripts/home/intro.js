'use strict';

//

$(document).ready(function () {

	if(document.location.origin + Routes.controllers.user.User.index().url == document.referrer){

		fadeInFromFront('body');
	}else{

	}

	var tour = new Shepherd.Tour({
		defaultStepOptions: {
			classes: 'shepherd-theme-default',
			showCancelLink: true,
			scrollTo: 'system_content'
		}
	});
	tour.addStep('welcome', {
		title: messages(MessageKeys.TIMEZONE) + ' ' + messages(MessageKeys.INFO),
		text: ['<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>', '<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>'],
		attachTo: '#timezoneInfo bottom',
		buttons: [
			{
				text: messages(MessageKeys.CANCEL),
				classes: 'shepherd-button-secondary',
				action: tour.cancel
			}, {
				text: messages(MessageKeys.NEXT),
				action: tour.next,
				classes: 'shepherd-button-example-primary'
			}
		]
	});
	tour.addStep('including', {
		title: messages(MessageKeys.LANGUAGE) + ' ' + messages(MessageKeys.INFO),
		text: '<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>',
		attachTo: '#languageInfo bottom',
		buttons: [
			{
				text: messages(MessageKeys.PREVIOUS),
				classes: 'shepherd-button-secondary',
				action: tour.back
			}, {
				text: messages(MessageKeys.NEXT),
				action: tour.next
			}
		]
	});
	tour.addStep('followup', {
		title: messages(MessageKeys.MENU) + ' ' + messages(MessageKeys.BUTTON),
		text: '<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>',
		attachTo: '#system_work-menu-button bottom',
		buttons: [
			{
				text: messages(MessageKeys.PREVIOUS),
				classes: 'shepherd-button-secondary',
				action: tour.back
			}, {
				text: messages(MessageKeys.NEXT),
				action: tour.next
			}
		]
	});
	tour.addStep('followup', {
		title: messages(MessageKeys.HELP) + ' ' + messages(MessageKeys.BUTTON),
		text: '<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>',
		attachTo: '#helpButton bottom',
		buttons: [
			{
				text: messages(MessageKeys.PREVIOUS),
				classes: 'shepherd-button-secondary',
				action: tour.back
			}, {
				text: messages(MessageKeys.END),
				action: tour.next
			}
		]
	});

	$('#tourButton').on('click', function(){

		tour.start();
	});
});

enablePrint();
enableHelp();
