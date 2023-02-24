package cz.erlebach.skitesting.common.utils
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

val TAG = "myapp"

/** Zjednodušená verbose logovací funkce */
fun lg(text: String, tag: String = TAG) {
    Log.v(tag, text)
}
/** Zjednodušená err logovací funkce  */
fun err(text: String, tag: String = TAG) {
    Log.e(tag, text)
}
/** info log */
fun info(text: String, tag: String = TAG) {
    Log.i(tag, text)
}
/** Funkce pro indikaci neočekávaného chování */
fun warning(text: String, tag: String = TAG) {
    Log.w(tag, text)
}
/** debug log */
fun debug(text: String, tag: String = TAG) {
    Log.d(tag, text)
}
fun wtf(text: String, exception: Throwable, tag: String = TAG) {
    Log.wtf(tag, text, exception)
}
fun wtf(text: String, tag: String = TAG) {
    Log.wtf(tag, text)
}

/** info výpis na obrazovku */
fun showSnackBar(view: View, text: String) {
    Snackbar.make(view,text, Snackbar.LENGTH_LONG).show()
}
/** Zobrazí toast */
fun toast(context: Context, text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, text, length).show()
}