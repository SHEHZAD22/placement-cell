package com.shehzad.careerplacer.student.ui.job.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "applied_job")
public class AppliedJob {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String position;

    public AppliedJob(String title, String position) {
        this.title = title;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
