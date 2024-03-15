import kotlin.system.exitProcess
import kotlin.text.toLowerCase
import kotlin.text.lowercase

val dailySalary: Double = 500.0
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
    REGULAR_HOLIDAY_AND_REST_DAY(doubleArrayOf(2.60, 3.38, 3.718));
    

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
        println("Time out: ${currTimeOut}\n")

        println("[0]\tEdit Day Type")
        println("[1]\tEdit Absent Status")
        println("[2]\tEdit Time In")
        println("[3]\tEdit Time Out")
        println("[N]\tNext Day")
        println("[X]\tExit Program")

        print("\n>>\t")
        val menuInput: String? = readLine()?.uppercase()

        when (menuInput) {
            "0" -> editDayType()
            "1" -> editAbsentStatus()
            // "2" -> editTimeIn()
            // "3" -> editTimeOut()
            "N" -> {
                computeDay()
                refreshDay()
                currDay = currDay + 1
            }
            "X" -> exitProcess(0) // TODO: maybe confirmation prompt?
        }
    }
}

fun refreshDay() {
    currShiftType = "REGULAR_SHIFT"
    currDayType = "NORMAL_DAY"
    currAbsentStatus = "NO"
    currTimeIn = inTime
    currTimeOut = outTime
}

fun editDayType() {
    println("Current Day Type: ${currDayType}")
    println("[N]\tNormal Day")
    println("[R]\tRest Day")
    println("[SNW]\tSpecial Non-Working Day")
    println("[SNWR]\tSpecial Non-Working Day & Rest Day")
    println("[RH]\tRegular Holiday")
    println("[RHR]\tRegular Holiday & Rest Day")
    println("[X]\tCancel Edit")

    print("\n>>\t")
    val input: String? = readLine()

    when (input) {
        "N" -> currDayType = "NORMAL_DAY"
        "R" -> currDayType = "REST_DAY"
        "SNW" -> currDayType = "SPECIAL_NON_WORKING_DAY"
        "SNWR" -> currDayType = "SPECIAL_NON_WORKING_DAY_AND_REST_DAY"
        "RH" -> currDayType = "REGULAR_HOLIDAY"
        "RHR" -> currDayType = "REGULAR_HOLIDAY_AND_REST_DAY"
        "X" -> { println("\nCurrent Day Type remains unchanged.")
                 return }
        else -> {
            println("\nInvalid input. Current Day Type remains unchanged.")
            return
        }
    }    

    println("\nCurrent Day Type successfully changed.")
}


fun editAbsentStatus() {
    /* TODO: if YES, currTimeIn and currTimeOut should automattically be "-" 
    and the user cannot modify these until the currentAbsentStatus is NO. */
    println("Current Absent Status: ${currAbsentStatus}\n")
    println("[Y]\tYes")
    println("[N]\tNo")
    println("[X]\tCancel Edit")

    print("\n>>\t")
    val input: String? = readLine()?.uppercase()

    when(input) {
        "Y" -> {
            currTimeIn == "-"
            currTimeOut == "-"
            currAbsentStatus = "YES"
        }
        "N" -> currAbsentStatus = "NO"
        "X" -> { println("\nCurrent Absent Status remains unchanged.")
                 return}
        else -> {
            println("\nInvalid input. Current Absent Status remains unchanged.")
            return
        }
    }
}

// fun editTimeIn() {
//     /* TODO: should automatically update currShiftType also depending on the user's timeIn */
// }

// fun editTimeIn() {
//     /* TODO: simple modification */
// }

/*  TODO: Work from 2200 to 0600 is considered night shift. An additional 10% of the hourly
rate is given for every hour of work during the night shift. (Night shift differential). */ 
fun computeDay() {
    var totalSalary : Double = 0.00
    if(currAbsentStatus == "YES") {
        salaryPerDay.add(totalSalary) // == 0.00
    } else {
        var numHours = computeHours()
        var overtimeHours = numHours - maxHours
        
        if(overtimeHours > 0) {
            totalSalary = dailySalary + ((dailySalary * dayType.valueOf(currDayType).multipliers[shiftType.valueOf(currShiftType).i]) * overtimeHours) // TODO: double check
            salaryPerDay.add(totalSalary) 
        } else {
            totalSalary = dailySalary + (dailySalary * dayType.valueOf(currDayType).multipliers[shiftType.valueOf(currShiftType).i])
            salaryPerDay.add(totalSalary) 
        }
    }
} 

fun computeHours(): Int {
    
    var hrInTime = (currTimeIn.substring(0,2)).toInt() + 1 // get hours only 
    var hrOutTime = (currTimeOut.substring(0,2)).toInt() + 1 // get hours only
    var hours: Int = 0
    while(hrInTime != hrOutTime){
        if(hrInTime == 24){
            hrInTime = 0 // refresh to 00:00
        }
        hrInTime++
        hours++
    }
    return hours
    
      
}


