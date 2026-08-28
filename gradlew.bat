@echo off
REM KAIVO note: see the comment block at the top of ./gradlew (the Unix
REM script) for why this forwards to a system-installed `gradle` instead of
REM being the official self-bootstrapping wrapper batch file.

where gradle >nul 2>nul
if %errorlevel% neq 0 (
  echo error: 'gradle' was not found on PATH.
  echo Install Gradle first, or run this in a CI job that installs it for you.
  exit /b 1
)

gradle %*
