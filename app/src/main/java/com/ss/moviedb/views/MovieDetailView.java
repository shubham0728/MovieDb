package com.ss.moviedb.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ss.moviedb.R;
import com.ss.moviedb.adapter.CastAdpater;
import com.ss.moviedb.adapter.MoviesAdapter;
import com.ss.moviedb.adapter.ProductionCompanyAdapter;
import com.ss.moviedb.api.MovieApiClient;
import com.ss.moviedb.api.MovieDbApi;
import com.ss.moviedb.interfaces.RecyclerViewItemClickListener;
import com.ss.moviedb.models.CastModel;
import com.ss.moviedb.models.CastModelResult;
import com.ss.moviedb.models.GenresModel;
import com.ss.moviedb.models.MovieDetailModel;
import com.ss.moviedb.models.TopRatedMovieResults;
import com.ss.moviedb.models.TopRatedMovies;
import com.ss.moviedb.util.PaginationScrollListener;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shubham Singhal on 01,April,2019
 */
public class MovieDetailView extends AppCompatActivity implements RecyclerViewItemClickListener {
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.img_movie)
	ImageView img_movie;
	@BindView(R.id.txt_tagLine)
	TextView txt_tagLine;
	@BindView(R.id.txt_title)
	TextView txt_title;
	@BindView(R.id.txt_desc)
	TextView txt_desc;
	@BindView(R.id.txt_genre)
	TextView txt_genre;
	@BindView(R.id.txt_year)
	TextView txt_year;
	@BindView(R.id.production_list)
	RecyclerView production_list;
	@BindView(R.id.cast_list)
	RecyclerView cast_list;
	@BindView(R.id.similarMovie_list)
	RecyclerView similarMovie_list;
	@BindView(R.id.layout_reviews)
	RelativeLayout layout_reviews;
	@BindView(R.id.progress)
	ProgressBar progress;

	private int movieID;
	private ProductionCompanyAdapter productionCompanyAdapter;
	private CastAdpater castAdpater;
	private MoviesAdapter adapter;
	private int currentPage = 1;
	public static String EXTRA_MESSAGE;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_view);
		ButterKnife.bind(this);
		productionCompanyAdapter = new ProductionCompanyAdapter(getApplicationContext());
		castAdpater = new CastAdpater(getApplicationContext());
		adapter = new MoviesAdapter(MovieDetailView.this, MovieDetailView.this);
		init();
		getMovieDetails();
		getCastDetails();
		getSimilarMovies();
	}

	/**
	 * Initialise all widgets.
	 */
	private void init() {
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		movieID = getIntent().getIntExtra("movieId", 0);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
		production_list.setLayoutManager(linearLayoutManager);

		LinearLayoutManager castlinearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
		cast_list.setLayoutManager(castlinearLayoutManager);

		LinearLayoutManager similarlinearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
		similarMovie_list.setLayoutManager(similarlinearLayoutManager);

		production_list.setAdapter(productionCompanyAdapter);
		cast_list.setAdapter(castAdpater);
		similarMovie_list.setAdapter(adapter);

		layout_reviews.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent in = new Intent(MovieDetailView.this, Reviews.class);
				in.putExtra("movieId", movieID);
				startActivity(in);

			}
		});

	}

	/**
	 * Fetch movie details.
	 */
	private void getMovieDetails() {
		try {
			MovieApiClient movieApiClient = MovieDbApi.getRetrofitClient().create(MovieApiClient.class);
			Call<MovieDetailModel> call = movieApiClient.getMovieDetails(movieID, getString(R.string.api_key), "en_US");
			call.enqueue(new Callback<MovieDetailModel>() {
				@Override
				public void onResponse(Call<MovieDetailModel> call, Response<MovieDetailModel> response) {
					if (response.isSuccessful()) {
						MovieDetailModel movieDetailModel = response.body();

						Glide.with(getApplicationContext())
								.load(getString(R.string.image_base_url) + movieDetailModel.getBackdrop_path())
								.placeholder(R.drawable.ic_launcher_background)
								.diskCacheStrategy(DiskCacheStrategy.ALL)
								.centerCrop()
								.into(img_movie);

						txt_title.setText(movieDetailModel.getTitle());
						txt_tagLine.setText(movieDetailModel.getTagline());
						txt_desc.setText(movieDetailModel.getOverview());

						String genre = "";
						for (GenresModel genresModel : movieDetailModel.getGenresModels()) {
							genre = genre + genresModel.getName() + " |";
						}
						genre = genre.substring(0, genre.length() - 1);
						txt_genre.setText(genre);

						toolbar.setTitle(movieDetailModel.getTitle());
						productionCompanyAdapter.addAll(movieDetailModel.getProductionCompanyModels());

						String inputPattern = "yyyy-mm-dd";
						String outputPattern = "MMM d, yyyy";
						SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
						SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

						Date date = null;
						String str = null;

						try {
							date = inputFormat.parse(movieDetailModel.getRelease_date());
							str = outputFormat.format(date);
							txt_year.setText(str);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(Call<MovieDetailModel> call, Throwable t) {
					if (t instanceof SocketTimeoutException || t instanceof IOException) {
						Toast.makeText(getApplicationContext(), getString(R.string.no_netowrk), Toast.LENGTH_SHORT).show();
						Log.e("ERROR", getString(R.string.no_netowrk), t);
					} else {
						Log.e("ERROR", getString(R.string.error), t);
						Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Fetch cast details.
	 */
	private void getCastDetails() {
		try {
			MovieApiClient movieApiClient = MovieDbApi.getRetrofitClient().create(MovieApiClient.class);
			Call<CastModel> call = movieApiClient.getCastDetails(movieID, getString(R.string.api_key));
			call.enqueue(new Callback<CastModel>() {
				@Override
				public void onResponse(Call<CastModel> call, Response<CastModel> response) {
					if (response.isSuccessful()) {
						CastModel castModel = response.body();
						List<CastModelResult> castModelResult = castModel.getCast();
						castAdpater.addAll(castModelResult);
					} else {
						Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(Call<CastModel> call, Throwable t) {
					if (t instanceof SocketTimeoutException || t instanceof IOException) {
						Toast.makeText(getApplicationContext(), getString(R.string.no_netowrk), Toast.LENGTH_SHORT).show();
						Log.e("ERROR", getString(R.string.no_netowrk), t);
					} else {
						Log.e("ERROR", getString(R.string.error), t);
						Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Fetch similar movies.
	 */
	private void getSimilarMovies() {
		try {
			MovieApiClient movieApiClient = MovieDbApi.getRetrofitClient().create(MovieApiClient.class);
			Call<TopRatedMovies> call = movieApiClient.getSimilarMovies(movieID, getString(R.string.api_key), "en_US", currentPage);
			call.enqueue(new Callback<TopRatedMovies>() {
				@Override
				public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
					if (response.isSuccessful()) {
						progress.setVisibility(View.GONE);
						TopRatedMovies topRatedMovies = response.body();
						List<TopRatedMovieResults> topRatedMovieResults = topRatedMovies.getResults();
						adapter.addAll(topRatedMovieResults);
					} else {
						progress.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(Call<TopRatedMovies> call, Throwable t) {
					if (t instanceof SocketTimeoutException || t instanceof IOException) {
						progress.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), getString(R.string.no_netowrk), Toast.LENGTH_SHORT).show();
						Log.e("ERROR", getString(R.string.no_netowrk), t);
					} else {
						progress.setVisibility(View.GONE);
						Log.e("ERROR", getString(R.string.error), t);
						Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			progress.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onRecyclerViewItemClick(int position) {

	}
}
