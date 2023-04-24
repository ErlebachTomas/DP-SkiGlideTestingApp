package cz.erlebach.skitesting.fragments.measurement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.utils.date.getDateFormatString
import cz.erlebach.skitesting.databinding.AdapterFragmentMeasurementListContentBinding
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.model.TestSession


/**
 * [RecyclerView.Adapter] který zobrazuje [TestSession] pro [MeasurementListFragment].
 */
class MeasurementRecyclerViewAdapter(var context: Context
):RecyclerView.Adapter<MeasurementRecyclerViewAdapter.ViewHolder>() {

    private var values: List<TestSession> = emptyList<TestSession>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            AdapterFragmentMeasurementListContentBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val testSession = values[position]


        holder.dateTV.text = getDateFormatString(testSession.datetime)

        holder.airTemperatureTV.text = testSession.airTemperature.toString()
        holder.snowTemperatureTV.text = testSession.snowTemperature.toString()
        holder.humidityTV.text = testSession.humidity.toString()
        holder.noteTV.text = testSession.note


        holder.testTypeTV.text = testSession.getTestTypeString(context)
        holder.snowTypeTV.text = testSession.getSnowTypeString(context)

        holder.itemView.findViewById<View>(R.id.adap_layout_measurement_row).setOnClickListener {

            val action = MeasurementListFragmentDirections.actionMeasurementFragmentToSkiRideListFragment(
                testSession.id
            )
            holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.findViewById<View>(R.id.adap_layout_measurement_row).setOnLongClickListener {

         val action = MeasurementListFragmentDirections.actionMeasurementFragmentToMeasurementUpdateFragment(
             testSession)

         holder.itemView.findNavController().navigate(action)

            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: AdapterFragmentMeasurementListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val testTypeTV : TextView = binding.mlTestType
        val dateTV: TextView = binding.mlDate

        val airTemperatureTV: TextView = binding.mlAirTemperature
        val snowTemperatureTV: TextView = binding.mlSnowTemperature
        val humidityTV: TextView = binding.mlHumidity
        val noteTV: TextView = binding.mlNote
        val snowTypeTV: TextView = binding.mlSnowType

    }

    /**
     * Vloží data do adaptéru
     */
    fun setData(list: List<BaseModel>){
        this.values = list.filterIsInstance<TestSession>()
        notifyDataSetChanged()
    }

}