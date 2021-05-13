package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotpopcorn.model.ProductionCompany
import com.example.hotpopcorn.model.api.ApiRequest
import com.example.hotpopcorn.model.api.CompanyRepository
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

// ViewModel which connects Company's Fragment with Company's Repository (and API):
class CompanyViewModel : ViewModel() {
    private val repository : CompanyRepository = CompanyRepository(ApiRequest.getAPI())

    //                                      COMPANY DETAILS
    private val mutCurrentCompany = MutableLiveData<ProductionCompany>()
    val currentCompany : LiveData<ProductionCompany> get() = mutCurrentCompany
    fun setCurrentCompany(currentCompanyID : Int) {
        viewModelScope.launch {
            val response = repository.getCompanyDetails(currentCompanyID).awaitResponse()
            if (response.isSuccessful) mutCurrentCompany.value = response.body()
        }
    }
}