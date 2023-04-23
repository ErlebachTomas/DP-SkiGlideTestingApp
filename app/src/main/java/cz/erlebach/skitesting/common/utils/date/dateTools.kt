package cz.erlebach.skitesting.common.utils.date

import cz.erlebach.skitesting.common.utils.wtf
import java.lang.Exception
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
    fun getDateFormatString(date: Date, pattern: String = "d MMMM yyyy h:mm"): String {
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date.time)
    }

/**
 * Převádí datum v ISO8601 formátu na formát dle zadaného patternu a vrací výsledný řetězec
 * @param dateString Datum v ISO8601 formátu, které má být převedeno.
 * @param pattern Pattern pro formátování datumu.
 * @return Formátovaný řetězec obsahující datum v požadovaném formátu.
 */
fun getDateFormatString(dateString: String, pattern: String = "d MMMM yyyy h:mm"): String {

        return try {
            val date = getDateFromISO8601(dateString)
            val format = SimpleDateFormat(pattern, Locale.getDefault())
            format.format(date.time)
        } catch (err : Exception) {
            wtf("chyba při formátovaní $dateString", err)
            dateString
        }
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

