'use strict';

$('#ex1').bootstrapSlider({

	formatter: function(value) {

		return 'Current value: ' + value;
	}
});

$("#ex2").bootstrapSlider({});

$("#ex4").bootstrapSlider({
	reversed: true,
	tooltip_position: $('html').hasClass('dir-ltr') ? 'right' : 'left'
});

$("#ex7").bootstrapSlider({});

$("#ex13").bootstrapSlider({
	value: 0,
	ticks: [0, 100, 200, 300, 400],
	ticks_labels: ['$0', '$100', '$200', '$300', '$400'],
	ticks_snap_bounds: 30
});

$("#topTooltip").bootstrapSlider({
	tooltip: 'always',
	tooltip_position: 'top'
});
$("#rightTooltip").bootstrapSlider({
	tooltip: 'always',
	tooltip_position: $('html').hasClass('dir-ltr') ? 'right' : 'left'
});
$("#bottomTooltip").bootstrapSlider({
	tooltip: 'always',
	tooltip_position: 'bottom'
});
$("#leftTooltip").bootstrapSlider({
	tooltip: 'always',
	tooltip_position: $('html').hasClass('dir-ltr') ? 'left' : 'right'
});

// Touch scroll off

$(".slider").on('touchmove.noScroll', function(e) {
	e.preventDefault();
});