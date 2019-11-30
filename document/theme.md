Make theme
=======

Bootstrap CSS customize
----------

### Build tools ###

Install

[Build tools](https://getbootstrap.com/docs/4.4/getting-started/build-tools/)

### Customize ###

e.g.

	github/bootstrap-4.4.1/scss/_variables.scss

### Build ###

##### Console #####

	MacBook:~ user$ cd github/bootstrap-4.4.1
	MacBook:bootstrap-4.4.1 user$ npm install
	MacBook:bootstrap-4.4.1 user$ npm run dist

### Position ###

	github/bootstrap-4.4.1/dist/css/bootstrap.min.css

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

	github/fineplay/public/themes/[theme name]/theme.css

###### File Content is Optional.

Project library CSS
----------

### Add enum ###

	models.user.User.java
	models.user.User.Theme.[THEME NAME]

### Add Message ###

	github/fineplay/conf/messages[|.[lang-CODE]]
	theme.[theme name] = [Theme name]

	github/fineplay/app/common/system/MessageKeys.java
	public static final String THEME_[THEME NAME] = "theme.[theme name]";

	github/fineplay/public/javascripts/messages.js
	THEME_[THEME NAME]: "theme.[theme name]",

###### Use MessagesCreator & MessageKeysCreator.

### Add Read theme code ###

	github/fineplay/app/views/libraries/bootstrap/head.scala.html

### Modify library CSS ###
###### Optional.

	github/fineplay/app/views/libraries/[library name]/head.scala.html
