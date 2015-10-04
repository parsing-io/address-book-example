# Requirements

The requirement is to build a simple tool for processing Address Book.
There are 3 pieces of information to get from the Address Book, but other might probably follow:
- How many males are in the address book?
- Who is the oldest person in the address book?
- How many days older is Bill than Paul?

Each line entry of the Address Book file contains the following tokens:
- name - contained from letters and '-' and ''' (quotation mark)
- sex - either "Male" or "Female"
- birth date - date (using Gregorian calender) in format DD/MM/YY where YY from
        the range 00 to 15 denotes birth dates 2000 - 2015
        and the range between 16 and 99 birth dates 1916 - 1999.
        The system default timezone is used.

Tokens are delimited by ', ' (comma and space).
Platform line separator is used.

The example of such Address Book file is as follows:

    Bill McKnight, Male, 16/03/77
    Paul Robinson, Male, 15/01/85
    Gemma Lane, Female, 20/11/91
    Sarah Stone, Female, 20/09/80
    Wes Jackson, Male, 14/08/74

As last requirement, the solution should be capable of processing large files with a small memory footprint.

# Solution
The solution is a small tool executed from the shell command line or within JVM via its simple API.
It processes an address book file. 

To support its performance testing, it can also produce a large address book file for testing. 
This generated file contains all persons that are part of the Gumtree address book file 
to enable functional testing as well.

The tool has one parameter: the pathname of the file to process/generate.
Other parameters are externalized to a configuration file named "application.conf".

## Introduction

The solution includes several alternative implementations to fit the context in which it is used.

The context might be:

**1. the address book data strictly adhere to the above rules**

There is no need to validate the data before processing. LowLevelOptimizedLineProcessor can be
used as it is fast.

**2. the data break these rules**

Validation is needed. RegexLineProcessor supports validation based on line regex pattern and
validation of birth dates.

**3. there are frequent changes to the data format and/or to the information to get from data**

RegexLineProcessor is easier to modify than LowLevelOptimizedLineProcessor. RegexLineProcessor
also uses higher abstraction and it's implementation is much simpler.

**4. the address book processing should be fast**

Then LowLevelOptimizedLineProcessor is the choice. 
For reading the address book file, either NIOFileProcessor or ScalaSourceFileProcessor 
can be used. ScalaSourceFileProcessor is slightly faster. 
The variant of NIOFileProcessor that uses directly Java1.1 BufferedReader
is cca 1,76 faster than reading lines using Java NIO Path and Java Streams (tested on reading 
1,000,000 lines in 1,000 loops). The variant that uses Java NIO Path and Java Streams
is showed in addressbook.fileprocessor.NIOFileProcessor#processFileNIO.
Changing the size of the buffer might also improve the performance with certain address book 
file sizes. The tool uses the default of 8192 bytes.

**5. Scala I/O API is preferred to Java NIO API or vice versa**

ScalaSourceFileProcessor or NIOFileProcessor can be used to interoperate in future 
with newly added code.

Which of these classes (LowLevelOptimizedLineProcessor, RegexLineProcessor, ScalaSourceFileProcessor
and NIOFileProcessor) should be used is passed to the tool as its parameters.

Note: as the tool is trivial, package names are omitted in the text.

## Design
As indicated above, the implementation is done at two levels. The first level is that of the address book file,
the second level of individual lines of the address book.

These two levels are modelled by two abstract classes, ABFileProcessor and ABLineProcessor. 

ABFileProcessor is stateless. Its implementations are ScalaSourceFileProcessor, which uses scala.io.Source, and
NIOFileProcessor, which uses Java NIO and Java Streams (or, alternatively, directly Java 1.1 BufferedReader).
(There is only an implementation InMemoryFileProcessor for unit tests).

ABLineProcessor is stateful. It exposes the API for getting results of the processing (the oldest person etc.).
It also defines the Person case class (to work with a contact using a model, whenever it is possible from
performance perspective) and it includes a Parser which is used by both its implementations.
RegexLineProcessor uses the Parser to parse all address book's files, whereas LowLevelOptimizedLineProcessor
uses it to create Person instances only for the results of the processing (oldest etc.).
RegexLineProcessor uses Java regular expressions to validate and to parse the line.

LowLevelOptimizedLineProcessor scans the lines for a few sequence of characters and avoids
math operations (because of different interpretation of years 00 - 15 in the birth date and of years 16 - 99)
by using flags and detecting when the processor first met a person born in the 19th (rather than 20th) century.

## Implementation
The tool is implemented in Scala. A comparison has been made between performance of Scala
and Java 8 implementation showing that the difference was within 1,25%, Scala being usually faster.

For date operations, Java 8 (successor of JodaTime) is used.

For string comparisons (when identifying certain person), string lengths are compared before the string
contents which is slightly faster than doing the pattern match immediately.

If it were more than a tool, e.g. enumeration can be used to model person's sex, logging library rather then
printing to the standard output etc.

Tests are using Spec from ScalaTest.

## Usage

Jar with dependencies is pre-built in the assembly/ directory of this project.

The tool can be launched:

    > java -cp path/to/address-book-example-assembly-0.1.jar addressbook.AddressBook path/to/data/generated.csv

Note that in the data/ directory of this project, there is a generated address book file of 100.000 persons.

The tool uses Scala 2.11.7 and has been tested on jdk1.8.0_40.
    
### Building the tool

The tool with its dependencies can be assembled as one jar using:

    > sbt assembly

### Processing a file

The only command line parameter passed to the tool is the aboslute pathname of the file to be processed.
Example of "application.conf" file for optimized processing of an address book stored as UTF-8 text file:

    exercise {
      parsing {
        action = "process"
        generate-number-of-lines = 1
        loop-count = 1
        name-length-minimal = 7
        file-processor = "scala-source"
        line-processor = "low-level-optimized"
        encoding = "UTF-8"
        linePattern = "^[-\\w' ]+,\\s(?:Male|Female),\\s(\\d{2}/\\d{2}/\\d{2})$"
        delimiterRegex = ",\\s"
        delimiter = ", "
        datePattern = "dd/MM/yy"
        dateDelimiter = "/"
      }
    }
    
### Generating a file

Example of "application.conf" when generating a test address book file of 100000 lines:

    exercise {
      parsing {
        action = "generate"
        generate-number-of-lines = 100000
        loop-count = 1000
        name-length-minimal = 7
        file-processor = "scala-source"
        line-processor = "low-level-optimized"
        encoding = "UTF-8"
        linePattern = "^[-\\w' ]+,\\s(?:Male|Female),\\s(\\d{2}/\\d{2}/\\d{2})$"
        delimiterRegex = ",\\s"
        delimiter = ", "
        datePattern = "dd/MM/yy"
        dateDelimiter = "/"
      }
    }
    
5 persons from Gumtree file are spread randomly across the generated file.

Variability for length of first name and surname is 6 and 8 respectively. (This is not configurable so far).
The configuration file application.conf contains information on its configuration keys as part of the file.

## Performance and memory footprint

Memory footprint: address book data are processed line by line. For this reason (and because the Address
Book requirements are quite modest) no information retrieval library (such as Lucene) has been used.

Memory usage is printed before the processing finishes. It remains low even with 100000+ lines long files.
Performance: As it is an exercise, performance of individual implementations has been compared. Processing
one address book of 100,000 lines, using -Xmx1G, took the following:
- nio, regex-validate
    - 382.355 ms
- scala-source, regex-validate
    - 362.356 ms
- scala-source, regex
    - 287.542 ms
- nio, regex
    - 230.188 ms
- nio, low-level-optimized
    -  25.540 ms
- scala-source, low-level-optimized
    -  23.723 ms
    
Note that processing has been always done in 1000 loops; however, the above is duration of 1 loop.
