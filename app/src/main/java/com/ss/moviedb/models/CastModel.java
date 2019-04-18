package com.ss.moviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Shubham Singhal on 01,April,2019
 */
public class CastModel implements Serializable {

	@SerializedName("id")
	@Expose
	private Integer id;

	@SerializedName("cast")
	@Expose
	private List<CastModelResult> cast;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<CastModelResult> getCast() {
		return cast;
	}

	public void setCast(List<CastModelResult> cast) {
		this.cast = cast;
	}
}

