@rem
@rem Gradle start up script for Windows. Requires gradle-wrapper.jar in
@rem gradle\wrapper\. If missing, generate it once with:
@rem     gradle wrapper --gradle-version 8.7
@rem

@if "%DEBUG%" == "" @echo off
setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_HOME=%DIRNAME%

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

if defined JAVA_HOME (
    set JAVA_EXE=%JAVA_HOME%/bin/java.exe
) else (
    set JAVA_EXE=java
)

"%JAVA_EXE%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

endlocal
