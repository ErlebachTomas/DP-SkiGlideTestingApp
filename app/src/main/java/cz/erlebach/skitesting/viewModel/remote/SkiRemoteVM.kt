package cz.erlebach.skitesting.viewModel.remote
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.repository.remote.SkiRemoteRepository

class SkiRemoteVM(repository: SkiRemoteRepository) : GenericRemoteServerVM<List<Ski>>(repository) {

    init {
        fetchData()  // todo kdy nacitat?
    }



}