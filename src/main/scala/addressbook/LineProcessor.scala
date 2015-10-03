package addressbook

import java.time.temporal.ChronoUnit


/**
 *
 */
object LineProcessor {
    var malesCount = 0
    var oldestTmp: Option[Person] = None
    var bill: Option[Person] = None
    var paul: Option[Person] = None

    def processLine(line: String): Unit = {

        // parse - do not create Person instances to keep GC low
        val (name, sex, birth) = Parser.parse(line)

        // process
        if (sex == "Male") malesCount += 1
        if (oldestTmp.isDefined && oldestTmp.get.birth.compareTo(birth) > 0 || oldestTmp.isEmpty) oldestTmp = Some(Person(name, sex, birth))
        name match {
            case "Bill McKnight" => bill = Some(Person(name, sex, birth))
            case "Paul Robinson" => paul = Some(Person(name, sex, birth))
            case n =>
        }
    }

    /**
     * returns the oldest person; since the file cannot be empty, there is always one
     */
    def oldest: Option[Person] = oldestTmp


    /**
     * How many days older is Bill than Paul?
     */
    def olderByDays: Option[Long] =
        if (bill.isEmpty || paul.isEmpty) None
        else Some(ChronoUnit.DAYS.between(bill.get.birth, paul.get.birth))


}

