#!/bin/sh
$AIR_HOME/bin/ascshd kill

echo "============================================="
echo "| Slow initial compile:"
echo "============================================="
$AIR_HOME/bin/ascshd mxmlc test.as -source-path+=. -o test.swf "-define=CONFIG::V1,\"He said\x2C\nit\'s a frickin\' \\\"laser\\x22\!\""

echo "============================================="
echo "| Faster second compile:"
echo "============================================="
$AIR_HOME/bin/ascshd mxmlc test.as -source-path+=. -o test.swf "-define=CONFIG::V1,\"He said\x2C\nit\'s a frickin\' \\\"laser\\x22\!\""

echo "============================================="
echo "| Even faster third compile (caches primed?):"
echo "============================================="
$AIR_HOME/bin/ascshd mxmlc test.as -source-path+=. -o test.swf "-define=CONFIG::V1,\"He said\x2C\nit\'s a frickin\' \\\"laser\\x22\!\""
