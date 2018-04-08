'use strict';

$('#picker').colorpicker({

	customClass: 'colorpicker-2x',
	color: '#00ff00',
	container: true,
	inline: true,
	sliders: {
		saturation: {
			maxLeft: 200,
			maxTop: 200,
			callLeft: 'setSaturationRatio',
			callTop: 'setBrightnessRatio'
		},
		hue: {
			maxTop: 200,
			callLeft: false,
			callTop: 'setHueRatio'
		},
		alpha: {
			maxTop: 200,
			callLeft: false,
			callTop: 'setAlphaRatio'
		}
	},
	extensions: [
		{
			name: 'swatches',
			colors: {
				'black': '#000000',
				'white': '#ffffff',
				'red': '#FF0000',
				'default': '#777777',
				'primary': '#337ab7',
				'success': '#5cb85c',
				'info': '#5bc0de',
				'warning': '#f0ad4e',
				'danger': '#d9534f'
			},
			namesAsValues: true
		}
	]
});

$('#okButton').on('click', function (e) {

	var button = $(this);

	var picker = button.parent().parent().find('#picker');
	console.dir(picker.colorpicker('getValue'));
	console.dir(picker.data('colorpicker').color.toRgb());
	console.dir(picker.data('colorpicker').color.toHex());
	console.dir(picker.data('colorpicker').color.toHsl());

	$('#getButton').css({
		"backgroundColor": picker.colorpicker('getValue')
	});
})

$('#cp2').colorpicker({
	color: '#0000ff'
});