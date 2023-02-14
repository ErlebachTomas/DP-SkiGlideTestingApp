package cz.erlebach.skitesting.model

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
    open val updated_at: String = generateDate(),
    var status: DataStatus = DataStatus.UNKNOWN
) {
    companion object {
        /**
         * Vygeneruje timestemp
         * @see generateDateISO8601string
         */
        @JvmStatic
        protected fun generateDate(): String {
            return generateDateISO8601string()
        }

        /**
         * Vygeneruje unikátní ID
         * @see UUID
         */
        @JvmStatic
        protected fun generateID(): String {
            return UUID.randomUUID().toString()
        }

    }
    /**
     * Porovná [updated_at]
     * @param model třída pro porovnání
     * @return true pokud je tato třída starší než vložená třída [model]
     */
    fun isOlder(model: BaseModel): Boolean {
        return getDateFromISO8601(this.updated_at).before(getDateFromISO8601(model.updated_at))
    }

    /**
     * Porovná [updated_at]
     * @see isOlder opak
     */
    fun isNewer(model: BaseModel): Boolean {
        return getDateFromISO8601(this.updated_at).after(getDateFromISO8601(model.updated_at))
    }

}