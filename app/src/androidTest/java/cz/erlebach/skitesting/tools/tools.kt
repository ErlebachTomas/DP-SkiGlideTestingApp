package cz.erlebach.skitesting.tools

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice

/**
 * Sleep fuction
 */
fun pauseTestFor(milliseconds: Long) {
    try {
        Thread.sleep(milliseconds)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

