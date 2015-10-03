package addressbook

import java.time.{ZoneId, LocalDate}
import java.util.{Calendar, Date, GregorianCalendar}

/**
 *
 */
    object Parser {
    
        val calendar = new GregorianCalendar()

        def parse(line: String): (String, String, LocalDate) = {
            val tokens: Array[String] = line.split(",\\s")
            val name = tokens(0)
            val sex = tokens(1)
            var birth = Util.stringToDate(tokens(2))
            if (birth.getYear > 2015) {
                calendar.setTime(Date.from(birth.atStartOfDay(ZoneId.systemDefault()).toInstant))
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 100)
                birth = birth.withYear(calendar.get(Calendar.YEAR))
            }
            (name, sex, birth)
        }
    }
