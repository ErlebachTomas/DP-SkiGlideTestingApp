package cz.erlebach.skitesting.repository.local
import cz.erlebach.skitesting.db.BaseDao


abstract class LocalBaseRepository<T>(private val baseDao: BaseDao<T>) {

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


