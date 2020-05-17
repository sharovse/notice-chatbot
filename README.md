# telegramExample
Example for Telegram

Run maven:
    mvn spring-boot:run

Run java:

    Собрать maven jar
	mvn clean package -DskipTests
    Запуск:
	Скопируй файл со свойствами в target/
	    cp src/main/resources/application.properties target/application.properties
	cd target/
	    java -jar notice-chatbot-0.0.1-SNAPSHOT.jar 


