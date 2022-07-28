package cz.erlebach.skitesting.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.erlebach.skitesting.MyDatabase
import java.util.*


/**
 * reprezentuje profil jednoho měření
 */
@Entity(tableName = MyDatabase.testSessionsTableName)
data class TestSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val datetime: Date,
    val airTemperature: Double,
    val snowTemperature: Double,
    val snowType: String,
    val humidity: Double?
    // todo snowType distribuce?

)