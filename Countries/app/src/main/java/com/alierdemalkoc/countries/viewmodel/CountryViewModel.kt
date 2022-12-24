package com.alierdemalkoc.countries.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.alierdemalkoc.countries.model.Country
import com.alierdemalkoc.countries.roomdb.CountryDatabase
import kotlinx.coroutines.launch

class CountryViewModel(application: Application) : BaseViewModel(application) {
    val countryLiveData = MutableLiveData<Country>()

    fun getDataFromRoom(uuid: Int){
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            val country = dao.getCountry(uuid)
            countryLiveData.value = country
        }
    }
}