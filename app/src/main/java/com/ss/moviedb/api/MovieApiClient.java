package com.ss.moviedb.api;

import com.ss.moviedb.models.CastModel;
import com.ss.moviedb.models.MovieDetailModel;
import com.ss.moviedb.models.ReviewModel;
import com.ss.moviedb.models.TopRatedMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Shubham Singhal on 30,March,2019
 */
public interface  MovieApiClient {

	@GET("movie/top_rated")
	Call<TopRatedMovies> getTopRatedMovies(@Query("api_key") String key, @Query("language") String language, @Query("page") int page);

	@GET("movie/{id}")
	Call<MovieDetailModel> getMovieDetails(@Path("id") int movieID, @Query("api_key") String key, @Query("language") String language);

	@GET("movie/{id}/credits")
	Call<CastModel> getCastDetails(@Path("id") int movieID, @Query("api_key") String key);

	@GET("movie/{id}/similar")
	Call<TopRatedMovies> getSimilarMovies(@Path("id") int movieID, @Query("api_key") String key, @Query("language") String language, @Query("page") int page);

	@GET("movie/{id}/reviews")
	Call<ReviewModel> getReviews(@Path("id") int movieID, @Query("api_key") String key, @Query("language") String language);

}
