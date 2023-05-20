package com.shehzad.careerplacer.student.ui.job.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.shehzad.careerplacer.student.ui.job.db.AppliedJob;
import com.shehzad.careerplacer.student.ui.job.db.AppliedJobDao;
import com.shehzad.careerplacer.student.ui.job.db.MyDatabase;

import java.util.List;

public class AppliedJobRepository {

    private AppliedJobDao mJobDao;
    private LiveData<List<AppliedJob>> mAllJob;
    private LiveData<Boolean> isExist;

    public AppliedJobRepository(Application application){
        MyDatabase db = MyDatabase.getDatabase(application);
        mJobDao = db.jobDao();
        mAllJob = mJobDao.getAll();
    }

    public LiveData<List<AppliedJob>> getAllJob(){
        return mAllJob;
    }

    public void insert(AppliedJob job){
        MyDatabase.executor.execute(() -> {
            mJobDao.insert(job);
        });
    }

  public LiveData<Integer> checkItem(String title, String position){
      MutableLiveData<Integer> data = new MutableLiveData();
      MyDatabase.executor.execute(() -> {
          int count = mJobDao.isExist(title,position);
          data.postValue(count);
      });
      return data;
  }
}
