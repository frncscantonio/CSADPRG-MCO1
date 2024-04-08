import kotlin.system.exitProcess


val dailySalary: Double = 500.0
val maxHours = 8
val workDays = 5
val normDays = 7
val inTime = "0900" 
val outTime = "0900" 
var currDay = 0
val weeklyData = mutableListOf<DayInfo>()

/* TODO: consider rest day */
// var currShiftType = "REGULAR_SHIFT"
// var currDayType = "NORMAL_DAY"
// var currAbsentStatus = "NO"
// var currTimeIn = inTime
// var currTimeOut = outTime

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
    print("IM IN?")
    val day = DayInfo(currDay.toString(), dailySalary, "NORMAL_DAY", inTime, outTime, 0.0)

    while (currDay < normDays) {
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
            "0" -> day.editDayType()
            "1" -> day.editTimeIn()
            "2" -> day.editTimeOut()
            "N" -> {
                computeDay(day)
                storeDay(day)
                refreshDay(day)
                currDay = currDay + 1
            }
            "X" -> exitProcess(0) // TODO: maybe confirmation prompt?
        }
    }
    printAllDays()
}

fun refreshDay(day: DayInfo) {
    if (currDay == 5 || currDay == 6) {
        day.DayType = "REST_DAY"
    } else {
        day.DayType = "NORMAL_DAY"
    }

    day.Day = currDay.toString()
    day.InTime = inTime
    day.OutTime = outTime
    day.DaySalary = 0.0
}

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

fun printAllDays(){
    for(day in weeklyData) {
        println("\nDate: ${day.Day}\n")
        println("Daily Rate:\t\t\t\t ${day.DailyRate}")
        println("Day Type: \t\t\t\t ${day.DayType}")
        println("IN Time:\t\t\t\t ${day.InTime}")
        println("OUT Time:\t\t\t\t ${day.OutTime}")
        // println("Hours Overtime (Night Shift Overtime):\t ${day.OverTimeHR}")
        println("Salary for the day:\t\t\t ${day.DaySalary}\n\n\n")
        // if(day.OverTimeHR > 0){
        //     println("Computation:\nDaily Rate:\t\t\t\t ${day.DailyRate}\nHours OT x OT Hourly Rate: insert here")
        //     // change print above 
        // }
    }
}

fun computeDay(day: DayInfo) {
        var totalSalary : Double = 0.00
        val hourlyRate : Double = dailySalary / maxHours

        // absent during normal day => not payed
        if(isAbsent(day) && day.DayType == "NORMAL_DAY") {
            day.DaySalary = totalSalary // == 0.00
        } 
        
        // payed rest days salary
        else if(!isAbsent(day) && day.DayType != "NORMAL_DAY") {
            totalSalary += dailySalary 
            day.DaySalary = totalSalary
        }

        else {
            var i = day.InTime.toInt() + 100;
            var cntr = 1;
            do {
                // w night differential
                if(isNS(i)) { 
                    if (cntr > 8) {
                        totalSalary += ((hourlyRate * dayType.valueOf(day.DayType).multipliers[2]) * 0.1); // overtime
                    } else {
                        totalSalary += ((hourlyRate * dayType.valueOf(day.DayType).multipliers[0]) * 0.1);
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
                i += 100;
                cntr += 1;

                if(i == 2400) { 
                    i = 0; // refresh to 00:00
                }
                
            } while(i != day.OutTime.toInt());
        }
        day.DaySalary = totalSalary
    } 

    fun isAbsent(day: DayInfo): Boolean {
        if (day.InTime == day.OutTime) {
            return true
        } else {
            return false
        }
    }

    fun isNS(x: Int): Boolean {
        return when {
            x in 0..6 -> true // 0 to 6
            x in 19..23 -> true // 19 to 24
            else -> false
        }
    }