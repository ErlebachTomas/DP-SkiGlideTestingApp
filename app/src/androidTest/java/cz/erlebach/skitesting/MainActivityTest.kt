package cz.erlebach.skitesting

import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import org.junit.Assert.*

import org.junit.Test

class MainActivityTest {

    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)

    }

    @Test
    fun login() {
        //undone not implemented yet
    }

    @Test
    fun logout() {
        //undone not implemented yet
    }

    fun checkToast(msg: String) {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.waitForIdle()
        org.junit.Assert.assertTrue(uiDevice.hasObject(By.text(msg)))
    }
}