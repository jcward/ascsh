@echo off

if "%AIR_HOME%"=="" (
  echo Error, need to set AIR_HOME environment variable, i.e. export AIR_HOME=/opt/air_sdk_3.6
	GOTO:EOF
)

if exist %AIR_HOME%\lib\compiler.jar (
  rem compile and install
  @javac -cp "%AIR_HOME%\lib;%AIR_HOME%\lib\compiler.jar;src/java" src\java\Commandline.java
	@javac -cp "%AIR_HOME%\lib;%AIR_HOME%\lib\compiler.jar;src/java" src\java\ascsh.java
  copy src\java\*.class %AIR_HOME%\lib\
  copy src\bin\* %AIR_HOME%\bin\
  echo Installed ascsh into %AIR_HOME%
) else (
  echo Error, expected to find ASC2 compiler at %%AIR_HOME%%\lib\compiler.jar
)

