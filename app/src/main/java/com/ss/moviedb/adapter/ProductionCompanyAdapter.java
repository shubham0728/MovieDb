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
import com.ss.moviedb.models.ProductionCompanyModel;
import com.ss.moviedb.models.TopRatedMovieResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham Singhal on 01,April,2019
 */
public class ProductionCompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int ITEM = 0;
	private static final int LOADING = 1;
	private Context mCtx;
	private List<ProductionCompanyModel> productionCompanyModels;
	private boolean isLoadingAdded = false;

	/**
	 * Constructor
	 *
	 * @param _ctx
	 */
	public ProductionCompanyAdapter(Context _ctx) {
		this.mCtx = _ctx;
		productionCompanyModels = new ArrayList<>();
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		RecyclerView.ViewHolder viewHolder = null;
		switch (i) {
			case ITEM:
				View productionView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.production_company_adapter, viewGroup, false);
				viewHolder = new ProductionCompanyViewHolder(productionView);
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
					ProductionCompanyViewHolder productionCompanyViewHolder = (ProductionCompanyViewHolder) viewHolder;
					ProductionCompanyModel productionCompanyModel = productionCompanyModels.get(i);
					Glide.with(mCtx)
							.load(mCtx.getString(R.string.image_base_url) + productionCompanyModel.getLogo_path())
							.placeholder(R.drawable.placeholder)
							.diskCacheStrategy(DiskCacheStrategy.ALL)   // cache image
							.centerCrop()
							.into(productionCompanyViewHolder.img_movie_production);
					productionCompanyViewHolder.txt_production_name.setText(productionCompanyModel.getName());
					productionCompanyViewHolder.progress_production.setVisibility(View.GONE);

				case LOADING:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		return productionCompanyModels.size();
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
	public void add(ProductionCompanyModel productionCompanyModel) {
		productionCompanyModels.add(productionCompanyModel);
		notifyItemInserted(productionCompanyModels.size() - 1);
	}

	/**
	 * Add all data from the api call.
	 *
	 * @param productionCompanyModels
	 */
	public void addAll(List<ProductionCompanyModel> productionCompanyModels) {
		for (ProductionCompanyModel result : productionCompanyModels) {
			add(result);
		}
	}

	public void remove(ProductionCompanyModel productionCompanyModel) {
		int position = productionCompanyModels.indexOf(productionCompanyModel);
		if (position > -1) {
			productionCompanyModels.remove(position);
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
		add(new ProductionCompanyModel());
	}

	public void removeLoadingFooter() {
		isLoadingAdded = false;

		int position = productionCompanyModels.size() - 1;
		ProductionCompanyModel productionCompanyModel = getItem(position);

		if (productionCompanyModel != null) {
			productionCompanyModels.remove(position);
			notifyItemRemoved(position);
		}
	}

	public ProductionCompanyModel getItem(int position) {
		return productionCompanyModels.get(position);
	}

	@Override
	public int getItemViewType(int position) {
		return (position == productionCompanyModels.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
	}


	public class ProductionCompanyViewHolder extends RecyclerView.ViewHolder {
		private TextView txt_production_name;
		private ProgressBar progress_production;
		private ImageView img_movie_production;

		public ProductionCompanyViewHolder(@NonNull View itemView) {
			super(itemView);
			txt_production_name = itemView.findViewById(R.id.txt_production_name);

			progress_production = itemView.findViewById(R.id.progress_production);
			img_movie_production = itemView.findViewById(R.id.img_movie_production);

		}
	}

	protected class LoadingViewHolder extends RecyclerView.ViewHolder {

		public LoadingViewHolder(View itemView) {
			super(itemView);
		}
	}
}
