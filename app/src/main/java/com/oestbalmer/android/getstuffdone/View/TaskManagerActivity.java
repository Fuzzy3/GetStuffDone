package com.oestbalmer.android.getstuffdone.View;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.oestbalmer.android.getstuffdone.Model.Task;
import com.oestbalmer.android.getstuffdone.R;
import com.oestbalmer.android.getstuffdone.RealmDatabase;
import com.oestbalmer.android.getstuffdone.RealmDatabaseImpl;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class TaskManagerActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_tasks) RecyclerView mRecyclerView;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    private TaskCardsAdapter mTaskCardsAdapter;
    private RealmDatabase mDatabase;
    private List<Task> mTaskListSelection;
    private List<String> mTaskListSelectionStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        mDatabase = RealmDatabaseImpl.get(this);
        setupView();
    }

    private void setupView() {
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTaskCardsAdapter = new TaskCardsAdapter(mDatabase.getAllTodayTasks());
        mRecyclerView.setAdapter(mTaskCardsAdapter);

        setupTaskListSelection();

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_add_task: {
                    Intent i = AddTaskActivity.newIntent(TaskManagerActivity.this);
                    startActivity(i);
                    break;
                }
                case R.id.action_add_from_pending_list: {
                    Toast.makeText(TaskManagerActivity.this, "SEE PENDING - not possible yet", Toast.LENGTH_SHORT).show();

                    displayPendingDialog();

                    break;
                }
                case R.id.action_see_pending: {
                    Toast.makeText(TaskManagerActivity.this, "SEE PENDING - not possible yet", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            return true;
            }
        });
    }

    private void displayPendingDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        /*Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);*/

        // Create and show the dialog.
        DialogFragment newFragment = QuickAddTaskFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "tag");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_useraccount:
                startActivity(UserAccountActivity.newInstance(TaskManagerActivity.this));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setupTaskListSelection() {
        mTaskListSelection = mDatabase.getAllPendingTasks();
        mTaskListSelectionStrings = new ArrayList<>();
        for(Task task : mTaskListSelection) {
            mTaskListSelectionStrings.add(task.getTaskTitle());
        }
    }

    private void showCompleteUndoSnackbar(Task task) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.task_manager_layout), task.getTaskTitle() + " completed!" ,Snackbar.LENGTH_LONG);
        mySnackbar.setAction("undo", new CompleteUndoListener(task));
        mySnackbar.show();
    }

    private void showDeleteUndoSnackbar(Task task) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.task_manager_layout), "Task removed!" ,Snackbar.LENGTH_LONG);
        mySnackbar.setAction("undo", new DeleteUndoListener(task));
        mySnackbar.show();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task_title) TextView mTextViewTaskTitle;
        @BindView(R.id.card_view) CardView mTaskCardView;
        private Task mViewHolderTask;

        public TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindUserViewHolder(Task task) {
            mViewHolderTask = task;
            mTextViewTaskTitle.setText(task.getTaskTitle());
        }

        @OnClick(R.id.button_complete)
        public void onCompleteButtonClicked() {
            mDatabase.completeTask(mViewHolderTask);
            showCompleteUndoSnackbar(mViewHolderTask);
            //Toast.makeText(TaskManagerActivity.this, mViewHolderTask.getTaskTitle() + " completed!", Toast.LENGTH_SHORT).show();
        }

        @OnClick(R.id.button_edit)
        public void onEditButtonClicked() {
            Intent i = AddTaskActivity.newIntent(TaskManagerActivity.this, mViewHolderTask.getId());
            startActivity(i);
        }

        @OnClick(R.id.button_remove)
        public void onRemoveButtonClicked() {
            Task taskCopy = new Task(mViewHolderTask);
            mDatabase.removeTask(mViewHolderTask);
            showDeleteUndoSnackbar(taskCopy);

        }


    }

    public class TaskCardsAdapter extends RealmRecyclerViewAdapter<Task, TaskViewHolder> {

        OrderedRealmCollection<Task> allTasks;

        public TaskCardsAdapter(@Nullable OrderedRealmCollection<Task> data) {
            super(data, true);
            allTasks = data;

        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.card_view, parent, false);

            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder userViewHolder, int i) {
            userViewHolder.bindUserViewHolder(allTasks.get(i));

        }

        @Override
        public int getItemCount() {
            return allTasks.size();
        }

    }

    public class CompleteUndoListener implements View.OnClickListener {
        private RealmDatabase mUndoRealmDatabase;
        private Task mUndoTask;

        public CompleteUndoListener(Task task) {
            mUndoRealmDatabase = RealmDatabaseImpl.get(TaskManagerActivity.this);
            mUndoTask = task;
        }

        @Override
        public void onClick(View v) {
            mUndoRealmDatabase.unCompleteTask(mUndoTask);
        }
    }

    public class DeleteUndoListener implements View.OnClickListener {
        private RealmDatabase mUndoRealmDatabase;
        private Task mUndoTask;

        public DeleteUndoListener(Task task) {
            mUndoRealmDatabase = RealmDatabaseImpl.get(TaskManagerActivity.this);
            mUndoTask = task;
        }

        @Override
        public void onClick(View v) {
            mUndoRealmDatabase.updateTask(mUndoTask);
        }
    }
}
