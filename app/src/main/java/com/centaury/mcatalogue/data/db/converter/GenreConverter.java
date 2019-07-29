package com.centaury.mcatalogue.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Centaury on 7/27/2019.
 */
public class GenreConverter {
    @TypeConverter
    public static List<Integer> restoreList(Integer listOfInteger) {
        return new Gson().fromJson(String.valueOf(listOfInteger), new TypeToken<List<Integer>>() {
        }.getType());
    }

    @TypeConverter
    public static Integer saveList(List<Integer> listOfInteger) {
        return Integer.valueOf(new Gson().toJson(listOfInteger));
    }
}
