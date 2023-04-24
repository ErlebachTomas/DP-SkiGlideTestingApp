package cz.erlebach.skitesting.model.wrappers

import androidx.room.Embedded
import androidx.room.Relation
import com.google.gson.annotations.SerializedName
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.SkiRide

/** Obalová třída pro [SkiRide] a [Ski] */
data class SkiRideWithSki(
    @Embedded
    @SerializedName("skiRide")
    val skiRide: SkiRide,
    @Relation(
        parentColumn = "skiID",
        entityColumn = "id"
    )
    @SerializedName("ski")
    val ski: Ski
)