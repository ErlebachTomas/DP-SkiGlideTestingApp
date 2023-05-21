package cz.erlebach.skitesting.fragments.recommendation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.utils.wtf
import cz.erlebach.skitesting.fragments.recommendation.recyclerview.Group
import cz.erlebach.skitesting.fragments.recommendation.recyclerview.ViewType
import cz.erlebach.skitesting.network.model.recomendation.SkiResult

/** Adaptér pro [ResultFragment] */
class ResultAdapter(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: MutableList<Group> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == ViewType.PARENT_VIEW) {
            val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_rc_content_parent_row, parent,false)
            GroupViewHolder(rowView)
        } else {
            val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_rc_content_child_row, parent,false)
            ChildViewHolder(rowView)
        }

    }
    /**
     * Nastaví view pro nadřazenou/podřízenou položku
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataList = list[position]

        when (dataList.type) {
            ViewType.PARENT_VIEW -> {
                bindParent(dataList, holder as? GroupViewHolder )
            }
            ViewType.CHILD_VIEW -> {
               bindChild(dataList, holder as? ChildViewHolder)
            }
        }
    }

    private fun bindChild(dataList: Group, groupViewHolder: ResultAdapter.ChildViewHolder?) {
        groupViewHolder?.apply {
            val data = dataList.subList.firstOrNull()
            skiNameTV?.text = data?.skiName
            diffTV?.text = String.format("%.2f",  data?.differenceFromBest)
            scoreTV?.text = data?.getPercentageScore()
            skiDescription?.text = data?.skiDescription
        }
    }

    private fun bindParent(g: Group, holder: GroupViewHolder?) {
        val testData = g.data?.testData
        holder?.apply {


           tittleTV.text = testData?.datetime.toString() //testData?.getFormatedDatetime()

           testTypeTV.text = testData?.getTestTypeString(context)
           snowTypeTV.text = testData?.getSnowTypeString(context)
           snowTemperatureTV.text = testData?.snowTemperature.toString()
           airTemperatureTV.text = testData?.airTemperature.toString()
           humidityTV.text = testData?.humidity.toString()

           distanceTV.text =  g.data?.formatedDistance()
           angleTV.text = g.data?.angleInDegree()
           noteTV.text = testData?.note

            btnDetail.setOnClickListener {
                expandOrCollapseParentItem(g, absoluteAdapterPosition)
                // expandOrCollapseParentItem(g, position)
            }
        }

    }
    private fun expandOrCollapseParentItem(singleBoarding: Group, position: Int) {

        if (singleBoarding.isExpanded) {
            collapseParentRow(position)
        } else {
            expandParentRow(position)
        }
    }


    private fun expandParentRow(position: Int) {
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.subList

        currentBoardingRow.isExpanded = true

        var nextPosition = position
        if (currentBoardingRow.type == ViewType.PARENT_VIEW) {

            for (service in services) {
                val parentModel = Group()
                parentModel.type = ViewType.CHILD_VIEW
                val subList: ArrayList<SkiResult> = ArrayList()
                subList.add(service)
                parentModel.subList = subList
                list.add(++nextPosition, parentModel)
            }

            notifyDataSetChanged()
        }
    }

    private fun collapseParentRow(position: Int) {

        val currentBoardingRow = list[position]
        val services = currentBoardingRow.subList
        currentBoardingRow.isExpanded = false

        if (currentBoardingRow.type == ViewType.PARENT_VIEW) {
            val count = services.size
            if (count > 0) {
                list.subList(position + 1, position + 1 + count).clear()
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(position: Int): Int = list[position].type

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



    inner class GroupViewHolder(row: View) : RecyclerView.ViewHolder(row) {


        val tittleTV = row.findViewById(R.id.rca_parent_tittle) as TextView
        val btnDetail = row.findViewById(R.id.btn_detail) as Button

        val snowTypeTV = row.findViewById(R.id.rca_snowType) as TextView
        val testTypeTV = row.findViewById(R.id.rca_testType) as TextView
        val airTemperatureTV = row.findViewById(R.id.rca_airTemperature) as TextView
        val snowTemperatureTV = row.findViewById(R.id.rca_snowTemperature) as TextView
        val humidityTV = row.findViewById(R.id.rca_humidity) as TextView
        val noteTV = row.findViewById(R.id.rca_note) as TextView
        val distanceTV = row.findViewById(R.id.rca_distance) as TextView
        val angleTV = row.findViewById(R.id.rca_angle) as TextView
    }

    inner class ChildViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val skiNameTV = row.findViewById(R.id.rc_skiName) as TextView?
        val scoreTV = row.findViewById(R.id.rc_score) as TextView?
        val diffTV = row.findViewById(R.id.rc_differenceFromBest) as TextView?
        val skiDescription = row.findViewById(R.id.rc_skiDescription) as TextView?

    }

    fun setData(data: List<Group>) {
        this.list = data.toMutableList()
        expandParentRow(0) // nejlepší bude už rozbalen

        notifyDataSetChanged()
    }

}
