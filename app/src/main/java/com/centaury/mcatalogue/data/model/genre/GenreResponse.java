package com.centaury.mcatalogue.data.model.genre;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GenreResponse{

	@SerializedName("genres")
	private List<GenresItem> genres;

	public void setGenres(List<GenresItem> genres){
		this.genres = genres;
	}

	public List<GenresItem> getGenres(){
		return genres;
	}

	@Override
 	public String toString(){
		return 
			"GenreResponse{" + 
			"genres = '" + genres + '\'' + 
			"}";
		}
}