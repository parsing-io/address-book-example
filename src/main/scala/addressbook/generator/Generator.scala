package addressbook.generator

import java.io.{File, PrintWriter}
import java.util.concurrent.ThreadLocalRandom._

/**
 * Generates an address book file using Gumtree data + random data
 *
 */
object Generator {

    val alpha = 'A' to 'Z'
    val fistNameVariability = 6
    val lastNameVariability = 8

    def generate(pathname: String, linesCount: Int, nameLengthMinimal: Int): Unit = {
        val fixed = Map(
            0 -> "Bill McKnight, Male, 16/03/77" ,
            1 -> "Paul Robinson, Male, 15/01/85",
            2 -> "Gemma Lane, Female, 20/11/91",
            3 -> "Sarah Stone, Female, 20/09/80",
            4 -> "Wes Jackson, Male, 14/08/74")
        val positionsForFixed = randomNumbers(5, 0, linesCount)
        var lastFixed = 0
        val out = new PrintWriter(new File(pathname))
        for (l <- 0 until linesCount) {
            if (positionsForFixed.contains(l)){
                out.println(fixed(lastFixed))
                lastFixed += 1
            }
            else {
                val name = randomName(nameLengthMinimal)
                val sex = if (current().nextBoolean()) "Male" else "Female"
                val birth = randomBirth
                out.println(s"$name, $sex, $birth")
            }
        }
        out.close()
    }

    def randomName(nameLengthMinimal: Int) = {
        val sb = new StringBuilder
        for (i <- 0 to current().nextInt(fistNameVariability) + nameLengthMinimal / 2) {
            sb.append(alpha(current().nextInt(26)))
        }
        sb.append(" ")
        for (i <- 0 to current().nextInt(lastNameVariability) + nameLengthMinimal / 2) {
            sb.append(alpha(current().nextInt(26)))
        }
        sb.toString()
    }

    def randomBirth = {
        val dayStr = "%02d".format(current().nextInt(31) + 1)
        val monthStr = "%02d".format(current().nextInt(12) + 1)
        var year = current().nextInt(40)
        if (year >= 30) year -= 30 else year += 70
        val yearStr = "%02d".format(year)
        s"$dayStr/$monthStr/$yearStr"
    }

    def randomNumbers(count: Int, lower: Int, upper: Int): Set[Int] = {
        // handle duplicates
        val distinct = current().ints(2 * count, lower, upper).toArray.toSet
        distinct.dropRight(distinct.size - count)
    }

}
