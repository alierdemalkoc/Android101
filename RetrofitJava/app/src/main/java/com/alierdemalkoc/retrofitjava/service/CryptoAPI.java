package com.alierdemalkoc.retrofitjava.service;

import com.alierdemalkoc.retrofitjava.model.CryptoModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CryptoAPI {
    @GET("prices?key=12fd5c40c87379057955ff47bc9f6f47b2409234")
    Observable<List<CryptoModel>> getData();
    //Call<List<CryptoModel>> getData();
}

//https://api.nomics.com/v1/prices?key=12fd5c40c87379057955ff47bc9f6f47b2409234
