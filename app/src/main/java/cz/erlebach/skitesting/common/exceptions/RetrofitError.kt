package cz.erlebach.skitesting.common.exceptions
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody

class RetrofitError(
    @SerializedName("error")
    val errorCode: Int = 0,
    @SerializedName("msg")
    val msg: String? = null,
    val errorBody: ResponseBody?
): Exception()