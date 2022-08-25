package cz.erlebach.skitesting.fragments.measurement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.AdapterFragmentMeasurementSkiRideListBinding
import cz.erlebach.skitesting.model.SkiRide


/**
 * [RecyclerView.Adapter] který zobrazuje [SkiRide] pro [SkiRideListFragment].
 */
class SkiRideListAdapter(
):RecyclerView.Adapter<SkiRideListAdapter.ViewHolder>() {

    private var values: List<SkiRide> = emptyList<SkiRide>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            AdapterFragmentMeasurementSkiRideListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSkiRide = values[position]

        val temp = currentSkiRide.skiID//todo holder ski name misto id

        holder.content.text = "[ID lyže:" + temp + "] " + currentSkiRide.result.toString()


        holder.itemView.findViewById<View>(R.id.adap_layout_skiRide_row).setOnClickListener {

            val action = SkiRideListFragmentDirections.actionSkiRideListFragmentToUpdateSkiRideFragment(
                currentSkiRide
                )

            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: AdapterFragmentMeasurementSkiRideListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        ///todo holder + ski name
        val content: TextView = binding.srlContent

        override fun toString(): String {
            return super.toString() + " '" + content.text + "'"
        }
    }

    fun setData(list: List<SkiRide>){
        this.values = list
        notifyDataSetChanged()
    }

}