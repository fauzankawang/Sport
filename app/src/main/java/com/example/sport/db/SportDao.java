package com.example.sport.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.sport.model.Sport;
import java.util.List;

@Dao
public interface SportDao {
    @Query("SELECT * FROM sports")
    LiveData<List<Sport>> getAllSports();

    @Query("SELECT * FROM sports WHERE isFavorite = 1")
    LiveData<List<Sport>> getFavoriteSports();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Sport sport);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Sport> sports);

    @Update
    void update(Sport sport);

    @Delete
    void delete(Sport sport);

    @Query("DELETE FROM sports")
    void deleteAll();

    @Query("SELECT * FROM sports WHERE id = :sportId LIMIT 1")
    Sport getSportById(String sportId);

    @Query("UPDATE sports SET isFavorite = :isFavorite WHERE id = :sportId")
    void updateFavoriteStatus(String sportId, boolean isFavorite);
}