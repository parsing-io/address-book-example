package addressbook.lineprocessor

import java.util.regex.{Matcher, Pattern}

import addressbook.Util

/**
 * Line is parsed using String.split(regex)
 *
 */
class RegexLineProcessor(validate: Boolean, linePattern: String, delimiter: String, delimiterRegex: String, datePattern: String, dateDelimiter: String)
        extends ABLineProcessor(delimiter: String, delimiterRegex: String, datePattern: String, dateDelimiter: String) {

    var oldestTmp: Option[Person] = None
    var bill: Option[Person] = None
    var paul: Option[Person] = None

    /**
     * returns true if the validation is switched on and the line is valid
     */
    override def processLine(line: String): Boolean = {

        // validate if requested
        var isValid = true
        if (validate) {
            isValid = RegexValidator.isValid(line)
            if (!isValid) println(s"invalid line: will be ignored; line = $line")
        }
        if (!validate || isValid) {

            // parse - do not create Person instances to keep GC low
            val (name, sex, birth) = Parser.parse(line)

            // process
            if (sex == Parser.male) malesCount += 1
            
            if (oldestTmp.isDefined && oldestTmp.get.birth.compareTo(birth) > 0 || oldestTmp.isEmpty) oldestTmp = Some(Person(name, sex, birth))
            name match {
                case `billName` => bill = Some(Person(name, sex, birth))
                case `paulName` => paul = Some(Person(name, sex, birth))
                case n =>
            }
        }
        validate && isValid
    }

    /**
     * returns the oldest person; since the file cannot be empty, there is always one
     */
    override def oldest: Option[Person] = oldestTmp

    override def olderByDays: Option[Long] = olderByDaysImpl(bill, paul)

    object RegexValidator {

        val linePatternP = Pattern.compile(linePattern)

        def isValid(line: String): Boolean = {
            val matcher: Matcher = linePatternP.matcher(line)
            if (!matcher.matches()) return false
            val group: String = matcher.group(1)
            try {
                Util.stringToDate(group)
                return true
            }
            catch {
                case e: Throwable => println(s"invalid date: ${e.getMessage}")
            }
            false
        }
    }
}

object RegexLineProcessor {
    def apply(validate: Boolean, linePattern: String, delimiter: String, delimiterRegex: String, datePattern: String, dateDelimiter: String) =
        new RegexLineProcessor(validate, linePattern, delimiter, delimiterRegex, datePattern, dateDelimiter)
}

