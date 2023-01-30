package cz.erlebach.skitesting.utils
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

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

/** info výpis na obrazovku */
fun showSnackBar(view: View, text: String) {
    Snackbar.make(view,text, Snackbar.LENGTH_LONG).show()
}
/** Zobrazí toast */
fun toast(context: Context, text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, text, length).show()
}