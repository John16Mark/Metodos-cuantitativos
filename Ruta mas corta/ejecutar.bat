@echo off
cls
javac -d ./bin src/def/*.java
java -cp bin def.Main