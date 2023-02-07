package cz.erlebach.skitesting
import cz.erlebach.skitesting.common.utils.getDateFromISO8601
import cz.erlebach.skitesting.common.utils.generateDateISO8601string

import org.junit.Assert
import org.junit.Test
import java.util.*

class ToolsTest {

        @Test
        fun date_isCorrect() {

            val date : Date = Date()
            val dateString = generateDateISO8601string(date);
            val convertedDate: Date = getDateFromISO8601(dateString)

            Assert.assertEquals(date, convertedDate)

        }

}