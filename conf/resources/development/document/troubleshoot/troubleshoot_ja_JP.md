問題解決
====

### DataTables の、 Language file が、適用されない
Japanese.lang 内のコメントを削除する。

### WebDriver が利用出来ない
	"org.webjars" % "jquery" % "2.1.1",
を削除

### Eclipse で補完出来ない
	eclipse with-source=true
を実行

### org.h2.jdbc.JdbcSQLException のメッセージ文字化け
	/Users/[user]/.ivy2/cache/com.h2database/h2/jars/h2-1.4.191.jar/org/h2/util/org/h2/res/_messages_ja.prop
が、謎エンコーディング
H2 のバージョンアップ

### 起動しない
```
This application is already running (Or delete /Users/[user]/workspace/fineplay/target/universal/fineplay-1.0-SNAPSHOT/RUNNING_PID file).
```
```
/fineplay/target/universal/fineplay-1.0-SNAPSHOT/RUNNING_PID
```
を削除

### 1バイトのUTF-8シーケンスのバイト1が無効です。というエラー

/fineplay/sbt-dist/conf/sbtconfig.txt
に、
	-Dfile.encoding=UTF-8
を追記

### PostgreSQL が起動しない
```
FATAL:  lock file "postmaster.pid" already exists
```
```
/usr/local/var/postgres/postmaster.pid
```
を削除

### Select2 の初回表示位置がおかしい
Webインスペクタを閉じる

### play.runsupport.ServerStartException
	[error] p.c.s.NettyServer - Failed to listen for HTTP on /0.0.0.0:9000!
	[error] (compile:run) play.runsupport.ServerStartException

ポート調査

	lsof -i :9000

Pleiades の場合、

	/workspace/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.php.debug.core.prefs

の、clientPortを変更

PHP > Debug > DEbugger > XDebug

の、デバッグ・ポートを変更

### npm install のエラー

	npm WARN bootstrap@4.0.0 requires a peer of jquery@1.9.1 - 3 but none is installed. You must install peer dependencies yourself.
というエラーの場合、

	npm install jquery --save
を実行