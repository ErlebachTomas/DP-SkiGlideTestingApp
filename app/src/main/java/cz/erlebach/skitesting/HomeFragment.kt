package cz.erlebach.skitesting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


/**
Domovská obrazovka
 */
class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val myView = inflater.inflate(R.layout.fragment_home, container, false)

        myView.findViewById<Button>(R.id.btn_sign_out).setOnClickListener { view ->
            (activity as MainActivity?)!!.logout()
        }
        return myView

    }

}