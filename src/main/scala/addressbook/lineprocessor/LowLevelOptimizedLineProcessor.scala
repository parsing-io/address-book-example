package addressbook.lineprocessor

/**
 * Line is parsed using String.indexOf(Char) rather than regex.
 *
 * Avoids math ops in year interpretation by tracking separately century (19XX or 20XX) and the two last digits of the year (XX).
 *
 */
class LowLevelOptimizedLineProcessor(delimiter: String, delimiterRegex: String, datePattern: String, dateDelimiter: String)
        extends ABLineProcessor(delimiter: String, delimiterRegex: String, datePattern: String, dateDelimiter: String) {

    // male is identified just by its first character here
    val maleFirstChar = male.charAt(0)

    /**
     * for optimization only; true if at least one already processed person was year 1999 or older;
     * false otherwise
     */
    var oldest19XX = false

    /**
     * lowest year available is 1916 since "15" in address book file means 2015
     */
    var oldestYearTwoDig = centuryThreshold + 1
    // month of the oldest person (if already processing those 19XX); temporary value
    var oldestMonth = 13
    // day of the oldest person (if already processing those 19XX); temporary value
    var oldestDay = 32

    /**
     * as there is at least one line in the address book file, the oldest person will be always specified;
     * for optimization, entire line is stored for (temporarily) oldest person
     */
    var oldestLine = ""
    var bill: Option[Person] = None
    var paul: Option[Person] = None

    // these are the only two names we are interested in
    val lengths = Set(billName.length, paulName.length)

    override def processLine(line: String): Boolean = {

        // fast detection of name and year
        val indexFirstDelim = line.indexOf(delimiter)
        val name = line.substring(0, indexFirstDelim)
        if (line.charAt(indexFirstDelim + 2) == maleFirstChar) malesCount += 1
        val indexYear = line.lastIndexOf(dateDelimiter)
        val yearTwoDig = line.substring(indexYear + 1, indexYear + 3).toInt
        if (yearTwoDig > centuryThreshold)

        // crossing the line between "young" and "old"
        if (!oldest19XX) {
            oldest19XX = true; oldestYearTwoDig = yearTwoDig; oldestLine = line
        }

        // finding an (even) older 19XX one
        else if (oldest19XX) {
            val month = line.substring(indexYear - 2, indexYear).toInt
            val day = line.substring(indexYear - 5, indexYear - 3).toInt
            if (yearTwoDig < oldestYearTwoDig || yearTwoDig == oldestYearTwoDig && month < oldestMonth ||
                    yearTwoDig == oldestYearTwoDig && month == oldestMonth && day < oldestDay) {
                    oldestYearTwoDig = yearTwoDig; oldestMonth = month; oldestDay = day; oldestLine = line
            }
        }

        if (lengths.contains(name.length)) // optimization only
            name match {
                // both these assignments happen only once during file processing
                case `billName` => bill = Some(createPersonTupled(Parser.parse(line)))
                case `paulName` => paul = Some(createPersonTupled(Parser.parse(line)))
                case n =>
            }
        // log.debug: println(s"line = $line, year = $yearTwoDig, oldestLine = $oldestLine, oldest19XX = $oldest19XX, oldestTwoDig = $oldestTwoDig")
        false // validation not supported
    }

    /**
     * returns the oldest person
     */
    override def oldest: Option[Person] = {
        if (oldestLine.isEmpty) None
        else {
            val (name, sex, birth) = Parser.parse(oldestLine)
            Some(Person(name, sex, birth))
        }
    }

    override def olderByDays: Option[Long] = olderByDaysImpl(bill, paul)
}

object LowLevelOptimizedLineProcessor {
    def apply(delimiter: String, delimiterRegex: String, datePattern: String, dateDelimiter: String) =
        new LowLevelOptimizedLineProcessor(delimiter, delimiterRegex, datePattern, dateDelimiter)
}
