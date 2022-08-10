package cz.erlebach.skitesting.utils

import java.text.SimpleDateFormat
import java.util.*

    /**
     * Naformátuje [Date] dle zadeného vzoru
     * @param Date datum a čas
     * @param pattern zvolený formát
     * @return Naformátovaný text odpovídající datu
     */
    fun getDateFormatString(date: Date, pattern: String = "MMMM d yyyy h:mm"): String {
        val format = SimpleDateFormat(pattern)
        return format.format(date.time)
    }
