package cz.erlebach.skitesting.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val id: String,
    val datetime: Date,
    val airTemperature: Double,
    val snowTemperature: Double,
    val snowType: Int,
    val testType: Int,
    val humidity: Double?,
    val note: String?,
    val updated_at: String?
): Parcelable