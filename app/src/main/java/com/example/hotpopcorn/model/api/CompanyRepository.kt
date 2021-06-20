package com.example.hotpopcorn.model.api

import com.example.hotpopcorn.model.ProductionCompany
import retrofit2.Call

// Functions connected with Company objects:
class CompanyRepository(private val apiRequest : ApiRequest) {
    fun getCompanyDetails(companyID : Int, language : String) : Call<ProductionCompany> {
        return apiRequest.getCompanyDetails(companyID, language)
    }
}