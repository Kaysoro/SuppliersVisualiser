call mvn clean compile
robocopy "src/main/java/com/steven/pescheteau/images" "target/classes/com/steven/pescheteau/images" /s /e
call mvn assembly:single
pause