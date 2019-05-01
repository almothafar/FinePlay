'use strict';

//

$(document).ready(function() {

	var dataTable=$('#companylist').DataTable({
		"language": {
			select: {
				rows: {
					_: " " + messages(MessageKeys.X__CASE__SELECTED).replace("{0}","%d")
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
				} )
				.on( 'deselect', function ( e, dt, type, indexes ) {

					var rowData = dataTable.rows( indexes ).data().toArray();
				} );

			$('#companylist'+'_wrapper').on( 'click', function () {

				updateSelection();
			});

		}
	});

	var updateSelection = function (){

		$('#deleteButton').attr("disabled", "disabled");
		$('#updateButton').attr("disabled", "disabled");
		$('#createButton').attr("disabled", "disabled");

		var rows = $('#companylist>tbody>tr');
		switch(rows.length){
			case 0:

				$('#createButton').prop('disabled', false);

				break;
			default:

				var selectedRows = $('#companylist>tbody>tr.selected');
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

	$('.nextButton').on('click', function(e){

		e.preventDefault();

		var selectedRows = $(this).parent().parent();
		var company = createCompanies(selectedRows)[0];

		var target = company['id'];
//				var target = $(this).parent().parent().find('td').eq(0).text();

		hideToStart('#masterContent', function(e){

			window.location.href = Routes.controllers.manage.company.organization.list.Read.index(target).url;
		});
	});

	// ////////// Read //////////

	// ////////// Download //////////

	$('#downloadButton').on("click", function(e){

		var downloadQuery = $('#'+'read'+'Form').serialize();
		var currentLink = $('#downloadButton').attr('href');
		var currentLinkArray = currentLink.split(/\?.*(?=csrfToken)/);
		var downloadLink = currentLinkArray[0]+'?'+downloadQuery+'&'+currentLinkArray[1];
		$('#downloadButton').attr('href', downloadLink);
	});

	// ////////// Edit //////////

	$('#deleteButton').on("click", function(){

		var selectedRows = $('#companylist>tbody>tr.selected');
		var company = createCompanies(selectedRows)[0];
		for(var property in company){

			$('#delete'+property).val(company[property]);
		}
		$('#delete'+'id').parent().parent().prev().find('input').val(company.name);

		initOperation('delete');
	});
	$('#updateButton').on("click", function(){

		var selectedRows = $('#companylist>tbody>tr.selected');
		var company = createCompanies(selectedRows)[0];
		for(var property in company){

			switch(property){
				default:

					$('#update'+property).val(company[property]);
					break;
			};
		}

		initOperation('update');
	});
	$('#createButton').on("click", function(){

		var company = {
			"id":"",
			"name":"",
			"localName":""
		};
		for(var property in company){

			switch(property){
				default:

					$('#create'+property).val(company[property]);
					break;
			};
		}

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

		var properties = ['id', 'name', 'localName'];
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

		$('#'+crud+'Message').text(messages(MessageKeys.PLEASE__WAIT));
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

				window.location.href = Routes.controllers.manage.company.Read.index().url;
			},
			function (jqXHR, textStatus, errorThrown) {

				var responseJson = jqXHR.responseJSON||{};
				if(responseJson['globalErrors']){

					var errorContainer = $('#'+crud+'Dialog form>div:first-child');
					var errors = responseJson.globalErrors;
					$.each(errors, function(i, error){

						errorContainer.append('<p class="text-danger">'+error+'</p>');
					});
				}
				if(responseJson['errors']){

					for(var property in responseJson.errors){

						var propertyErrorContainer = $('#'+crud+property).parent().find('div')
						var propertyErrors = responseJson.errors[property];
						$.each(propertyErrors, function(i, error){

							propertyErrorContainer.append('<p class="text-danger">'+error+'</p>');
						});
					}

				}

				$('#'+crud+'Message').text(messages(MessageKeys.FAILURE));
				$('#'+crud+'Description').html(messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
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

	var createCompanies = function(selectedRows){

		var companies = [];
		selectedRows.each(function(){

			var tr = this;
			var index = $('#companylist>tbody>tr').index(tr);
			var columns = dataTable.rows().data()[ index ];
			var company = {
				"id":columns[0],
				"name":columns[1],
				"localName":columns[2],
				"version":columns[4]
			};
			companies.push(company);
		});

		return companies;
	};

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

		$('#'+crud+'Message').text(messages(MessageKeys.PLEASE__WAIT));
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

				window.location.href = Routes.controllers.manage.company.Read.index().url;
			},
			function (jqXHR, textStatus, errorThrown) {

				var responseJson = jqXHR.responseJSON||{};
				if(responseJson['globalErrors']){

					var errorContainer = $('#'+crud+'Dialog form>div:first-child');
					var errors = responseJson.globalErrors;
					$.each(errors, function(i, error){

						errorContainer.append('<p class="text-danger">'+error+'</p>');
					});
				}
				if(responseJson['errors']){

					for(var property in responseJson.errors){

						var propertyErrorContainer = $('#'+crud+property).parent().find('div')
						var propertyErrors = responseJson.errors[property];
						$.each(propertyErrors, function(i, error){

							propertyErrorContainer.append('<p class="text-danger">'+error+'</p>');
						});
					}

				}

				$('#'+crud+'Message').text(messages(MessageKeys.FAILURE));
				$('#'+crud+'Description').html(messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
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

//

if(document.location.origin+Routes.controllers.manage.company.organization.list.Read.index(0).url.replace(/\?.*/,'') == document.referrer.replace(/\?.*/,'') ||
	document.location.origin+Routes.controllers.manage.company.organization.tree.Read.index(0).url.replace(/\?.*/,'') == document.referrer.replace(/\?.*/,'')){

	showFromStart('#masterContent');
}else{

	$('#masterContent').removeClass("d-none");
}
