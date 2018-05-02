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
    ArtistView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mApiObservableInteractor = new ApiObservableInteratorImp();
        mArtistPresenter = new ArtistPresenter(mApiObservableInteractor , this);
        mArtistPresenter.bind(this);
        mRecyclerView = findViewById(R.id.artistList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mSearchView = findViewById(R.id.artistSearchView);
        mArtistPresenter.loadData(mSearchView);


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
        mSearchArtistAdapter.notifyDataSetChanged();
        Toast.makeText(this, mList.get(0).getStrArtist(), Toast.LENGTH_SHORT).show();

    }



}
