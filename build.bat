@REM - - - - - - - -
set TOMCAT_HOME=C:\Tomcat
set WEBAPP_NAME=hey
@REM - - - - - - - -
mkdir target\WEB-INF\classes
mkdir target\WEB-INF\lib
cd src
javac -cp .;%TOMCAT_HOME%\lib\*;..\WebContent\WEB-INF\lib\* -d ..\target\WEB-INF\classes hey\action\*.java
javac -cp .;%TOMCAT_HOME%\lib\*;..\WebContent\WEB-INF\lib\* -d ..\target\WEB-INF\classes hey\model\*.java
cd ..
xcopy /s /q /y WebContent\* target
xcopy /s /q /y src\*.* target\WEB-INF\classes
xcopy /s /q /y src\hey target\WEB-INF\classes
xcopy /s /q /y src\rmiserver target\WEB-INF\classes
cd target
jar cf ..\%WEBAPP_NAME%.war *
cd ..
@REM deploy the 'hey.war' archive into Tomcat's webapps:
@REM xcopy /s /q /y  %WEBAPP_NAME%.war %TOMCAT_HOME%\webapps