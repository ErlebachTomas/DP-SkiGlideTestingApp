package cz.erlebach.skitesting.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.erlebach.skitesting.MyDatabase
import kotlinx.parcelize.Parcelize

/**
 * Třída reprezentuje jeden záznam konkrétního měření dané lyže
 */
@Parcelize
@Entity(tableName = MyDatabase.skiRideTableName)
data class SkiRide(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    /** id [SkiRide] */
    val id: Int,
    //todo FK https://stackoverflow.com/a/65754091
    /** FK [Ski] */
    val skiID: Int,
    /** FK [Tessession] */
    val testSessionID: Long,
    val result: Double,
    val note: String?,
    val updated_at: String?
) : Parcelable