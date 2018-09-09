Add Language
=======

### Add Configuration ###

	github/fineplay/conf/application.conf
	play.i18n.langs = [ "en-US", "ja-JP", "[LANGUAGE]-[COUNTRY]" ]

###### Country code is required.

### Add Message ###

	github/fineplay/conf/messages[|.[lang-CODE]]
	github/fineplay/app/common/system/MessageKeys.java
	github/fineplay/public/javascripts/messages.js

###### Use MessagesCreator & MessageKeysCreator.

### Add Initial Locale code ###
###### Optional.

	github/fineplay/app/controllers.registuser.RegistUser#normalizeLang

### Add Select Locale code ###

	github/fineplay/app/common.utils.Locales#getLocaleIdToNameMap
