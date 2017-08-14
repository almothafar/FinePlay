'use strict';

var Forms = function() {

	var getKey = function() {

		return location.pathname + location.search;
	};

	var isEnabled = function(enabledValue) {

		return enabledValue == undefined ? true : enabledValue;
	};

	var getVersion = function(versionValue) {

		return versionValue == undefined ? 0 : versionValue;
	};

	return {
		store: function(formSelector){

			var storedJson = window.localStorage.getItem(getKey());
			var formDatas = storedJson ? JSON.parse(storedJson) : {};

			var forms = $(formSelector);
			forms.each(function(){

				var formData = {};
				var form = $(this);
				if(form.attr('id')){

					var id = form.attr('id');
					var currentVersion = getVersion(form.data('store-version'));
					formData.version = currentVersion;
					formData.data = form.serializeArray();

					formDatas[id] = formData;
				}
			});

			if(1 <= Object.keys(formDatas).length){

				var storeJson = JSON.stringify(formDatas);
				window.localStorage.setItem(getKey(), storeJson);
			}
		},
		restore: function(formSelector){

			var storedJson = window.localStorage.getItem(getKey());
			if(storedJson){

				var formDatas = JSON.parse(storedJson);

				var forms = $(formSelector);
				forms.each(function(){

					var form = $(this);
					if(form.attr('id')){

						var id = form.attr('id');

						var formIsEnabled = isEnabled(form.data('store-enabled'));
						if(formIsEnabled){

							if(formDatas[id]){

								var formData = formDatas[id];
								var version = formData.version;
								var data = formData.data;

								var currentVersion = getVersion(form.data('store-version'));
								if(version == currentVersion){

									form.find(':checkbox').prop('checked', false);

									$.each(data, function(i, param){

										var name = param.name;
										var value = param.value;

										var elem = form.find('[name='+name+']').eq(0);

										var elemIsEnabled = isEnabled(elem.data('store-enabled'));
										if(elemIsEnabled){

											var placeholder = elem.prop('placeholder');
											elem.prop('placeholder', '');
											switch(elem.get(0).tagName){

												case "INPUT":

													switch(elem.prop('type')){

														case "checkbox":

															elem.prop('checked', true);
															break;
														case "radio":

															elem = form.find('[name='+name+'][value='+value+']').eq(0);
															elem.prop('checked', true);
															break;
														default:

															elem.val(value);
															break;
													}
													break;
												case "TEXTAREA":

													elem.val(value);
													break;
												case "SELECT":

													var selectedElem = elem.find('[value='+value+']').eq(0);
													selectedElem.prop('selected', true);
													break;
												default:
											}
											elem.prop('placeholder', placeholder);
										}
									});
								}
							}
						}
					}
				});
			}
		}
	}
}();
