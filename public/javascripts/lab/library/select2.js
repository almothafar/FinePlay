'use strict';

$(document).ready(function() {

	$(".js-example-basic-single").css({'width': '100%'})
	$(".js-example-basic-multiple").css({'width': '100%'})
	$(".js-example-tags").css({'width': '100%'})

	$(".js-example-basic-single.form-control-lg").css({'width': '100%'})
	$(".js-example-basic-multiple.form-control-lg").css({'width': '100%'})
	$(".js-example-tags.form-control-lg").css({'width': '100%'})

	$(".js-example-basic-single.form-control-sm").css({'width': '100%'})
	$(".js-example-basic-multiple.form-control-sm").css({'width': '100%'})
	$(".js-example-tags.form-control-sm").css({'width': '100%'})

	$(".js-example-basic-single").select2({
		placeholder: "Select a state",
		allowClear: true
	});
	$(".js-example-basic-multiple").select2({
		placeholder: "Select a state"
	});
	$(".js-example-tags").select2({
		tags: true
	})

	$(".js-example-basic-single.form-control-lg").select2({
		placeholder: "Select a state",
		allowClear: true
	});
	$(".js-example-basic-multiple.form-control-lg").select2({
		placeholder: "Select a state"
	});
	$(".js-example-tags.form-control-lg").select2({
		tags: true
	})

	$(".js-example-basic-single.form-control-sm").select2({
		placeholder: "Select a state",
		allowClear: true
	});
	$(".js-example-basic-multiple.form-control-sm").select2({
		placeholder: "Select a state"
	});
	$(".js-example-tags.form-control-sm").select2({
		tags: true
	})
});
