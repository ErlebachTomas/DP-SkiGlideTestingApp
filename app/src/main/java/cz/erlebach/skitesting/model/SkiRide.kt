package cz.erlebach.skitesting.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.erlebach.skitesting.MyDatabase

/**
 * Třída reprezentuje jeden záznam konkrétního měření dané lyže
 */
@Entity(tableName = MyDatabase.skiRideTableName)
data class SkiRide(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    //todo FK https://stackoverflow.com/a/65754091
    val skiID: Int,
    val testSessionID: Int,
    val time: Int, //todo timestemp
    val note: String?
)