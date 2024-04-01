import kotlin.system.exitProcess
import kotlin.text.toLowerCase
import kotlin.text.lowercase

val dailySalary: Double = 500.0
val maxHours = 8
val workDays = 5
val normDays = 7
val inTime = "0900" 
val outTime = "0900" 
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
            "2" -> editTimeIn()
            "3" -> editTimeOut()
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
    if(currDay == 5 || currDay == 6) {
        currDayType = "REST_DAY"
        currAbsentStatus = "YES"
    }
    currShiftType = "REGULAR_SHIFT"
    currDayType = "NORMAL_DAY"
    currAbsentStatus = "NO"
    currTimeIn = inTime
    currTimeOut = outTime
}


// TODO: day 6 and 7 should automatically be rest days
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
    val input: String? = readLine()?.uppercase()

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
    println("Current Absent Status: ${currAbsentStatus}\n")
    println("[Y]\tYes")
    println("[N]\tNo")
    println("[X]\tCancel Edit")

    print("\n>>\t")
    val input: String = readLine()!!.uppercase()

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

// fun computeDay() {
//     var totalSalary : Double = 0.00
//     val hourlyRate : Double = dailySalary / maxHours

//     // absent during normal day => not payed
//     if(currAbsentStatus == "YES" && dayType == "NORMAL_DAY") {
//         salaryPerDay.add(totalSalary) // == 0.00
//     } 
    
//     // payed rest days salary
//     else if(currAbsentStatus == "NO" && dayType == "REST_DAY") {
//         totalSalary += dailySalary 
//         salaryPerDay.add(totalSalary)
//     }

//     else {
//         var numHours = computeHours()
//         var overtimeHours = numHours - maxHours
//         var multiplier = Double = 0.00

//         totalSalary += dailySalary 

//         // regular daily salary
//         if(currDayType == "NORMAL_DAY" && numHours == maxHours && overtimeHours == 0 && currTimeIn.toInt() >= 0600 ) {
//             salaryPerDay.add(totalSalary)
//         }

//         // with no overtime hours
//         else if(overtimeHours == 0) {
//             for(int i = currTimeIn.toInt(); i != currTimeOut; i += 100) {
//                 if(i == 2400) { 
//                     i == 0000 // refresh to 00:00
//                 }

//                 // night shift (w night differential)
//                 if(i >= 2200 && i <= 0600) { 
//                     totalSalary = totalSalary + ((hourlyRate * dayType.valueOf(currDayType).multipliers[0]) * 0.1)
//                 }
                
//                 // non-night shift (no night differential)
//                 else { 
//                     totalSalary = totalSalary + (hourlyRate * dayType.valueOf(currDayType).multipliers[0])
//                 }
//             }
//         }

//         // with overtime hours
//         else if(overtimeHours) {
//             for(int i = currTimeIn.toInt(); i < currTimeOut.toInt() || i > inTime.toInt(); i += 100) {
//                 if(i == 2400) { 
//                     i == 0000 // refresh to 00:00
//                 }

//                 // night shift (w night differential)
//                 if(i >= 2200 && i <= 0600) { 
//                     totalSalary = totalSalary + ((hourlyRate * dayType.valueOf(currDayType).multipliers[1]) * 0.1)
//                 }
                
//                 // non-night shift (no night differential)
//                 else { 
//                     totalSalary = totalSalary + (hourlyRate * dayType.valueOf(currDayType).multipliers[2])
//                 }
//             }
//         }
//     }
// } 


fun computeDay() {
    var totalSalary : Double = 0.00
    val hourlyRate : Double = dailySalary / maxHours

    // absent during normal day => not payed
    if(currAbsentStatus == "YES" && dayType == "NORMAL_DAY") {
        salaryPerDay.add(totalSalary) // == 0.00
    } 
    
    // payed rest days salary
    else if(currAbsentStatus == "NO" && dayType != "NORMAL_DAY") {
        totalSalary += dailySalary 
        salaryPerDay.add(totalSalary)
    }

    else {
        var numHours = computeHours()
        var overtimeHours = numHours - maxHours

        totalSalary += dailySalary 

        // regular daily salary
        if(currDayType == "NORMAL_DAY" && numHours == maxHours && overtimeHours == 0 && currTimeIn.toInt() >= 0600 ) {
            salaryPerDay.add(totalSalary)
        }

        else {
            val multiplierIndex = if (overtimeHours > 0) 1 else 0

            var i = currTimeIn.toInt()
            while (i < currTimeOut.toInt() || i > inTime.toInt()) {
                if (i == 2400) {
                    i = 0 // refresh to 00:00
                }

                val multiplier = dayType.valueOf(currDayType).multipliers[multiplierIndex]

                if (i >= 2200 || i < 600) {
                    totalSalary += hourlyRate * multiplier * 0.1 // night shift with night differential
                } else {
                    totalSalary += hourlyRate * multiplier // non-night shift without night differential
                }
                i += 100
            }
            
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

fun editTimeIn() {

    if(currAbsentStatus == "NO"){
        do{
            println("Current Time In: ${currTimeIn}")
            println("Military Time Format e.g., (0100)")
            println("[X]\tCancel Edit")

            print("\n>>\t")
            val input : String = (readLine().toString()).uppercase()
            if(input.length == 1 && input == "X"){
                return
            }
            if(!input.all { char -> char.isDigit()}){
                println("\nInvalid input. Please Enter Numbers Only.\n\n")
            }else if(input.length != 4){
                println("\nInvalid input. Please Enter 4 Numbers Only.\n\n")
            }else{
                currTimeIn = input
            }
        }while(!input.all { char -> char.isDigit()} || input.length != 4)
    }else{
        println("You Are Absent Today!")
        // replace later with Absent Status and show currtime as "-"
    }
}

fun editTimeOut() {
    if(currAbsentStatus == "NO"){
        do{
            println("Current Time Out: ${currTimeIn}")
            println("Military Time Format e.g., (0100)")
            println("[X]\tCancel Edit")

            print("\n>>\t")
            val input : String = (readLine().toString()).uppercase()
            if(input.length == 1 && input == "X"){
                return
            }
            if(!input.all { char -> char.isDigit()}){
                println("\nInvalid input. Please Enter Numbers Only.\n\n")
            }else if(input.length != 4){
                println("\nInvalid input. Please Enter 4 Numbers Only.\n\n")
            }else{
                currTimeOut = input
            }
        }while(!input.all { char -> char.isDigit()} || input.length != 4)
    }else{
        println("You Are Absent Today!")
        // replace later with Absent Status and show currtime as "-"
    }
}
