package cz.erlebach.skitesting.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import cz.erlebach.skitesting.MyDatabase
import kotlinx.parcelize.Parcelize

/**
 * Třída reprezentuje jeden záznam konkrétního měření dané lyže
 */
@Parcelize
@Entity(tableName = MyDatabase.skiRideTableName)
data class SkiRide(
    @PrimaryKey
    @ColumnInfo(name = "id")
    /** id [SkiRide] */
    @SerializedName("UUID")
    override val id: String = generateID(),
    /** FK [Ski] */ //undone FK https://stackoverflow.com/a/65754091
    @SerializedName("skiID")
    val skiID: String,
    /** FK [Tessession] */
    @SerializedName("testSessionID")
    val testSessionID: String,
    @SerializedName("result")
    val result: Double,
    @SerializedName("note")
    val note: String?,
    @SerializedName("updated_at")
    override val updatedAt: String = generateDate(),
): BaseModel(), Parcelable {
    override fun toString(): String {

        return "[$updatedAt] : $result"

    }

}