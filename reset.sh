LANG=en_US.UTF-8

rm -r ./target/*
rm -r ./logs/*
rm -r ./bin/*
rm -r ./generate/*
touch ./generate/.empty

rm -r ./document/javadoc/*
rm -r ./document/tabledef_prod/*
rm -r ./document/doxygen/*
rm -r ./project/project/
rm -r ./project/target/
find ./ -name '.DS_Store' | xargs rm
rm -r ./target/.history
