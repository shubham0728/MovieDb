package com.ss.moviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Shubham Singhal on 01,April,2019
 */
public class CastModelResult implements Serializable{

		@SerializedName("cast_id")
		@Expose
		private Integer cast_id;

		@SerializedName("character")
		@Expose
		private String character;

		@SerializedName("name")
		@Expose
		private String name;

		@SerializedName("profile_path")
		@Expose
		private String profile_path;

		public Integer getCast_id() {
			return cast_id;
		}

		public void setCast_id(Integer cast_id) {
			this.cast_id = cast_id;
		}

		public String getCharacter() {
			return character;
		}

		public void setCharacter(String character) {
			this.character = character;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getProfile_path() {
			return profile_path;
		}

		public void setProfile_path(String profile_path) {
			this.profile_path = profile_path;
		}


}
