package cz.erlebach.skitesting.network.model.recomendation
import com.google.gson.annotations.SerializedName
import kotlin.math.acos


/**
 * Odpověd ze serveru
 */
data class RecommendationDataBody(
    @SerializedName("skiResults") val skiResults: List<SkiResult>,
    @SerializedName("id") val id: Int,
    @SerializedName("testData") val testData: TestData,
    @SerializedName("vector") val vector: List<Double>,
    @SerializedName("distance") val distance: Double,
    @SerializedName("angle") val angle: Double
) {

    /** Vypočte úhel na dvě desetiny ve stupních */
     fun angleInDegree(): String {
        return String.format("%.2f°",Math.toDegrees(acos(this.angle)))
    }

    /**
     * Míra podobnosti testů
     */
    fun formatedDistance(): String {
        return String.format("%.2f", this.distance)
    }


}