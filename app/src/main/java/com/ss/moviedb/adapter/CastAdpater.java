package com.ss.moviedb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ss.moviedb.R;
import com.ss.moviedb.models.CastModelResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham Singhal on 01,April,2019
 */
public class CastAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


	private static final int ITEM = 0;
	private static final int LOADING = 1;
	private Context mCtx;
	private List<CastModelResult> castModelResults;
	private boolean isLoadingAdded = false;

	/**
	 * Constructor
	 *
	 * @param _ctx
	 */
	public CastAdpater(Context _ctx) {
		this.mCtx = _ctx;
		castModelResults = new ArrayList<>();
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		RecyclerView.ViewHolder viewHolder = null;
		switch (i) {
			case ITEM:
				View castView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.production_company_adapter, viewGroup, false);
				viewHolder = new CastViewHolder(castView);
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
					CastViewHolder castViewHolder = (CastViewHolder) viewHolder;
					CastModelResult castModelResult = castModelResults.get(i);
					if (castModelResult.getProfile_path() != null) {
						Glide.with(mCtx)
								.load(mCtx.getString(R.string.image_base_url) + castModelResult.getProfile_path())
								.placeholder(R.drawable.placeholder)
								.diskCacheStrategy(DiskCacheStrategy.ALL)   // cache image
								.centerCrop()
								.into(castViewHolder.img_movie_production);
					}
					castViewHolder.txt_production_name.setText(castModelResult.getName());
					castViewHolder.progress_production.setVisibility(View.GONE);

				case LOADING:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		return castModelResults.size();
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Add single entity from api call.
	 *
	 * @param castModelResult
	 */
	public void add(CastModelResult castModelResult) {
		castModelResults.add(castModelResult);
		notifyItemInserted(castModelResults.size() - 1);
	}

	/**
	 * Add all data from the api call.
	 *
	 * @param castModelResults
	 */
	public void addAll(List<CastModelResult> castModelResults) {
		for (CastModelResult result : castModelResults) {
			add(result);
		}
	}

	public void remove(CastModelResult castModelResult) {
		int position = castModelResults.indexOf(castModelResult);
		if (position > -1) {
			castModelResults.remove(position);
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
		add(new CastModelResult());
	}

	/**
	 * Remove loader after page is loaded.
	 */
	public void removeLoadingFooter() {
		isLoadingAdded = false;

		int position = castModelResults.size() - 1;
		CastModelResult castModelResult = getItem(position);

		if (castModelResult != null) {
			castModelResults.remove(position);
			notifyItemRemoved(position);
		}
	}

	public CastModelResult getItem(int position) {
		return castModelResults.get(position);
	}

	@Override
	public int getItemViewType(int position) {
		return (position == castModelResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
	}

	/**
	 * Cast ViewHolder Class
	 */
	public class CastViewHolder extends RecyclerView.ViewHolder {
		private TextView txt_production_name;
		private ProgressBar progress_production;
		private ImageView img_movie_production;

		public CastViewHolder(@NonNull View itemView) {
			super(itemView);
			txt_production_name = itemView.findViewById(R.id.txt_production_name);
			progress_production = itemView.findViewById(R.id.progress_production);
			img_movie_production = itemView.findViewById(R.id.img_movie_production);

		}
	}

	/**
	 * Loader ViewHolder Class
	 */
	protected class LoadingViewHolder extends RecyclerView.ViewHolder {

		public LoadingViewHolder(View itemView) {
			super(itemView);
		}
	}
}
