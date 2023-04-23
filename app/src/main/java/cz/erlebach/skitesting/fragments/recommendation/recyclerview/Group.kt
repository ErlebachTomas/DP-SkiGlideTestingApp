package cz.erlebach.skitesting.fragments.recommendation.recyclerview

import cz.erlebach.skitesting.network.model.recomendation.RecommendationDataBody
import cz.erlebach.skitesting.network.model.recomendation.SkiResult

data class Group(
    val data: RecommendationDataBody? = null,
    var subList : MutableList<SkiResult> = ArrayList(),

    var type: Int = ViewType.PARENT_VIEW,
    var isExpanded:Boolean = false
)