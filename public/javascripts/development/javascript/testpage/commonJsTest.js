'use strict';

mocha.setup('tdd');
var assert = chai.assert;

var testFunction = function(arg) {

	return arg;
}

suite('Test', function(){

	suite('Test of testFunction()', function(){

			test('Success',function() {
			assert.equal("1",testFunction(1),'Argment 1');
		});
	});

	suite('Test of testFunction()', function(){

			test('Failure',function() {
			assert.equal("3",testFunction(2),'Argment 2');
		});
	});
});

mocha.run();
