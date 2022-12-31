package cz.erlebach.skitesting.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

    /**
     * Naformátuje [Date] dle zadeného vzoru
     * @param date [Date] datum a čas
     * @param pattern [String] zvolený formát
     * @return [String] Naformátovaný text odpovídající datu
     */
    fun getDateFormatString(date: Date, pattern: String = "MMMM d yyyy h:mm"): String {
        val format = SimpleDateFormat(pattern)
        return format.format(date.time)
    }

    /**
     * Konvertuje [Date] na ISO8601 date string
     * @param date [Date] datum a čas
     * @return [String] Naformátovaný text odpovídající datu
     */
    fun generateDateISO8601string(date: Date = Date()): String {
        val offset = ZoneOffset.UTC
        val offsetDateTime : OffsetDateTime = date.toInstant().atOffset(offset)
        return offsetDateTime.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * Parsuje ISO8601 date string na date
     * @param dateString [String] datum a čas
     * @return [Date] instance data
     */
    fun getDateFromISO8601(dateString: String): Date {
        val timeFormatter = DateTimeFormatter.ISO_DATE_TIME
        val offsetDateTime = OffsetDateTime.parse(dateString, timeFormatter)
        return Date.from(Instant.from(offsetDateTime))
    }

