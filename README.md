<img src="./public/images/en-US/logo.png" alt="fine✿play" height="54"/>

Play(Java) 2.7 & Bootstrap 4.1  
multilingual responsive project template.

| Phone | Tablet    |
|-------|-----------|
| <img src="./public/images/iPhone.png" height="300"/>  | <img src="./public/images/iPad.png" height="400"/> |

How to play
----------

##### Environment #####

	macOS High Sierra version 10.13.6（17G65）
	openjdk version "11" 2018-09-25 / OpenJDK Runtime Environment 18.9 (build 11+28)
	Safari version 11.1.2 (13605.3.8)

##### OpenJDK #####

Install

[OpenJDK](http://jdk.java.net/11/)

##### Console #####

	MacBook:~ user$ cd github/FinePlay

	MacBook:FinePlay user$ chmod +x ./sbt-dist/bin/sbt
	MacBook:FinePlay user$ chmod +x ./sbt
	MacBook:FinePlay user$ chmod +x ./start.sh

	MacBook:FinePlay user$ ./start.sh

##### Safari #####

Open URL

[http://localhost:9000](http://localhost:9000)

By clicking the green user icon, users for development can be selected.

Document
-------

### Javadoc ###

##### Console #####

	MacBook:~ user$ cd github/FinePlay
	MacBook:FinePlay user$ ./sbt doc
	MacBook:~ user$ open -a Safari ./target/scala-2.12/api/index.html

### Official Document ###

[Play Framework](https://www.playframework.com/documentation/2.7.0-M2)

[Bootstrap](http://getbootstrap.com/docs/4.1)

[Font Awesome](https://fontawesome.com/how-to-use)

This **template** is unofficial.

Notice
---------------

+ This project is not considered to develop continuously. (However, this template is a very thin wrapper for open source software, so don't worry about it.)
+ This project is not thought to be used for multi purpose. (Since this project is a template, it is forked or modified and used.)
+ Please confirm the license of the library software you are using with this project once again.

TODO
---------------

###### Whether this project will be updated in the future is undecided. ######

#### FinePlay 2.7.x ####
+ Update Play Framework 2.7.x
+ Update OpenJDK 11
+ Update Bootbox 5.0
+ Update Shepherd 2.0.0

#### Play Framework 2.7.x ####
+ Learn how to set cookies with the API of 2.7.x.
+ Update application.conf
+ Update messages

#### Bootstrap 4.2.x ####
+ Rewrite notify implementation to Toast based.
+ Rewrite custom-switch implementation to Switch based.
+ Rename wordbreak class.
+ Move include of Hammer.

#### Drop IE ####
+ Refactor system_menu.

Release History
---------------

+ **2.7.0-M3-βc1** - 2018-09-09
+ **2.6.13-β7** - 2018-06-18
+ **2.6.3-α1** - 2017-08-14
   + First commit

###### The version no is an ornament. It is not understood by a great person.

License
-------
Copyright &copy; 2017-2018 [hiro20v](https://github.com/hiro20v)  
Distributed under the [MIT License][mit].

[MIT]: http://opensource.org/licenses/MIT
