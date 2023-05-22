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
 * Fragment pro přihlašování do aplikace
 */
class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val myView = inflater.inflate(R.layout.fragment_main_login, container, false)

        myView.findViewById<Button>(R.id.btn_login).setOnClickListener { _ ->

            (activity as MainActivity?)!!.login() // volání funkce aktivity z fragmentu

        }

        return myView
    }





}