package com.example.arvin.refactoringmusicapp.presenter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.arvin.refactoringmusicapp.R;
import com.example.arvin.refactoringmusicapp.model.AlbumResponse;
import com.example.arvin.refactoringmusicapp.model.ApiObservableInteratorImp;
import com.example.arvin.refactoringmusicapp.model.ArtistResponse;
import com.example.arvin.refactoringmusicapp.model.api.ApiObservableArtistService;
import com.example.arvin.refactoringmusicapp.model.api.ApiObservableInteractor;
import com.example.arvin.refactoringmusicapp.view.ArtistView;
import com.jakewharton.rxbinding2.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtistPresenter {

    ArtistView mArtistView;
    ApiObservableArtistService mApiObservableArtistService;
    ApiObservableInteractor mApiObservableInteractor;


    SearchView mSearchView;

    public ArtistPresenter( ApiObservableInteractor mApiObservableInteractor){
        this.mApiObservableInteractor = mApiObservableInteractor;
    }

    public void bind(ArtistView view) {
        this.mArtistView = view;
    }

    public void unbind() {
        mArtistView = null;
    }

    @SuppressLint("CheckResult")
    public void loadData() {

//        try {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.theaudiodb.com")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mApiObservableArtistService = retrofit.create(ApiObservableArtistService.class);

            mApiObservableArtistService.searchArtist("Beyonce")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ArtistResponse>() {
                        @Override
                        public void accept(ArtistResponse response) throws Exception {

                            mArtistView.updateUi(response.getArtists());

                        }
                    });


            RxSearchView.queryTextChanges(mSearchView)
                    .subscribe(new Consumer<CharSequence>() {
                        @Override
                        public void accept(CharSequence charSequence) throws Exception {
                            System.out.print(charSequence);
                        }
                    });
//            RxSearchView.queryTextChanges(mSearchView)
//                    .subscribeOn(AndroidSchedulers.mainThread())
//                    .debounce(100, TimeUnit.MILLISECONDS)
//
//                    .filter(new Predicate<CharSequence>() {
//                        @Override
//                        public boolean test(CharSequence charSequence) throws Exception {
//                            return !charSequence.toString().isEmpty();
//                        }
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .switchMap(new Function<CharSequence, ObservableSource<ArtistResponse>>() {
//                        @Override
//                        public ObservableSource<ArtistResponse> apply(CharSequence query) throws Exception {
//                            return mApiObservableInteractor.searchArtist(query.toString());
//
//
//                        }
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<ArtistResponse>() {
//                                   @Override
//                                   public void accept(ArtistResponse response) throws Exception {
//
//                                      mArtistView.updateUi(response.getArtists());
//
//                                   }
//                               },
//                            new Consumer<Throwable>() {
//                                @Override
//                                public void accept(Throwable throwable) throws Exception {
//                                    Log.d("Error" , throwable.getMessage());
//                                }
//                            });
//        } catch (Exception e) {
//            Log.d("Error" , e.getMessage());
//        }


    }

    @SuppressLint("CheckResult")
    public void networkCall(String name){

        mApiObservableInteractor.searchArtist(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArtistResponse>() {

                    @Override
                    public void accept(ArtistResponse response) throws Exception {

                        if(mArtistView != null) {
                            mArtistView.updateUi(response.getArtists());
                        }

                       // Toast.makeText(mContext, response.getResults().get(0).getArtistName(), Toast.LENGTH_SHORT).show();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                       // Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                    }
                });

    }




}
