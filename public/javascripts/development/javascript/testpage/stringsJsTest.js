'use strict';

mocha.setup('tdd');
var assert = chai.assert;

suite('Test', function(){

	suite('Test of removeNewLine()', function(){

			test('Replace multi new line.',function() {
			assert.equal("aaabbbcccddd",Strings.removeNewLine("aaa\rbbb\nccc\r\nddd"),'removeNewLine 1');
		});
	});
});

mocha.run();
