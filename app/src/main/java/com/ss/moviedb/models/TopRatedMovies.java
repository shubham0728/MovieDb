package com.ss.moviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham Singhal on 30,March,2019
 */
public class TopRatedMovies implements Serializable {

	@SerializedName("page")
	@Expose
	private Integer page;

	@SerializedName("total_results")
	@Expose
	private Integer totalResults;

	@SerializedName("total_pages")
	@Expose
	private Integer totalPages;

	@SerializedName("results")
	@Expose
	private List<TopRatedMovieResults> results = new ArrayList<TopRatedMovieResults>();

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<TopRatedMovieResults> getResults() {
		return results;
	}

	public void setResults(List<TopRatedMovieResults> results) {
		this.results = results;
	}
}
