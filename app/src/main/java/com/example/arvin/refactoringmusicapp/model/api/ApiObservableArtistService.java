package com.example.arvin.refactoringmusicapp.model.api;

import com.example.arvin.refactoringmusicapp.model.AlbumResponse;
import com.example.arvin.refactoringmusicapp.model.ArtistResponse;
import com.example.arvin.refactoringmusicapp.model.ResultResponse;
import com.example.arvin.refactoringmusicapp.model.TrendingResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sujan on 13/03/2018.
 */

public interface ApiObservableArtistService {

    //Search
    @GET("api/v1/json/195003/search.php")
    Observable<ArtistResponse> searchArtist(@Query("s") String artistName);

    @GET("api/v1/json/195003/searchalbum.php?")
    Observable<AlbumResponse> searchAlbums(@Query("s") String artistName);

    //Artist info
    @GET("api/v1/json/195003/artist.php")
    Observable<ArtistResponse> searchArtistDetails(@Query("i") int artistId);

    @GET("/api/v1/json/1/trending.php?country=us&type=itunes&format=albums")
    Observable<TrendingResponse> getTrendingAlbums();

    @GET("/api/v1/json/1/trending.php?country=us&type=itunes&format=singles")
    Observable<TrendingResponse> getTrendingSingles();

    //Pop
    @GET("search?term=pop&amp;media=music&amp;entity=song&amp;limit=50")
    Observable<ResultResponse> getPopData();

    @GET("api/v1/json/195003/search.php")
    retrofit2.Call<ArtistResponse> search(@Query("s") String artistName);


}
