#!/bin/sh
./build.sh

echo "--- Running"

PROGRAM=${1:-"Main.Game"}
export CLASSPATH="bin;Libs;Resources"
java $PROGRAM