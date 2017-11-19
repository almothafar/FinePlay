LANG=en_US.UTF-8
rm -r ./target/*
rm -r ./logs/*
rm -r ./generate/*
rm -r ./document/tabledef/*
rm -r ./project/project/
rm -r ./project/target/
rm -r ./bin/*
find ./ -name '.DS_Store' | xargs rm
rm -r ./target/.history
