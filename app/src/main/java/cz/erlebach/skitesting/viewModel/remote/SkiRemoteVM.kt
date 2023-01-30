package cz.erlebach.skitesting.viewModel.remote
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.repository.remote.SkiRemoteRepository

class SkiRemoteVM(repository: SkiRemoteRepository, userID : String) : GenericRemoteServerVM<Ski>(repository, userID) {

    init {
        fetchData()  // todo kdy nacitat?
    }



}