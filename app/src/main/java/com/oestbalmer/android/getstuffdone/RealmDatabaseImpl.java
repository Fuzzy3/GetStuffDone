package com.oestbalmer.android.getstuffdone;

import android.content.Context;

import com.oestbalmer.android.getstuffdone.Model.Task;
import com.oestbalmer.android.getstuffdone.Model.TaskType;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmDatabaseImpl implements RealmDatabase {

    private static Realm mRealm;
    private static RealmDatabaseImpl mRealmDatabase;

    private RealmDatabaseImpl() {
        mRealm = Realm.getDefaultInstance();
    }

    public static RealmDatabaseImpl get(Context context) {
        if (mRealmDatabase == null) {
            Realm.init(context);
            mRealmDatabase = new RealmDatabaseImpl();
        }
        return mRealmDatabase;
    }

    public void updateTask(Task task) {
        final Task fTask = task;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealmOrUpdate(fTask);
            }
        });
    }

    @Override
    public void updateTask(Task task, String title, String taskType) {
        final Task fTask = task;
        final String fTitle = title;
        final String fTaskType = taskType;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fTask.setTaskTitle(fTitle);
                fTask.setTaskType(fTaskType);
            }
        });
    }

    @Override
    public void completeTask(Task task) {
        final Task fTask = task;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fTask.setTaskType(TaskType.completed());
            }
        });
    }

    @Override
    public void unCompleteTask(Task task) {
        final Task fTask = task;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fTask.setTaskType(TaskType.today());
            }
        });
    }

    @Override
    public void removeTask(Task task) {
        final RealmResults<Task> taskToRemove = mRealm.where(Task.class).equalTo("mId", task.getId()).findAll();
        if(taskToRemove.size() != 0) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    taskToRemove.deleteAllFromRealm();
                }
            });
        }
    }

    @Override
    public Task getTaskFromId(String id) {
        Task task = mRealm.where(Task.class).equalTo("mId", id).findFirst();
        return task;
    }

    @Override
    public OrderedRealmCollection<Task> getAllTodayTasks() {
        return mRealm.where(Task.class).equalTo("mTaskType", TaskType.today()).findAll();
    }

    @Override
    public OrderedRealmCollection<Task> getAllPendingTasks() {
        return mRealm.where(Task.class).equalTo("mTaskType", TaskType.pending()).findAll();
    }

    @Override
    public OrderedRealmCollection<Task> getAllTasks() {
        return mRealm.where(Task.class).findAll();
    }


}
