package com.shehzad.careerplacer.student.ui.job.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shehzad.careerplacer.student.ui.job.JobFragment;
import com.shehzad.careerplacer.student.ui.job.db.AppliedJob;
import com.shehzad.careerplacer.student.ui.job.repository.AppliedJobRepository;

import java.util.List;

/*
* view model is to keep refrence to the job repo and list of all jobs to up_to_date
 */
public class AppliedJobViewModel extends AndroidViewModel {

    private AppliedJobRepository mRepository;
    private final LiveData<List<AppliedJob>> mAllJob;
    private LiveData<Boolean> isExist;

    public AppliedJobViewModel(Application application) {
        super(application);

        mRepository = new AppliedJobRepository(application);
        mAllJob = mRepository.getAllJob();

    }

    public void insert(AppliedJob job){
        mRepository.insert(job);
    }

    public LiveData<List<AppliedJob>> getAllJob(){
        return mAllJob;
    }

//    public LiveData<Boolean> isExist(int id){
//        isExist = mRepository.isExist(id);
//        return isExist;
//    }

    public LiveData<Integer> isExist(String title, String position){
        return mRepository.checkItem(title, position);
    }
}
