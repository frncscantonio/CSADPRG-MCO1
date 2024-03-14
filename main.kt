import kotlin.system.exitProcess

val dailySalary = 500.0
val maxHours = 8
val workDays = 5
val normDays = 7
val inTime = "0900" // TODO: convert to military time
val outTime = "0900" // TODO: convert to military time
var salaryPerDay = ArrayList<Double>() 
var currDay = 0

/* default settings per day (can be modified by the user) */
var currShiftType = "REGULAR_SHIFT"
var currDayType = "NORMAL_DAY"
var currAbsentStatus = "NO"
var currTimeIn = inTime
var currTimeOut = outTime

enum class dayType(val multipliers: DoubleArray) {
    NORMAL_DAY(doubleArrayOf(1.0, 1.25, 1.375)),
    REST_DAY(doubleArrayOf(1.30, 1.69, 1.859)),
    SPECIAL_NON_WORKING_DAY(doubleArrayOf(1.30, 1.69, 1.859)),
    SPECIAL_NON_WORKING_DAY_AND_REST_DAY(doubleArrayOf(1.50, 1.95, 2.145)),
    REGULAR_HOLIDAY(doubleArrayOf(2.0, 2.60, 2.86)),
    REGULAR_HOLIDAY_AND_REST_DAY(doubleArrayOf(2.60, 3.38, 3.718))

    operator fun get(index: Int): Double {
        return multipliers[index]
    }
}

enum class shiftType(val i: Int) {
    REGULAR_SHIFT(1),
    NIGHT_SHIFT(2)
}


fun main() {
    while (currDay < normDays) {
        
        println("Day ${currDay + 1}")
        println("Day Type: ${currDayType}")
        println("Shift Type: ${currShiftType}")
        println("Absent? ${currAbsentStatus}")
        println("Time in: ${currTimeIn}")
        println("Time out: ${currTimeOut}")

        println("[0] Edit Day Type")
        println("[1] Edit Absent Status")
        println("[2] Edit Time In")
        println("[3] Edit Time Out")
        println("[N] Next Day")
        println("[X] Exit Program")

        val menuInput: String? = readLine()

        when (menuInput) {
            "0" -> currDayType = editDayType(currDayType)
            "1" -> currAbsentStatus = editAbsent(currAbsentStatus)
            "2" -> currTimeIn = editTimeIn(currTimeIn)
            "3" -> currTimeOut = editTimeOut(currTimeOut)
            "N" -> {
                computeDay(currDay, currDayType, currShiftType, currAbsentStatus, currTimeIn, currTimeOut)
                refreshDay()
                currDay = currDay + 1
            }
            "X" -> exitProcess(0) // TODO: maybe confirmation prompt?
        }
    }

    fun refreshDay() {
        currShiftType = "REGULAR_SHIFT"
        currDayType = "NORMAL_DAY"
        currAbsentStatus = "NO"
        currTimeIn = inTime
        currTimeOut = outTime
    }

    fun editDayType(x: String): String {
        println("Current Day Type: ${x}")
        println("[N] Normal Day")
        println("[R] Rest Day")
        println("[SNW] Special Non-Working Day")
        println("[SNWR] Special Non-Working Day & Rest Day")
        println("[RH] Regular Holiday")
        println("[RHR] Regular Holiday & Rest Day")

        val input: String? = readLine()

        return when (input) {
            "N" -> "NORMAL_DAY"
            "R" -> "REST_DAY"
            "SNW" -> "SPECIAL_NON_WORKING_DAY"
            "SNWR" -> "SPECIAL_NON_WORKING_DAY_AND_REST_DAY"
            "RH" -> "REGULAR_HOLIDAY"
            "RHR" -> "REGULAR_HOLIDAY_AND_REST_DAY"
            else -> {
                println("Invalid input. Current Day Type remains unchanged.")
                x 
            }       
        }.also {
            println("Current Day Type successfully changed.")
        }
    }

    fun editAbsentStatus() {
        /* TODO: if YES, currTimeIn and currTimeOut should automattically be "-" 
        and the user cannot modify these until the currentAbsentStatus is NO. */
    }

    fun editTimeIn() {
        /* TODO: should automatically update currShiftType also depending on the user's timeIn */
    }

    fun editTimeIn() {
        /* TODO: simple modification */
    }

    /*  TODO: Work from 2200 to 0600 is considered night shift. An additional 10% of the hourly
    rate is given for every hour of work during the night shift. (Night shift differential). */ 
    fun computeDay() {
        if(currAbsentStatus == "YES") {
            salaryPerDay.add(0.00)
        } else {
            var numHours = computeHours()
            var overtimeHours = numHours - maxHours
            var totalSalary = 0

            if(overtimeHours > 0) {
                totalSalary = dailySalary + ((dailySalary * dayType.valueOf(currDayType).multipliers[dayType.valueOf(currShiftType).i]) * overtimeHours) // TODO: double check
                salaryPerDay.add(totalSalary) 
            } else {
                totalSalary = dailySalary + (dailySalary * dayType.valueOf(currDayType).multipliers[dayType.valueOf(currShiftType).i])
                salaryPerDay.add(totalSalary) 
            }
        }
    } 

    fun computeHours(): Int {
        /* TODO */
        var hrInTime = (currTimeIn.substring(0,2)).toInt() + 1 //get hours only 
        var hrOutTime = (currTimeOut.substring(0,2)).toInt() + 1 //get hours only
        var hours: Int = 0
        while(hrInTime != hrOutTime){
            if(hrInTime == 24){
                hrInTime -= 24
            }
            hrInTime++
            hours++
        }
    
        return hours
}

