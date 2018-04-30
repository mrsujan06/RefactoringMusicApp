package com.example.arvin.refactoringmusicapp.model.api;

import com.example.arvin.refactoringmusicapp.model.ArtistResponse;

import io.reactivex.Observable;
import retrofit2.http.Query;

public interface ApiObservableInteractor {
    Observable<ArtistResponse> searchArtist(@Query("s") String artistName);
}
