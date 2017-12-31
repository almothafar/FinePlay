'use strict';

$(document).ready(function() {

	$("#create"+"companyId").css({'width': '100%'})
	$("#update"+"companyId").css({'width': '100%'})
});

//

$(document).ready(function() {

	var dataTable=$('#userlist').DataTable({
		"language": {
			select: {
				rows: {
					_: " " + Messages(MessageKeys.X__CASE__SELECTED).replace("{0}","%d")
//					_: "You have selected %d rows",
//					0: "Click a row to select it",
//					1: "Only 1 row selected"
				}
			}
		},
		select: {
			style: 'single',
		},
		"lengthMenu": [ [10, 50, 100, 500, -1], [10, 50, 100, 500, 1000] ],
		"initComplete": function () {

			dataTable
				.on( 'select', function ( e, dt, type, indexes ) {

					var rowData = dataTable.rows( indexes ).data().toArray();
//					events.prepend( '<div><b>'+type+' selection</b> - '+JSON.stringify( rowData )+'</div>' );
				} )
				.on( 'deselect', function ( e, dt, type, indexes ) {

					var rowData = dataTable.rows( indexes ).data().toArray();
//					events.prepend( '<div><b>'+type+' <i>de</i>selection</b> - '+JSON.stringify( rowData )+'</div>' );
				} );

			$('#userlist'+'_wrapper').on( 'click', function () {

					updateSelection();
			});

		}
	});

	var updateSelection = function (){

		$('#deleteButton').attr("disabled", "disabled");
		$('#updateButton').attr("disabled", "disabled");
		$('#createButton').attr("disabled", "disabled");

		var rows = $('#userlist>tbody>tr');
		switch(rows.length){
			case 0:

				$('#createButton').prop('disabled', false);

				break;
			default:

				var selectedRows = $('#userlist>tbody>tr.selected');
				switch(selectedRows.length){
					case 0:

						$('#createButton').prop('disabled', false);
						break;
					case 1:

						$('#createButton').prop('disabled', false);
						$('#updateButton').prop('disabled', false);
						$('#deleteButton').prop('disabled', false);
						break;
					default:

						break;
				};

				break;
		};
	};

	updateSelection();

	// ////////// Read //////////

	var jsdate =  moment(Messages("clientDateTime")).toDate();

	$('#expireFrom').pickadate({
		format: 'yyyy-mm-dd',
		formatSubmit: 'yyyy-mm-dd',
		hiddenName: true
	});
	var datepicker = $('#expireFrom').pickadate( 'picker' );
	datepicker.set('highlight', jsdate);

	$('#expireTo').pickadate({
		format: 'yyyy-mm-dd',
		formatSubmit: 'yyyy-mm-dd',
		hiddenName: true
	});
	var datepicker = $('#expireTo').pickadate( 'picker' );
	datepicker.set('highlight', jsdate);

	$('.input-group .input-group-text').on('click', function(e){

		$(e.currentTarget).parent().parent().find('.picker__input').trigger("click");
		e.stopPropagation();
	});

	Components.applyRangeToCheckboxs('.read'+'roles', 1);

	// ////////// Download //////////

	$('#downloadButton').on("click", function(e){

		var downloadQuery = $('#'+'read'+'Form').serialize();
		var currentLink = $('#downloadButton').attr('href');
		var currentLinkArray = currentLink.split(/\?.*(?=csrfToken)/);
		var downloadLink = currentLinkArray[0]+'?'+downloadQuery+'&'+currentLinkArray[1];
		$('#downloadButton').attr('href', downloadLink);
	});

	// ////////// Edit //////////

	var select2Options = {

		ajax: {
			url: function (params) {

				return Routes.apis.company.Company.companies('').url.replace(/\?.*/,'') + "?" + getToken();
			},
			dataType: 'json',
			delay: 250,
			data: function (params) {
				return {
					name: params.term,
					page: params.page,
					size: 100
				};
			},
			processResults: function (data, params) {

				params.page = params.page || 0;

				return {
					results: data.companies,
					pagination: {
						more: ((params.page + 1) * 30) < data.totalCount
					}
				};
			},
			cache: true
		},
		escapeMarkup: function (markup) { return markup; },
		minimumInputLength: 1,
		templateResult: formatRepo,
		templateSelection: formatRepoSelection
	};
	$("#create"+"companyId").select2(select2Options);
	$("#createDelete"+"companyId").on("click", function () {

		$("#create"+"companyId").val(null).trigger("change");
	});
	$("#update"+"companyId").select2(select2Options);
	$("#updateDelete"+"companyId").on("click", function () {

		$("#update"+"companyId").val(null).trigger("change");
	});

	function formatRepo (company) {

		if (company.loading) return company.text;

		var companyName = resolveCompanyName(company);
		var markup = '<div class="select2-result-repository clearfix">' +
				'<div class="select2-result-repository__name ma-1">' +
					'<address>' +
						'<strong>' + companyName + '</strong><br>' +
						'address...<br>' +
					'</address>' +
				'</div>' +
			'</div>';

		return markup;
	}

	function formatRepoSelection (company) {

		var companyName = resolveCompanyName(company);
		return companyName;
	}

	function resolveCompanyName (company) {

		var companyName;
		if(company.names){

			companyName = company.names[Messages("langCode")] || company.names["en-US"];
		}else {

			companyName = company.text;
		}

		return companyName;
	}

	$('#deleteButton').on("click", function(){

		var selectedRows = $('#userlist>tbody>tr.selected');
		var user = createUsers(selectedRows)[0];
		for(var property in user){

			$('#delete'+property).val(user[property]);
		}
		$('#delete'+'userId').parent().parent().prev().find('input').val(user.userId);

		initOperation('delete');
	});
	$('#updateButton').on("click", function(){

		var selectedRows = $('#userlist>tbody>tr.selected');
		var user = createUsers(selectedRows)[0];
		for(var property in user){

			switch(property){
				case 'roles':

					$('.update'+property).each(function(){

						if(-1 != $.inArray($(this).val(), user[property])){

							$(this).prop('checked', true);
						}else{

							$(this).prop('checked', false);
						}
					});
					break;
				case 'companyId':

					$('#update'+property+'>option').val(user[property]);
					break;
				case 'companyName':

					$('#update'+'companyId'+'>option').text(user[property]);
					break;
				default:

					$('#update'+property).val(user[property]);
					break;
			};
		}

		$("#update"+"companyId").select2(select2Options);

		initOperation('update');
	});
	$('#createButton').on("click", function(){

		var user = {
			"userId":"",
			"password":"",
			"rePassword":"",
			"roles":"CUSTOMER",
			"companyId":"",
			"companyName":""
		};
		for(var property in user){

			switch(property){
				case 'roles':

					$('.create'+property).each(function(){

						if("CUSTOMER" == $(this).val()){

							$(this).prop('checked', true);
						}else{

							$(this).prop('checked', false);
						}
					});
					break;
				case 'companyId':

					$('#create'+property+'>option').val(user[property]);
					break;
				case 'companyName':

					$('#create'+'companyId'+'>option').text(user[property]);
					break;
				default:

					$('#create'+property).val(user[property]);
					break;
			};
		}

		$("#create"+"companyId").select2(select2Options);

		initOperation('create');
	});

	$('#deleteOk').on("click", function(){

		initOperation('delete');
		operation('delete');
	});
	$('#updateOk').on("click", function(){

		initOperation('update');
		operation('update');
	});
	$('#createOk').on("click", function(){

		initOperation('create');
		operation('create');
	});

	var initOperation = function(crud){

		var errorContainer = $('#'+crud+'Dialog form>div:first-child');
		errorContainer.empty();

		var properties = ['companyId', 'userId', 'newUserId', 'password', 'rePassword'];
		for(var i in properties){

			var propertyErrorContainer = $('#'+crud+properties[i]).parent().find("div");
			propertyErrorContainer.empty();
		}

		$('#'+crud+'Message').text('');
		$('#'+crud+'Description').text('-');
		$('#'+crud+'Progress').removeClass('progress-danger').val(0);
		$('#'+crud+'Cancel').prop('disabled', false);
		$('#'+crud+'Ok').prop('disabled', false);
	}

	var operation = function(crud){

		$('#'+crud+'Message').text(Messages(MessageKeys.PLEASE__WAIT));
		$('#'+crud+'Progress').val(100);
		$('#'+crud+'Cancel').prop('disabled', true);
		$('#'+crud+'Ok').prop('disabled', true);

		var form = $('#'+crud+'Form');
		var url = form.attr('action');
		$.ajax({
			method:"POST",
			url:url,
			data:form.serialize(),
			processData: false,
			dataType: "json",
			timeout: 3 * 1000
		})
		.then(
			function (responseJson) {

				window.location.href = Routes.controllers.manage.user.Read.index().url;
			},
			function (jqXHR, textStatus, errorThrown) {

				var responseJson = jqXHR.responseJSON||{};
				if(responseJson['globalErrors']){

					var errorContainer = $('#'+crud+'Dialog form>div:first-child');
					var errors = responseJson.globalErrors;
					$.each(errors, function(i, error){

						errorContainer.append('<p class="text-warning">'+error+'</p>');
					});
				}
				if(responseJson['errors']){

					for(var property in responseJson.errors){

						var propertyErrorContainer = $('#'+crud+property).parent().find('div')
						var propertyErrors = responseJson.errors[property];
						$.each(propertyErrors, function(i, error){

							propertyErrorContainer.append('<p class="text-warning">'+error+'</p>');
						});
					}

				}

				$('#'+crud+'Message').text(Messages(MessageKeys.FAILURE));
				$('#'+crud+'Description').html(Messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + Messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
				$('#'+crud+'Progress>.progress-bar').addClass('bg-danger');
				$('#'+crud+'Cancel').prop('disabled', false);
				$('#'+crud+'Ok').prop('disabled', false);

				if(responseJson['globalErrors'] || responseJson['errors']) {

					shake('#'+crud+'Dialog');
				}
			}
		);
		return false;
	}

	var createUsers = function(selectedRows){

		var users = [];
		selectedRows.each(function(){

			var tr = this;
			var index = $('#userlist>tbody>tr').index(tr);
			var columns = dataTable.rows().data()[ index ];
			var user = {
				"userId":columns[0],
				"newUserId":columns[0],
				"roles":JSON.parse(columns[2]),
				"companyId":columns[3],
				"companyName":columns[4],
				"expireDateTime":columns[5],
				"signInDateTime":("" != columns[6] ? columns[6]:null),
				"signOutDateTime":("" != columns[7] ? columns[7]:null),
				"updateDateTime":columns[9]
			};
			users.push(user);
		});

		return users;
	};

	Components.applyRangeToCheckboxs('.createroles', 1);
	Components.applyRangeToCheckboxs('.updateroles', 1);

	// ////////// Upload //////////

	$('#uploadFile').on('change', function(e){

		var selectedFilePath = document.uploadForm.uploadFile.value;
		var selectedFilePaths = selectedFilePath.split(/\\|\\/);
		var selectedFileName = selectedFilePaths[selectedFilePaths.length - 1];

		$(this).next('label.custom-file-label').attr('data-filename', selectedFileName);
	});

	$('#uploadButton').on("click", function(){

		initUploadOperation('upload');

		$(this).next('label.custom-file-label').attr('data-filename', '');

		$('#uploadFile').val("");

		$('#updateRadio').prop('checked', true);
	});
	$('#uploadOk').on("click", function(){

		initUploadOperation('upload');
		uploadOperation('upload');
	});

	var initUploadOperation = function(crud){

		var errorContainer = $('#'+crud+'Dialog form>div:first-child');
		errorContainer.empty();

		var properties = ['uploadFile'];
		for(var i in properties){

			var propertyErrorContainer = $('#'+crud+properties[i]).parent().find("div");
			propertyErrorContainer.empty();
		}

		$('#'+crud+'Message').text('');
		$('#'+crud+'Description').text('-');
		$('#'+crud+'Progress').removeClass('progress-danger').val(0);
		$('#'+crud+'Cancel').prop('disabled', false);
		$('#'+crud+'Ok').prop('disabled', false);
	}

	var uploadOperation = function(crud){

		$('#'+crud+'Message').text(Messages(MessageKeys.PLEASE__WAIT));
		$('#'+crud+'Progress').val(100);
		$('#'+crud+'Cancel').prop('disabled', true);
		$('#'+crud+'Ok').prop('disabled', true);

		var form = $('#'+crud+'Form');
		var url = form.attr('action');
		var formData = new FormData(form[0]);
		$.ajax({
			method:"POST",
			url:url,
			data:formData,
			processData: false,
			contentType: false,
			dataType: "json",
			timeout: 30 * 1000
		})
		.then(
			function (responseJson) {

				window.location.href = Routes.controllers.manage.user.Read.index().url;
			},
			function (jqXHR, textStatus, errorThrown) {

				var responseJson = jqXHR.responseJSON||{};
				if(responseJson['globalErrors']){

					var errorContainer = $('#'+crud+'Dialog form>div:first-child');
					var errors = responseJson.globalErrors;
					$.each(errors, function(i, error){

						errorContainer.append('<p class="text-warning">'+error+'</p>');
					});
				}
				if(responseJson['errors']){

					for(var property in responseJson.errors){

						var propertyErrorContainer = $('#'+crud+property).parent().find('div')
						var propertyErrors = responseJson.errors[property];
						$.each(propertyErrors, function(i, error){

							propertyErrorContainer.append('<p class="text-warning">'+error+'</p>');
						});
					}

				}

				$('#'+crud+'Message').text(Messages(MessageKeys.FAILURE));
				$('#'+crud+'Description').html(Messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + Messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
				$('#'+crud+'Progress>.progress-bar').addClass('bg-danger');
				$('#'+crud+'Cancel').prop('disabled', false);
				$('#'+crud+'Ok').prop('disabled', false);

				if(responseJson['globalErrors'] || responseJson['errors']) {

					shake('#'+crud+'Dialog');
				}
			}
		);
		return false;
	}

	$('#'+'upload'+'Dialog').on('hide.bs.modal', function (e) {

		if($('#uploadFileInfo').attr('aria-describedby')){

			$('#uploadFileInfo').trigger('click');
		}
		$('#uploadFileInfo').popover('hide');
	})

	// ////////// Setting //////////

	var updateTableClolumn = function(checkboxInput){

		var index = $('.settingDisplayColumns').index(checkboxInput);
		var checkbox = $(checkboxInput);
		var isVisible = checkbox.prop('checked');

		dataTable.column( index ).visible( isVisible );
	}
	$('.settingDisplayColumns').each(function(){

		updateTableClolumn(this);
	});

	Components.applyRangeToCheckboxs('.settingDisplayColumns', 1, null, function(e){

		updateTableClolumn(e.currentTarget);
	});
});

// ////////// Common //////////

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		if($("#createDialog").is(".show")){

			$('#createOk').trigger("click");
		}else if($("#deleteDialog").is(".show")){

			$('#deleteOk').trigger("click");
		}else if($("#updateDialog").is(".show")){

			$('#updateOk').trigger("click");
		}else if($("#uploadDialog").is(".show")){

			$('#uploadOk').trigger("click");
		}else{

		}

		e.preventDefault();
	}
})

$('[data-toggle="tooltip"]').tooltip();
$('[data-toggle="popover"]').popover();
