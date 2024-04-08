import kotlin.text.toLowerCase
import kotlin.text.lowercase

class DayInfo(
    var Day: String,
    var DailyRate: Double,
    var DayType: String,
    var InTime: String,
    var OutTime: String,
    // val OverTimeHR: Int,
    var DaySalary: Double
) {

    fun editDayType() {
        println("Current Day Type: $DayType")
        println("[X]\tCancel Edit")

        print("\n>>\t")
        val input: String? = readLine()?.uppercase()
        when (input) {
            "N" -> DayType = "NORMAL_DAY"
            "R" -> DayType = "REST_DAY"
            "SNW" -> DayType = "SPECIAL_NON_WORKING_DAY"
            "SNWR" -> DayType = "SPECIAL_NON_WORKING_DAY_AND_REST_DAY"
            "RH" -> DayType = "REGULAR_HOLIDAY"
            "RHR" -> DayType = "REGULAR_HOLIDAY_AND_REST_DAY"
            "X" -> println("\nCurrent Day Type remains unchanged.")
            else -> println("\nInvalid input. Current Day Type remains unchanged.")
        }

        println("\nCurrent Day Type successfully changed.")
    }

    fun editTimeIn() {
        if (DayType == "NORMAL_DAY") {
            do {
                println("Current Time In: $InTime")
                println("Military Time Format e.g., (0100)")
                println("[X]\tCancel Edit")

                print("\n>>\t")
                val input: String = (readLine()?.uppercase() ?: "")
                if (input.length == 1 && input == "X") {
                    return
                }
                if (!input.all { it.isDigit() }) {
                    println("\nInvalid input. Please Enter Numbers Only.\n\n")
                } else if (input.length != 4) {
                    println("\nInvalid input. Please Enter 4 Numbers Only.\n\n")
                } else {
                    InTime = input
                }
            } while (!input.all { it.isDigit() } || input.length != 4)
        } else {
            println("You Are Absent Today!")
        }
    }

    fun editTimeOut() {
        if (DayType == "NORMAL_DAY") {
            do {
                println("Current Time Out: $OutTime")
                println("Military Time Format e.g., (0100)")
                println("[X]\tCancel Edit")

                print("\n>>\t")
                val input: String = (readLine()?.uppercase() ?: "")
                if (input.length == 1 && input == "X") {
                    return
                }
                if (!input.all { it.isDigit() }) {
                    println("\nInvalid input. Please Enter Numbers Only.\n\n")
                } else if (input.length != 4) {
                    println("\nInvalid input. Please Enter 4 Numbers Only.\n\n")
                } else {
                    OutTime = input
                }
            } while (!input.all { it.isDigit() } || input.length != 4)
        } else {
            println("You Are Absent Today!")
        }
    }
}