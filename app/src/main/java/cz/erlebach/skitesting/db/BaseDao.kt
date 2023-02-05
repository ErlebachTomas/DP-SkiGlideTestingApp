package cz.erlebach.skitesting.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Zakládní template Data Access Object rozhraní pro práci s ROOM databází, definující základní operace
 * @param T model class
 */
interface BaseDao<T> {

    /**
     * Vložení objektu do databáze
     * @param obj, který má být vložen do databáze
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
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