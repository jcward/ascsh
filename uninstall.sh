if [ -z "$AIR_HOME" ]; then
  echo "Error, need to set AIR_HOME environment variable, i.e. export AIR_HOME=/opt/air_sdk_3.6"
	exit 1
fi

if [ -f "$AIR_HOME/lib/ascsh.class" ]
then
  # compile and install
  rm $AIR_HOME/lib/ascsh.class
  rm $AIR_HOME/lib/Commandline.class
  rm $AIR_HOME/bin/ascsh
  rm $AIR_HOME/bin/ascsh.bat
  rm $AIR_HOME/bin/ascshd
  echo "Removed ascsh from $AIR_HOME"
else
	echo "ascsh in not installed in $AIR_HOME"
fi
