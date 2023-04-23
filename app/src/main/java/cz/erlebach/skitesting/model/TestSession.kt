package cz.erlebach.skitesting.model

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.R
import kotlinx.parcelize.Parcelize
import java.util.*


/**
 * Třída reprezentuje profil jednoho měření
 */
@Parcelize
@Entity(tableName = MyDatabase.testSessionsTableName)
data class TestSession(
    @PrimaryKey
    @SerializedName("UUID")
    override val id: String = generateID(),
    @SerializedName("datetime")
    val datetime: Date,
    @SerializedName("airTemperature")
    val airTemperature: Double,
    @SerializedName("snowTemperature")
    val snowTemperature: Double,
    @SerializedName("snowType")
    val snowType: Int,
    @SerializedName("testType")
    val testType: Int,
    @SerializedName("humidity")
    val humidity: Double?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("updated_at")
    override val updatedAt: String = generateDate()
): BaseModel(), Parcelable {

    override fun toString(): String {
        return "$datetime $testType"
    }
    fun getTestTypeString(context: Context): String {
        return TestSession.getTestTypeString(context, this.testType)
    }
    fun getSnowTypeString(context: Context): String {
        return TestSession.getSnowTypeString(context, this.snowType)
    }

    companion object {

        /**
        Metoda pro získání řetězce z pole definovaného v resources, na základě indexu
        *@param context kontext aktivity nebo fragmentu
        *@param value index požadovaného řetězce
        *@param arrayID identifikátor pole v resources
        *@param defaultVal výchozí hodnota vrácená v případě, že index je mimo rozsah pole nebo hodnota indexu je menší nebo rovna nule
        * @return řetězec z pole podle zadaného indexu, nebo výchozí hodnota
         */
        private fun getRString(context: Context, value: Int, arrayID : Int, defaultVal: String = ""): String {
            val strings = context.resources.getStringArray(arrayID)
            return if (value >= 0 && strings.size > value) {
                strings[value]
            } else {
                 defaultVal
            }
        }

        /**

        Vrátí řetězec popisující typ testu na základě předaného číselného kódu.
        @param context kontext aktivity nebo aplikace pro přístup k zdrojům.
        @param testType číselný kód popisující typ testu.
        @return řetězec popisující typ testu.
         */
        fun getTestTypeString(context: Context, testType: Int): String {
            return getRString(context, testType, R.array.testType)
        }
        /**

        Vrátí řetězec popisující typ sněhu na základě předaného číselného kódu.
        @param context kontext aktivity nebo aplikace pro přístup k zdrojům.
        @param snowType číselný kód popisující typ sněhu.
        @return řetězec popisující typ sněhu.
         */
        fun getSnowTypeString(context: Context, snowType: Int): String {
            return TestSession.getRString(context, snowType, R.array.snowType)
        }
    }
}