package cz.erlebach.skitesting.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.tools.AlertDialogButtonID
import cz.erlebach.skitesting.tools.pauseTestFor
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
     * Přidá náhodný počet lyží a zkontroluje zda jejich počet odpovídá
     */
    @Test
    fun addMultipleSkis() {

        val skis = arrayListOf<String>("Sporten Favorit JR 22",
            "Atomic PRO S2",
            "Peltonen Nanogrip Facile 20",
            "Salomon Aero 7",
            "Salomon RC 8",
            "ATOMIC Redster S7",
            "Peltonen Nanogrip Infra X",
            "Atomic Pro Classic",
            "Sporten Ranger",
            "Fischer Orbiter EF",
            "Peltonen G-Grip Facile",
            "Atomic Z3",
            "Leki PRC 850"
        )
        skis.shuffle() // promíchá seznam
        val  length = (1 until skis.size).random()
        for (index in 0 until length ) {
            addSki(skis[index]) // přidá náhodný počet lyží
        }

        onView(withId(R.id.fsl_recyclerView)).check(matches(hasChildCount(length)))
        testdeleteSkis()


    }

    /**
     * Test přidání páru lyží. Otevře aktivitu, vloží nový záznam a zkontroluje list
     */
    private fun testInsertSki(skiName:String) {

        addSki(skiName)

        onView(ViewMatchers.withId(R.id.fsl_recyclerView))
            .perform(
                // scrollTo neprojde testem, pokud položka není v listu.
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(skiName))
                )
            )
    }

    /**
     * přidání lyže z aktivity
     */
    private fun addSki(skiName:String) {
        onView(withId(R.id.fsl_btnAddSki)).perform(click())
        pauseTestFor(500)
        onView(withId(R.id.fas_tx_name)).perform(clearText(), replaceText(skiName))
        pauseTestFor(500)
        onView(withId(R.id.fas_btnSave)).perform(click())
        pauseTestFor(1000)
    }

    /**
     * Upraví jméno lyže
     */
    private fun testUpdateSki(skiName:String, newSkiName: String) {
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
    private fun testdeleteSkis() {
        onView(withId(R.id.fsl_btnDelete)).perform(click())
        pauseTestFor(1000)
        onView(withId(AlertDialogButtonID.POSITIVE)).perform(click())
        pauseTestFor(1000)
    }







}