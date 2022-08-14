package cz.erlebach.skitesting.activity

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withText
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.tools.pauseTestFor
import org.junit.Before
import org.junit.Test
import java.util.*


class MeasurementActivityTest {
    private lateinit var activityScenario: ActivityScenario<MeasurementActivity>

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(MeasurementActivity::class.java)
    }

    /**
     * Komplexní E2E test
     * */
    @Test
    fun testActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.ml_btn_addNew)).perform(ViewActions.click())
        pauseTestFor(500)
        Espresso.onView(ViewMatchers.withId(R.id.mf_snowTemperature)).perform(
            ViewActions.clearText(),
            ViewActions.replaceText((-10..10).random().toString())
        )
        Espresso.onView(ViewMatchers.withId(R.id.mf_temperature)).perform(
            ViewActions.clearText(),
            replaceText((-10..10).random().toString()))

        Espresso.onView(ViewMatchers.withId(R.id.mf_btnTimePicker)).perform(click())
        setCurrentTime()

        Espresso.onView(ViewMatchers.withId(R.id.mf_btnDatePicker)).perform(click())
        setDateTime()

            Espresso.onView(ViewMatchers.withId(R.id.mf_btnSave)).perform(click())

        pauseTestFor(3000)

    }

    /**
     * Nastaví datum v datePickeru
     * @param calendar vybrané datum
     * */
    private fun setDateTime(calendar: Calendar = Calendar.getInstance()) {

        onView(isAssignableFrom(DatePicker::class.java)).perform(
            PickerActions.setDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
        onView(withText("OK")).perform(click())

    }

    /**
     * Nastaví aktuální čas do TimePickeru     *
     */
    private fun setCurrentTime() {

        val calendar = Calendar.getInstance()
        calendar.get(Calendar.MINUTE)

        setTime(calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE))
    }

    /**
     * Nastaví čas v timePickeru
     * @param hour
     * @param minute
     * */
    private fun setTime(hour: Int, minute: Int){

        onView(isAssignableFrom(TimePicker::class.java)).perform(
            PickerActions.setTime(hour,minute)
        )
        onView(withText("OK")).perform(click())
    }



}