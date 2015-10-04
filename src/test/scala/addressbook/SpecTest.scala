package addressbook

import org.scalatest.Spec
import org.scalatest.prop.TableDrivenPropertyChecks._

/**
     Spec allows you to define tests as methods, which saves one function literal per test compared to style classes
     that represent tests as functions. Fewer function literals translates into faster compile times and fewer
     generated class files, which can help minimize build times. As a result, using Spec can be a good choice in
     large projects where build times are a concern as well as when generating large numbers of tests programatically
     via static code generators.
 */
//noinspection UnitMethodIsParameterless
class SpecTest extends Spec {

    val processor = LineProcessor

    object `address book processor` {

        object `valid address book` {

            val data = Table("line",
                    "Bill McKnight, Male, 16/03/77" ,
                    "Paul Robinson, Male, 15/01/85" ,
                    "Gemma Lane, Female, 20/11/91" ,
                    "Sarah Stone, Female, 20/09/80" ,
                    "Wes Jackson, Male, 14/08/74" )
            forAll(data) {line => processor.processLine(line)}

            def `should find 3 males` {
                assert(processor.malesCount == 3)
            }

            def `should find Wes the oldest`: Unit = {
                assert(processor.oldest.get.name == "Wes Jackson")
            }

            def `should compute how many days is Bill older than Paul`: Unit = {
                assert(processor.olderByDays.get == 2862)
            }
        }

    }

}