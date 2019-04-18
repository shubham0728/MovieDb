package com.ss.moviedb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ss.moviedb.R;
import com.ss.moviedb.interfaces.RecyclerViewItemClickListener;
import com.ss.moviedb.models.TopRatedMovieResults;
import com.ss.moviedb.views.MovieDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham Singhal on 30,March,2019
 */
public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


	private static final int ITEM = 0;
	private static final int LOADING = 1;
	private Context mCtx;
	private RecyclerViewItemClickListener mrecyclerViewItemClickListener;
	private List<TopRatedMovieResults> movieResults;
	private List<TopRatedMovieResults> filteredMovieResults;
	private boolean isLoadingAdded = false;

	/**
	 * Constructor
	 *
	 * @param _ctx
	 * @param recyclerViewItemClickListener
	 */
	public MoviesAdapter(Context _ctx, RecyclerViewItemClickListener recyclerViewItemClickListener) {
		this.mCtx = _ctx;
		this.mrecyclerViewItemClickListener = recyclerViewItemClickListener;
		movieResults = new ArrayList<>();
		filteredMovieResults = new ArrayList<>();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		RecyclerView.ViewHolder viewHolder = null;
		switch (i) {
			case ITEM:
				View movieView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list, viewGroup, false);
				viewHolder = new MovieViewHolder(movieView);
				break;
			case LOADING:
				View loadView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_progress, viewGroup, false);
				viewHolder = new LoadingViewHolder(loadView);
				break;
		}
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
		try {
			switch (getItemViewType(i)) {
				case ITEM:
					MovieViewHolder movieViewHolder = (MovieViewHolder) viewHolder;
					TopRatedMovieResults topRatedMovieResults = filteredMovieResults.get(i);
					movieViewHolder.txt_movie_title.setText(topRatedMovieResults.getTitle());
					//	movieViewHolder.txt_movie_desc.setText(topRatedMovieResults.getOverview());
					String year = topRatedMovieResults.getReleaseDate().substring(0, 4);
					String language = topRatedMovieResults.getOriginalLanguage().toUpperCase();
					movieViewHolder.txt_movie_year.setText(year + "-" + language);
					Glide.with(mCtx)
							.load(mCtx.getString(R.string.image_base_url) + topRatedMovieResults.getBackdropPath())
							.placeholder(R.drawable.placeholder)
							.diskCacheStrategy(DiskCacheStrategy.ALL)   // cache image
							.into(movieViewHolder.img_movie);

					if (mCtx instanceof MovieDetailView)
						movieViewHolder.fab.setVisibility(View.GONE);

				case LOADING:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		return filteredMovieResults.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return (position == filteredMovieResults.size() && isLoadingAdded) ? LOADING : ITEM;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				String searchString = charSequence.toString();
				if (searchString.isEmpty()) {
					filteredMovieResults = movieResults;
				} else {
					List<TopRatedMovieResults> filteredList = new ArrayList<>();
					for (TopRatedMovieResults row : movieResults) {
						if (row.getTitle() != null) {
							String title = row.getTitle().trim().toLowerCase();
							String[] split = title.split("\\s+");
							for (int i = 0; i < split.length; i++) {
								if (split[i].startsWith(searchString.toLowerCase()))
									if (!filteredList.contains(row))
										filteredList.add(row);
							}

							if (searchString.contains(" ")) {
								if (title.matches("(.*)" + searchString + "(.*)"))
									if (!filteredList.contains(row))
										filteredList.add(row);

							}

						}
					}

					filteredMovieResults = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = filteredMovieResults;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				filteredMovieResults = (ArrayList<TopRatedMovieResults>) filterResults.values;
				notifyDataSetChanged();
			}

		};
	}

	/**
	 * Add single entity from api call.
	 *
	 * @param topRatedMovieResults
	 */
	public void add(TopRatedMovieResults topRatedMovieResults) {
		movieResults.add(topRatedMovieResults);
		filteredMovieResults.add(topRatedMovieResults);
		notifyItemInserted(movieResults.size() - 1);
		notifyItemInserted(filteredMovieResults.size() - 1);
	}

	/**
	 * Add all data from the api call.
	 *
	 * @param topRatedMovieResults
	 */
	public void addAll(List<TopRatedMovieResults> topRatedMovieResults) {
		for (TopRatedMovieResults result : topRatedMovieResults) {
			add(result);
		}
	}

	/**
	 * @param topRatedMovieResults
	 */
	public void remove(TopRatedMovieResults topRatedMovieResults) {
		int position = filteredMovieResults.indexOf(topRatedMovieResults);
		if (position > -1) {
			filteredMovieResults.remove(position);
			notifyItemRemoved(position);
		}
	}

	public void clear() {
		isLoadingAdded = false;
		while (getItemCount() > 0) {
			remove(getItem(0));
		}
	}

	public boolean isEmpty() {
		return getItemCount() == 0;
	}


	/**
	 * Start loader when loading next page.
	 */
	public void addLoadingFooter() {
		isLoadingAdded = true;
		add(new TopRatedMovieResults());
	}

	/**
	 * Remove loader after page is loaded.
	 */
	public void removeLoadingFooter() {
		isLoadingAdded = false;

		int position = filteredMovieResults.size() - 1;
		TopRatedMovieResults topRatedMovieResults = getItem(position);

		if (topRatedMovieResults != null) {
			filteredMovieResults.remove(position);
			notifyItemRemoved(position);
		}
	}

	public TopRatedMovieResults getItem(int position) {
		return filteredMovieResults.get(position);
	}

	public List<TopRatedMovieResults> getAll() {
		return filteredMovieResults;
	}

	/**
	 * Movie ViewHolder Class with RecyclerView click listener.
	 */
	public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private TextView txt_movie_title;
		private FloatingActionButton fab;
		private TextView txt_movie_year;
		private ProgressBar progress_movie;
		private ImageView img_movie;

		public MovieViewHolder(@NonNull View itemView) {
			super(itemView);

			fab = itemView.findViewById(R.id.fab);
			txt_movie_title = itemView.findViewById(R.id.txt_movie_title);
			txt_movie_year = itemView.findViewById(R.id.txt_movie_year);
			progress_movie = itemView.findViewById(R.id.progress_movie);
			img_movie = itemView.findViewById(R.id.img_movie);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			mrecyclerViewItemClickListener.onRecyclerViewItemClick(getAdapterPosition());
		}
	}

	protected class LoadingViewHolder extends RecyclerView.ViewHolder {

		public LoadingViewHolder(View itemView) {
			super(itemView);
		}
	}

}
