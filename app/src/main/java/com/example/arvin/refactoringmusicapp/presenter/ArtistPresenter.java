package com.example.arvin.refactoringmusicapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.arvin.refactoringmusicapp.MainActivity;
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
import io.reactivex.disposables.Disposable;
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
    Context mContext;


    SearchView mSearchView;

    public ArtistPresenter(ApiObservableInteractor mApiObservableInteractor , Context context) {
        this.mApiObservableInteractor = mApiObservableInteractor;
        this.mContext = context;
    }

    public void bind(ArtistView view) {
        this.mArtistView = view;
    }

    public void unbind() {
        mArtistView = null;
    }

    @SuppressLint("CheckResult")
    public void loadData(SearchView searchView) {
//
        try {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.theaudiodb.com")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mApiObservableArtistService = retrofit.create(ApiObservableArtistService.class);

            RxSearchView.queryTextChanges(searchView)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .debounce(100, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<CharSequence>() {
                        @Override
                        public boolean test(CharSequence charSequence) throws Exception {
                            return !charSequence.toString().isEmpty();
                        }
                    })
                    .distinctUntilChanged()
                    .subscribeOn(Schedulers.io())
                    .switchMap(new Function<CharSequence, ObservableSource<ArtistResponse>>() {
                        @Override
                        public ObservableSource<ArtistResponse> apply(CharSequence query) throws Exception {
                            return mApiObservableInteractor.searchArtist(query.toString());


                        }
                    })

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ArtistResponse>() {
                                   @Override
                                   public void accept(ArtistResponse response) throws Exception {


                                       mArtistView.updateUi(response.getArtists());


                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Toast.makeText(mContext.getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.d("Error", throwable.getMessage());
                                }
                            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }


    }

}