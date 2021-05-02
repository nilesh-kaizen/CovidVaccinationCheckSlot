set districtID=392
set ageLimit=18
set emailId=xxx@gmail.com
set password=password
set toEmailId=xxx@gmail.com,xxx@gmail.com

javac -cp activation.jar;json-simple-1.1.1.jar;mail.jar CheckSlot.java 
jar cfve CheckSlot.jar CheckSlot CheckSlot*.class
java -cp CheckSlot.jar;activation.jar;json-simple-1.1.1.jar;mail.jar CheckSlot %districtID% %ageLimit% %emailId% %password% %toEmailId%


