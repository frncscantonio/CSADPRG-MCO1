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
val weeklyData = mutableListOf<DayInfo>()

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
            "2" -> editTimeIn()
            "3" -> editTimeOut()
            "N" -> {
                computeDay()
                storeDay()
                refreshDay()
                currDay = currDay + 1
            }
            "X" -> exitProcess(0) // TODO: maybe confirmation prompt?
        }
    }
    printAllDays()
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


/*  TODO: Work from 2200 to 0600 is considered night shift. An additional 10% of the hourly
rate is given for every hour of work during the night shift. (Night shift differential). */ 
fun computeDay() {
    var totalSalary : Double = 0.00
    if(currAbsentStatus == "YES") {
        salaryPerDay.add(totalSalary) // == 0.00
        /* TODO: need to consider the 2 rest days (payed) */
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

class DayInfo(
    val Day: String,
    val DailyRate: Double,
    val InTime: String,
    val OutTime: String,
    val OverTimeHR: Int,
    val DaySalary: Double
)

fun storeDay(){
    var day = ""
    when(currDay){
        0 -> day = "Monday"
        1 -> day = "Tuesday"
        2 -> day = "Wednesday"
        3 -> day = "Thursday"
        4 -> day = "Friday"
        5 -> day = "Saturday"
        6 -> day = "Sunday"
    }
    weeklyData.add(DayInfo(day ,500.00, currTimeIn, currTimeOut, 0, 500.00)) //change ples
}

fun printAllDays(){
    // give user option if they would like to see the weeklyinfo
    for(day in weeklyData) {
        println("Date: ${day.Day}\n")
        println("Daily Rate:\t\t\t\t ${day.DailyRate}")
        println("IN Time:\t\t\t\t ${day.InTime}")
        println("OUT Time:\t\t\t\t ${day.OutTime}")
        println("Hours Overtime (Night Shift Overtime):\t ${day.OverTimeHR}")
        println("Salary for the day:\t\t\t ${day.DaySalary}\n\n\n")
        if(day.OverTimeHR > 0){
            println("Computation:\nDaily Rate:\t\t\t\t ${day.DailyRate}\nHours OT x OT Hourly Rate: insert here")
            // change print above
        }
    }
}