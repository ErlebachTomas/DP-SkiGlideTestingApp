package cz.erlebach.skitesting.repository

import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.common.utils.info
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.repository.local.SkiLocalRepository
import cz.erlebach.skitesting.repository.remote.SkiRemoteRepository
import kotlinx.coroutines.delay

class SkiRepository (
    private val skiLocalRepository: SkiLocalRepository,
    private val skiRemoteRepository: SkiRemoteRepository,
    private val account: IAccountManagement,
   // todo private val db: MyDatabase
    ){

    fun getData() = networkBoundResource(
        localFlow = {
            lg("localFlow")
            skiLocalRepository.getDataFlow
        },
        fetchFromRemote = {
            lg("fetchFromRemote")
            delay(2000)
            skiRemoteRepository.getList(account.getUserID())
        },
        sync = { listResponse ->
                lg("sync ")
                info("=== listResponse ===")
                for (i in listResponse) {
                   info(i.toString())

                }

                info("synchronized ${listResponse.count()}")



                /* todo implementace synchronizace
                db.withTransaction {
                // if novější tak update, pokud není vložit
                }
                */
        }
    )
}