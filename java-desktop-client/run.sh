#!/bin/bash
echo "Cleaning old build files..."
rm -rf out

echo "Compiling all Java source files into out..."
mkdir -p out
javac -d out $(find src -name "*.java")

if [ $? -eq 0 ]; then
    echo "Compilation successful! Launching app..."
    java -cp out GeneralUser.views.WelcomeFrame
else
    echo "Compilation failed. Please check the errors above."
fi