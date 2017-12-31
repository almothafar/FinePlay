'use strict';

$('#picker').colorpicker({

	customClass: 'colorpicker-2x',
	color: '#ffaa00',
	container: true,
	inline: true,
	sliders: {
		saturation: {
			maxLeft: 200,
			maxTop: 200
		},
		hue: {
			maxTop: 200
		},
		alpha: {
			maxTop: 200
		}
	}
});

$('#okButton').on('click', function (e) {

	var button = $(this);

	var picker = button.parent().parent().find('#picker');
	console.dir(picker.colorpicker('getValue'));
	console.dir(picker.data('colorpicker').color.toRGB());
	console.dir(picker.data('colorpicker').color.toHex());
	console.dir(picker.data('colorpicker').color.toHSL());

	$('#getButton').css({
		"backgroundColor": picker.colorpicker('getValue')
	});
})

$('#cp2').colorpicker();