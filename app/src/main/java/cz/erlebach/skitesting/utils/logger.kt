package cz.erlebach.skitesting.utils
import android.util.Log

val TAG = "myapp"

/** Zjednodušená verbose logovací funkce pro debug */
fun lg(text: String, tag: String = TAG) {
    Log.v(tag, text)
}
/** Zjednodušená err logovací funkce pro debug */
fun err(text: String, tag: String = TAG) {
    Log.e(tag, text)
}
/** info log */
fun info(text: String, tag: String = TAG) {
    Log.i(tag, text)
}