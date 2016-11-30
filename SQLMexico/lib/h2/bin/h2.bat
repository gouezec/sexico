@Set PATH=C:\Users\to27287\eclipse\jre\bin;%PATH%
@java -cp "h2-1.4.193.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console %*
@if errorlevel 1 pause