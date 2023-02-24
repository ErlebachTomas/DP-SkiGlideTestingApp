package cz.erlebach.skitesting.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
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
    @SerializedName("UUID")
    override val id: String = generateID(),
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("icon")
    val icon: String? = null,
    @SerializedName("updated_at")
    override val updatedAt: String = generateDate()
) : BaseModel(), Parcelable {
    override fun toString(): String {

        return if(description == null) {
            "[$updatedAt] $name |$id|"
        } else {
            "[$updatedAt] $name ($description) |$id|"
        }
    }

}
