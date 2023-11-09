#!/bin/sh
echo --- Cleaning

rm -fr bin

echo --- Compiling Java
javac -cp "src;Libs/*;Resources" src/*/*.java -d bin