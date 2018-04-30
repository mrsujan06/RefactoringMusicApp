package com.example.arvin.refactoringmusicapp.model;

import com.example.arvin.refactoringmusicapp.model.api.ApiObservableArtistService;
import com.example.arvin.refactoringmusicapp.model.api.ApiObservableInteractor;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiObservableInteratorImp  implements ApiObservableInteractor{

    ApiObservableArtistService mApiObservableArtistService;

    public ApiObservableInteratorImp(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.theaudiodb.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiObservableArtistService = retrofit.create(ApiObservableArtistService.class);

    }

    public void networkcall(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.theaudiodb.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiObservableArtistService = retrofit.create(ApiObservableArtistService.class);

    }



    @Override
    public Observable<ArtistResponse> searchArtist(String artistName) {
        return mApiObservableArtistService.searchArtist(artistName);
    }
}
