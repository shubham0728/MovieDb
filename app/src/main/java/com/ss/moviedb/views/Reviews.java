package com.ss.moviedb.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ss.moviedb.R;
import com.ss.moviedb.adapter.ReviewAdapter;
import com.ss.moviedb.api.MovieApiClient;
import com.ss.moviedb.api.MovieDbApi;
import com.ss.moviedb.models.ReviewModel;
import com.ss.moviedb.models.ReviewModelResult;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shubham Singhal on 01,April,2019
 */
public class Reviews extends AppCompatActivity {

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.review_list)
	RecyclerView review_list;
	@BindView(R.id.layout_nothing_to_show)
	RelativeLayout layout_nothing_to_show;
	@BindView(R.id.progress)
	ProgressBar progress;

	private int movieID;
	private ReviewAdapter adapter;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviews);
		ButterKnife.bind(this);
		adapter = new ReviewAdapter(getApplicationContext());
		init();
		fetchReviews();
	}

	/**
	 * Initialise all widgets.
	 */
	private void init() {
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setTitle("Reviews");
		toolbar.setNavigationIcon(R.drawable.ic_arrow);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
		review_list.setLayoutManager(linearLayoutManager);
		review_list.setAdapter(adapter);
		movieID = getIntent().getIntExtra("movieId", 0);

	}

	/**
	 * Fetch movie reviews
	 */
	private void fetchReviews() {
		try {
			MovieApiClient movieApiClient = MovieDbApi.getRetrofitClient().create(MovieApiClient.class);
			Call<ReviewModel> call = movieApiClient.getReviews(movieID, getString(R.string.api_key), "en_US");
			call.enqueue(new Callback<ReviewModel>() {
				@Override
				public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
					if (response.isSuccessful()) {
						progress.setVisibility(View.GONE);
						ReviewModel reviewModel = response.body();
						List<ReviewModelResult> reviewModelResults = reviewModel.getReviewModelResults();
						adapter.addAll(reviewModelResults);
					} else {
						progress.setVisibility(View.GONE);
						layout_nothing_to_show.setVisibility(View.VISIBLE);
						Toast.makeText(Reviews.this, getString(R.string.no_netowrk), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(Call<ReviewModel> call, Throwable t) {
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
}
