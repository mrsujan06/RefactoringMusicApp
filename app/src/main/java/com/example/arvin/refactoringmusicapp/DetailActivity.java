package com.example.arvin.refactoringmusicapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arvin.refactoringmusicapp.model.AlbumResponse;
import com.example.arvin.refactoringmusicapp.model.api.ApiObservableArtistService;
import com.jakewharton.rxbinding2.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity  {

    @BindView(R.id.imageViewArtist)
    ImageView imageViewArtist;
    @BindView(R.id.fanart)
    ImageView imageFanArt;
    @BindView(R.id.fanart2)
    ImageView imageFanArt2;
    @BindView(R.id.fanart3)
    ImageView imageFanArt3;
    @BindView(R.id.logo)
    ImageView imageViewLogo;
    @BindView(R.id.banner)
    ImageView imageViewBanner;
    @BindView(R.id.toolbar3)
    Toolbar toolbar3;
    @BindView(R.id.toolbar4)
    Toolbar toolbar4;
    @BindView(R.id.toolbar5)
    Toolbar toolbar5;
    @BindView(R.id.toolbar6)
    Toolbar toolbar6;
    @BindView(R.id.recyclerViewAlbums)
    RecyclerView recyclerViewAlbums;

    ApiObservableArtistService apiObservableArtistService;
    SearchView mSearchView;
    RecyclerView albumList;


    ApiObservableArtistService reqInterface3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        //Toolbar3
        toolbar3.setTitle("Bio");
        toolbar3.setTitleTextColor(0xFFFFFFFF);

        //Toolbar4
        toolbar4.setTitle("Pictures");
        toolbar4.setTitleTextColor(0xFFFFFFFF);

        //Toolbar4
        toolbar5.setTitle("Albums");
        toolbar5.setTitleTextColor(0xFFFFFFFF);


//        mSearchView = findViewById(R.id.artistSearchView);
//        albumList = findViewById(R.id.recyclerViewAlbums);
//        albumList.setLayoutManager(new LinearLayoutManager(this));
//        albumList.setAdapter(searchAlbumAdapter = new SearchAlbumAdapter());
        // networkCallForAlbums("Simple plan");
//
//
//        retroCall();
//        loadData();


        getIncomingIntent();




    }



    private void getIncomingIntent() {

        if (getIntent().hasExtra("image_url") && getIntent().hasExtra("bio") && getIntent().hasExtra("logo")
                && getIntent().hasExtra("fanart") && getIntent().hasExtra("fanart2") && getIntent().hasExtra("fanart3")
                && getIntent().hasExtra("banner"))

        {

            String imageUrl = (String) getIntent().getExtras().getSerializable("image_url");
            String bio = (String) getIntent().getExtras().getSerializable("bio");
            String logo = (String) getIntent().getExtras().getSerializable("logo");
            String fanart = (String) getIntent().getExtras().getSerializable("fanart");
            String fanart2 = (String) getIntent().getExtras().getSerializable("fanart2");
            String fanart3 = (String) getIntent().getExtras().getSerializable("fanart3");
            String banner = (String) getIntent().getExtras().getSerializable("banner");
            setImage(imageUrl, bio, logo, fanart, fanart2, fanart3, banner);

        }


    }

    private void setImage(String imageUrl, String bio, String logo, String fanart, String fanart2, String fanart3, String banner) {

        //fanart
        Glide.with(this)
                .asBitmap()
                .load(fanart)
                .into(imageFanArt);

        //fanart
        Glide.with(this)
                .asBitmap()
                .load(fanart2)
                .into(imageFanArt2);

        //fanart
        Glide.with(this)
                .asBitmap()
                .load(fanart3)
                .into(imageFanArt3);

        //logo
        Glide.with(this)
                .asBitmap()
                .load(logo)
                .into(imageViewLogo);

        //banner
        Glide.with(this)
                .asBitmap()
                .load(banner)
                .into(imageViewBanner);


        //Artist Photo
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(imageViewArtist);

        //bio
        TextView artistBio = findViewById(R.id.bio);
        artistBio.setText(bio);

    }


    public void retroCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.theaudiodb.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiObservableArtistService = retrofit.create(ApiObservableArtistService.class);

    }




}
