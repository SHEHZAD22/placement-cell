package com.shehzad.careerplacer.student.ui.job.model;

public class StudentResumeModel {
    String name, email, phoneNumber, qualification, branch, pincode, location, resume;
    String jobTitle, jobPosition;

    public StudentResumeModel() {
    }

    public StudentResumeModel(String name, String email, String phoneNumber, String qualification, String branch, String pincode, String location, String resume, String jobTitle, String jobPosition) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.qualification = qualification;
        this.branch = branch;
        this.pincode = pincode;
        this.location = location;
        this.resume = resume;
        this.jobTitle = jobTitle;
        this.jobPosition = jobPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }
}
