package com.oestbalmer.android.getstuffdone.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Task extends RealmObject {

    @PrimaryKey
    private String mId;
    private String mTaskDescription;
    private String mTaskTitle;
    private String mTaskType;

    public Task(String taskTitle, String taskType) {
        mId = UUID.randomUUID().toString();
        mTaskTitle = taskTitle;
        mTaskType = taskType;
    }

    public Task() {
        mId = UUID.randomUUID().toString();
    }

    public Task(String id, String taskTitle, String taskType) {
        mId = id;
        mTaskTitle = taskTitle;
        mTaskType = taskType;
    }

    public String getTaskType() {
        return mTaskType;
    }

    public void setTaskType(String taskType) {
        mTaskType = taskType;
    }

    public Task(Task task) {
        mId = task.getId();
        mTaskTitle = task.getTaskTitle();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTaskDescription() {
        return mTaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        mTaskDescription = taskDescription;
    }

    public boolean isCompleted() {
        return mTaskType == TaskType.completed() ? true : false;
    }

    public String getTaskTitle() {
        return mTaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        mTaskTitle = taskTitle;
    }

}
