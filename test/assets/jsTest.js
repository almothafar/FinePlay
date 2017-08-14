var assert = require("assert");
describe("Test", function() {
	it("say hello", function() {

		var testFunc = function(){

			return "result";
		};
		assert.equal(testFunc(), "result");
	});
});