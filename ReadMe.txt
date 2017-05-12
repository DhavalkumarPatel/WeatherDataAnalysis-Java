step 1: On unzipping my solution you will get following deliverables in one folder:
	1. Source Code - Java Maven Project source code along with Makefile to run (see step 2 for running the code)
	2. Report - pdf document as required
	3. AWS Log Files - controller and sys log files
	4. AWS Result Files - successfull output result files

Step 2: Run Weather Data Source Code (location:: 1. Source Code/HW1Part1)

2A-Using Makefile given:
	- Copy the input file into input folder (HW1Part1/input)
	- Edit the file path in Makefile's (HW1Part1/Makefile) local.filepath variable 
	- You can change two other variables as per your need in Makefile
		- local.fibonacciFlag - To enable or disable Fibonacci delay
		- local.verifyAverageResultsFlag - To enable or disable the code for comparing the average results of different approaches
	- Save the Makefile
	- Open terminal and cd to this HW1Part1 directory
	- run "make alone" command
	- This will execute all the five programs and print their respective performance statatics in console.


2B-Without Makefile
	- As this is the first time I have used Makefile it might cause issuess so for the safe side this is the other way of running the program.
	- cd to HW1Part1/src/main/java
	- run javac HW1Part1/*.java
	- run java HW1Part1.Main ${local.filepath} ${local.fibonacciFlag} ${local.verifyAverageResultsFlag}
	- e.g. java HW1Part1.Main input/1912.csv N N 
	- This will execute all the five programs and print their respective performance statatics in console.



