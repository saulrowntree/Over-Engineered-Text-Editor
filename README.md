# QUBEditron

This project was completed as part of a university assignment.
Consists of a number of microservices, each acting alongside eachother to provide a simple text editing and metric gathering tool.
Mainly exists as a demonstration of proficiency in development across a range of languages as well as an understanding of cloud computing practises, much of the code contained within has not been optimised and some areas, mainly the front end, are not an accurate reflection of my JS ability. 

Each services' tests can be ran with the .gitlab-ci.yml scripts written within them, or with the actual tests files. 

The Character Count Service (editor-charcount) is written in JavaScript and testd with Chai. 

The Comma Count Service (editor-commacount) is written in Go and tested with Testing and net/http/httptest. 

The Palindrome Count Service (editor-palindromecount) is written in Python with Flask and tested with unittest. 

The Spelling Error Count Service (editor-spellingerrors) is written in Java with Helidon MP and tested with JUnit5 and the Helidon JUnit extension, HelidonTest. 

The Vowel Count Service (editor-vowelcount) is written in Ruby with Sinatra and tested with test/unit and rack/test. 

The Word Count Service (editor-wordcount) is written in PHP and tested using raw PHP.

The front end of the application is written in JavaScript and static HTML.

The Database Service (editor-database), Monitor Service (editor-monitor), Reverse Proxy Service (editor-reverseproxy) and the Service Registry Service (editor-serviceregistry) are all written in Java with Helidon MP and testing accordingly with JUnit5 and HelidonTest. The database uses SQLite for persistent storage. 

Each of these services are containerised with Docker and a DockerFile exists within each service. 
