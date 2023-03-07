package cz.erlebach.skitesting.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import cz.erlebach.skitesting.MyDatabase
import kotlinx.parcelize.Parcelize
import java.util.*


/**
 * reprezentuje profil jednoho měření
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



}