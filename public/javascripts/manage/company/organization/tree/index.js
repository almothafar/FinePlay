'use strict';

//

$(document).ready(function() {

	var data = JSON.parse($('#unitTreeJSON').text());

	$('#unitTree').tree({
		data: data,
		autoOpen: true,
		dragAndDrop: true,
		onCreateLi: function(node, $li) {

			$li.addClass('list-group-item').css('display', 'list-item').parent().addClass('list-group');
		}
	});

	$('#unitTree').bind(
		'tree.move',
		function(e) {

			$("#unsavedInfo>span").html("<strong>" + Messages(MessageKeys.SAVE) + " " + Messages(MessageKeys.INFO) + "</strong> " + Messages(MessageKeys.SYSTEM_ERROR_DATA_UNSAVE));
			$("#unsavedInfo").show();
		}
	);

	var updateState = function (){

		$('#updateButton').attr("disabled", "disabled");

		switch(data.length){
			case 0:

				$('#updateButton').attr("disabled", "disabled");
				break;
			default:

				$('#updateButton').prop('disabled', false);
				break;
		};

		$("#unsavedInfo").append("<span></span>").hide();
	};

	updateState();

	// ////////// Edit //////////

	$('#updateButton').on("click", function(){

		var unitTreeJSON = $('#unitTree').tree('toJson');
		$('#update' + 'unitTreeJSON').val(unitTreeJSON);

		initOperation('update');
	});

	$('#updateOk').on("click", function(){

		initOperation('update');
		operation('update');
	});

	var initOperation = function(crud){

		var errorContainer = $('#'+crud+'Dialog form>div:first-child');
		errorContainer.empty();

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

				window.location.href = Routes.controllers.manage.company.organization.tree.Read.index(Messages("companyId")).url;
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

				$('#'+crud+'Message').text(Messages(MessageKeys.FAILURE));
				$('#'+crud+'Description').html(Messages(MessageKeys.STATUS) + '&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;' + Messages(MessageKeys.ERROR) + '&nbsp;<strong>'+errorThrown+'</strong>');
				$('#'+crud+'Progress>.progress-bar').addClass('bg-danger');
				$('#'+crud+'Cancel').prop('disabled', false);
				$('#'+crud+'Ok').prop('disabled', false);

				if(responseJson['globalErrors']) {

					shake('#'+crud+'Dialog');
				}
			}
		);
		return false;
	}

});

// ////////// Common //////////

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		if($("#updateDialog").is(".show")){

			$('#updateOk').trigger("click");
		}else{

		}

		e.preventDefault();
	}
})

$('[data-toggle="tooltip"]').tooltip();
$('[data-toggle="popover"]').popover();

//

if(document.location.origin+Routes.controllers.manage.company.Read.index().url == document.referrer){

	showFromRight('#detailContent');
}else{

	$('#detailContent').removeClass("d-none");
}

$('#previousButton').on('click', function(e){

	e.preventDefault();

	hideToRight('#detailContent', function(e){

		window.location.href = Routes.controllers.manage.company.Read.index().url;
	});
});

$('#tasklist>label').on('click', function (e) {

	var tasks = $('#tasklist>label');
	var taskIndex = tasks.index($(e.currentTarget));
	if(0 == taskIndex){

		window.location.href = Routes.controllers.manage.company.organization.list.Read.index(Messages("companyId")).url;
	}else if(1 == taskIndex){

		window.location.href = Routes.controllers.manage.company.organization.tree.Read.index(Messages("companyId")).url;
	}
});
