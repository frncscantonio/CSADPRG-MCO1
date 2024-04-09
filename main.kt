/********************
Last names: ANTONIO, DAMING, FERRER, SHIOZAWA
Language: Kotlin
Paradigm(s): Imperative Programming and Object-Oriented Programming
********************/

import kotlin.system.exitProcess

/* default settings */
val dailySalary: Double = 500.0
val maxHours = 8
val workDays = 5
val normDays = 7
val inTime = "0900" 
val outTime = "0900" 
val defDay = "NORMAL_DAY"

var currDay = 0
val weeklyData = mutableListOf<DayInfo>()

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

/**
 * Handles the main menu of the program
 * @return -
 */
fun main() {

    var isNLast = false
    var day = DayInfo(currDay.toString(), dailySalary, defDay, inTime, outTime, 0.0)

    while (currDay < normDays) {

        // refreshing day info if next day
        if(isNLast) {
            if(currDay == 5 || currDay == 6) {
                day = DayInfo(currDay.toString(), dailySalary, "REST_DAY", inTime, outTime, 0.0)
            } else {
                day = DayInfo(currDay.toString(), dailySalary, defDay, inTime, outTime, 0.0)
            }
        }

        println("\nDay ${currDay + 1}")
        println("Day Type: ${day.DayType}")
        println("Time in: ${day.InTime}")
        println("Time out: ${day.OutTime}\n")

        println("[0]\tEdit Day Type")
        println("[1]\tEdit Time In")
        println("[2]\tEdit Time Out")
        println("[N]\tNext Day")
        println("[X]\tExit Program")

        print("\n>>\t")
        val menuInput: String? = readLine()?.uppercase()

        when (menuInput) {
            "0" -> {day.editDayType(); isNLast = false;}
            "1" -> {day.editTimeIn(); isNLast = false;}
            "2" -> {day.editTimeOut(); isNLast = false;}
            "N" -> {
                computeDay(day)
                storeDay(day)
                currDay = currDay + 1
                isNLast = true
            }
            "X" -> exitProcess(0) 
        }
    }
    printAllDays()
}


/**
 * Computes the overall salary for the day
 * @return -
 */
fun computeDay(day: DayInfo) {
    var totalSalary : Double = 0.00
    val hourlyRate : Double = dailySalary / maxHours

    // absent during normal day => not payed
    if(isAbsent(day) && day.DayType == "NORMAL_DAY") {
        totalSalary = 0.00 // == 0.00
    } 
    
    // payed rest days salary
    else if(isAbsent(day) && day.DayType != "NORMAL_DAY") {
        totalSalary += dailySalary 
    }

    else {

        var i = ((day.InTime).substring(0,2)).toInt() + 1;
        var cntr = 1;
        do {
            // w night differential
            if(isNS(i)) { 
                if (cntr > 8) {
                    totalSalary += ((hourlyRate * dayType.valueOf(day.DayType).multipliers[2])); // overtime
                } else {
                    totalSalary += ((hourlyRate * dayType.valueOf(day.DayType).multipliers[0]) + (hourlyRate *  1.10));
                }
            }
            
            // no night differential
            else { 
                if (cntr > 8) {
                    totalSalary += ((hourlyRate * dayType.valueOf(day.DayType).multipliers[1])); // overtime
                } else {
                    totalSalary += ((hourlyRate * dayType.valueOf(day.DayType).multipliers[0]));
                }
            }
            i++;
            cntr++;

            if(i == 24) { 
                i = 0; // refresh to 00:00
            }
        } while(i != ((day.OutTime).substring(0,2)).toInt())
    }

    
    day.DaySalary = String.format("%.2f", totalSalary).toDouble()
} 

/**
 * Checks whether or not the employee is absent
 * @return Boolean
 */
fun isAbsent(day: DayInfo): Boolean {
    if (day.InTime == day.OutTime) {
        return true
    } else {
        return false
    }
}

/**
 * Checks whether the given hour falls under night-shift hours or not
 * @return Boolean
 */
fun isNS(x: Int): Boolean {
    return when {
        x in 0..6 -> true // 0 to 6
        x in 22..23 -> true // 19 to 24
        else -> false
    }
}

/**
 * Stores a DayInfo object to weeklyData
 * @return -
 */
fun storeDay(day: DayInfo){
    when(day.Day){
        "0" -> day.Day = "Monday"
        "1" -> day.Day = "Tuesday"
        "2" -> day.Day = "Wednesday"
        "3" -> day.Day = "Thursday"
        "4" -> day.Day = "Friday"
        "5" -> day.Day = "Saturday"
        "6" -> day.Day = "Sunday"
    }
    weeklyData.add(day)
}

/**
 * Prints a summary report and the total salary for the week
 * @return -
 */
fun printAllDays(){
    var totalSalary : Double = 0.00

    println("___________________________________________________________")

    for(day in weeklyData) {
        println("\nDate: ${day.Day}\n")
        println("Daily Rate:\t\t\t\t ${day.DailyRate}")
        println("Day Type: \t\t\t\t ${day.DayType}")
        println("IN Time:\t\t\t\t ${day.InTime}")
        println("OUT Time:\t\t\t\t ${day.OutTime}")
        println("Salary for the day:\t\t\t ${day.DaySalary}\n\n\n")

        totalSalary += day.DaySalary
    }
    println("\nTOTAL SALARY FOR THE WEEK:\t\t\t ${totalSalary}\n\n\n")
}
