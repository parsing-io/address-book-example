package addressbook

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.typesafe.config.{Config, ConfigFactory}

/**
 * Utils for configuration and type conversion.
 */
object Util {

    val conf: Config = ConfigFactory.load()

    // date and time
    val dateTimeFormatter = DateTimeFormatter.ofPattern(conf.getString("exercise.parsing.datePattern"))
    def stringToDate(date: String): LocalDate = LocalDate.parse(date, dateTimeFormatter)
    def dateToString(date: LocalDate): String = dateTimeFormatter.format(date)

}
