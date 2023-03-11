package cz.erlebach.skitesting.network.SyncWorker

import android.content.Context
import androidx.room.withTransaction
import androidx.work.WorkerParameters
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.DataBody
import cz.erlebach.skitesting.network.model.SkiDataBody
import retrofit2.Response

class SyncWorkerTestSession(context: Context,
                            params: WorkerParameters
): SyncWorker<TestSession>(context, params) {

    private var api = RetrofitApiService(this.context).testSessionAPI
    private var database = MyDatabase.getDatabase(applicationContext).testSessionDao()

    override suspend fun syncData(data: TestSession): Response<TestSession> {
       return api.syncData(DataBody(super.account.getUserID(), data))
    }

    override suspend fun unsynchronizedData(): List<TestSession> {
        return database.getDataByStatus(DataStatus.UNKNOWN) +
                database.getDataByStatus(DataStatus.OFFLINE)
    }

    override suspend fun loadDataForRemoval(): List<TestSession> {
        return database.getDataByStatus(DataStatus.REMOVED) // todo sync remove
    }

    override suspend fun deleteData(data: TestSession) {
        db.withTransaction {
            api.delete(DataBody(userID = super.account.getUserID(), data = data))
            database.delete(data)
        }
    }

    override suspend fun updateData(data: TestSession) {
        data.status = DataStatus.ONLINE
        database.update(data)
    }
}