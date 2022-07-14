package cz.erlebach.skitesting.fragments.skiProfile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.erlebach.skitesting.R

import cz.erlebach.skitesting.viewModel.SkiVM


class SkiListFragment : Fragment() {

    private lateinit var skiViewModel: SkiVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ski_list, container, false)

        init(view) // nastaví adaptér

        view.findViewById<View>(R.id.fsl_btnAddSki).setOnClickListener {
            // přepnutí fragmentu na vkládání přes nav
            findNavController().navigate(R.id.action_skiListFragment_to_addSkiFragment)
        }

        view.findViewById<View>(R.id.fsl_btnBack).setOnClickListener {
            // ukončit aktivitu
            activity?.finish()

        }
        view.findViewById<View>(R.id.fsl_btnDelete).setOnClickListener {
            deleteAllItems()
        }


        return view
    }

    /** Inicializace vm a adaptéru */
    private fun init(view : View ) {

         skiViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
            SkiVM::class.java
        )

        val adapter = SkiListAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.fsl_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        skiViewModel.readAllData.observe(viewLifecycleOwner) { ski ->
            adapter.setData(ski)
        }

    }
    /** vymazat vše */
    private fun deleteAllItems() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->

            skiViewModel.deleteAll()

            Toast.makeText(requireContext(), getString(R.string.delete_success_message), Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton("No") {_, _ ->}

        builder.setTitle( getString(R.string.delete))
        builder.setMessage(getString(R.string.delete_everything))
        builder.create().show()
    }


}