package cz.erlebach.skitesting.repository

import androidx.room.withTransaction
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.common.utils.debug
import cz.erlebach.skitesting.common.utils.info
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.repository.local.SkiLocalRepository
import cz.erlebach.skitesting.repository.remote.SkiRemoteRepository
import cz.erlebach.skitesting.repository.resource.networkBoundResource
import kotlinx.coroutines.delay

class SkiRepository (
    private val skiLocalRepository: SkiLocalRepository,
    private val skiRemoteRepository: SkiRemoteRepository,
    private val account: IAccountManagement,
    private val db: MyDatabase
    ) {

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
            for (onlineSki: Ski in listResponse) {
                info(onlineSki.toString())

                // if novější tak update, pokud není vložit jako novou

                val offlineSki = db.skiDao().getSki(onlineSki.id)

                if (offlineSki != null) {

                    if (onlineSki.isNewer(offlineSki)) {
                        info("aktualizuji ${onlineSki.toString()}")

                        db.skiDao().updateSki(onlineSki)
                    }

                } else {
                    info("stahuji a vkládám ${onlineSki.toString()}")
                    db.skiDao().addSki(onlineSki)
                }


            }
            info("synchronized ${listResponse.count()}")
        }
    )
}
