package com.example.schoolmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.schoolmoney.fragments.AppFragmentManager;
import com.example.schoolmoney.fragments.BottomButtonFragment;
import com.example.schoolmoney.fragments.ChildrenPageFragment;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFragmentManager();
        createBottomButtons();
        addDefaultPage();
    }

    private void createFragmentManager(){
        fragmentManager = getSupportFragmentManager();
    }
    private void createBottomButtons(){
        AppFragmentManager.addFragment(new BottomButtonFragment());
    }

    //Добавляю фрагмент который будет выводиться при запуске. Возможно будет добавить настройкую
    private void addDefaultPage(){
        AppFragmentManager.addFragment(new ChildrenPageFragment());
    }
}