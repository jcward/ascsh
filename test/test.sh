#!/bin/sh
$AIR_HOME/bin/ascshd kill

echo "Slow initial compile:"
$AIR_HOME/bin/ascshd mxmlc test.as -source-path+=. -o test.swf "-define=CONFIG::V1,\"He said\x2C\nit\'s a frickin\' \\\"laser\\x22\!\""

echo "Faster second compile:"
$AIR_HOME/bin/ascshd mxmlc test.as -source-path+=. -o test.swf "-define=CONFIG::V1,\"He said\x2C\nit\'s a frickin\' \\\"laser\\x22\!\""
