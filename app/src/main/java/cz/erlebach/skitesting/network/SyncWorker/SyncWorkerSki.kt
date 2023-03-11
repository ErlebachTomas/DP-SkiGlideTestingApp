package cz.erlebach.skitesting.network.SyncWorker

import android.content.Context
import androidx.room.withTransaction
import androidx.work.WorkerParameters
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.SkiDataBody
import retrofit2.Response

class SyncWorkerSki(context: Context,
                    params: WorkerParameters
): SyncWorker<Ski>(context, params) {

    private var api = RetrofitApiService(this.context).skiAPI
    private var database = MyDatabase.getDatabase(applicationContext).skiDao()

    override suspend fun syncData(data: Ski): Response<Ski> {
       return api.syncData(SkiDataBody(super.account.getUserID(), data))
    }

    override suspend fun unsynchronizedData(): List<Ski> {
        return database.getDataByStatus(DataStatus.UNKNOWN) +
                database.getDataByStatus(DataStatus.OFFLINE)
    }

    override suspend fun loadDataForRemoval(): List<Ski> {
        return database.getDataByStatus(DataStatus.REMOVED) // todo sync remove
    }

    override suspend fun deleteData(data: Ski) {
        db.withTransaction {
            api.delete(SkiDataBody(userID = super.account.getUserID(), ski = data))
            database.delete(data)
        }
    }

    override suspend fun updateData(data: Ski) {
        data.status = DataStatus.ONLINE
        database.updateSki(data)
        lg("nahrávám offline záznam ${data.name} na server")

    }
}