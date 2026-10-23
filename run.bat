@echo off
javac -d out src\*.java
if errorlevel 1 (
  echo Compile failed.
  pause
  exit /b 1
)
java -cp out Main