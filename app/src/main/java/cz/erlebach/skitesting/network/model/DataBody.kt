package cz.erlebach.skitesting.network.model

import com.google.gson.annotations.SerializedName
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.model.Ski

data class DataBody(
    @SerializedName("userID") val userID: String,
    @SerializedName("data") val data: BaseModel
)



