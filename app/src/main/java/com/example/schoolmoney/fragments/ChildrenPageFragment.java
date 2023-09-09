package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Child;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.zip.Inflater;


public class ChildrenPageFragment extends Fragment {

    private View view;
    private AppLab appLab;
    private ChildAdapter childAdapter;
    private RecyclerView childrenRecycleView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_children_page, container, false);
        childrenRecycleView = view.findViewById(R.id.child_recycler_view);
        childrenRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appLab = AppLab.getAppLab(getContext());
        AppFragmentManager.createBottomButtons();
        setFabButton();
        updateUI();
        return view;
    }

    private void setFabButton() {
        FloatingActionButton addNewChildButton = view.findViewById(R.id.add_new_child_fab_button);
        addNewChildButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new CreateNewChildFragment());
        });
    }

    private void updateUI() {
        appLab = AppLab.getAppLab(getActivity());
        List<Child> childrenList = appLab.getChildrenList();

        if (childAdapter == null) {
            childAdapter = new ChildAdapter(childrenList);
            childrenRecycleView.setAdapter(childAdapter);
        } else {
            childAdapter.notifyDataSetChanged();
        }

    }

    private class ChildHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView childNameTextView;
        //private final LinearLayout layout;

        private Child child;

        public ChildHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.child_item, parent, false));
            childNameTextView = itemView.findViewById(R.id.child_item_title);
            //layout = itemView.findViewById(R.id.child_item_row);
            itemView.setOnClickListener(this);
        }

        public void bind(Child child) {
            if (child.getChildName() != null) {
                this.child = child;
                childNameTextView.setText(child.getChildName());
            }
        }

        @Override
        public void onClick(View v) {
            AppFragmentManager.openFragment(ChildCardFragment.newInstance(child.getUuid()));
        }
    }

    private class ChildAdapter extends RecyclerView.Adapter<ChildHolder> {

        private final List<Child> childrenList;

        public ChildAdapter(List<Child> childrenList) {
            this.childrenList = childrenList;
        }

        @NonNull
        @Override
        public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ChildHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildHolder holder, int position) {
            Child child = childrenList.get(position);
            holder.bind(child);
        }

        @Override
        public int getItemCount() {
            return childrenList.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}