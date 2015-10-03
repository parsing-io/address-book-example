# parsing and I/O exercise

--------------------------------------------------------------------------------
1. Are the dates using Gregorian calendar?
The exercise does not get into implementation details. Dates are currently in a text file and as such it is up to the candidate to handle them in the most appropriate way.
2. May I use the system default timezone?
Yes
3. May I assume that the year values in interval 00 to 15 should be interpreted as 20XX and the others as 19XX?
Yes
4. Is the above the only usage of the application or is it likely that its usage will grow
        in variability and/or complexity?
        In technical terms: Is it OK to hardcode these 3 questions in the application or
        or should there be a kind of parametrizable DSL to formulate questions at runtime
        or should the application make use of a information retrieval library that will simplify future
        modifications/extensions of the questions? (However, the information retrieval library
        would hit the performance by creating first an index.)
Please make some assumptions (thinking of real production applications) and document them in the README.
5. Is this always the case that the address book is stored in just one file or is it
        stored in multiple files on one machine or is it distributed?
Same answer as Q4
6. Should the application recover from being interrupted/killed?
Same answer as Q4
7. Should the application indicate its current progress during address book processing?
Same answer as Q4
8. Is the input file strictly adhering to the above syntax or might there be variations in delimiting items,
        labeling male/female, leading zeroes in dates etc.? Are all whitespace characters spaces?
Same answer as Q4
9. May I use Scala and sbt?
Yes
--------------------------------------------------------------------------------

if the last two digit specification of birth ("XX") is in interval 00 to 15, use year 20XX, if not, use year 19XX
this is the threshold partitioning years into 19th and 20th century
the following constant is the highest supported year of the 20th century

