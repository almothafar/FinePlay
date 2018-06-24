'use strict';

var Components = function() {

	var readyIcons = function(selector, func){

		var icons = $(selector).find('.i').filter(function (i) {

			var isIconEnabled =
				$(this).hasClass('fas') ||
				$(this).hasClass('far') ||
				$(this).hasClass('fal') ||
				$(this).hasClass('fab') ||
				$(this).hasClass('material-icons') ||
				$(this).hasClass('ion') ||
				$(this).hasClass('icofont');
			return isIconEnabled;
		});

		var isIconsLoaded = function(){

			var loadedIcons = icons.filter(function (i) {

				var isIconLoaded = 1 <= $(this).width();
				return isIconLoaded;
			});

			var isIconsLoaded = icons.length == loadedIcons.length;
			return isIconsLoaded;
		}

		var timerId = setInterval(function() {

			if (isIconsLoaded()) {

				clearInterval(timerId);

				if(func){

					func();
				}

				return;
			}
		}, 100);
	};

	return {

		applyRangeToCheckboxs: function(checkboxsSelector, min, max, func) {

			var target = $(checkboxsSelector);
			target.on('click', function(e) {

				var clickedCheckbox = $(this);
				var checkedCount = target.filter(':checked').length;
				if (clickedCheckbox.prop('checked')) {

					var isMoreThanChecked = (max && max < checkedCount);
					if(isMoreThanChecked){

						clickedCheckbox.prop('checked', false);
						return false;
					}else{

						if(func){

							func(e);
						}
					}
				} else {

					var isLessThanMinChecked = (min && checkedCount < min);
					if(isLessThanMinChecked){

						clickedCheckbox.prop('checked', true);
						return false;
					}else{

						if(func){

							func(e);
						}
					}
				}
			});
		},

		applyScrollToNav: function(navSelector) {

			readyIcons(navSelector, function() {

				var navs = $(navSelector);
				navs.each(function(){

					var nav = $(this);
					nav.css({ flexWrap:"nowrap", msFlexWrap:"nowrap"});
					nav.width(10000);

					nav.wrap('<div class="w-100" style="overflow-x:scroll; -webkit-overflow-scrolling:touch;"></div>');

					var navItems = nav.find('.nav-item');
					var width = navItems.map(function(){return $(this).outerWidth(true)}).get().reduce(function(a,b){return a+b;},0);
					nav.width(width);

					var navWrapper = nav.parent();
					var updateWrapperState = function(){

						if(nav.outerWidth(true) <= navWrapper.width()){

							navWrapper.css({"overflow-x": "initial"});
						}else{

							navWrapper.css({"overflow-x": "scroll"});
						}
					};

					updateWrapperState();

					var timer = false;
					$(window).resize(function() {

						if (timer !== false) {

							clearTimeout(timer);
						}
						timer = setTimeout(function() {

							updateWrapperState();
						}, 100);
					});
				});
			});
		},

		applyFoldToNav: function(navSelector) {

			readyIcons(navSelector, function() {

				var dropDownWidth = 80;

				var navs = $(navSelector);
				navs.each(function(){

					var nav = $(this);
					nav.width(10000);

					var navItems = nav.children('.nav-item:not(.dropdown)');
					var width = navItems.map(function(){return $(this).outerWidth(true)}).get().reduce(function(a,b){return a+b;},0);

					nav.append('<li class="nav-item dropdown float-xs-right">'+
							'<a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false" style="width: ' + dropDownWidth + 'px; display:hidden;">...</a>'+
							'<div class="dropdown-menu" style="left:auto; right:0px;"></div>'+
							'</li>');

					var dropDown = nav.find('.dropdown');
					var dropDownMenu = dropDown.find('.dropdown-menu');
					navItems.each(function(){

						var navItem = $(this);
						navItem.on('click', function(e){

							dropDown.find('>a').removeClass('active');
							dropDownMenu.children('.dropdown-item').removeClass('active');
						});
						var foldTab = navItem.find('>a').clone().removeClass('nav-link').addClass('dropdown-item');
						foldTab.find('>span').addClass('float-xs-right');
						foldTab.on('click', function(e){

							var clickedFoldTab = $(this);
							if(!clickedFoldTab.hasClass('active')){

								dropDownMenu.children('.dropdown-item').removeClass('active');
								navItem.addClass('active');

								navItems.find('>a').removeClass('active');
								navItem.find('>a').addClass('active');
							}
						});
						foldTab.appendTo(dropDownMenu);
					});
					var foldTabs = dropDownMenu.children('.dropdown-item');

					var visibleWidths = [];
					var totalWidth = 0;
					for(var i=0;i < navItems.length;i++){

						totalWidth = totalWidth + navItems.eq(i).outerWidth(true);
						var visibleWidth = totalWidth + dropDownWidth;
						visibleWidths[i] = visibleWidth;
					}

					nav.width('100%');

					var updateTabState = function(){

						var navWidth = nav.width();
						$.each(visibleWidths, function(i, visibleWidth){

							var isShow = visibleWidth <= navWidth;

							var navItem = navItems.eq(i);
							isShow ? navItem.show():navItem.hide();

							var foldTab = foldTabs.eq(i);
							isShow ? foldTab.hide():foldTab.show();

							var navItemLink = navItem.find('>a');
							navItemLink.hasClass('active') ? foldTab.addClass('active'):foldTab.removeClass('active')
						});

						var isFold = 1 <= navItems.filter(':hidden').length;
						isFold ? dropDown.show():dropDown.hide();
						var isFoldActive = 1 <= navItems.find('a').filter(':hidden.active').length;
						var dropDownLink = dropDown.find('>a');
						isFoldActive ? dropDownLink.addClass('active'):dropDownLink.removeClass('active');
					};

					updateTabState();

					var timer = false;
					$(window).resize(function() {

						if (timer !== false) {

							clearTimeout(timer);
						}
						timer = setTimeout(function() {

							updateTabState();
						}, 100);
					});
				});
			});
		},

		applyCompressToNav: function(navSelector) {

			readyIcons(navSelector, function() {

				var navs = $(navSelector);
				navs.each(function(){

					var nav = $(this);
					nav.width(10000);

					var navItems = nav.children('.nav-item');
					var navItemsLength = navItems.length;
					var width = navItems.map(function(){return $(this).outerWidth(true)}).get().reduce(function(a,b){return a+b;},0);
					var initialWidths = [];
					navItems.each(function(){

						var navItem = $(this);
						initialWidths.push(navItem.outerWidth(true));
						navItem.find('>a').css({overflow:"hidden", whiteSpace: "nowrap", textOverflow: "ellipsis"});
					});

					nav.width('100%');

					var updateTabState = function(){

						if(width <= nav.width()){

							$.each(initialWidths, function(i, initialWidth){

								var navItem = navItems.eq(i);
								navItem.css('width','initial');
								var badge = navItem.find('>a>span');
								badge.css('visibility','initial');
							});
						}else{

							var maxTabWidth = nav.width() / navItemsLength;

							$.each(initialWidths, function(i, initialWidth){

								var navItem = navItems.eq(i);
								navItem.css('width','initial');
								var badge = navItem.find('>a>span');
								badge.css('visibility','initial');
								if(initialWidth <= maxTabWidth){

								}else{

									navItem.width(maxTabWidth);
									badge.css('visibility','hidden');
								}
							});
						}
					};

					updateTabState();

					var timer = false;
					$(window).resize(function() {

						if (timer !== false) {

							clearTimeout(timer);
						}
						timer = setTimeout(function() {

							updateTabState();
						}, 100);
					});
				});
			});
		},

		applyDragToNav: function(navSelector) {

			var navs = $(navSelector);
			navs.each(function(){

				var nav = $(this);

				nav.find('>.nav-item>a').css('white-space','nowrap');
				nav.sortable({
				});
			});
		}
	}
}();
