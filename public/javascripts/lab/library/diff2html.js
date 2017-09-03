'use strict';

var lineDiffExample =
	"--- a/src/my/really/big/path/sample.js\n" +
	"+++ b/src/my/small/path/sample.js\n" +
	"@@ -1 +1,2 @@\n" +
	"-test\n" +
	"+test1r\n" +
	"+test2r\n" +
	'diff --git a/src/core/init.java b/src/core/init.java\n' +
	'index e49196a..50f310c 100644\n' +
	'--- a/src/core/init.java\n' +
	'+++ b/src/core/init.java\n' +
	'@@ -101,7 +101,7 @@\n' +
	' /**\n' +
	'   * Setter for property filesize.\n' +
	'   *\n' +
	"   * @param filesize value of property 'filesize'.\n" +
	'   */\n' +
	'   public void setFilesize(int filesize) {\n' +
	'-    this.filesizeOld = filesizeOld;\n' +
	'+    this.filesizeNew = filesizeNew;\n' +
	'   }\n';

$(document).ready(function() {
	var diff2htmlUi = new Diff2HtmlUI({diff: lineDiffExample});

	diff2htmlUi.draw('#line-by-line', {
		inputFormat: 'json',
		showFiles: true,
		matching: 'lines'
	});
	diff2htmlUi.fileListCloseable('#line-by-line', false);
	diff2htmlUi.highlightCode('#line-by-line');

	diff2htmlUi.draw('#side-by-side', {
		inputFormat: 'json',
		showFiles: true,
		matching: 'lines',
		outputFormat: 'side-by-side',
		synchronisedScroll: true
	});
	diff2htmlUi.fileListCloseable('#side-by-side', false);
	diff2htmlUi.highlightCode('#side-by-side');
});
