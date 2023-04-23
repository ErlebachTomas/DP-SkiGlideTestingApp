package cz.erlebach.skitesting

import android.content.Context
import android.content.res.Resources
import cz.erlebach.skitesting.common.utils.date.generateDateISO8601string
import cz.erlebach.skitesting.common.utils.date.getDateFromISO8601
import cz.erlebach.skitesting.model.TestSession.Companion.getTestTypeString
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Date

class ToolsTest {

        @Test
        fun date_isCorrect() {

            val date = Date()
            val dateString = generateDateISO8601string(date)
            val convertedDate: Date = getDateFromISO8601(dateString)

            Assert.assertEquals(date, convertedDate)

        }


    @Test
    fun testGetTestTypeString() {
        val context = mock(Context::class.java)
        val resources = mock(Resources::class.java)
        `when`(context.resources).thenReturn(resources)
        `when`(resources.getStringArray(R.array.testType)).thenReturn(arrayOf("Test 1", "Test 2", "Test 3"))

        for (testType in 0..2) {
            Assert.assertEquals("Test ${testType + 1}", getTestTypeString(context, testType))
        }
    }
}