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
    @PrimaryKey
    @ColumnInfo(name = "id")
    /** id [SkiRide] */
    val id: String,
    //todo FK https://stackoverflow.com/a/65754091
    /** FK [Ski] */
    val skiID: String,
    /** FK [Tessession] */
    val testSessionID: String,
    val result: Double,
    val note: String?,
    val updated_at: String?
) : Parcelable