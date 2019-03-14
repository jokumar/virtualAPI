cd %~dp0
for /f "tokens=5" %%a in ('netstat -aon ^| find ":9001" ^| find "LISTENING"') do taskkill /f /pid %%a
mvn spring-boot:run