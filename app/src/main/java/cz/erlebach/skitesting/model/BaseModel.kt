package cz.erlebach.skitesting.model

import com.google.gson.annotations.SerializedName
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.common.utils.generateDateISO8601string
import cz.erlebach.skitesting.common.utils.getDateFromISO8601
import java.util.*

/**
 * Abstraktní třída pro data
 * @param updated_at čas poslední modifikace
 * @param status stav synchronizace
 */
abstract class BaseModel (
    @SerializedName("status")
    var status: DataStatus = DataStatus.UNKNOWN
) {
    /**
     * Jednoznačný identifikátor [id]
     */
    abstract val id :String

    /**
     * Čas poslední modifikace (slouží pro porovnání aktuálnosti záznamu)
     */
    abstract val updatedAt: String
    companion object {
        /**
         * Vygeneruje timestemp
         * @see generateDateISO8601string
         */
        @JvmStatic
        fun generateDate(): String {
            return generateDateISO8601string()
        }

        /**
         * Vygeneruje unikátní ID
         * @see UUID
         */
        @JvmStatic
        fun generateID(): String {
            return UUID.randomUUID().toString()
        }

    }
    /**
     * Porovná [updatedAt]
     * @param model třída pro porovnání
     * @return true pokud je tato třída starší než vložená třída [model]
     */
    fun isOlder(model: BaseModel): Boolean {
        return getDateFromISO8601(this.updatedAt).before(getDateFromISO8601(model.updatedAt))
    }

    /**
     * Porovná [updatedAt]
     * @see isOlder opak
     */
    fun isNewer(model: BaseModel): Boolean {
        return getDateFromISO8601(this.updatedAt).after(getDateFromISO8601(model.updatedAt))
    }

}