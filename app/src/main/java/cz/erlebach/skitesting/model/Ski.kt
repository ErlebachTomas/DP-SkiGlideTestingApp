package cz.erlebach.skitesting.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import kotlinx.parcelize.Parcelize


/**
 * Třída reprezentuje profil Lyže
 */
@Parcelize
@Entity(tableName = MyDatabase.skiTableName)
data class Ski(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    val name: String,
    val description: String?,
    val updated_at: String?,
    var status: DataStatus?
    //todo not null status
): Parcelable {

    override fun toString(): String {

        return if( description == null) {
            "[$id] $name"
        } else {
            "[$id] $name ($description)"
        }
    }
}
// parcelable nutné pro možnost přidání jako argument k fragmentu
// todo val description: String? + color?