Make theme
=======

Bootstrap CSS customize
----------

### Build tools ###

Install

[Build tools](https://getbootstrap.com/docs/4.1/getting-started/build-tools/)

### Customize ###

e.g.

	github/bootstrap-4.1.1/scss/_variables.scss

### Build ###

##### Console #####

	MacBook:~ user$ cd github/bootstrap-4.1.1
	MacBook:bootstrap-4.1.1 user$ npm install
	MacBook:bootstrap-4.1.1 user$ npm run dist

### Position ###

	github/bootstrap-4.1.1/dist/css/bootstrap.min.css

to

	github/fineplay/public/themes/[theme name]/bootstrap.min.css

### Update Integrity ###

	github/fineplay/app/views/libraries/adjust/bootstrap/head.scala.html
	github/fineplay/app/views/libraries/bootstrap/head.scala.html
	github/fineplay/app/views/libraries/bootstrap/last.scala.html
	github/fineplay/public/themes/[theme name]/bootstrap.min.css
	github/fineplay/app/views/libraries/jquery/last.scala.html
	github/fineplay/app/views/libraries/jqueryui/head.scala.html
	github/fineplay/app/views/libraries/jqueryui/last.scala.html
	etc...

###### Use IntegrityValuesCreator.

Project CSS
----------

### Make ###

	github/FinePlay/public/themes/[theme name]/theme.css

###### File Content is Optional.

Project library CSS
----------

### Add enum ###

	models.user.User.java
	models.user.User.Theme.[THEME NAME]

### Add Message ###

	github/FinePlay/conf/messages[|.[lang-CODE]]
	theme.[theme name] = [Theme name]

	github/FinePlay/app/common/system/MessageKeys.java
	public static final String THEME_[THEME NAME] = "theme.[theme name]";

	github/FinePlay/public/javascripts/messages.js
	THEME_[THEME NAME]: "theme.[theme name]",

###### Use MessagesCreator & MessageKeysCreator.

### Add Util code ###

	common.utils.Themes.java#getThemeIdToNameMap()
	themeIdToNameMap.put(Theme.[THEME NAME].name(), messages.get(Locales.toLang(locale), MessageKeys.THEME_[THEME NAME]));

### Modify library CSS ###
###### Optional.

	github/FinePlay/app/views/libraries/[library name]/head.scala.html
