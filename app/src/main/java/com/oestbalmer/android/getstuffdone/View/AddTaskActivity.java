package com.oestbalmer.android.getstuffdone.View;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.oestbalmer.android.getstuffdone.Model.Task;
import com.oestbalmer.android.getstuffdone.Model.TaskType;
import com.oestbalmer.android.getstuffdone.R;
import com.oestbalmer.android.getstuffdone.RealmDatabase;
import com.oestbalmer.android.getstuffdone.RealmDatabaseImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TASK_ID_COLLECTER = "TASKIDCOLLECTOR";
    private boolean isNewTask;
    private Task mTask;
    private RealmDatabase mRealmDatabase;

    @BindView(R.id.pending_today_spinner) Spinner mSpinner;
    @BindView(R.id.task_title_edit_text) EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        mRealmDatabase = RealmDatabaseImpl.get(this);
        setupView();


        String taskId = getIntent().getExtras().getString(TASK_ID_COLLECTER);

        if(!taskId.equals("0")) {
            isNewTask = false;
            mTask = mRealmDatabase.getTaskFromId(taskId);
            fillInputs();
        } else {
            isNewTask = true;
            mTask = new Task();
        }
    }

    private void setupView() {
        ButterKnife.bind(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.TodoOrPendingList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);
    }

    private void fillInputs() {
        mEditText.setText(mTask.getTaskTitle());
        Log.i("WHAT IS THE TYPE", mTask.getTaskType());
        if(mTask.getTaskType().equals(TaskType.pending())) {
            mSpinner.setSelection(1);
        }
    }

    private String getTaskType() {
        Log.i("SPINNER TYPE: ", mSpinner.getSelectedItem().toString());
        if(mSpinner.getSelectedItem().toString().equals(TaskType.pending())) {
            return TaskType.pending();
        } else {
            return TaskType.today();
        }
    }

    @OnClick(R.id.done_with_task_button)
    public void onDoneButtonClick() {
        if(!mEditText.getText().toString().equals("")) {
            if(isNewTask) {
                mTask.setTaskTitle(mEditText.getText().toString());
                mTask.setTaskType(getTaskType());
                mRealmDatabase.updateTask(mTask);
            } else{
                mRealmDatabase.updateTask(mTask, mEditText.getText().toString(), getTaskType());
            }
            finish();
        } else {
            Toast.makeText(this, "Please specify the task", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, AddTaskActivity.class);
        i.putExtra(TASK_ID_COLLECTER, "0");
        return i;
    }

    public static Intent newIntent(Context packageContext, String TaskId) {
        Intent i = new Intent(packageContext, AddTaskActivity.class);
        i.putExtra(TASK_ID_COLLECTER, TaskId);
        return i;
    }

}
