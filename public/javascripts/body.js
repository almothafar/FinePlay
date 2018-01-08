'use strict';

var bodyMessages = function(messageKey){

	return $("#body_messages").data('messages')[messageKey];
}

//

$(document).ready(function () {

	console.log('DOM loaded.');
});

$(window).on('load', function () {

	console.log('completely loaded.');
});

if(("Dev" == getMode()) && window.Popper){

	$("#debugButton").removeClass("d-none");
	$("#debugButton").popover({

		html: true
	});
}

//

var initReportErrorResult = function(){

	$('#reportErrorContent').empty();

	$('#reportErrorMessage').text('');
	$('#reportErrorDescription').text('-');
	$('#reportErrorProgress>.progress-bar').removeClass('bg-danger').css('width', '0%');
	$('#reportErrorCancel').prop('disabled', false);
};

$('#reportErrorButton').on('click', function (e) {

	$('#reportErrorCancel').prop('disabled', true);
	$('#reportErrorMessage').text(bodyMessages(MessageKeys.PLEASE__WAIT));
	$('#reportErrorProgress>.progress-bar').addClass('bg-primary').css('width', '100%')

	var timeout = "10000";
	$.ajax({
		method: "POST",
		url: Routes.apis.system.Logger.log().url + "?" + getToken(),
		data: $('#reportErrorContent').val(),
		contentType: 'application/json',
		dataType: "json",
		timeout: timeout
	})
	.then(
		function (responseJson) {

			$('#reportErrorDialog').modal('hide');
			alert(responseJson['message']);
		},
		function (jqXHR, textStatus, errorThrown) {

			$('#reportErrorProgress>.progress-bar').removeClass('bg-primary').addClass('bg-danger');
			$('#reportErrorMessage').text(bodyMessages(MessageKeys.FAILURE));
			$('#reportErrorCancel').prop('disabled', false);
			$('#reportErrorDescription').html(bodyMessages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + bodyMessages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
		}
	);
})

$('#searchStackOverflowButton').on('click', function(){ window.open('https://stackoverflow.com/search?q=[js]' + $('#searchErrorMessage').val()); });
$('#searchStackOverflowJaButton').on('click', function(){ window.open('https://ja.stackoverflow.com/search?q=[javascript]' + $('#searchErrorMessage').val()); });
$('#searchQiitaButton').on('click', function(){ window.open('https://qiita.com/search?q=' + $('#searchErrorMessage').val()); });
$('#searchTeratailButton').on('click', function(){ window.open('https://teratail.com/questions/search?q=' + $('#searchErrorMessage').val()); });

var reportError = function(reportErrorContent){

	initReportErrorResult();
	$('#reportErrorContent').val(JSON.stringify(reportErrorContent, null, "	"));
	$('#searchErrorMessage').val(reportErrorContent.message);
	$('#reportErrorDialog').modal('show');
}

window.onerror = function (msg, url, lineNo, columnNo, error) {

	var userId = bodyMessages("userId");
	var string = msg.toLowerCase();
	var substring = "script error";
	var reportErrorContent;
	if (string.indexOf(substring) > -1){

		reportErrorContent = {
			userid: userId,
			message: msg
		}
	} else {

		reportErrorContent = {
			userid: userId,
			message: msg,
			sourceURL: url,
			line: lineNo,
			column: columnNo,
			error: error
		}
	}
	reportError(reportErrorContent);

	return false;
};

//

$("#menuButton").on('click', function(event) {

	$("#system_base").toggleClass('system_expand');
	return false;
});

getContent().on('click', function(event) {

	$("#system_base").removeClass('system_expand');
	$("#system_base").removeClass('system_help-expand');
});

$("#printButton").on('click', function(event) {

	$(window).trigger('beforeprint');
	window.print();
	$(window).trigger('afterprint');
});

$("#scrollTopButton").on('click', function(event) {

	getContent().animate({ scrollTop: 0 }, 'fast');
});

$('#signOutLink').on('click', function(event){

	event.preventDefault();

	var signOutLinkUrl = $(this).prop('href');

	fadeOutToFront('body', function(event){

		window.location.href = signOutLinkUrl;
	});
});

//

var refreshHelp = function(){

	var mainHtml = $('#system_help-main')[0].outerHTML;
	$('#system_help-main').remove();
	$('#system_help-content').append(mainHtml);

	var activeNavItems = $('#system_content .nav>.nav-item>.nav-link.active');
	var activeDropdownItems = $('#system_content .dropdown>.dropdown-menu>.dropdown-item.active');
	$('#system_help-main').scrollspy('refresh');
	activeNavItems.addClass('active');
	activeDropdownItems.addClass('active');
	$('#system_help-main').find('#help-wrapper>#help-top>.anchorjs-link').hide();

	$('#system_help-main .btn-clipboard').tooltip();
}

$("#helpButton").on('click', function(event) {

	var helpNav = $('#system_help-header>#system_help-navbar');
	var helpMain = $('#system_help-content>#system_help-main');

	$("#system_base").toggleClass('system_help-expand');

	if($('#system_help-content').hasClass('help-notready')){

		var helpLink = '/' + bodyMessages("currentSection") +  '/' + bodyMessages("currentWork") +  '/help?'+ getToken();
		var timeout = "10000";
		$.ajax({
			method:"GET",
			url: helpLink,
			contentType: 'text/plain',
			dataType: "html",
			timeout: timeout
		})
		.then(
			function (responseHtml) {

				helpMain.empty();

				helpMain.append('<div id="help-wrapper" class="container-fluid bd-content"></div>');
				var helpWrapper = helpMain.find('#help-wrapper');

				helpWrapper.append(responseHtml);

				var titles = helpWrapper.find('h1.bd-title');
				titles.each(function(){

					var idAnchor = $(this).attr('id');
					var title = $(this).text();
					helpNav.find('ul').append('<li class="nav-item"><a href="#' + idAnchor + '" class="nav-link">' + title + '</a></li>');
				});

				helpWrapper.prepend('<h1 class="bd-title w-100" id="help-top"></h1>');

				// add tooltip
				$('#system_help-main .highlight').each(function () {

					var btnHtml = '<div class="bd-clipboard"><button class="btn-clipboard" title="Copy to clipboard" data-toggle="tooltip">Copy</button></div>';
					$(this).before(btnHtml);
				});

				refreshHelp();

				// enable clipboard
				var clipboard = new Clipboard('#system_help-main .btn-clipboard', {
					target: function (trigger) {
						return trigger.parentNode.nextElementSibling
					}
				});
				clipboard.on('success', function (e) {
					$(e.trigger)
						.attr('title', 'Copied!')
						.tooltip('_fixTitle')
						.tooltip('show')
						.attr('title', 'Copy to clipboard')
						.tooltip('_fixTitle');
					e.clearSelection();
				});

				// enable dummy image
				var images = $('#system_help-main img[data-src^="holder.js"]').get();
				Holder.run({
					images: images
				});

				Components.applyScrollToNav('#system_help-navbar>.nav');

				$('#system_help-content').removeClass('help-notready');
			},
			function (jqXHR, textStatus, errorThrown) {

				var errorMessage;
				switch (jqXHR.status){
					case 404:
						// Server down or Bug

						errorMessage = '' +
							'<div class="alert alert-warning m-3" role="alert">' +
								'<i class="fas fa-exclamation-triangle"></i>' +
								' ' + bodyMessages("getHelpError") +
							'</div>';
						break;
					default:

						errorMessage = errorThrown;
						break;
				}

				helpMain.html(errorMessage);

				refreshHelp();
			}
		);
	}
	return false;
});

var highlightHelp = function(searchText){

	var regExp = new RegExp('('+searchText+')', 'gi');
	$('.help-text').each(function(){

		var highlighted = $(this).html().replace(regExp, '<span class="matched">$1</span>');
		$(this).html(highlighted);
	});
}
var highlightedHelpTops = function(targetRootSelector, highlightedSelector){

	var highlightedTops = [];
	$(highlightedSelector).each(function(i){

		var parents = $(this).parentsUntil(targetRootSelector);
		var highlightedTop = parents[parents.length - 1];
		highlightedTops.push(highlightedTop);
	});
	return $.unique(highlightedTops);
}
var filterHighlightHelp = function(){

	var tops = highlightedHelpTops('#help-wrapper', '#help-wrapper .matched');

	$('#help-wrapper').children().hide();
	$(tops).show();
	$('#help-wrapper>h1,h2,h3,h4,h5,h6').show();
}

var storedHelpWrapper;
var keyupEvents = [];
$("#help-search-text").on('keyup', function () {

	keyupEvents.push('keyup');

	var searchText = $(this).val();
	setTimeout(function () {

		keyupEvents.pop();

		var helpNav = $('#system_help-header>#system_help-navbar');
		var helpMain = $('#system_help-content>#system_help-main');

		if (0 == keyupEvents.length) {

			var helpWrapper = helpMain.find('#help-wrapper');
			if(0 == searchText.length){

				if(storedHelpWrapper){

					helpWrapper.remove();
					helpMain.append(storedHelpWrapper);
					storedHelpWrapper = null;
				}
			}else{

				var sourceHtml;
				if(storedHelpWrapper){

					sourceHtml = storedHelpWrapper[0].outerHTML;
					helpWrapper.remove();
				}else{

					sourceHtml = helpWrapper[0].outerHTML;
					storedHelpWrapper = helpWrapper.detach();
				}

				helpMain.append(sourceHtml);
				highlightHelp(searchText);

				filterHighlightHelp();
			}

			refreshHelp();
		}
	}, 1000);
});

$("#help-search-clear").on('click', function () {

	$('#help-search-text').val('');
	$('#help-search-text').trigger("keyup");
});

$("#help-search-text").on('focus', function () {

	if(!$("#system_base").hasClass("system_help-expand")){

		$('#system_menu-left-corner').focus();
	}
});

//

$("#system_magnify-text").on('click', function(event) {

	hideMagnifyText();
});

//

$('#debugButton').on('shown.bs.popover', function () {

	$("#applyBorderButton").on("click", function(){$("*").css("border", "solid 1px lavender");});
	$("#showResponsiveButton").on("click", function(){$("#system_responsive-bar").removeClass("d-none");});
	$("#Memory").text($("#debugButton").data("memory"));
	$("#Play_ver").text($("#debugButton").data("playver"));
	$("#jQuery_ver").text($.fn.jquery);
});

//

if("Dev" == getMode()){

	$('.draft').each(function(){

		var replacedHtml = $(this.outerHTML.replace(/>(.*?)</gi, '><span>$1</span><')).html();
		$(this).html(replacedHtml);
	});
}

//

document.onkeydown = function(e){

	if(e.altKey && e.keyCode === 72){

		$('.secret').removeClass('secret');
		return false;
	}
};
