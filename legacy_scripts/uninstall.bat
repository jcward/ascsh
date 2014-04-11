@echo off

if "%AIR_HOME%"=="" (
  echo Error, need to set AIR_HOME environment variable, i.e. export AIR_HOME=/opt/air_sdk_3.6
	GOTO:EOF
)

if exist %AIR_HOME%\lib\ascsh.class (
  rem uninstall
  del %AIR_HOME%\lib\ascsh_cmd.class
	del %AIR_HOME%\lib\Commandline.class
	del %AIR_HOME%\bin\ascsh*
  echo Removed ascsh from %AIR_HOME%
) else (
	echo ascsh in not installed in %AIR_HOME%
)
