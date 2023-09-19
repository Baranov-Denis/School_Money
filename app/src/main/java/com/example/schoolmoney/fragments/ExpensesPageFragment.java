package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Money;
import com.example.schoolmoney.appLab.Parent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExpensesPageFragment extends Fragment {

    private View view;
    private AppLab appLab;
    private RecyclerView moneyRecyclerView;
    private MoneyAdapter moneyAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_expenses_page, container, false);
        moneyRecyclerView = view.findViewById(R.id.spend_money_recycler_view);
        moneyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appLab = AppLab.getAppLab(getContext());
        layoutManager = (LinearLayoutManager) moneyRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPosition(appLab.getMoneyPosition());
        }
        appLab.setMoneyPosition(0);
        AppFragmentManager.createBottomButtons();
        setFabButton();
        updateUI();
        AppFragmentManager.closeApp(this);
        return view;
    }

    private void setFabButton() {
        FloatingActionButton addNewChildButton = view.findViewById(R.id.add_new_spend_money_fab_button);
        addNewChildButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new AddNewSpendMoneyFragment());
        });
    }


    /**
     * Recycler для MONEY
     */

    private class MoneyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // private LinearLayout layout;
        private TextView moneyValue;
        private TextView moneyDate;
        private TextView moneyTitle;
        private Money money;


        public MoneyHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.spend_money_item, parent, false));
            moneyValue = itemView.findViewById(R.id.spend_money_item_value);
            moneyDate = itemView.findViewById(R.id.spend_money_item_date);
            moneyTitle = itemView.findViewById(R.id.spend_money_item_title);
            itemView.setOnClickListener(this);
        }

        public void bind(Money money) {
            this.money = money;
            moneyTitle.setText(money.getTitle());
            moneyValue.setText(money.getValueExpenses() + "");
            moneyDate.setText(money.getDate());
        }

        @Override
        public void onClick(View v) {
            appLab.setMoneyPosition(layoutManager.findFirstVisibleItemPosition());
            AppFragmentManager.openFragment(MoneyCardFragment.newInstance(money.getMoneyUuid()));
        }

    }

    private class MoneyAdapter extends RecyclerView.Adapter<MoneyHolder> {

        private final List<Money> money;

        public MoneyAdapter(List<Money> money) {
            List<Money> onlySpendMoney = new ArrayList<>();
            for (Money moneyIt : money) {
                if (Integer.parseInt(moneyIt.getValueExpenses()) > 0) {
                    onlySpendMoney.add(moneyIt);
                }
            }
            this.money = onlySpendMoney;
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

            if (Integer.parseInt(moneyItem.getValueExpenses()) > 0) {
                holder.bind(moneyItem);
            }
        }

        @Override
        public int getItemCount() {
            return money.size();
        }
    }

    private void updateUI() {

        List<Money> moneyList = appLab.getMoneyList();

        if (moneyAdapter == null) {

            moneyAdapter = new MoneyAdapter(moneyList);
            moneyRecyclerView.setAdapter(moneyAdapter);

        } else {
            moneyAdapter.notifyDataSetChanged();
        }


    }
}