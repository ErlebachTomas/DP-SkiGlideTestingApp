package cz.erlebach.skitesting.network.model
import com.google.gson.annotations.SerializedName
import cz.erlebach.skitesting.model.Ski


data class SkiDataBody(
    @SerializedName("userID") val userID: String,
    @SerializedName("ski") val ski: Ski
    )
