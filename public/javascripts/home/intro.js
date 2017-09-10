'use strict';

//

$(document).ready(function () {

	if(document.location.origin+Routes.controllers.user.User.index() == document.referrer){

		fadeInFromFront('body');
	}else{

	}

	var tour = new Shepherd.Tour({
		defaults: {
			classes: 'shepherd-theme-default',
			showCancelLink: true,
			scrollTo: 'system_content'
		}
	});
	tour.addStep('welcome', {
		title: Messages(MessageKeys.TIMEZONE) + ' ' + Messages(MessageKeys.INFO),
		text: ['<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>', '<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>'],
		attachTo: '#timezoneInfo bottom',
		buttons: [
			{
				text: Messages(MessageKeys.CANCEL),
				classes: 'shepherd-button-secondary',
				action: tour.cancel
			}, {
				text: Messages(MessageKeys.NEXT),
				action: tour.next,
				classes: 'shepherd-button-example-primary'
			}
		]
	});
	tour.addStep('including', {
		title: Messages(MessageKeys.LANGUAGE) + ' ' + Messages(MessageKeys.INFO),
		text: '<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>',
		attachTo: '#languageInfo bottom',
		buttons: [
			{
				text: Messages(MessageKeys.PREVIOUS),
				classes: 'shepherd-button-secondary',
				action: tour.back
			}, {
				text: Messages(MessageKeys.NEXT),
				action: tour.next
			}
		]
	});
	tour.addStep('followup', {
		title: Messages(MessageKeys.MENU) + ' ' + Messages(MessageKeys.BUTTON),
		text: '<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>',
		attachTo: '#menuButton bottom',
		buttons: [
			{
				text: Messages(MessageKeys.PREVIOUS),
				classes: 'shepherd-button-secondary',
				action: tour.back
			}, {
				text: Messages(MessageKeys.NEXT),
				action: tour.next
			}
		]
	});
	tour.addStep('followup', {
		title: Messages(MessageKeys.HELP) + ' ' + Messages(MessageKeys.BUTTON),
		text: '<span class="draft"><span class="draft-text">Star Shepherd on Github so you remember it for your next project</span></span>',
		attachTo: '#helpButton bottom',
		buttons: [
			{
				text: Messages(MessageKeys.PREVIOUS),
				classes: 'shepherd-button-secondary',
				action: tour.back
			}, {
				text: Messages(MessageKeys.END),
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
