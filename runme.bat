@echo off
echo "Compiling..."
mkdir build
javac -cp "./libs/mysql-connector-java-8.0.12.jar;./libs/activation.jar;./libs/javax.mail.jar;./libs/javax.json-api-1.1.4.jar;./libs/javax.json-1.1.jar;build" -d build *.java
echo "Complete!"
echo "Clearing log"
del error.log
echo "Starting the System..."
java -cp "./libs/mysql-connector-java-8.0.12.jar;./libs/activation.jar;./libs/javax.mail.jar;./libs/javax.json-api-1.1.4.jar;./libs/javax.json-1.1.jar;build" Main
del "build/*.class"
pause