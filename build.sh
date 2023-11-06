#!/bin/sh
echo --- Cleaning

rm -fr bin

echo --- Compiling Java
javac src/*/*.java -d bin