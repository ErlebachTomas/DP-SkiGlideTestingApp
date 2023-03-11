package cz.erlebach.skitesting.network.SyncWorker
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.isDeviceOnline
import cz.erlebach.skitesting.common.utils.lg
import retrofit2.Response


abstract class SyncWorker<T>(
    val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    protected val account: IAccountManagement = SessionManager.getInstance(this.context)
    protected val db: MyDatabase = MyDatabase.getDatabase(context)

    /** provede volání API pro synchronizaci dat */
    protected abstract suspend  fun syncData(data: T): Response<T>
    /** získá list nesynchronizovaných dat */
    protected abstract suspend fun unsynchronizedData(): List<T>
    /** získá seznam dat pro smazání */
    protected abstract suspend fun loadDataForRemoval(): List<T>
    /** nahraje offline záznam na server */
    protected abstract suspend fun updateData(data: T)
    /** vymaže záznam */
    protected abstract suspend fun  deleteData(data: T)

    override suspend fun doWork(): Result {

        try {
            sync(unsynchronizedData())

            remove(loadDataForRemoval())

        } catch (e: Exception) {
            err(e.message.toString())
            return Result.failure()
         }

        return Result.success()
    }

    private suspend fun sync(dataList: List<T>) {
        if (dataList.isNotEmpty()) {
            for (data in dataList) {
                if (isDeviceOnline(context)) {
                        val response = syncData(data)
                        if (response.isSuccessful) {
                            updateData(data)
                        } else {
                            err(response.errorBody().toString())
                        }
                } else {
                    lg("zařízení je offline")
                    break
                }
            }
        }
    }

    private suspend fun remove(dataForRemoval: List<T>) {
        if (dataForRemoval.isNotEmpty()) {
            for (data in dataForRemoval) {
                if (isDeviceOnline(context)) {
                      deleteData(data)
                } else {
                    lg("zařízení je offline")
                    break
                }
            }
        }
    }




}