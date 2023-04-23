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
import cz.erlebach.skitesting.fragments.template.MyViewModelFactory
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.common.utils.toast
import cz.erlebach.skitesting.repository.SkiRepository
import cz.erlebach.skitesting.viewModel.SkiVM


class SkiListFragment : Fragment() {

    private lateinit var viewModel: SkiVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ski_list, container, false)

        initVM(view)

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

    private fun initVM(view: View) {

        val adapter = SkiListAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.fsl_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val viewModelFactory = MyViewModelFactory(SkiVM(SkiRepository(requireContext())))

        try {
            viewModel = ViewModelProvider(this, viewModelFactory)[SkiVM::class.java]
            viewModel.data.observe(viewLifecycleOwner) { resource ->
              resource.data?.let {
                    adapter.setData(it) }
                }

        } catch (err: IllegalStateException) {
                toast(requireContext(), err.message.toString())
       } catch (E: Exception) {
           toast(requireContext(), E.message.toString())
        }


    }

    /** vymazat vše */
    private fun deleteAllItems() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->

             viewModel.deleteAll()

            Toast.makeText(requireContext(), getString(R.string.delete_success_message), Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton(R.string.No) {_, _ ->}

        builder.setTitle( getString(R.string.delete))
        builder.setMessage(getString(R.string.delete_everything))
        builder.create().show()
    }


}