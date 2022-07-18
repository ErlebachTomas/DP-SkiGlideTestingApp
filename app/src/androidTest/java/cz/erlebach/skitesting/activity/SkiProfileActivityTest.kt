package cz.erlebach.skitesting.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.tools.AlertDialogButtonID
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test


/**
 * Android Espresso E2E testing
 */
class SkiProfileActivityTest {

    private lateinit var activityScenario: ActivityScenario<SkiProfileActivity>

    @Before
    fun setUp() {
        activityScenario = launch(cz.erlebach.skitesting.activity.SkiProfileActivity::class.java)
    }

    /**
     * Komplexní E2E test
     * */
    @Test 
    fun testSkiProfileActivity() {
        
        val skiName = "Lyže expresso test"
        testInsertSki(skiName)
        val newSkiName = "test update expresso"
        testUpdateSki(skiName, newSkiName)
        testdeleteSkis()
    }

    /**
     * Test přidání páru lyží. Otevře aktivitu, vloží nový záznam a zkontroluje list
     */   
    fun testInsertSki(skiName:String) {

       
        onView(withId(R.id.fsl_btnAddSki)).perform(click())
        pauseTestFor(500)
        onView(withId(R.id.fas_tx_name)).perform(clearText(), replaceText(skiName))
        pauseTestFor(500)
        onView(withId(R.id.fas_btnSave)).perform(click())
        pauseTestFor(1000)

        onView(ViewMatchers.withId(R.id.fsl_recyclerView))
            .perform(
                // scrollTo neprojde testem, pokud položka není v listu.
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(skiName))
                )
            )
    }
    /**
     * Upraví jméno lyže
     */
    fun testUpdateSki(skiName:String, newSkiName: String) {
        onView(allOf(withId(R.id.fsl_recyclerView), isDisplayed()))
            .perform(actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText(skiName)), click()));
        pauseTestFor(1000)
        onView(withId(R.id.fus_tx_name)).perform(clearText(), replaceText(newSkiName))
        onView(withId(R.id.fus_btnSave)).perform(click())
        pauseTestFor(1000)
        onView(ViewMatchers.withId(R.id.fsl_recyclerView))
            .perform(
                // scrollTo neprojde testem, pokud položka není v listu.
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(newSkiName))
                )
            )
    }
    /** Odstraní všechny lyže */
    fun testdeleteSkis() {
        onView(withId(R.id.fsl_btnDelete)).perform(click())
        pauseTestFor(1000)
        onView(withId(AlertDialogButtonID.POSITIVE)).perform(click())
        pauseTestFor(1000)
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