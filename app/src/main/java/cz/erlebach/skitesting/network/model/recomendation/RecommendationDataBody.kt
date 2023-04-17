package cz.erlebach.skitesting.network.model.recomendation
import com.google.gson.annotations.SerializedName


data class RecommendationDataBody(
    @SerializedName("skiResults") val skiResults: List<SkiResult>,
    @SerializedName("id") val id: Int,
    @SerializedName("data") val testData: TestData,
    @SerializedName("vector") val vector: List<Double>,
    @SerializedName("distance") val distance: Double,
    @SerializedName("angle") val angle: Double
)
