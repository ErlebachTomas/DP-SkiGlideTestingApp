package cz.erlebach.skitesting.fragments.skiProfile

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.viewModel.SkiVM

/**
 * Fragment editace profilu lyže
 */
class UpdateSkiFragment : Fragment() {

    /**
     * Parametry fragmentu s využitím techniky navigation-safe-args
     */
    private val args by navArgs<UpdateSkiFragmentArgs>()

    /**
     * viewModel lyží
     */
    private lateinit var viewModel: SkiVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_update_ski, container, false)

        viewModel = ViewModelProvider(this)[SkiVM::class.java]

        showItems(view)

        view.findViewById<Button>(R.id.fus_btnSave).setOnClickListener {
            updateItem(view)
        }

        return view
    }

    /**
     * Vypíše do formuláře aktuální uložené údaje předáné v argumentu
     */
    private fun showItems(view: View) {

        view.findViewById<TextView>(R.id.fus_tx_name).text = args.currentSki.name

    }

    /** Editace hotnot a přepsání záznamu, při úspěchu přejde na list */
    private fun updateItem(view : View){

        val newName = view.findViewById<TextView>(R.id.fus_tx_name).text.toString()

        if (TextUtils.isEmpty(newName)) {
        // špatně vyplněno
            Toast.makeText(requireContext(),getString(R.string.update_failure_message) , Toast.LENGTH_SHORT).show()
        } else {
            val updatedSki = Ski(args.currentSki.id, newName, null)
            viewModel.updateSki(updatedSki)
            Toast.makeText(requireContext(), getString(R.string.update_success_message), Toast.LENGTH_SHORT).show()


            findNavController().navigate(R.id.action_updateSkiFragment_to_skiListFragment)
        }
    }


}