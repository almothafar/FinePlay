Library
=======

Selection criteria
-------

- Cool !
- Fit Content security policy
- Fit Play framework 2.6
- Fit Bootstrap 4.1
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

ECMAScript/CSS Include
-------

There are a variety of cases depending on the library.

### Position ###

	github/fineplay/public/[library name]/[library].min.css
	github/fineplay/public/[library name]/[library].min.js

###### WebJars or Manual

### Make Library wrapper template ###
###### Optional.

	github/fineplay/app/views/libraries/[library name]/head.scala.html
	github/fineplay/app/views/libraries/[library name]/last.scala.html

### Use Library(wrapper template) ###

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
