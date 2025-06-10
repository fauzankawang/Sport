package com.example.sport.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.sport.model.Sport;

@Database(entities = {Sport.class}, version = 4, exportSchema = false)
public abstract class SportDatabase extends RoomDatabase {
    private static volatile SportDatabase INSTANCE;
    public abstract SportDao sportDao();

    public static SportDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SportDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    SportDatabase.class,
                                    "sport_database")
                            .fallbackToDestructiveMigration() // Menambahkan ini untuk menangani perubahan skema
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}