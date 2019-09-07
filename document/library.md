Library
=======

Selection criteria
-------

- Cool !
- Fit Content security policy
- Fit Play framework 2.8
- Fit Bootstrap 4.3
- Browser support
- Responsive support
- Multilingual support
- High resolution support
- Touch operation support
- Internet knowledge base
- Project activity
- User base
- Easy development
- Beautiful code
- Delivery WebJars/Maven repository
- License of not copyleft

Java Include
-------

### Position ###

###### Maven

write to

	github/fineplay/build.sbt

or

###### Manual

	github/fineplay/lib/[library].jar

ECMAScript/CSS Include
-------

- There are a variety of cases depending on the library.
- As of 2018, most libraries do not support content security policy.

### Position ###

###### WebJars

write to

	github/fineplay/build.sbt

or

###### Manual

	github/fineplay/public/[library name]/[library].min.css
	github/fineplay/public/[library name]/[library].min.js

### Make Library wrapper template ###
###### Optional.

	github/fineplay/app/views/libraries/[library name]/head.scala.html
	github/fineplay/app/views/libraries/[library name]/last.scala.html

### Use Library ###

	github/fineplay/app/views/[view name].scala.html

```
@()

@frame(){

	@head("[section]"){

		@libraries.standard.head()
		// Direct reference
		<link rel='stylesheet' href='@routes.Assets.versioned("stylesheets/[library name]/[library].min.css")'>
		// Template reference
//		@libraries.[library name].head()
	}

	@body("[section]","[work]"){

		//	My view code.
		<H1>Hello world.</H1>

		@libraries.standard.last()
		// Direct reference
		<script src='@routes.Assets.versioned("javascripts/[library name]/[library].min.js")'></script>
		// Template reference
//		@libraries.[library name].last()
	}
}
```
