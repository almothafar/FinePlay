LANG=en_US.UTF-8
./sbt \
-J-XX:StartFlightRecording=name=fineplay,\
filename=./logs/flight_$(date +"%Y-%m-%d_%H-%M-%S").jfr,\
dumponexit=true,delay=2m,\
maxsize=512m,\
settings=profile,\
path-to-gc-roots=true \
-J-Xmx2g -debug -jvm-debug 9999 run
