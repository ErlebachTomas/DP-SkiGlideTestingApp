package cz.erlebach.skitesting.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import cz.erlebach.skitesting.MyDatabase
import kotlinx.parcelize.Parcelize

/**
 * Třída reprezentuje jeden záznam konkrétního měření dané lyže
 */
@Parcelize
@Entity(
    tableName = MyDatabase.skiRideTableName,
    foreignKeys = [
        ForeignKey(
            entity = TestSession::class,
            parentColumns = ["id"],
            childColumns = ["testSessionID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Ski::class,
            parentColumns = ["id"],
            childColumns = ["skiID"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["skiID"]),
        Index(value = ["testSessionID"])
    ]
)
data class SkiRide(
    @PrimaryKey
    @ColumnInfo(name = "id")
    /** id [SkiRide] */
    @SerializedName("UUID")
    override val id: String = generateID(),
    /** FK [Ski] */
    @ColumnInfo(name = "skiID")
    @SerializedName("skiID")
    val skiID: String,
    /** FK [Tessession] */
    @ColumnInfo(name = "testSessionID")
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