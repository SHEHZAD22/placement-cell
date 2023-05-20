package com.shehzad.careerplacer.student.ui.job.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("SELECT COUNT() FROM applied_job WHERE title = :title AND position = :position")
    int isExist(String title, String position);
//    @Query("SELECT EXISTS(SELECT * FROM applied_job WHERE title = :title AND position = :position)")
//    Boolean isExist(String title, String position);
//    @Query("SELECT EXISTS(SELECT 1 FROM applied_job WHERE title = :title AND position = :position LIMIT 1)")
//    LiveData<Boolean> isExist(String title, String position);

    @Query("DELETE FROM applied_job")
    void deleteAll();
}
