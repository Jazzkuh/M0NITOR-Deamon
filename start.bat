@echo off

REM Batch script to run the Deamon Java application



SET JAVA_MEM_MIN=512M

SET JAVA_MEM_MAX=512M

SET JAR_PATH=C:\Users\Jazz\IdeaProjects\M0NITOR-Deamon\target\Deamon-1.0-SNAPSHOT-shaded.jar



REM Check if the JAR file exists

IF NOT EXIST "%JAR_PATH%" (

    echo ERROR: JAR file not found at the specified path: %JAR_PATH%

    exit /b 1

)

REM Run the Java application

echo Starting Java application with minimum heap size %JAVA_MEM_MIN% and maximum heap size %JAVA_MEM_MAX%...

java -Xms%JAVA_MEM_MIN% -Xmx%JAVA_MEM_MAX% -jar "%JAR_PATH%"



REM Check if the Java command was successful

IF ERRORLEVEL 1 (

    echo ERROR: Failed to start the Java application. Please check the logs for more details.

    exit /b 1

)



echo Java application terminated successfully.