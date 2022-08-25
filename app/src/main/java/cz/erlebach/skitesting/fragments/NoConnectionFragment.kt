package cz.erlebach.skitesting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cz.erlebach.skitesting.MainActivity
import cz.erlebach.skitesting.R


/**
 Obrazovka v případě ztráty internetového připojení
 */
class NoConnectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val myView = inflater.inflate(R.layout.fragment_main_no_connection, container, false)

        myView.findViewById<Button>(R.id.btn_retry_conn).setOnClickListener { _ ->
            (activity as MainActivity?)!!.login()
        }
        myView.findViewById<Button>(R.id.btn_skip).setOnClickListener { _ ->
            (activity as MainActivity?)!!.skip()
        }
        return myView
    }


}