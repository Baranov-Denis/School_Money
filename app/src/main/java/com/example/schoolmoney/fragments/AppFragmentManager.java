package com.example.schoolmoney.fragments;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.schoolmoney.MainActivity;
import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.Animation;

public class AppFragmentManager {

    public static void openFragment(Fragment fragment, Enum direction) {
        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();

        transaction = setAnimation(transaction, direction);
        // добавление нового фрагмента в транзакцию
        transaction.replace(R.id.main_container_for_all_fragments, fragment);

        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        transaction.addToBackStack(null);

        // Завершение транзакции
        transaction.commit();
    }

    public static void openFragmentInButtonsView(Fragment fragment, Enum direction) {
        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
        transaction = setAnimation(transaction, direction);
        // добавление нового фрагмента в транзакцию
        transaction.replace(R.id.bottom_buttons_layout, fragment);
        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        transaction.addToBackStack(null);
        // Завершение транзакции
        transaction.commit();
    }

    public static void openFragmentInNewButtonsView(Fragment fragment, Enum direction, int numberOfButton) {
        openFragment(new BottomButtonFragment(numberOfButton), direction);

        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
        transaction = setAnimation(transaction, direction);
        // добавление нового фрагмента в транзакцию
        transaction.replace(R.id.bottom_buttons_layout, fragment);
        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        transaction.addToBackStack(null);
        // Завершение транзакции
        transaction.commit();
    }

    private static FragmentTransaction setAnimation(FragmentTransaction transaction, Enum direction) {
        if (direction.equals(Animation.FROM_RIGHT)) {
            transaction.setCustomAnimations(R.anim.appears_from_right, R.anim.hide_to_left, R.anim.appears_from_right, R.anim.hide_to_left);
        } else if (direction.equals(Animation.FROM_LEFT)) {
            transaction.setCustomAnimations(R.anim.appears_from_left, R.anim.hide_to_right, R.anim.appears_from_left, R.anim.hide_to_right);
        } else if (direction.equals(Animation.FADE_IN)) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        } else if (direction.equals(Animation.FROM_BOTTOM)) {
            transaction.setCustomAnimations(R.anim.appear_from_bottom, R.anim.hide_to_top, R.anim.appear_from_bottom, R.anim.hide_to_top);
        } else if (direction.equals(Animation.FROM_TOP)) {
            transaction.setCustomAnimations(R.anim.appear_from_top, R.anim.hide_to_bottom, R.anim.appear_from_top, R.anim.hide_to_bottom);
        } else if (direction.equals(Animation.NULL)) {
            return transaction;
        }
        return transaction;
    }

    public static void addFragment(Fragment fragment, Enum direction) {

        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();

        transaction = setAnimation(transaction, direction);
        // добавление нового фрагмента в транзакцию
        transaction.add(R.id.main_container_for_all_fragments, fragment);

        // Добавление транзакции в back stack для того чтобы пользователь мог вернуться к предыдущему фрагменту при нажатии кнопки "Назад"
        transaction.addToBackStack(null);

        // Завершение транзакции
        transaction.commit();
    }

    public static void createBottomButtons() {
        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();

        // transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        // добавление нового фрагмента в транзакцию
        transaction.add(R.id.main_container_for_all_fragments, new BottomButtonFragment());

        // Завершение транзакции
        transaction.commit();
        // AppFragmentManager.addFragment(new BottomButtonFragment());
    }


    public static void closeApp(Fragment currentFragment) {
        currentFragment.requireActivity().getOnBackPressedDispatcher().addCallback(currentFragment.getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                currentFragment.requireActivity().finish();
            }
        });
    }
}
