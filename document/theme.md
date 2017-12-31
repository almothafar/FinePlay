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

	github/fineplay/public/themes/[Theme name]/bootstrap.min.css

Project CSS
----------

### Make ###

	github/FinePlay/public/themes/[Theme name]/theme.css

###### File Content is Optional.

Project library CSS
----------

### Add enum ###

	models.user.User.java
	models.user.User.java.THEME.[Theme name]

### Modify library CSS ###
###### Optional.

	github/FinePlay/app/views/libraries/[Library name]/head.scala.html
