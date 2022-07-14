package cz.erlebach.skitesting.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.erlebach.skitesting.MyDatabase
import kotlinx.parcelize.Parcelize

/**
 * Třída reprezentuje profil Lyže
 */
@Parcelize
@Entity(tableName = MyDatabase.skiTableName)
data class Ski(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    val name: String,
    val description: String?
): Parcelable
// parcelable nutné pro možnost přidání jako argument k fragmentu
// todo val description: String? + color?