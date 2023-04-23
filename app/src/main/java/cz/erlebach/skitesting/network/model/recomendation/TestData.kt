package cz.erlebach.skitesting.network.model.recomendation

import android.content.Context
import com.google.gson.annotations.SerializedName
import cz.erlebach.skitesting.common.utils.date.getDateFormatString
import cz.erlebach.skitesting.model.TestSession


/** Reprezentuje [TestSession] na serveru */
data class TestData (
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
) {
    /**
    Vrátí řetězec popisující typ testu na základě předaného číselného kódu. */
    fun getTestTypeString(context: Context): String {
        return TestSession.getTestTypeString(context, this.testType)
    }
    /**
    Vrátí řetězec popisující typ sněhu na základě předaného číselného kódu.*/
    fun getSnowTypeString(context: Context): String {
        return TestSession.getSnowTypeString(context, this.snowType)
    }

    fun getFormatedDatetime(pattern: String = "MMMM d yyyy h:mm"): String {
        return getDateFormatString(this.datetime, pattern)
    }


}
