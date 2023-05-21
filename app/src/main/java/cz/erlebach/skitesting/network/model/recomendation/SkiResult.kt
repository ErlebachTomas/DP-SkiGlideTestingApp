package cz.erlebach.skitesting.network.model.recomendation

import com.google.gson.annotations.SerializedName

data class SkiResult(
    @SerializedName("differenceFromBest") val differenceFromBest: Double,
    @SerializedName("score") val score: Double,
    @SerializedName("skiUUID") val skiUUID: String,
    @SerializedName("skiName") val skiName: String,
    @SerializedName("skiDescription") val skiDescription: String?,
    @SerializedName("results") val results: List<Double>,
    @SerializedName("mean") val mean: Double
) {
    fun getPercentageScore(): String {
        return String.format("%.2f%", this.score * 100)

    }


}