package cz.erlebach.skitesting.network.model.recomendation

import com.google.gson.annotations.SerializedName

data class TestData(
    @SerializedName("UUID") val UUID: String,
    @SerializedName("ownerUserID") val ownerUserID: String,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("airTemperature") val airTemperature: Double,
    @SerializedName("snowTemperature") val snowTemperature: Double,
    @SerializedName("snowType") val snowType: Int,
    @SerializedName("testType") val testType: Int,
    @SerializedName("status") val status: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("humidity") val humidity: Double?,
    @SerializedName("note") val note: String?
)
