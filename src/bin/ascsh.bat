@echo off

rem This is a copy of AIRSDK/bin/mxmlc.bat that calls ascsh instead of mxmlc

setlocal

if "x%AIR_SDK_HOME%"=="x"  (set AIR_SDK_HOME=%~dp0..) else echo Using AIR SDK: %AIR_SDK_HOME%

@java -Dsun.io.useCanonCaches=false -Xms32m -Xmx512m -Dflexlib="%AIR_SDK_HOME%\frameworks" -cp "%AIR_SDK_HOME%\lib;%AIR_SDK_HOME%\lib\compiler.jar" ascsh %*
