package com.centaury.mcatalogue.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class MoviesResponse implements Parcelable {

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<MovieResultsItem> results;

	@SerializedName("total_results")
	private int totalResults;

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setResults(List<MovieResultsItem> results){
		this.results = results;
	}

	public List<MovieResultsItem> getResults(){
		return results;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	@Override
 	public String toString(){
		return 
			"MoviesResponse{" + 
			"page = '" + page + '\'' + 
			",total_pages = '" + totalPages + '\'' + 
			",results = '" + results + '\'' + 
			",total_results = '" + totalResults + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.page);
		dest.writeInt(this.totalPages);
		dest.writeTypedList(this.results);
		dest.writeInt(this.totalResults);
	}

	public MoviesResponse() {
	}

	protected MoviesResponse(Parcel in) {
		this.page = in.readInt();
		this.totalPages = in.readInt();
		this.results = in.createTypedArrayList(MovieResultsItem.CREATOR);
		this.totalResults = in.readInt();
	}

	public static final Parcelable.Creator<MoviesResponse> CREATOR = new Parcelable.Creator<MoviesResponse>() {
		@Override
		public MoviesResponse createFromParcel(Parcel source) {
			return new MoviesResponse(source);
		}

		@Override
		public MoviesResponse[] newArray(int size) {
			return new MoviesResponse[size];
		}
	};
}