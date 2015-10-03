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
    }

    /**
     * returns the oldest person; since the file cannot be empty, there is always one
     */
    def oldest: Option[Person] = throw new UnsupportedOperationException("not yet implemented")


    /**
     * How many days older is Bill than Paul?
     */
    def olderByDays: Option[Long] = throw new UnsupportedOperationException("not yet implemented")


}

