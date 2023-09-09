package com.example.schoolmoney.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.schoolmoney.MainActivity;
import com.example.schoolmoney.R;

public class AppFragmentManager {

    public static void openFragment(Fragment fragment) {

        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();

        //transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        // добавление нового фрагмента в транзакцию
        transaction.replace(R.id.main_container_for_all_fragments, fragment);

        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        transaction.addToBackStack(null);

        // Завершение транзакции
        transaction.commit();
    }



    public static void addFragment(Fragment fragment) {

        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();

       // transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        // добавление нового фрагмента в транзакцию
        transaction.add(R.id.main_container_for_all_fragments, fragment);

        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        transaction.addToBackStack(null);

        // Завершение транзакции
        transaction.commit();
    }

    public static void createBottomButtons(){
        AppFragmentManager.addFragment(new BottomButtonFragment());
    }

}
