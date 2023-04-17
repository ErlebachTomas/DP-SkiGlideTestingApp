package cz.erlebach.skitesting.fragments.recommendation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.fragments.recommendation.recyclerview.ChildData
import cz.erlebach.skitesting.fragments.recommendation.recyclerview.Group
import cz.erlebach.skitesting.fragments.recommendation.recyclerview.ViewType
import cz.erlebach.skitesting.network.model.recomendation.SkiResult

/** Adaptér pro [ResultFragment] */
class ResultAdapter(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: MutableList<Group> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == ViewType.PARENT_VIEW) {
            val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.content_parent_row, parent,false)
            GroupViewHolder(rowView)
        } else {
            val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.content_child_row, parent,false)
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
                (holder as? GroupViewHolder)?.apply {

                    parentTV?.text = dataList.data?.testData?.UUID + " " + dataList.data?.distance.toString()

                    downIV.setOnClickListener {
                        expandOrCollapseParentItem(dataList, position)
                    }
                }
            }

            ViewType.CHILD_VIEW -> {
                (holder as? ChildViewHolder)?.apply {
                    val singleService = dataList.subList.firstOrNull()
                    childTV?.text = "${singleService?.skiName} ${singleService?.mean}"
                }
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

    class GroupViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val parentTV = row.findViewById(R.id.parent_Title) as TextView?
        val downIV = row.findViewById(R.id.down_iv) as Button
    }
    class ChildViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val childTV = row.findViewById(R.id.child_Title) as TextView?

    }

    fun setData(data: List<Group>) {
        this.list = data.toMutableList()
        expandParentRow(0) // nejlepší bude už rozbalen

        notifyDataSetChanged()
    }

}
