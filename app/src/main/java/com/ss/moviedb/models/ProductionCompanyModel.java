package com.ss.moviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Shubham Singhal on 01,April,2019
 */
public class ProductionCompanyModel implements Serializable {

	@SerializedName("logo_path")
	@Expose
	private String logo_path;

	@SerializedName("id")
	@Expose
	private Integer id;

	@SerializedName("name")
	@Expose
	private String name;

	public String getLogo_path() {
		return logo_path;
	}

	public void setLogo_path(String logo_path) {
		this.logo_path = logo_path;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
