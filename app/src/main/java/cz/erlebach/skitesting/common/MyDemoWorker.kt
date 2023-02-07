package cz.erlebach.skitesting.common

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cz.erlebach.skitesting.common.utils.info
import cz.erlebach.skitesting.common.utils.lg

/**
 * Test CoroutineWorkera
 */
class MyWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            try {
                lg("MyWorker", "Run work manager")

                doTask()

                Result.success()
            } catch (e: Exception) {
                lg("MyWorker", "exception in doWork ${e.message}")
                Result.failure()
            }
        } catch (e: Exception) {
            lg("MyWorker", "exception in doWork ${e.message}")
            Result.failure()
        }
    }

    private fun doTask() {
    info("working...","MyWorker")
    }

}