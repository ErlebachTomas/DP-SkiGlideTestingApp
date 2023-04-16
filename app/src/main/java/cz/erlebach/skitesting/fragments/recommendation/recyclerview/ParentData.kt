package cz.erlebach.skitesting.fragments.recommendation.recyclerview

data class ParentData(
    val text:String? = null,
    var type: Int = ViewType.PARENT_VIEW,
    var subList : MutableList<ChildData> = ArrayList(),
    var isExpanded:Boolean = false
)