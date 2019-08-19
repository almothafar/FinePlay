[English](./README.md) | 日本語

<img src="./public/images/ja-JP/logo.png" alt="fine✿play" height="54"/>

Play(Java) 2.7 & Bootstrap 4.3  
多言語、レスポンシブな、プロジェクトのひな形(何かのたたき台に)。

| 携帯 | タブレット |
|-------|-----------|
| <img src="./public/images/iPhone.png" height="300"/>  | <img src="./public/images/iPad.png" height="400"/> |

遊び方
----------

##### 環境 #####

	macOS Mojave バージョン 10.14.6（18G87）
	時間帯 UTC
	openjdk バージョン "11.0.4" 2019-07-16 LTS / OpenJDK Runtime Environment Corretto-11.0.4.11.1 (build 11.0.4+11-LTS)
	Safari バージョン 12.1.2 (14607.3.9)

##### OpenJDK #####

インストール

[Amazon Corretto](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html)

##### コンソール #####

	MacBook:~ user$ cd github/FinePlay

	MacBook:FinePlay user$ chmod +x ./sbt-dist/bin/sbt
	MacBook:FinePlay user$ chmod +x ./sbt
	MacBook:FinePlay user$ chmod +x ./start.sh

	MacBook:FinePlay user$ ./start.sh

##### Safari #####

URL を開く

[http://localhost:9000](http://localhost:9000)

緑色のユーザーアイコンをクリックすると、開発用のユーザーを選択できます。

書類
-------

### 公式書類 ###

[JDK](https://docs.oracle.com/javase/jp/11/docs/api/)

[Play Framework](https://www.playframework.com/documentation/2.7.x)

[Bootstrap](http://getbootstrap.com/docs/4.3)

[Font Awesome](https://fontawesome.com/how-to-use)

この **ひな形** は、非公式です。

サポート / 追加コンテンツ
---------------

##### Play Framework #####
+ ライトベンド社で、[商用サポート](https://www.lightbend.com/subscription)が提供されているようです。

##### Bootstrap #####
+ [ドキュメント](https://getbootstrap.com)を閲覧すると、広告収入になると思います。
+ [テーマ](https://themes.getbootstrap.com)を販売しているようです。

##### Font Awesome #####
+ [Pro版](https://fontawesome.com/pro)を購入すると、より多くのアイコンが使用できるようです。

注意
---------------

+ このプロジェクトは、継続性を考慮していません。（しかし、このひな形は、オープンソースソフトウェアのための非常に薄いラッパーなので、心配しないでください。）
+ このプロジェクトは、汎用的に使用する事は、考えられていません。（このプロジェクトは、ひな形なので、フォークもしくは、修正して使用します。）
+ このプロジェクトで使用している、ライブラリのライセンスは、利用者において改めて、確認してください。

やる事(やるとは、言ってない)
---------------

###### このプロジェクトが、将来更新されるかどうかは、未定です。 ######

+ Bootstrap 4.4 リリース版に更新  
   + popper.js の更新
+ system_menuのリファクタリング

リリース履歴
---------------

+ **2.7.3-b2** - 2019-07-06
+ **2.6.13-β7** - 2018-06-18
+ **2.6.3-α1** - 2017-08-14
   + 最初のコミット

###### バージョン番号なんて飾りです。偉い人にはそれがわからんのですよ。

ライセンス
-------
著作権 &copy; 2017-2019 [hiro20v](https://github.com/hiro20v)  
[MIT License][mit] の下で配布されます。

[MIT]: http://opensource.org/licenses/MIT
