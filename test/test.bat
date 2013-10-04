@echo off
%AIR_HOME%\bin\ascshd kill

echo =============================================
echo = Slow initial compile:
echo =============================================
%AIR_HOME%\bin\ascshd mxmlc test.as -source-path+=. -o test.swf "-define=CONFIG::V1,\"He said\x2C\nit\'s a frickin\' \\\"laser\\x22\!\""

echo =============================================
echo = Faster second compile:
echo =============================================
%AIR_HOME%\bin\ascshd mxmlc test.as -source-path+=. -o test.swf "-define=CONFIG::V1,\"He said\x2C\nit\'s a frickin\' \\\"laser\\x22\!\""

echo =============================================
echo = Even faster third compile (caches primed?):
echo =============================================
%AIR_HOME%\bin\ascshd mxmlc test.as -source-path+=. -o test.swf "-define=CONFIG::V1,\"He said\x2C\nit\'s a frickin\' \\\"laser\\x22\!\""

echo =============================================
echo = Testing ascsh (without the daemon, type
echo = mxmlc simple.as to recompile, CTRL-C to quit)
echo =============================================
%AIR_HOME%\bin\ascsh mxmlc simple.as
