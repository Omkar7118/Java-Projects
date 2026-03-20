@echo off
rem ═══════════════════════════════════════════════
rem  Footwear Shop Management System – Build & Run
rem  Requires: JDK 21+  (javac + java on PATH)
rem ═══════════════════════════════════════════════
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

echo ============================================
echo   Footwear Shop – Build and Launch
echo ============================================

rem ── 1. Check javac ──
where javac >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] javac not found. Install JDK 21+ and add to PATH.
    pause
    exit /b 1
)
echo [OK] Java compiler found.

rem ── 2. Download SQLite JDBC if missing ──
set LIB=sqlite-jdbc-3.45.1.0.jar
if not exist "%LIB%" (
    echo [downloading] SQLite JDBC…
    powershell -Command "Invoke-WebRequest -Uri https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar -OutFile %LIB%"
    if not exist "%LIB%" (
        echo [ERROR] Download failed. Place %LIB% in this folder manually.
        pause
        exit /b 1
    )
    echo [OK] SQLite JDBC downloaded.
) else (
    echo [OK] SQLite JDBC already present.
)

rem ── 3. Compile ──
if not exist out mkdir out
echo [compiling] …

rem Collect all .java files
set SOURCES=
for /r src\main\java %%f in (*.java) do (
    set SOURCES=!SOURCES! "%%f"
)

javac -cp %LIB% -d out !SOURCES!
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b 1
)
echo [OK] Compilation successful.

rem ── 4. Run ──
echo [launching] Footwear Shop…
java -cp out;%LIB% shop.App

echo [OK] Application closed.
pause
