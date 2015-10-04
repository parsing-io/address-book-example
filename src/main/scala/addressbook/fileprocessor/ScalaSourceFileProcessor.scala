package addressbook.fileprocessor

import addressbook.lineprocessor.ABLineProcessor

import scala.io.Source

/**
 * Uses scala.io.Source to read the Address Book file.
 */
class ScalaSourceFileProcessor(override val lineProcessor: ABLineProcessor, pathname: String, encoding: String)
        extends ABFileProcessor(lineProcessor: ABLineProcessor, pathname: String, encoding: String) {

    override def processFile(): Unit = {
        val source = Source.fromFile(pathname, encoding)
        for (line <- source.getLines()) lineProcessor.processLine(line)
        source.close()
    }

}

object ScalaSourceFileProcessor {
    def apply(lineProcessor: ABLineProcessor, pathname: String, encoding: String): ScalaSourceFileProcessor = new ScalaSourceFileProcessor(lineProcessor, pathname, encoding)
}
