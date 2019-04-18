package com.ss.moviedb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ss.moviedb.R;
import com.ss.moviedb.models.ProductionCompanyModel;
import com.ss.moviedb.models.ReviewModelResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham Singhal on 02,April,2019
 */
public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<ReviewModelResult> reviewModelResults;
	private Context mCtx;
	private static final int ITEM = 0;
	private static final int LOADING = 1;
	private boolean isLoadingAdded = false;

	public ReviewAdapter(Context _ctx){
		this.mCtx  = _ctx;
		reviewModelResults = new ArrayList<>();
	}


	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		RecyclerView.ViewHolder viewHolder = null;
		switch (i) {
			case ITEM:
				View productionView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_adapter, viewGroup, false);
				viewHolder = new ReviewViewHolder(productionView);
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
					ReviewViewHolder reviewViewHolder = (ReviewViewHolder) viewHolder;
					ReviewModelResult reviewModelResult = reviewModelResults.get(i);

					reviewViewHolder.txt_name.setText(reviewModelResult.getAuthor());
					reviewViewHolder.txt_review.setText(reviewModelResult.getContent());

				case LOADING:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		return reviewModelResults.size();
	}


	@Override
	public long getItemId(int position) {
		return position;
	}


	/**
	 * Add single entity from api call.
	 *
	 * @param productionCompanyModel
	 */
	public void add(ReviewModelResult productionCompanyModel) {
		reviewModelResults.add(productionCompanyModel);
		notifyItemInserted(reviewModelResults.size() - 1);
	}

	/**
	 * Add all data from the api call.
	 *
	 * @param reviewModelResults
	 */
	public void addAll(List<ReviewModelResult> reviewModelResults) {
		for (ReviewModelResult result : reviewModelResults) {
			add(result);
		}
	}

	public void remove(ReviewModelResult reviewModelResult) {
		int position = reviewModelResults.indexOf(reviewModelResult);
		if (position > -1) {
			reviewModelResults.remove(position);
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


	public void addLoadingFooter() {
		isLoadingAdded = true;
		add(new ReviewModelResult());
	}

	public void removeLoadingFooter() {
		isLoadingAdded = false;

		int position = reviewModelResults.size() - 1;
		ReviewModelResult reviewModelResult = getItem(position);

		if (reviewModelResult != null) {
			reviewModelResults.remove(position);
			notifyItemRemoved(position);
		}
	}

	public ReviewModelResult getItem(int position) {
		return reviewModelResults.get(position);
	}

	@Override
	public int getItemViewType(int position) {
		return (position == reviewModelResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
	}


	public class ReviewViewHolder extends RecyclerView.ViewHolder {
		private TextView txt_name;
		private TextView txt_review;

		public ReviewViewHolder(@NonNull View itemView) {
			super(itemView);
			txt_name = itemView.findViewById(R.id.txt_name);
			txt_review = itemView.findViewById(R.id.txt_review);
		}
	}

	protected class LoadingViewHolder extends RecyclerView.ViewHolder {

		public LoadingViewHolder(View itemView) {
			super(itemView);
		}
	}
}
