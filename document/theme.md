Make theme
=======

Bootstrap CSS customize
----------

### Build tools ###

Install

[Build tools](http://getbootstrap.com/docs/4.0/getting-started/build-tools/)

### Customize ###

e.g.

	github/bootstrap-4.0.0/scss/_variables.scss

### Build ###

##### Console #####

	MacBook:~ user$ cd github/bootstrap-4.0.0
	MacBook:bootstrap-4.0.0 user$ npm install
	MacBook:bootstrap-4.0.0 user$ npm run dist

### Deploy ###

	github/bootstrap-4.0.0/dist/css/bootstrap.min.css

to

	github/fineplay/public/themes/[theme name]/bootstrap.min.css

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
