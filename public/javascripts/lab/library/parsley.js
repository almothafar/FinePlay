'use strict';

window.Parsley.addValidator('userid',　ParsleyValidators.UserIdValidator);
window.Parsley.addValidator('password',　ParsleyValidators.PasswordValidator);
window.Parsley.addValidator('hiragana',　ParsleyValidators.HiraganaValidator);
window.Parsley.addValidator('katakana',　ParsleyValidators.KatakanaValidator);
$('#parsleyForm').parsley();

window.Parsley.on('form:error', function() {

	shake('#parsleyPanel');
});
