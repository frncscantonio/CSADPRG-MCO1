/* from R */

// # default values
// dailySalary <- 500
// maxHours <- 8
// workDays <- 5
// normDays <- 7
// currDay <- 0
// inTime <- 0900
// outTime <- 0900
// shiftType <- c("REGULAR SHIFT" = 1, "NIGHT SHIFT" = 2)
// dayType <- c(
//     "NORMAL DAY" = c(1.0, 1.25, 1.375), 
//     "REST DAY" = c(1.30, 1.69, 1.859), 
//     "SPECIAL NON-WORKING DAY" = c(1.30, 1.69, 1.859), 
//     "SPECIAL NON-WORKING DAY AND REST DAY" = c(1.50, 1.95, 2.145), 
//     "REGULAR HOLIDAY" = c(2, 2.60, 2.86),
//     "REGULAR HOLIDAY AND REST DAY" = c(2.60, 3.38 3.718)) 
// dailySalary <- vector("numeric", length = normDays)

// # source('functions.r') # to access all functions in functions.r

// while(currDay < normDays) {
//     do{
//         currShiftType <= "REGULAR SHIFT"
//         currDayType <= "NORMAL DAY"
//         currAbsentStatus <= "NO"
//         currTimeIn <= inTime 
//         currTimeOut <= outTime 

//         # menu
//         print(paste("Day ", currDay + 1))
//         print(paste("Day Type: ", names(dayType)[which(dayType == dayType["currDayType"])])
//         print(paste("Shift Type: ", names(dayType)[which(dayType == dayType["currShiftType"])]))
//         print(paste("Absent? ", currAbsentStatus)
//         print(paste("Time in: ", currInTime))
//         print(paste("Time out: ", currOutTime))

//         print("[0] Edit Day Type")
//         print("[1] Edit Absent Status")
//         print("[2] Edit Time In")
//         print("[3] Edit Time Out")
//         print("[N] Next Day")
//         print("[X] Exit Program")

        
//         menuInput <- readline()
//         match(menuInput,
//             '0' = currDayType <- editDayType(currDayType)
//             '1' = currAbsentStatus <- editAbsent(currAbsentStatus)
//             '2' = currTimeIn <- editTimeIn(currTimeIn)
//             '3' = currTimeOut <- editTimeOut(currTimeOut)
//             'N' = computeDay(currDay, currDayType, currShiftType, currAbsentStatus, currTimeIn, currTimeOut);
//                 currDay < currDay + 1
//             'X' = q()
//         )
//     while(menuInput != 'N')

// }

// editDayType <- function(x) {
//     print(paste("Current Day Type: ", names(dayType)[which(dayType == dayType["x"])])
//     print("[N] Normal Day")
//     print("[R] Rest Day")
//     print("[SNW] Special Non-Working Day")
//     print("[SNWR] Special Non-Working Day & Rest Day")
//     print("[RH] Regular Holiday")
//     print("[RHR] Regular Holiday & Rest Day")

//     input <- readline()
//     match(menuInput,
//         'N' = x <- "NORMAL DAY"
//         'R' = x <- "REST DAY"
//         'SNW' = x <- "SPECIAL NON-WORKING DAY"
//         'SNWR' = x <- "SPECIAL NON-WORKING DAY AND REST DAY"
//         'RH' = x <- "REGULAR HOLIDAY"
//         'RHR' = x <- "REGULAR HOLIDAY AND REST DAY"
//     )

//     print(paste("Current Day Type successfuly changed."))
//     return(x)
// }

// computeDay <- function(day, dayType, shiftType, absentStatus, timeIn, timeOut) {
//     if(absentStatus == "YES") {
//         dailySalary[day] <- 0
//     } else {
//         numHrs <- computeHours(timeIn, timeOut) # compute number of hours to check if overtime
//         overtimeHrs <- numHrs - maxHours 
//         if(overtimeHrs > 0) {
//             dailySalary[day] <- dailySalary * (dayType[dayType][shiftType] * overtimeHrs)
//         } else {
//             dailySalary[day] <- dailySalary * dayType[dayType][shiftType]
//         }
//     }
// }

// TODO: Work from 2200 to 0600 is considered night shift. An additional 10% of the hourly
// rate is given for every hour of work during the night shift. (Night shift differential).

import kotlin.system.exitProcess

val dailySalary = 500.0
val maxHours = 8
val workDays = 5
val normDays = 7
var currDay = 0
val inTime = "0900" // TODO: convert to military time
val outTime = "0900" // TODO: convert to military time
val dailySalaryArray = DoubleArray()

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

enum class shiftType(val i: DoubleArray) {
    REGULAR_SHIFT(1),
    NIGHT_SHIFT(2)

    operator fun get(index: Int): Double {
        return i[index]
    }
}

fun main() {
    while (currDay < normDays) {
        var currShiftType = "REGULAR_SHIFT"
        var currDayType = "NORMAL_DAY"
        var currAbsentStatus = "NO"
        var currTimeIn = inTime
        var currTimeOut = outTime

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
                currDay = currDay + 1
            }
            "X" -> exitProcess(0) // TODO: maybe confirmation prompt?
        }
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

    fun computeDay 
}

