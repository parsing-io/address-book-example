package addressbook.fileprocessor

import java.io.{IOException, File, FileReader, BufferedReader}
import java.nio.charset.Charset
import java.nio.file.{Paths, Files}
import java.util

import addressbook.lineprocessor.ABLineProcessor

/**
 * Uses Java NIO to read the address book file.
 */
//noinspection ComparingUnrelatedTypes
class NIOFileProcessor(override val lineProcessor: ABLineProcessor, pathname: String, encoding: String)
        extends ABFileProcessor(lineProcessor: ABLineProcessor, pathname: String, encoding: String) {

    // using Java 1.1 BufferedReader
    override def processFile(): Unit = {
        // Java NIO:
        var bufferedReader: BufferedReader = null
        try {
            bufferedReader = Files.newBufferedReader(Paths.get(pathname), Charset.forName(encoding))
            var line = bufferedReader.readLine()
            while (line != null) {
                lineProcessor.processLine(line)
                line = bufferedReader.readLine()
            }
        }
        catch {
            case e: IOException => println(s"I/O exception = ${e.getMessage}")
            case x: Throwable => println(s"exception = ${x.getMessage}")
        }
        finally bufferedReader.close()
    }

    // using Java NIO and Java Streams; not used as it is slower
    def processFileNIO(): Unit = {
        var stream: java.util.stream.Stream[String] = null
        try {
            stream = Files.lines(Paths.get(pathname), Charset.forName(encoding))
            val iterator: util.Iterator[String] = stream.iterator()
            while (iterator.hasNext) lineProcessor.processLine(iterator.next())
        }
        catch {
            case e: IOException => println(s"I/O exception = ${e.getMessage}")
            case x: Throwable => println(s"exception = ${x.getMessage}")
        }
        finally stream.close()
    }

}

object NIOFileProcessor {
    def apply(lineProcessor: ABLineProcessor, pathname: String, encoding: String): NIOFileProcessor = new NIOFileProcessor(lineProcessor, pathname, encoding)
}
