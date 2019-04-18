package com.ss.moviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham Singhal on 01,April,2019
 */
public class MovieDetailModel implements Serializable {

	@SerializedName("backdrop_path")
	@Expose
	private String backdrop_path;

	@SerializedName("budget")
	@Expose
	private Integer budget;

	@SerializedName("genres")
	@Expose
	private List<GenresModel> genresModels = new ArrayList<>();

	@SerializedName("id")
	@Expose
	private Integer id;

	@SerializedName("original_language")
	@Expose
	private String original_language;

	@SerializedName("original_title")
	@Expose
	private String original_title;

	@SerializedName("overview")
	@Expose
	private String overview;

	@SerializedName("poster_path")
	@Expose
	private String poster_path;

	@SerializedName("tagline")
	@Expose
	private String tagline;

	@SerializedName("title")
	@Expose
	private String title;

	@SerializedName("release_date")
	@Expose
	private String release_date;

	@SerializedName("production_companies")
	@Expose
	private List<ProductionCompanyModel> productionCompanyModels = new ArrayList<>();

	public String getBackdrop_path() {
		return backdrop_path;
	}

	public void setBackdrop_path(String backdrop_path) {
		this.backdrop_path = backdrop_path;
	}

	public Integer getBudget() {
		return budget;
	}

	public void setBudget(Integer budget) {
		this.budget = budget;
	}

	public List<GenresModel> getGenresModels() {
		return genresModels;
	}

	public void setGenresModels(List<GenresModel> genresModels) {
		this.genresModels = genresModels;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOriginal_language() {
		return original_language;
	}

	public void setOriginal_language(String original_language) {
		this.original_language = original_language;
	}

	public String getOriginal_title() {
		return original_title;
	}

	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getPoster_path() {
		return poster_path;
	}

	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ProductionCompanyModel> getProductionCompanyModels() {
		return productionCompanyModels;
	}

	public void setProductionCompanyModels(List<ProductionCompanyModel> productionCompanyModels) {
		this.productionCompanyModels = productionCompanyModels;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}
}
