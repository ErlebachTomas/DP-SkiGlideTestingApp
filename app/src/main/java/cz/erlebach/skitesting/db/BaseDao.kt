package cz.erlebach.skitesting.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Zakládní template Data Access Object rozhraní pro práci s ROOM databází, definující základní operace
 */
interface BaseDao<T> {

    /**
     * Vložení objektu do databáze
     * @param obj, který má být vložen do databáze
     */
    @Insert
    suspend fun insert(obj: T)

    /**
     * Aktualizace objektu z databáze
     * @param obj objekt, který má být aktualizován
     */
    @Update
    suspend fun update(obj: T)

    /**
     * Odstranění objektu z databáze
     * @param obj objekt, který má být odstraněn
     */
    @Delete
    suspend fun delete(obj: T)


}