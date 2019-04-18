package com.ss.moviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Shubham Singhal on 01,April,2019
 */
public class GenresModel implements Serializable {
	@SerializedName("id")
	@Expose
	private Integer id;

	@SerializedName("name")
	@Expose
	private String name;

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
