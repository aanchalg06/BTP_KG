#!/bin/bash

# Run Python script
python Triple_Extractor.py

# Compile Java code
javac -cp .:json-simple-1.1.1.jar Main.java

# Run Java program
java -cp .:json-simple-1.1.1.jar Main
