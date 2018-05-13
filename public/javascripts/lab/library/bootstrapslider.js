'use strict';

$('#ex1').bootstrapSlider({

	formatter: function(value) {

		return 'Current value: ' + value;
	}
});

$("#ex2").bootstrapSlider({});

$("#ex4").bootstrapSlider({
	reversed: true
});

$("#ex7").bootstrapSlider({});

$("#ex13").bootstrapSlider({
	value: 0,
	ticks: [0, 100, 200, 300, 400],
	ticks_labels: ['$0', '$100', '$200', '$300', '$400'],
	ticks_snap_bounds: 30
});
