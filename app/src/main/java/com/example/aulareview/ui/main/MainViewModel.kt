package com.example.aulareview.ui.main

import androidx.lifecycle.*
import com.example.aulareview.domain.mapper.toModel
import com.example.aulareview.domain.model.SampleModel
import com.example.aulareview.domain.repository.SampleRepository
import com.example.aulareview.domain.repository.impl.SampleRepositoryImpl
import com.example.aulareview.networking.model.SampleResponse
import com.example.aulareview.networking.service.Resource
import com.example.aulareview.ui.model.ErrorModel
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = SampleRepositoryImpl()

    private val _sampleData = MutableLiveData<List<SampleModel>>()
    val sampleData: LiveData<List<SampleModel>> = _sampleData

    private val _sampleError = MutableLiveData<ErrorModel>()
    val sampleError: LiveData<ErrorModel> = _sampleError

    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val callObserver: Observer<Resource<List<SampleResponse>>> =
        Observer { response -> processResponse(response) }
    //setando observador


    private suspend fun callObserverAPI() {
        //suspend function que observa se houve alguma mudança
        repository.getSampleData().observeForever { callObserver.onChanged(it) }
    }

    private fun processResponse(response: Resource<List<SampleResponse>>?) {
        //Aqui iremos processar a response de acordo com o retorno na camada de Repository
        when (response?.status) {
            Resource.Status.SUCCESS -> {
                isLoading.value = false
                response.data?.let { _sampleData.value = it.toModel() }
            }
            Resource.Status.ERROR -> {
                isLoading.value = false
                response.apiError?.let {
                    _sampleError.value = ErrorModel(
                        title = "Erro na chamada de api",
                        message = it,
                        errorCode = "0001"
                    )
                }
            }
            Resource.Status.LOADING -> {
                isLoading.value = true
            }
            null -> TODO()
        }
    }

    fun getSampleData() {
        //realizamos a chamada na camada de repository e adicionamos o observador
        viewModelScope.launch {
            callObserverAPI()
            repository.getSampleData()
        }
    }

}