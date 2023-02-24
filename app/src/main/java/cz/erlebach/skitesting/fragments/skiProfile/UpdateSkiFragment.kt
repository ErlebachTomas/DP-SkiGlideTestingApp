package cz.erlebach.skitesting.fragments.skiProfile

import android.app.AlertDialog
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
import cz.erlebach.skitesting.common.utils.generateDateISO8601string
import cz.erlebach.skitesting.viewModel.local.SkiVM

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
    // // todo private lateinit var remoteVM : SkiRemoteVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_ski_update_ski, container, false)

        viewModel = ViewModelProvider(this)[SkiVM::class.java]

        //todo initVM()

        showItems(view)

        view.findViewById<Button>(R.id.fus_btnSave).setOnClickListener {
            updateItem(view)
        }

        view.findViewById<Button>(R.id.fus_btn_back).setOnClickListener {
            findNavController().navigate(R.id.action_updateSkiFragment_to_skiListFragment)
        }

        view.findViewById<Button>(R.id.fus_btn_delete).setOnClickListener {
            deleteItem()
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
            val updatedSki = Ski(
                id=args.currentSki.id,
                name=newName,
                null,
                updatedAt = generateDateISO8601string()
            )

            viewModel.updateSki(updatedSki)
            // todo remoteVM.update(updatedSki)

            Toast.makeText(requireContext(), getString(R.string.update_success_message), Toast.LENGTH_SHORT).show()


            findNavController().navigate(R.id.action_updateSkiFragment_to_skiListFragment)
        }
    }
    /** Zobrazí dialog a vymaže záznam */
    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->

            viewModel.deleteSki(args.currentSki)
            //todo remoteVM.delete(args.currentSki)

            Toast.makeText(requireContext(), getString(R.string.delete_success_message), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateSkiFragment_to_skiListFragment)
        }
        builder.setNegativeButton(R.string.No) { _, _ -> }

        builder.setTitle( getString(R.string.delete_info,args.currentSki.name))
        builder.setMessage(getString(R.string.delete_message,args.currentSki.name))
        builder.create().show()
    }
    /* todo
    private fun initVM() {

        val account = SessionManager.getInstance(requireContext())

        val repository = SkiRemoteRepository(requireContext())
        val viewModelFactory = MyViewModelFactory(SkiRemoteVM(repository,account))
        remoteVM = ViewModelProvider(this, viewModelFactory)[SkiRemoteVM::class.java]

    }
    */
}