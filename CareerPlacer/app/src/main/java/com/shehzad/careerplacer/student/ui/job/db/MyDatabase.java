package com.shehzad.careerplacer.student.ui.job.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Database(entities = {AppliedJob.class},version = 2)
//public abstract class MyDatabase extends RoomDatabase {
//    public abstract AppliedJobDao appliedJobDao();
//
//    public static MyDatabase dbInstance;
//
////    static final Migration MIGRATION_1_2 = new Migration(1,2) {
////        @Override
////        public void migrate(@NonNull SupportSQLiteDatabase database) {
////
////        }
////    };
//
//    public static MyDatabase getDbInstance(Context context){
//        if (dbInstance == null){
//            dbInstance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "job_db")
////                    .addMigrations(MIGRATION_1_2)
//                    .build();
//        }
//        return dbInstance;
//    }
//
//}

@Database(entities = {AppliedJob.class}, version = 3)
public abstract class MyDatabase extends RoomDatabase {
    private static volatile MyDatabase dbInstance;

    public abstract AppliedJobDao jobDao();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized MyDatabase getDatabase(final Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "job_db")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return dbInstance;
    }

//    private static final RoomDatabase.Callback myRoomDatabaseCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//
//            executor.execute(() -> {
//                AppliedJobDao jobDao = dbInstance.jobDao();
//                jobDao.deleteAll();
//
//                AppliedJob job = new AppliedJob("Hello", "World");
//                jobDao.insert(job);
//            });
//        }
//    };
}
