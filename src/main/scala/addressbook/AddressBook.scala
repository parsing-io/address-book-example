package addressbook

import java.nio.file.{Paths, Files}
import java.util.Date
import addressbook.fileprocessor.{ABFileProcessor, NIOFileProcessor, ScalaSourceFileProcessor}
import addressbook.generator.Generator
import addressbook.lineprocessor.{ABLineProcessor, LowLevelOptimizedLineProcessor, RegexLineProcessor}

/**
 * Processes Address Book files and generates them.
 */
object AddressBook {

    def main(args: Array[String]): Unit = {
        val usage: String = """Usage: <pathname>
           pathname ... the address book file to be processed or generated """
        if (args.length < 1) {
            println(usage)
            System.exit(1)
        }
        val pathname = args(0)
        val action = Util.conf.getString("exercise.parsing.action")
        action match {
            case "generate" =>
                generate(pathname)
            case "process" =>
                process(pathname)
            case _ => println(usage)
        }

    }

    private def generate(pathname: String): Unit = {
        val linesCount = Util.conf.getInt("exercise.parsing.generate-number-of-lines")
        val nameLengthMinimal = Util.conf.getInt("exercise.parsing.name-length-minimal")
        println(s"generate: pathname = '$pathname', linesCount = '$linesCount', nameLengthMinimal = '$nameLengthMinimal'")
        Generator.generate(pathname, linesCount, nameLengthMinimal)
    }

    private def process(pathname: String): Unit = {

        // get parameters
        if (!Files.exists(Paths.get(pathname))) {println(s"file not found; exiting"); System.exit(1)}
        val fileProcessorStr = Util.conf.getString("exercise.parsing.file-processor")
        val lineProcessorStr = Util.conf.getString("exercise.parsing.line-processor")
        val encoding = Util.conf.getString("exercise.parsing.encoding")
        val linePattern = Util.conf.getString("exercise.parsing.linePattern")
        val delimiter = Util.conf.getString("exercise.parsing.delimiter")
        val delimiterRegex = Util.conf.getString("exercise.parsing.delimiterRegex")
        val datePattern = Util.conf.getString("exercise.parsing.datePattern")
        val dateDelimiter = Util.conf.getString("exercise.parsing.dateDelimiter")
        var fileProcessor: Option[ABFileProcessor] = None
        var lineProcessor: Option[ABLineProcessor] = None
        val validate = lineProcessorStr.endsWith("-validate")
        lineProcessorStr match {
            case "regex" | "regex-validate" => lineProcessor = Some(new RegexLineProcessor(validate = validate, linePattern, delimiter, delimiterRegex, datePattern, dateDelimiter))
            case "low-level-optimized" => lineProcessor = Some(new LowLevelOptimizedLineProcessor(delimiter, delimiterRegex, datePattern, dateDelimiter))
            case s => println(s"unsupported line processor = $s"); System.exit(1)
        }
        fileProcessorStr match {
            case "nio" => fileProcessor = Some(NIOFileProcessor(lineProcessor.get, pathname, encoding))
            case "scala-source" => fileProcessor = Some(ScalaSourceFileProcessor(lineProcessor.get, pathname, encoding))
            case s => println(s"unsupported file processor = $s"); System.exit(1)
        }
        val loopCount = Util.conf.getInt("exercise.parsing.loop-count")
        println(s"process: fileProcessor = '${fileProcessor.get.getClass.getName}', lineProcessor = '${lineProcessor.get.getClass.getName}', pathname = '$pathname', loopCount = '$loopCount', encoding = '$encoding', validate = '$validate', linePattern = '$linePattern', delimiter = '$delimiter', delimiterRegex = '$delimiterRegex', datePattern = '$datePattern', dateDelimiter = '$dateDelimiter'")

        // process
        fileProcessor.get.processFile()
        fileProcessor.get.report()

        // for performance testing only
        val now = new Date().getTime
        for (i <- 2 to loopCount) fileProcessor.get.processFile()
        val avgDur = (new Date().getTime - now).toDouble / loopCount
        val runtime = Runtime.getRuntime
        val usedMemory = runtime.totalMemory - runtime.freeMemory
        println(s"average loop duration = $avgDur ms, used memory = $usedMemory bytes")
    }
}
