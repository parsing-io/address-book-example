package addressbook.fileprocessor

import addressbook.lineprocessor.ABLineProcessor

/**
 * Processes the address book file.
 *
 *
 */
abstract class ABFileProcessor(val lineProcessor: ABLineProcessor, pathname: String, encoding: String) {

    /**
     * Opens the address book file, reads it line by line and closes it.
     */
    def processFile()

    /**
     * Report results of the processing
     */
    def report(): Unit = {
        val malesCount = lineProcessor.malesCount
        val oldest = lineProcessor.oldest
        val olderByDays = lineProcessor.olderByDays
        println(s"number of men in the address book = $malesCount")
        if (oldest.isEmpty) println(s"the address book file was empty")
        else println(s"the oldest person = ${oldest.get.name}")
        if (olderByDays.isEmpty) println(s"Bill and/or Paul were not found in the address book")
        else println(s"how many days is Bill older than Paul = ${olderByDays.get}")
    }

}
