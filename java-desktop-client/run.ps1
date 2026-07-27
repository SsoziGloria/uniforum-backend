Write-Host "Cleaning old build files..." -ForegroundColor Cyan
Remove-Item -Recurse -Force out/* -ErrorAction SilentlyContinue

Write-Host "Compiling all Java source files into out..." -ForegroundColor Cyan
javac -d out @(Get-ChildItem -Path src -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful! Launching app..." -ForegroundColor Green
    java -cp out GeneralUser.views.WelcomeFrame
} else {
    Write-Host "Compilation failed. Please check the errors above." -ForegroundColor Red
}