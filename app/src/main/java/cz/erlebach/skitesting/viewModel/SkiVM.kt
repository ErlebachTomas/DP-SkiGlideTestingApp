package cz.erlebach.skitesting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import cz.erlebach.skitesting.repository.SkiRepository

class SkiVM (val repository: SkiRepository): ViewModel() {

    val ski = repository.getData().asLiveData()

}