package com.example.schoolmoney.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Child;
import com.example.schoolmoney.appLab.Money;
import com.example.schoolmoney.appLab.Parent;
import com.example.schoolmoney.fragments.windows.DeleteChildFloatingWindowFragment;
import com.example.schoolmoney.fragments.windows.MoneyFloatingWindowFragment;
import com.example.schoolmoney.fragments.windows.ParentFloatingWindowFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class ChildCardFragment extends Fragment {

    private static final String CHILD_UUID = "child_uuid";
    private AppLab appLab;
    private Child child;
    private EditText noteEditText;
    private ParentAdapter parentAdapter;
    private RecyclerView parentRecycleView;
    private MoneyAdapter moneyAdapter;
    private RecyclerView moneyRecyclerView;
    private EditText childNameTextView;
    private boolean dontWantToDelete = true;


    private void deleteThisChild() {
        dontWantToDelete = false;
    }


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
        moneyRecyclerView = view.findViewById(R.id.child_money_recycler_view);
        moneyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindChild();
        setButtons();
        updateUI();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Получите активность, в которой находится фрагмент
        AppCompatActivity activity = (AppCompatActivity) requireActivity();

        // Включите обработку кнопки "Назад"
        activity.getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Здесь вы можете определить, что должно произойти при нажатии кнопки "Назад"
                // Например, выполнить какое-то действие или перейти на другой фрагмент
                // Если вы хотите, чтобы кнопка "Назад" просто закрыла фрагмент, можно вызвать
                // метод requireActivity().onBackPressed().
                goToList();
            }
        });
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
        childNameTextView = view.findViewById(R.id.child_name_on_child_card_text_view);
        noteEditText = view.findViewById(R.id.note_text_view);
        childNameTextView.setText(child.getChildName());
        noteEditText.setText(child.getNote());
    }

    private void setButtons() {
        Button deleteButton = view.findViewById(R.id.delete_button_on_child_card_fragment);
        Button cancelButton = view.findViewById(R.id.cancel_button_on_child_card_fragment);
        ImageButton addParent = view.findViewById(R.id.add_new_parent_fab_button);
        ImageButton addMoney = view.findViewById(R.id.add_new_child_money_fab_button);
        ImageButton changeNameButton = view.findViewById(R.id.change_child_name_image_button);


        deleteButton.setOnLongClickListener(o -> {
            deleteThisChild();
            AppFragmentManager.addFragment(new DeleteChildFloatingWindowFragment(child));
            return true;
        });

        cancelButton.setOnClickListener(o -> {
            goToList();
        });

        addParent.setOnClickListener(o -> {
            AppFragmentManager.openFragment(CreateNewParentFragment.newInstance(child.getUuid()));
        });

        addMoney.setOnClickListener(o -> {
            AppFragmentManager.openFragment(AddMoneyFormChildrenFragment.newInstance(child.getUuid()));
        });

        changeNameButton.setOnClickListener(o -> {
            childNameTextView.setEnabled(true);
            childNameTextView.requestFocus();
            // Откройте клавиатуру
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(childNameTextView, InputMethodManager.SHOW_IMPLICIT);
        });
    }


    /**
     * Recycler для parents
     */

    private class ParentHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        // private LinearLayout layout;
        private TextView parentName;
        private TextView parentPhone;
        private Parent parent;


        public ParentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.parent_item, parent, false));
            parentName = itemView.findViewById(R.id.parent_item_title);
            parentPhone = itemView.findViewById(R.id.parent_phone);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(Parent parent) {
            this.parent = parent;
            parentName.setText(parent.getParentName());
            parentPhone.setText(parent.getParentPhone());

        }

        @Override
        public boolean onLongClick(View v) {
            AppFragmentManager.addFragment(new ParentFloatingWindowFragment(child, parent.getParentName()));
            return true;
        }


        @Override
        public void onClick(View v) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + parent.getParentPhone()));
            requireActivity().startActivity(callIntent);
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


    /**
     * Recycler для MONEY
     */

    private class MoneyHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        // private LinearLayout layout;
        private TextView moneyValue;
        private TextView moneyDate;
        private Money money;


        public MoneyHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.money_item, parent, false));
            moneyValue = itemView.findViewById(R.id.money_item_value);
            moneyDate = itemView.findViewById(R.id.money_item_date);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Money money) {
            this.money = money;
            moneyValue.setText(money.getValueIncome() + "");
            moneyDate.setText(money.getDate());

        }

        @Override
        public boolean onLongClick(View v) {
            AppFragmentManager.addFragment(new MoneyFloatingWindowFragment(money.getMoneyUuid(), child));
            return false;
        }


    }

    private class MoneyAdapter extends RecyclerView.Adapter<MoneyHolder> {

        private final List<Money> money;

        public MoneyAdapter(List<Money> money) {
            this.money = money;
        }

        @NonNull
        @Override
        public MoneyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new MoneyHolder(inflater, parent);
        }


        @Override
        public void onBindViewHolder(@NonNull MoneyHolder holder, int position) {
            Money moneyItem = money.get(position);
            holder.bind(moneyItem);
        }

        @Override
        public int getItemCount() {
            return money.size();
        }
    }


    private void updateUI() {
        List<Parent> parentsList = child.getParentsList();
        List<Money> moneyList = child.getMoneyList();

        if (parentAdapter == null) {

            parentAdapter = new ParentAdapter(parentsList);
            parentRecycleView.setAdapter(parentAdapter);

        } else {
            parentAdapter.notifyDataSetChanged();
        }

        if (moneyAdapter == null) {
            moneyAdapter = new MoneyAdapter(moneyList);
            moneyRecyclerView.setAdapter(moneyAdapter);
        } else {
            moneyAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Другое
     */


    @Override
    public void onPause() {
        Log.i(AppLab.GLOBAL_TAG, "onPause " + appLab.getChildPosition());
        super.onPause();
        if (dontWantToDelete) {
            appLab.changeNoteAndName(child, childNameTextView.getText().toString(), noteEditText.getText().toString());
        }
        updateUI();

        Log.i(AppLab.GLOBAL_TAG, "onPause2 " + appLab.getChildPosition());
    }

    @Override
    public void onStop() {
        Log.i(AppLab.GLOBAL_TAG, "onStop 1 " + appLab.getChildPosition());
        super.onStop();
        Log.i(AppLab.GLOBAL_TAG, "onStop 2 " + appLab.getChildPosition());
    }

    @Override
    public void onDestroyView() {
        Log.i(AppLab.GLOBAL_TAG, "onDestroyView " + appLab.getChildPosition());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(AppLab.GLOBAL_TAG, "onDestroy " + appLab.getChildPosition());
        super.onDestroy();
        if (dontWantToDelete) {
            appLab.changeNoteAndName(child, childNameTextView.getText().toString(), noteEditText.getText().toString());
        }
        goToList();
    }

    @Override
    public void onResume() {
        Log.i(AppLab.GLOBAL_TAG, "onResume  " + appLab.getChildPosition());
        updateUI();
        super.onResume();
    }


    private void goToList() {
        Log.i(AppLab.GLOBAL_TAG, "go to list  " + appLab.getChildPosition());
        AppFragmentManager.openFragment(new ChildrenPageFragment());
    }
}