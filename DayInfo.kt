import kotlin.text.toLowerCase
import kotlin.text.lowercase

class DayInfo(
    var Day: String,
    var DailyRate: Double,
    var DayType: String,
    var InTime: String,
    var OutTime: String,
    var DaySalary: Double
) {

    /**
    * Handles the menu and backend for changing day type
    * @return -
    */
    fun editDayType() {
        println("\nCurrent Day Type: $DayType")
        println("[N]\tNORMAL_DAY")
        println("[R]\tREST_DAY")
        println("[SNW]\tSNW")
        println("[SNWR]\tSPECIAL_NON_WORKING_DAY_AND_REST_DAY")
        println("[RH]\tREGULAR_HOLIDAY")
        println("[RHR]\tREGULAR_HOLIDAY_AND_REST_DAY")
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

    /**
    * Handles the menu and backend for changing time in
    * @return -
    */
    fun editTimeIn() {
        do {
            println("\nCurrent Time In: $InTime")
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
    }

    /**
    * Handles the menu and backend for changing time out
    * @return -
    */
    fun editTimeOut() {
        do {
            println("\nCurrent Time Out: $OutTime")
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
    }
}