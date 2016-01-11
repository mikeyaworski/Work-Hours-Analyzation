# Work-Hours-Analyzation

Short Description:

This project produces aggregate data for a list of work hours (typically part-time jobs). Create a text file according to the format shown below, and then add all of your shifts to this text file.

--------------------------------------------------

Data Output:

You're going to get total information for individual jobs and their combination.
You're going to get yearly information for individual jobs and their combination.

The information consist of:
- first and last dates
- total hours
- average hours per week
- average hours per shift

A specific list is here:
- total hours all time
- total hours for each job
- total hours for each year
- total hours for each different job each year
- average hours per week all time
- average hours per week for each job
- average hours per week each year
- average hours per week each year for each job
- first and last date worked
- first and last date worked for each year
- first and last date worked for each job
- average hours per shift, all time
- average hours per shift, for each job
- average hours per shift, for each year
- average hours per shift, for each job each year

--------------------------------------------------

How to Format:

Example:

Job 1:
-Jan 1, 2015; 4pm-8pm; 4h
-Jan.2/15; 12-6; 0.5h break; 3.5h

Job 2:
-Jan. 3,15; 5.25

Explanation:
- Make sure you have a job name before you list the shifts. The job name cannot have a hypen in front of it.
- The job name must be on one line. If you have a line break, it will consider that as two separate jobs.
- Make sure that you do have a hypen before a shift. Otherwise, it is considered a job name.
- The content of the shift just needs to have semi-colon separated pieces and have at least two pieces (at least one semi-colon).
- The first piece of the shift needs to be the date. The date can be in any of the formats shown in the example. You may specify the year as a two digit number (e.g. 15) and it will be parsed as 2000 + that number (e.g. 2015).
- Currently, dates are not supported in the form year/month/day or anything like that. You must have it in a similar format to the examples. This is to avoid confusion in how to parse that format since it is ambiguous.
- The last piece of the shift needs to be the length of the shift (in hours). It may or may not have the "h" on the end. It is only accepted as a real number, optionally followed by an "h".
- Note: As an example, the length the shift does NOT consider 3.15 as "3 hours and 15 minutes". It considers it as "3 hours and 0.15 hours" or "3 hours and 9 minutes". If you want "3 hours and 15 minutes", you have to use 3.25.
- The pieces in between the first and last piece are not parsed. They are optional and can contain whatever content you want.

--------------------------------------------------

How to Use the App:

You can open the project files in a version of your own Eclipse if you would like. Otherwise, there is an Executable JAR File located in the root directory of the project.

This program may contain up to two optional arguments. The first argument will be the input file to be analyzed. The second argument will be the output file to output the analysis to.
- You may enter an input file (one argument) without an output file.
- You may not specify an output file unless you have also specified an input file. This is because if you only enter one argument, that will be considered the input file.
- The files being entered are paths to files on your computer.
- If no arguments are given, the default input file location is a file called "work_hours.txt" in the same directory as this project or JAR file.
- The output file must already exist (it will not create one for you).
- The output of the analysis will completely OVERWRITE the file, not append to it.
- When the program writes the analysis to the file, it will also write it to the console. If it cannot access the output file for some reason, it will still print to the console.

How to specifically use the JAR file:
Arguments are separated by whitespace.
Make sure you have Java linked in your PATH variable.
If that is the case, you make execute the JAR file in your command line (e.g. Windows Command Prompt) with the following example commands:

No arguments:
java -jar workhoursanalyzation.jar

Input file argument:
java -jar workhoursanalyzation.jar C:\MyPath\work_hours_input.txt

Output file argument:
java -jar workhoursanalyzation.jar C:\MyPath\work_hours_input.txt C:\MyPath\work_hours_output.txt

Output file argument with the input file remaining in the root directory (default):
java -jar workhoursanalyzation.jar work_hours.txt C:\MyPath\work_hours_output.txt