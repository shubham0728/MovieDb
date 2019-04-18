package com.ss.moviedb.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ss.moviedb.R;
import com.ss.moviedb.adapter.MoviesAdapter;
import com.ss.moviedb.api.MovieApiClient;
import com.ss.moviedb.api.MovieDbApi;
import com.ss.moviedb.interfaces.RecyclerViewItemClickListener;
import com.ss.moviedb.models.MovieDetailModel;
import com.ss.moviedb.models.TopRatedMovieResults;
import com.ss.moviedb.models.TopRatedMovies;
import com.ss.moviedb.util.PaginationScrollListener;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shubham Singhal on 30,March,2019
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

	private Toolbar toolbar;
	private RecyclerView recyclerView;
	private ProgressBar progress;
	private RelativeLayout layout_nothing_to_show;
	private int currentPage = 1;
	private MoviesAdapter adapter;

	private boolean isLastPage = false;
	private boolean isLoading = false;
	private int TotalPages = 8;
	private Handler handler = new Handler();
	private List<TopRatedMovieResults> filteredList = new ArrayList<>();
	private Context mCtx;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	/**
	 * Initialise all widgets.
	 */
	private void init() {
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		layout_nothing_to_show = findViewById(R.id.layout_nothing_to_show);
		recyclerView = findViewById(R.id.movie_list);
		recyclerView.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
		recyclerView.setLayoutManager(gridLayoutManager);
		adapter = new MoviesAdapter(MainActivity.this, MainActivity.this);
		recyclerView.setAdapter(adapter);

		/**
		 * Scroll Listener.
		 */
		recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
			@Override
			protected void loadMoreItems() {
				isLoading = true;
				currentPage = currentPage + 1;

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						progress.setVisibility(View.VISIBLE);
						loadNextPageOfTopRatedMovies();
					}
				}, 500);
			}

			@Override
			public int getTotalPageCount() {
				return TotalPages;
			}

			@Override
			public boolean isLastPage() {
				return isLastPage;
			}

			@Override
			public boolean isLoading() {
				return isLoading;
			}
		});

		progress = findViewById(R.id.progress);
		checkConnection();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);

		// Inflate the menu; this adds items to the action bar if it is present.
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchEditText.setHint(getString(R.string.search));
		searchEditText.setTextColor(getResources().getColor(R.color.black));
		searchEditText.setHintTextColor(getResources().getColor(R.color.grey));

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				adapter.getFilter().filter(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				adapter.getFilter().filter(newText);
				return true;
			}
		});

		if (searchView.isIconified()) {

		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_search) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Fetch first batch of movies on launch.
	 */
	private void getFirsTopRatedMovies() {
		try {
			MovieApiClient movieApiClient = MovieDbApi.getRetrofitClient().create(MovieApiClient.class);
			Call<TopRatedMovies> call = movieApiClient.getTopRatedMovies(getString(R.string.api_key), "en_US", currentPage);
			call.enqueue(new Callback<TopRatedMovies>() {
				@Override
				public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
					if (response.isSuccessful()) {
						TopRatedMovies topRatedMovies = response.body();
						List<TopRatedMovieResults> topRatedMovieResults = topRatedMovies.getResults();
						progress.setVisibility(View.GONE);
						adapter.addAll(topRatedMovieResults);

						if (currentPage <= TotalPages)
							adapter.addLoadingFooter();
						else
							isLastPage = true;
					} else {
						progress.setVisibility(View.GONE);
						layout_nothing_to_show.setVisibility(View.VISIBLE);
						Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(Call<TopRatedMovies> call, Throwable t) {
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
		}
	}

	/**
	 * Load next bactch of movies after reaching page end.
	 * Check if current page is equal to total number of pages.
	 */
	private void loadNextPageOfTopRatedMovies() {
		try {
			MovieApiClient movieApiClient = MovieDbApi.getRetrofitClient().create(MovieApiClient.class);
			Call<TopRatedMovies> call = movieApiClient.getTopRatedMovies(getString(R.string.api_key), "en_US", currentPage);
			call.enqueue(new Callback<TopRatedMovies>() {
				@Override
				public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
					if (response.isSuccessful()) {
						adapter.removeLoadingFooter();
						isLoading = false;
						progress.setVisibility(View.GONE);
						TopRatedMovies topRatedMovies = response.body();
						List<TopRatedMovieResults> topRatedMovieResults = topRatedMovies.getResults();
						adapter.addAll(topRatedMovieResults);

						if (currentPage != TotalPages)
							adapter.addLoadingFooter();
						else
							isLastPage = true;
					} else {
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


	/**
	 * Recycler view click listner.
	 *
	 * @param position
	 */
	@Override
	public void onRecyclerViewItemClick(int position) {
		TopRatedMovieResults topRatedMovieResults = adapter.getItem(position);
		Intent in = new Intent(MainActivity.this, MovieDetailView.class);
		in.putExtra("movieId", topRatedMovieResults.getId());
		startActivity(in);
	}

	/**
	 * To check if the device is connected to the internet.
	 *
	 * @return
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Performs actions based on network status.
	 * If Online -  Fetch first batch of movies.
	 * If offline - display nothing.
	 */
	public void checkConnection() {
		if (isOnline()) {
			getFirsTopRatedMovies();
		} else {
			progress.setVisibility(View.GONE);
			layout_nothing_to_show.setVisibility(View.VISIBLE);
			Toast.makeText(this, getString(R.string.no_netowrk), Toast.LENGTH_SHORT).show();
		}
	}


}
