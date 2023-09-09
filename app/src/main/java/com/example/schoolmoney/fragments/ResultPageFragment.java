package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolmoney.R;


public class ResultPageFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppFragmentManager.createBottomButtons();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_page, container, false);
    }
}