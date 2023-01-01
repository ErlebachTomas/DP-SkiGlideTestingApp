package cz.erlebach.skitesting.utils
import android.util.Log

val TAG = "myapp"

/** Zjednodušená verbose logovací funkce pro debug */
fun log(text: String, tag: String = TAG) {
    Log.v(tag, text)
}
/** Zjednodušená err logovací funkce pro debug */
fun err(text: String, tag: String = TAG) {
    Log.e(tag, text)
}