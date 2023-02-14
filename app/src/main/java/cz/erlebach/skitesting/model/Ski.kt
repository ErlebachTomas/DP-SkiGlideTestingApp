package cz.erlebach.skitesting.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.erlebach.skitesting.MyDatabase
import kotlinx.parcelize.Parcelize
import java.util.*


/**
 * Třída reprezentuje profil Lyže
 * @param id identifikátor
 * @param name název lyže
 * @param description popis
 */
@Parcelize
@Entity(tableName = MyDatabase.skiTableName)
data class Ski (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = generateID(),
    val name: String,
    val description: String? = null,
    val icon: String? = null,
    override val updated_at : String = generateDate()
) : BaseModel(), Parcelable {

    override fun toString(): String {

        return if(description == null) {
            "[$id] $name"
        } else {
            "[$id] $name ($description)"
        }
    }

} // parcelable nutné pro možnost přidání jako argument k fragmentu
