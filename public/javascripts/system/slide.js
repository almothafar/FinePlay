'use strict';

$(document).ready(function() {

	var slidejsMessages = function(messageKey) {

		return $("#slidejs_messages").data('messages')[messageKey];
	}

	var publicPath = slidejsMessages('publicPath');
	var url = slidejsMessages('defaultUrl');
	$.ajax({
		method:"GET",
		url:url,
		dataType: "html",
		timeout: 3 * 1000
	})
	.then(
		function (responseHtml) {

			var reveal = $(responseHtml).filter('.reveal');

			if(1 == reveal.length){

				$('body').prepend(reveal);

				Reveal.initialize({
					controls: true,
					progress: true,
					history: true,
					center: true,
					rtl: slidejsMessages('rtl'),

					transition: 'convex',

					dependencies: [
						{ src: publicPath + 'lib/reveal.js/js/classList.js', condition: function() { return !document.body.classList; } },
						{ src: publicPath + 'lib/reveal.js/markdown/marked.js', condition: function() { return !!document.querySelector( '[data-markdown]' ); } },
						{ src: publicPath + 'lib/reveal.js/markdown/markdown.js', condition: function() { return !!document.querySelector( '[data-markdown]' ); } },
						{ src: publicPath + 'lib/reveal.js/highlight/highlight.js', async: true, callback: function() { hljs.initHighlighting(); } },
						{ src: publicPath + 'lib/reveal.js/search/search.js', async: true },
						{ src: publicPath + 'lib/reveal.js/zoom-js/zoom.js', async: true },
						{ src: publicPath + 'lib/reveal.js/notes/notes.js', async: true }
					]
				});
			}else{

				$('body').prepend('<div class="reveal"><div class="slides"><div><h1>Illeagl Slide.</h1><p>' + responseHtml + '</p></div></div></div>');
			}
		},
		function (jqXHR, textStatus, errorThrown) {

			$('body').prepend('<div class="reveal"><div class="slides"><div><h1>Error.</h1><p>' + 'Status'+'&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;'+'Error'+'&nbsp;<strong>'+errorThrown+'</strong>' + '</p></div></div></div>');
		}
	);

	(function(){

		var gamepad;

		var BUTTON_A     = 0;
		var BUTTON_B     = 1;
		var BUTTON_X     = 2;
		var BUTTON_Y     = 3;
		var BUTTON_LB    = 4;
		var BUTTON_RB    = 5;
		var BUTTON_LT    = 6;
		var BUTTON_RT    = 7;
		var BUTTON_BACK  = 8;
		var BUTTON_START = 9;
		var BUTTON_L3    = 10;
		var BUTTON_R3    = 11;
		var BUTTON_UP    = 12;
		var BUTTON_DOWN  = 13;
		var BUTTON_LEFT  = 14;
		var BUTTON_RIGHT = 15;
		var BUTTON_HOME  = 16;

		var KEY_LEFT  = 37;
		var KEY_UP    = 38;
		var KEY_RIGHT = 39;
		var KEY_DOWN  = 40;

		var keyCodes = {};
		keyCodes[BUTTON_X] = KEY_LEFT;
		keyCodes[BUTTON_Y] = KEY_UP;
		keyCodes[BUTTON_B] = KEY_RIGHT;
		keyCodes[BUTTON_A] = KEY_DOWN;

		var keyFuncs = {};
		keyFuncs[KEY_LEFT]  = function(){Reveal.left()};
		keyFuncs[KEY_UP]    = function(){Reveal.up()};
		keyFuncs[KEY_RIGHT] = function(){Reveal.right()};
		keyFuncs[KEY_DOWN]  = function(){Reveal.down()};

		var af = window.requestAnimationFrame || window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame;

		var pressedEvents = [];
		function updateStatus() {

			scangamepads();

			if(gamepad){

				var gamepadId = gamepad.id;
				$("#gamepad").removeClass("badge-danger").addClass("badge-success").text(gamepadId);

				$.each(keyCodes, function(padCode, keyCode) {

					var pressed = isPressed(gamepad.buttons[padCode]);
					if (pressed) {

						pressedEvents.push('pressed');
						setTimeout(function () {

							pressedEvents.pop();
							if (0 == pressedEvents.length) {

								keyFuncs[keyCode]();
							}
						}, 30);
					}
				});
			}else{

				$("#gamepad").removeClass("badge-success").addClass("badge-danger").text('');
			}

			af(updateStatus);
		}

		function scangamepads() {

			var gamepads = navigator.getGamepads ? navigator.getGamepads() : (navigator.webkitGetGamepads ? navigator.webkitGetGamepads() : []);
			gamepad = gamepads[0];
		}

		function isPressed(button) {

			var pressed = false;
			if (typeof(button) == "object") {

				pressed = button.pressed;
			}

			return pressed;
		}

		if ('GamepadEvent' in window) {

			window.addEventListener("gamepadconnected", function(e) {

				af(updateStatus);
			});
		} else {

			setInterval(scangamepads, 500);
		}
	})();
});
