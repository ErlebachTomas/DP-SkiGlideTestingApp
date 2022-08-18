package cz.erlebach.skitesting.repository
import cz.erlebach.skitesting.db.BaseDao

/**
 * @param T model class
 */
abstract class BaseRepository<T>(private val baseDao: BaseDao<T>) {


    suspend fun update(entity: T) {
        return  baseDao.update(entity)
    }
    suspend fun insert(entity: T) {
        baseDao.insert(entity)
    }
    suspend fun delete(entity: T) {
       baseDao.delete(entity)
    }

}


