package cz.erlebach.skitesting.fragments.skiProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import cz.erlebach.skitesting.R

class Blank1Fragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val w = inflater.inflate(R.layout.fragment_blank1, container, false)
            w.findViewById<Button>(R.id.testBtn).setOnClickListener {
                findNavController().navigate(R.id.action_blank1Fragment_to_skiListFragment)
            }

        return w
    }


}