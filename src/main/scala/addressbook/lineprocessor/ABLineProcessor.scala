package addressbook.lineprocessor

import java.time.temporal.ChronoUnit
import java.time.{ZoneId, LocalDate}
import java.util.{Calendar, Date, GregorianCalendar}

import addressbook.Util


/**
 * Processes a single line of the address book in one pass.
 * Exposes a field and methods to get results of file processing
 *
 */
abstract class ABLineProcessor(delimiter: String, delimiterRegex: String, datePattern: String, dateDelimiter: String) {

    // names of persons that will be tracked
    val billName = "Bill McKnight"
    val paulName = "Paul Robinson"
    
    // used both in the address book file and in the report
    val male = "Male"
    val female = "Female"
    // if the last two digit specification of birth ("XX") is in interval 00 to 15, use year 20XX, if not, use year 19XX
    // this is the threshold partitioning years into 19th and 20th century
    // the following constant is the highest supported year of the 20th century
    val centuryThreshold = 15
    val yearThreshold = 2000 + centuryThreshold

    // not encapsulated since very simple
    var malesCount = 0

    /**
     * processes the specified line
     * returns true if the line is valid and the validation feature has been switched on; otherwise, it returns false
     * validation is switched on by setting addressbook.lineprocessor.RegexLineProcessor#validate to true
     * optimized implementations of this class need not support the validation
     */
    def processLine(line: String): Boolean

    /**
     * returns the oldest person
     */
    def oldest: Option[Person]

    def olderByDays: Option[Long]

    case class Person(name: String, sex: String, birth: LocalDate)
    // tuple to Person conversion
    val createPersonTupled = (Person.apply _).tupled

    /**
     * Regex-based parsing.
     * It is used by RegexLineProcessor for all parsing.
     * It is used by LowLevelOptimizedLineProcessor to parse only the one line that is the result.
     */
    object Parser {
        val male = "Male"
        val calendar = new GregorianCalendar()

        def parse(line: String): (String, String, LocalDate) = {
            val tokens: Array[String] = line.split(delimiterRegex)
            val name = tokens(0)
            val sex = tokens(1)
            var birth = Util.stringToDate(tokens(2))
            if (birth.getYear > yearThreshold) {
                calendar.setTime(Date.from(birth.atStartOfDay(ZoneId.systemDefault()).toInstant))
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 100)
                birth = birth.withYear(calendar.get(Calendar.YEAR))
            }
            (name, sex, birth)
        }
    }

    // used by implementations of descendants of this class
    protected def olderByDaysImpl(bill: Option[Person], paul: Option[Person]): Option[Long] = {
        if (bill.isEmpty || paul.isEmpty) None
        else Some(ChronoUnit.DAYS.between(bill.get.birth, paul.get.birth))
    }

}
