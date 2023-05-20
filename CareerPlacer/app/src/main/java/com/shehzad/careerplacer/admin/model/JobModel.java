package com.shehzad.careerplacer.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class JobModel implements Parcelable {
    String title, position, salary, jobType, skills, qualification, lastApplyDate, description, location, aboutCompany, url, uploadDate, time, key;

    public JobModel() {
    }

    public JobModel(String title, String position, String salary, String jobType, String skills, String qualification, String lastApplyDate, String description, String location, String aboutCompany, String url, String uploadDate, String time, String key) {
        this.title = title;
        this.position = position;
        this.salary = salary;
        this.jobType = jobType;
        this.skills = skills;
        this.qualification = qualification;
        this.lastApplyDate = lastApplyDate;
        this.description = description;
        this.location = location;
        this.aboutCompany = aboutCompany;
        this.url = url;
        this.uploadDate = uploadDate;
        this.time = time;
        this.key = key;
    }

    protected JobModel(Parcel in) {
        title = in.readString();
        position = in.readString();
        salary = in.readString();
        jobType = in.readString();
        skills = in.readString();
        qualification = in.readString();
        lastApplyDate = in.readString();
        description = in.readString();
        location = in.readString();
        aboutCompany = in.readString();
        url = in.readString();
        uploadDate = in.readString();
        time = in.readString();
        key = in.readString();
    }

    public static final Creator<JobModel> CREATOR = new Creator<JobModel>() {
        @Override
        public JobModel createFromParcel(Parcel in) {
            return new JobModel(in);
        }

        @Override
        public JobModel[] newArray(int size) {
            return new JobModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getLastApplyDate() {
        return lastApplyDate;
    }

    public void setLastApplyDate(String lastApplyDate) {
        this.lastApplyDate = lastApplyDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAboutCompany() {
        return aboutCompany;
    }

    public void setAboutCompany(String aboutCompany) {
        this.aboutCompany = aboutCompany;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(position);
        dest.writeString(salary);
        dest.writeString(jobType);
        dest.writeString(skills);
        dest.writeString(qualification);
        dest.writeString(lastApplyDate);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(aboutCompany);
        dest.writeString(url);
        dest.writeString(uploadDate);
        dest.writeString(time);
        dest.writeString(key);
    }
}
