'use strict';

$('#ex1').bootstrapSlider({

	formatter: function(value) {

		return 'Current value: ' + value;
	}
});

$("#ex2").bootstrapSlider({});
