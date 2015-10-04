package addressbook.fileprocessor

import addressbook.lineprocessor.ABLineProcessor

/**
 * For unit testing
 */
class InMemoryFileProcessor(override val lineProcessor: ABLineProcessor, pathname: String, encoding: String, lines: List[String])
        extends ABFileProcessor(lineProcessor: ABLineProcessor, pathname: String, encoding: String) {

    override def processFile(): Unit = lines.foreach(lineProcessor.processLine)
}

object InMemoryFileProcessor {
    def apply(lineProcessor: ABLineProcessor, pathname: String, encoding: String, lines: List[String]): InMemoryFileProcessor = new InMemoryFileProcessor(lineProcessor, pathname, encoding, lines)
}

