package com.example.arvin.refactoringmusicapp;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.arvin.refactoringmusicapp.adapter.SearchArtistAdapter;
import com.example.arvin.refactoringmusicapp.model.ApiObservableInteratorImp;
import com.example.arvin.refactoringmusicapp.model.Artist;
import com.example.arvin.refactoringmusicapp.model.ArtistResponse;
import com.example.arvin.refactoringmusicapp.model.api.ApiObservableArtistService;
import com.example.arvin.refactoringmusicapp.model.api.ApiObservableInteractor;
import com.example.arvin.refactoringmusicapp.presenter.ArtistPresenter;
import com.example.arvin.refactoringmusicapp.view.ArtistView;
import com.jakewharton.rxbinding2.widget.RxSearchView;

import java.util.List;
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

public class MainActivity extends AppCompatActivity implements ArtistView {


    ApiObservableInteractor mApiObservableInteractor;
    ArtistPresenter mArtistPresenter;
    RecyclerView mRecyclerView;
    SearchArtistAdapter mSearchArtistAdapter;
    SearchView mSearchView;
    private ApiObservableArtistService mApiObservableArtistService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mApiObservableInteractor = new ApiObservableInteratorImp();
        mArtistPresenter = new ArtistPresenter(mApiObservableInteractor);
        mArtistPresenter.bind(this);
        mRecyclerView = findViewById(R.id.artistList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mSearchView = findViewById(R.id.artistSearchView);
        loadData();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mArtistPresenter.unbind();
    }


    @Override
    public void updateUi(List<Artist> mList) {

        mSearchArtistAdapter = new SearchArtistAdapter();
        mSearchArtistAdapter.addArtistResponses(mList);
        mRecyclerView.setAdapter(mSearchArtistAdapter);
        mRecyclerView.notify();
        Toast.makeText(this, mList.get(0).getStrArtist(), Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("CheckResult")
    public void loadData() {
//
    try {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.theaudiodb.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiObservableArtistService = retrofit.create(ApiObservableArtistService.class);

            RxSearchView.queryTextChanges(mSearchView)
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


                                       updateUi(response.getArtists());

                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                  Log.d("Error" , throwable.getMessage());
                                }
                            });
        } catch (Exception e) {
            Log.d("Error" , e.getMessage());
        }


    }


}
