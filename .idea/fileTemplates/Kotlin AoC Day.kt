#set($date=${PACKAGE_NAME})
#set($date=$date.substring(31, 33))
package ${PACKAGE_NAME}

import io.github.melvinsc.utils.day.Day

fun main() = Day.setMain(Day$date)

object Day$date : Day() {
    override fun first(inputData: String): Int {

        return -1
    }

    override fun second(inputData: String): Int {

        return -1
    }
}