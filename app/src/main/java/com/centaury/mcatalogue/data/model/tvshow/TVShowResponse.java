package com.centaury.mcatalogue.data.model.tvshow;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class TVShowResponse implements Parcelable {

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<TVShowResultsItem> results;

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

	public void setResults(List<TVShowResultsItem> results){
		this.results = results;
	}

	public List<TVShowResultsItem> getResults(){
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
			"TVShowResponse{" + 
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

	public TVShowResponse() {
	}

	protected TVShowResponse(Parcel in) {
		this.page = in.readInt();
		this.totalPages = in.readInt();
		this.results = in.createTypedArrayList(TVShowResultsItem.CREATOR);
		this.totalResults = in.readInt();
	}

	public static final Parcelable.Creator<TVShowResponse> CREATOR = new Parcelable.Creator<TVShowResponse>() {
		@Override
		public TVShowResponse createFromParcel(Parcel source) {
			return new TVShowResponse(source);
		}

		@Override
		public TVShowResponse[] newArray(int size) {
			return new TVShowResponse[size];
		}
	};
}