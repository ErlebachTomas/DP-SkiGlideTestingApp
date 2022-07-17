package cz.erlebach.skitesting.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import cz.erlebach.skitesting.MainActivity
import cz.erlebach.skitesting.R
import org.junit.Test

/**
 * Android Espresso E2E testing
 */

class SkiProfileActivityTest {

    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)

    }
    /**
     * Test přidání páru lyží
     */
    @Test
    fun testOpenProfileAndAddSki() {

        val skiName = "Lyže expresso test"
        launch(cz.erlebach.skitesting.activity.SkiProfileActivity::class.java)
        onView(withId(R.id.fsl_btnAddSki)).perform(click())
        pauseTestFor(500);
        onView(withId(R.id.fas_tx_name)).perform(clearText(), replaceText(skiName));
        pauseTestFor(500);
        onView(withId(R.id.fas_btnSave)).perform(click())
        pauseTestFor(1000);

        onView(ViewMatchers.withId(R.id.fsl_recyclerView))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(skiName))
                )
            )

    }
    /**
     * Sleep
     */
    private fun pauseTestFor(milliseconds: Long) {
        try {
            Thread.sleep(milliseconds)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}