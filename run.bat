@echo off
setlocal
set JAR=libs\sqlite-jdbc-3.42.0.0.jar
if not exist out mkdir out
powershell -Command "javac -d out -cp '%JAR%' (Get-ChildItem -Recurse src -Filter *.java).FullName"
if %errorlevel% neq 0 pause & exit /b %errorlevel%
start "" java -cp "out;%JAR%" GUI.MainFrame
