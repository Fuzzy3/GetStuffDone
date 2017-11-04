package com.oestbalmer.android.getstuffdone;


import com.oestbalmer.android.getstuffdone.Model.Task;

import io.realm.OrderedRealmCollection;
import io.realm.RealmList;

public interface RealmDatabase {

    void updateTask(Task task);
    void updateTask(Task task, String title, String taskType);
    Task getTaskFromId(String id);
    OrderedRealmCollection<Task> getAllTasks();
    OrderedRealmCollection<Task> getAllTodayTasks();
    OrderedRealmCollection<Task> getAllPendingTasks();
    void completeTask(Task task);
    void unCompleteTask(Task task);
    void removeTask(Task task);


}