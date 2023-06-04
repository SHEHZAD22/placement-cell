package com.shehzad.careerplacer.student.ui.job.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppliedJobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppliedJob appliedJob);

    @Query("SELECT * FROM applied_job")
    LiveData<List<AppliedJob>> getAll();

    @Query("SELECT COUNT() " +
            "FROM applied_job WHERE title = :title " +
            "AND position = :position")
    int isExist(String title, String position);

}
