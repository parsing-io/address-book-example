package addressbook

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.typesafe.config.{Config, ConfigFactory}

/**
  *
  */
object Util {

    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    def stringToDate(date: String): LocalDate = LocalDate.parse(date, dateTimeFormatter)
    def dateToString(date: LocalDate): String = dateTimeFormatter.format(date)

 }
