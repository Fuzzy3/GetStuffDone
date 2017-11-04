package com.oestbalmer.android.getstuffdone.View;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oestbalmer.android.getstuffdone.Model.Task;
import com.oestbalmer.android.getstuffdone.R;
import com.oestbalmer.android.getstuffdone.RealmDatabase;
import com.oestbalmer.android.getstuffdone.RealmDatabaseImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class QuickAddTaskFragment extends DialogFragment {

    @BindView(R.id.pending_quick_task_recycler_view) RecyclerView mRecyclerView;
    private QuickTasksAdapter mQuickTasksAdapter;
    private RealmDatabase mDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list, container, false);
        ButterKnife.bind(this, v);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);

        mDatabase = RealmDatabaseImpl.get(inflater.getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        mQuickTasksAdapter = new QuickTasksAdapter(mDatabase.getAllPendingTasks());
        mRecyclerView.setAdapter(mQuickTasksAdapter);

        return v;
    }

    public static QuickAddTaskFragment newInstance() {

        Bundle args = new Bundle();

        QuickAddTaskFragment fragment = new QuickAddTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }



    public class QuickTaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.quick_task_title) TextView mTextViewQuickTaskTitle;

        private Task mViewHolderTask;

        public QuickTaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindUserViewHolder(Task task) {
            mViewHolderTask = task;
            mTextViewQuickTaskTitle.setText(task.getTaskTitle());
        }
    }

    public class QuickTasksAdapter extends RealmRecyclerViewAdapter<Task, QuickTaskViewHolder> {

        OrderedRealmCollection<Task> allQuickTasks;

        public QuickTasksAdapter(@Nullable OrderedRealmCollection<Task> data) {
            super(data, true);
            allQuickTasks = data;
        }

        @Override
        public QuickTaskViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.fragment_task_item, parent, false);

            return new QuickTaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(QuickTaskViewHolder userViewHolder, int i) {
            userViewHolder.bindUserViewHolder(allQuickTasks.get(i));

        }

        @Override
        public int getItemCount() {
            return allQuickTasks.size();
        }

    }
}
