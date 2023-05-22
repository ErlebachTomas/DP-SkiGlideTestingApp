package cz.erlebach.skitesting.network.SyncWorker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.isDeviceOnline
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.SkiDataBody


class SyncWorkerSkiOld(
    val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    // todo generic přes abstract? předávat ISkiAPI::class.java ?

    private var api = RetrofitApiService(this.context).skiAPI
    private var database = MyDatabase.getDatabase(applicationContext).skiDao()
    private val account: IAccountManagement = SessionManager.getInstance(this.context)
    override suspend fun doWork(): Result {
        lg("worker pracuje")

        val dataList = unsynchronizedData()
        if (dataList.isNotEmpty()) {
            for (data in dataList) {

                if (isDeviceOnline(context)) {
                    try {
                        val response = api.syncData(SkiDataBody(account.getUserID(), data)) // provést volání API pro synchronizaci dat
                        if (response.isSuccessful) {
                            data.status = DataStatus.ONLINE
                            database.updateSki(data)
                            lg("nahrávám offline záznam ${data.name} na server")

                        } else {
                            err(response.errorBody().toString())
                            return Result.failure()
                        }
                    } catch (e: Exception) {
                        err(e.message.toString())
                        return Result.failure()
                    }
                } else {
                    lg("zařízení je offline")
                    break
                }
            }
        }


        return Result.success()
    }


    private suspend fun unsynchronizedData(): List<Ski> {
        return database.getDataByStatus(DataStatus.UNKNOWN) +
                   database.getDataByStatus(DataStatus.OFFLINE)
    }
    private suspend fun loadDataForRemoval(): List<Ski> {
        return database.getDataByStatus(DataStatus.REMOVED) // todo sync remove
    }

}