jar.name=HW1Part1-1.0.jar
jar.path=target/${jar.name}
local.input=input
local.output=output
local.log=log

# input file
local.filepath=input/1912.csv

# fibonacci flag? Y or N
local.fibonacciFlag=Y

# Do you want to compare the average results of different program versions? Y or N
local.verifyAverageResultsFlag=N

###################################################33

# Compiles code and builds jar (with dependencies).
jar:
	mvn clean package

alone: jar 
	java -jar ${jar.path} ${local.filepath} ${local.fibonacciFlag} ${local.verifyAverageResultsFlag}
