package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Child;
import com.example.schoolmoney.appLab.Parent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;


public class ChildCardFragment extends Fragment {

    private static final String CHILD_UUID = "child_uuid";
    private AppLab appLab;
    private Child child;
    private EditText noteEditText;
    private ParentAdapter parentAdapter;
    private RecyclerView parentRecycleView;


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_child_card, container, false);
        UUID childUUID = (UUID) getArguments().getSerializable(ChildCardFragment.CHILD_UUID);
        appLab = AppLab.getAppLab(getActivity());
        child = appLab.getChildByUUID(childUUID);
        parentRecycleView = view.findViewById(R.id.parent_recycler_view);
        parentRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindChild();
        setButtons();
        updateUI();
        if (child.getParentsList().size()>0) {
            Log.i("!!@$%!%!%%!%", "8888888888888888888888888888888888888 " + child.getParentsList().get(child.getParentsList().size() - 1).getParentName());
        }
        return view;
    }


    public static ChildCardFragment newInstance(UUID childUUID) {
        Bundle args = new Bundle();
        args.putSerializable(CHILD_UUID, childUUID);
        ChildCardFragment childCardFragment = new ChildCardFragment();
        childCardFragment.setArguments(args);
        return childCardFragment;
    }

    //Показываю имя ребенка в заголовке
    private void bindChild() {
        TextView childNameTextView = view.findViewById(R.id.child_name_on_child_card_text_view);
        noteEditText = view.findViewById(R.id.note_text_view);
        childNameTextView.setText(child.getChildName());
        noteEditText.setText(child.getNote());
    }

    private void setButtons() {
        Button saveButton = view.findViewById(R.id.save_button_on_child_card_fragment);
        Button cancelButton = view.findViewById(R.id.cancel_button_on_child_card_fragment);
        FloatingActionButton addParent = view.findViewById(R.id.add_new_parent_fab_button);


        saveButton.setOnClickListener(o -> {
            appLab.addNote(child.getUuid(), noteEditText.getText().toString());
            goToList();
        });

        cancelButton.setOnClickListener(o -> {
            noteEditText.setText(child.getNote());
            goToList();
        });

        addParent.setOnClickListener(o -> {
            AppFragmentManager.openFragment(CreateNewParentFragment.newInstance(child.getUuid()));
        });
    }

    /**
     * Recycler для parents
     */
    private class ParentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       // private LinearLayout layout;
        private TextView parentName;
        private TextView parentPhone;
        private Parent parent;


        public ParentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.parent_item, parent, false));
            parentName = itemView.findViewById(R.id.parent_item_title);
            parentPhone = itemView.findViewById(R.id.parent_phone);

        }

        public void bind(Parent parent) {
            this.parent = parent;
            parentName.setText(parent.getParentName());
            parentPhone.setText(parent.getParentPhone());

        }

        @Override
        public void onClick(View v) {
        }
    }

    private class ParentAdapter extends RecyclerView.Adapter<ParentHolder> {

        private final List<Parent> parents;

        public ParentAdapter(List<Parent> parents) {
            this.parents = parents;
        }

        @NonNull
        @Override
        public ParentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ParentHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ParentHolder holder, int position) {

            Parent parent = parents.get(position);
            holder.bind(parent);
        }

        @Override
        public int getItemCount() {
            return parents.size();
        }
    }


    private void updateUI() {
        List<Parent> parentsList = child.getParentsList();

        if (parentAdapter == null) {

            parentAdapter = new ParentAdapter(parentsList);
            parentRecycleView.setAdapter(parentAdapter);

        } else {
            parentAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Другое
     */


    @Override
    public void onPause() {
        super.onPause();
        appLab.addNote(child.getUuid(), noteEditText.getText().toString());
        updateUI();
        // goToList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        goToList();
    }

    @Override
    public void onResume() {
        updateUI();
        super.onResume();
    }


    private void goToList() {
        AppFragmentManager.openFragment(new ChildrenPageFragment());
    }
}