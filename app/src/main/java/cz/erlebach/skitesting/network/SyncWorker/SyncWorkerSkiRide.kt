package cz.erlebach.skitesting.network.SyncWorker

import android.content.Context
import androidx.room.withTransaction
import androidx.work.WorkerParameters
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.GeneralDataBody
import retrofit2.Response

class SyncWorkerSkiRide(context: Context,
                        params: WorkerParameters
): SyncWorker<SkiRide>(context, params) {

    private var api = RetrofitApiService(this.context).skiRideAPI
    private var database = MyDatabase.getDatabase(applicationContext).skiRideDao()

    override suspend fun syncData(data: SkiRide): Response<SkiRide> {
       return api.syncData(GeneralDataBody(super.account.getUserID(), data))
    }

    override suspend fun unsynchronizedData(): List<SkiRide> {
        return database.getDataByStatus(DataStatus.UNKNOWN) +
                database.getDataByStatus(DataStatus.OFFLINE)
    }

    override suspend fun loadDataForRemoval(): List<SkiRide> {
        return database.getDataByStatus(DataStatus.REMOVED)
    }

    override suspend fun deleteData(data: SkiRide) {
        db.withTransaction {
            api.delete(GeneralDataBody(userID = super.account.getUserID(), data = data))
            database.delete(data)
        }
    }

    override suspend fun updateData(data: SkiRide) {
        data.status = DataStatus.ONLINE
        database.update(data)
    }
}