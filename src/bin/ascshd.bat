@echo off

setlocal

if "x%AIR_SDK_HOME%"=="x"  (set AIR_SDK_HOME=%~dp0..) else echo Using AIR SDK: %AIR_SDK_HOME%

@ruby "%AIR_SDK_HOME%\bin\ascshd" %*
