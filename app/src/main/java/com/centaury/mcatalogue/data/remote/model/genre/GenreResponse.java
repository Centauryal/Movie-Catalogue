package com.centaury.mcatalogue.data.remote.model.genre;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GenreResponse {

    @SerializedName("genres")
    private List<GenresItem> genres;

    public List<GenresItem> getGenres() {
        return genres;
    }

    public void setGenres(List<GenresItem> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return
                "GenreResponse{" +
                        "genres = '" + genres + '\'' +
                        "}";
    }
}