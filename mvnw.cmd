@echo off
setlocal enabledelayedexpansion

set "MAVEN_PROJECTBASEDIR=%~dp0"
set "WRAPPER_DIR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper"
set "WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar"
set "WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

if not exist "%WRAPPER_JAR%" (
  if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
  echo Baixando Maven Wrapper...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%'"
  if errorlevel 1 (
    echo Falha ao baixar o Maven Wrapper. Verifique sua internet e tente novamente.
    exit /b 1
  )
)

where java >nul 2>nul
if errorlevel 1 (
  echo Java nao encontrado. Instale um JDK e configure o PATH/JAVA_HOME.
  exit /b 1
)

java -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -jar "%WRAPPER_JAR%" %*
