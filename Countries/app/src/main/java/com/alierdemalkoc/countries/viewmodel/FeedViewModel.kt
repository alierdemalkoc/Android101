package com.alierdemalkoc.countries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alierdemalkoc.countries.model.Country
import com.alierdemalkoc.countries.roomdb.CountryDatabase
import com.alierdemalkoc.countries.services.CountryAPIService
import com.alierdemalkoc.countries.util.MySharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {
    private val countryAPIService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = MySharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000L

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData(){
        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromSQL()
        } else {
            getDataFromAPI()
        }
    }
    fun refreshFromAPI(){
        getDataFromAPI()
    }
    fun getDataFromSQL(){
        countryLoading.value = true

        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showDatabase(countries)
            Toast.makeText(getApplication(),"Countries From SQL",Toast.LENGTH_LONG).show()
        }
    }
    private fun getDataFromAPI(){
        countryLoading.value = true

        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                    storeInSQL(t)
                        Toast.makeText(getApplication(),"Countries From API",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }
    private fun showDatabase(countryList: List<Country>){
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }
    private fun storeInSQL(list: List<Country>){
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size){
                list[i].uuid = listLong[i].toInt()
                i = i + 1
            }
            showDatabase(list)
        }
        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}