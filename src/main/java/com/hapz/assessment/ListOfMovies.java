package com.hapz.assessment;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ListOfMovies {

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("data")
	private List<MovieDetails> dataList;

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<MovieDetails> getDataList() {
		return dataList;
	}

	public void setDataList(List<MovieDetails> dataList) {
		this.dataList = dataList;
	}

}
