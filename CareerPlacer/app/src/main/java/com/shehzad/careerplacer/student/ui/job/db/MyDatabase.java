package com.shehzad.careerplacer.student.ui.job.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {AppliedJob.class}, version = 4)
public abstract class MyDatabase extends RoomDatabase {
    private static volatile MyDatabase dbInstance;

    public abstract AppliedJobDao jobDao();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService executor = Executors
            .newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized MyDatabase getDatabase(final Context context) {
        if (dbInstance == null) {
            dbInstance = Room
                    .databaseBuilder(
                            context.getApplicationContext(),
                            MyDatabase.class, "job_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return dbInstance;
    }
}
