package com.hapz.assessment;
import com.google.gson.annotations.SerializedName;

public class MovieDetails {

	@SerializedName("Title")
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
