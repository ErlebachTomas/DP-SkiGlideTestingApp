package cz.erlebach.skitesting.common.utils.dataStatus

/**
 * Číselník pro flag atribut,
 * vyjadřuje status dat ve vztahu k synchronizaci se serverem
 */
enum class DataStatus {
    /**
     * Změna provedena pouze offline
     */
    OFFLINE,
    /**
     * Synchronizováno se serverem, data jsou aktuální
     */
    ONLINE,
    /**
     * Neznámý status, ještě neproběhla synchronizace
     */
    UNKNOWN,
    /**
     * Objekt byl lokálně smazán, ale ještě existuje na serveru
     */
    REMOVED
}

