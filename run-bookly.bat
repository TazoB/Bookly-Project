@echo off
cd /d "%~dp0"
java --module-path "javafx\javafx-sdk-21.0.6\lib" --add-modules javafx.controls,javafx.fxml -jar out\artifacts\Bookly_jar\Bookly.jar
pause