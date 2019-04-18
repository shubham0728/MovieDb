package com.ss.moviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Shubham Singhal on 02,April,2019
 */
public class ReviewModel implements Serializable {

	@SerializedName("id")
	@Expose
	private Integer id;

	@SerializedName("page")
	@Expose
	private Integer page;

	@SerializedName("total_pages")
	@Expose
	private Integer total_pages;


	@SerializedName("total_results")
	@Expose
	private Integer total_results;

	@SerializedName("results")
	@Expose
	private List<ReviewModelResult> reviewModelResults;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(Integer total_pages) {
		this.total_pages = total_pages;
	}

	public Integer getTotal_results() {
		return total_results;
	}

	public void setTotal_results(Integer total_results) {
		this.total_results = total_results;
	}

	public List<ReviewModelResult> getReviewModelResults() {
		return reviewModelResults;
	}

	public void setReviewModelResults(List<ReviewModelResult> reviewModelResults) {
		this.reviewModelResults = reviewModelResults;
	}
}
