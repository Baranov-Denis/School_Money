package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.Animation;
import com.example.schoolmoney.appLab.AppLab;
import com.example.schoolmoney.appLab.Money;

import java.util.List;

public class SpendsListPageFragment extends Fragment {

    private View view;
    private AppLab appLab;
    private RecyclerView moneyRecyclerView;
    private MoneyAdapter moneyAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_spends_list_page, container, false);
        moneyRecyclerView = view.findViewById(R.id.spend_money_recycler_view);
        moneyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appLab = AppLab.getAppLab(getContext());
        layoutManager = (LinearLayoutManager) moneyRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPosition(appLab.getMoneyPosition());
        }
        appLab.setMoneyPosition(0);
        //AppFragmentManager.createBottomButtons();
        setFabButton();
        updateUI();
        AppFragmentManager.closeApp(this);



        return view;
    }

    private void setFabButton() {
        ImageButton addNewSpendButton = view.findViewById(R.id.add_new_spend_money_fab_button);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runAnimation(addNewSpendButton);
            }
        }, 10000);

        addNewSpendButton.setOnClickListener(o -> {
            AppFragmentManager.openFragment(new AddNewSpendMoneyFragment(), Animation.FROM_RIGHT);
        });
    }

    private void runAnimation(View yourView) {
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        yourView.startAnimation(animation);
        Handler handler = new Handler();

// Создайте переменную для отслеживания количества выполнений анимации
        int animationCount = 0;
        animationCount++;

        if (animationCount < 5) {
            // Если анимацию нужно запустить ещё раз, поставьте задержку на 10 секунд
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runAnimation(yourView);
                }
            }, 10000); // 15000 миллисекунд = 15 секунд
        }
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
            LinearLayout layout = itemView.findViewById(R.id.money_item_row);
            this.money = money;

            if (Integer.parseInt(money.getValueExpenses()) > 0) {
                layout.setBackgroundResource(R.drawable.red_button);
                moneyValue.setText(money.getValueExpenses().toString());
            } else {
                layout.setBackgroundResource(R.drawable.recycler_item_blue);
                moneyValue.setText(money.getValueIncome().toString());
            }
            moneyTitle.setText(money.getTitle());
            moneyDate.setText(money.getDate());
        }

        @Override
        public void onClick(View v) {
            appLab.setMoneyPosition(layoutManager.findFirstVisibleItemPosition());
            AppFragmentManager.openFragment(MoneyCardFragment.newInstance(money.getMoneyUuid()), Animation.FROM_RIGHT);
        }

    }

    private class MoneyAdapter extends RecyclerView.Adapter<MoneyHolder> {

        private final List<Money> money;

       /* public MoneyAdapter(List<Money> money) {
            List<Money> onlySpendMoney = new ArrayList<>();
            for (Money moneyIt : money) {
                if (Integer.parseInt(moneyIt.getValueExpenses()) > 0) {
                    onlySpendMoney.add(moneyIt);
                }
            }
            this.money = onlySpendMoney;
        }*/
        public MoneyAdapter(List<Money> money) {
          /*  List<Money> onlySpendMoney = new ArrayList<>();
            for (Money moneyIt : money) {
                if (Integer.parseInt(moneyIt.getValueExpenses()) > 0) {
                    onlySpendMoney.add(moneyIt);
                }
            }
            this.money = onlySpendMoney;*/
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
          //  if (Integer.parseInt(moneyItem.getValueExpenses()) > 0) {
                holder.bind(moneyItem);
          //  }
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