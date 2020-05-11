#!/bin/bash
echo Paper submission System runner
if [[ ! -f app.props ]]
then
  echo "Initial run"
  read -p "Please enter a hostname for the database [localhost]" $host
  if [ -z "$host" ]
  then
    host="localhost"
  fi
  read -p "Please enter a username for the database [student]" $user
  if [ -z "$user" ]
  then
    user="student"
  fi
  read -p "Please enter a password for the database [student]" $pswd
  if [ -z "$pswd" ]
  then
    pswd="student"
  fi
  echo "Re-importing the database dump to MySQL"
  mysql --user="$user" --password="$pswd" --host="$host" --execute="DROP DATABASE IF EXISTS SCM; SOURCE csm_2020-03-20.sql;"
  if [ $? -ne 0 ]
  then
    echo "Something went wrong while attempting to Import the database dump. Please execute the following command and re-run this script."
    echo mysql --user="$user" --password="$pswd" --host="$host" --execute="DROP DATABASE IF EXISTS SCM; SOURCE csm_2020-03-20.sql;"
  fi
  echo "Creating the DB config..."
  echo db.password=$pswd > app.props
  echo db.user=$user >> app.props
  echo db.name=CSM >> app.props
  echo db.host=$host >> app.props
fi
echo "Compiling..."
if [[ ! -d build ]]
then
  mkdir build
fi
javac -cp "./libs/mysql-connector-java-8.0.12.jar;./libs/activation.jar;./libs/javax.mail.jar;./libs/javax.json-api-1.1.4.jar;./libs/javax.json-1.1.jar;build" -d build *.java
echo "Complete!"
if [[ -f error.log ]]
then
  echo "Clearing log"
  rm error.log
fi
echo "Starting the System..."
java -cp "./libs/mysql-connector-java-8.0.12.jar;./libs/activation.jar;./libs/javax.mail.jar;./libs/javax.json-api-1.1.4.jar;./libs/javax.json-1.1.jar;build" Main
rm build/*.class