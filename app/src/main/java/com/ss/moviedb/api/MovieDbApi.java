package com.ss.moviedb.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shubham Singhal on 30,March,2019
 */
public class MovieDbApi {

	private static Retrofit retrofit = null;

	/**
	 * Create Retrofit builder.
	 * @return
	 */
	public static Retrofit getRetrofitClient() {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					.addConverterFactory(GsonConverterFactory.create())
					.baseUrl("https://api.themoviedb.org/3/")
					.build();
		}
		return retrofit;
	}
}
