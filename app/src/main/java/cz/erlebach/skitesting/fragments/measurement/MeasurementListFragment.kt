package cz.erlebach.skitesting.fragments.measurement

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.viewModel.TestSessionVM

/**
 * A fragment representing a list of Items.
 */
class MeasurementListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_measurement_list_layout, container, false)

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[TestSessionVM::class.java]

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = MeasurementRecyclerViewAdapter()

                val adapter = MeasurementRecyclerViewAdapter()
                viewModel.readAllData.observe(viewLifecycleOwner) { item ->
                    adapter.setData(item)
                }
            }
        }
        return view
    }

}