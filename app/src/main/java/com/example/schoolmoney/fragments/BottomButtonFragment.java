package com.example.schoolmoney.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.schoolmoney.R;
import com.example.schoolmoney.appLab.Animation;
import com.example.schoolmoney.appLab.FragmentTag;


public class BottomButtonFragment extends Fragment {

    private View view;
    private int numberOfButton;
    private LinearLayout linearLayout;


    private Button childrenButton;
    private Button spendButton;
    private Button resultButton;

    public int getNumberOfButton() {
        return numberOfButton;
    }

    public void setNumberOfButton(int numberOfButton) {
        this.numberOfButton = numberOfButton;
    }

    public BottomButtonFragment(int numberOfButton) {
        setNumberOfButton(numberOfButton);
    }

    public BottomButtonFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottom_buttons, container, false);
        setButtons();
        linearLayout = view.findViewById(R.id.bottom_buttons_layout);
        if(getNumberOfButton() == -1){
            AppFragmentManager.openFragmentInButtonsView(new ChildrenListPageFragment(), Animation.NULL);
            setButtonDark(childrenButton);
        }else if (getNumberOfButton()==0) {
            //AppFragmentManager.openFragmentInButtonsView(new ChildrenListPageFragment(), Animation.NULL);
            setButtonDark(childrenButton);
        }else if(getNumberOfButton()==1){
            setButtonDark(spendButton);
        }else {
            setButtonDark(resultButton);
        }

        return view;
    }

    private void setButtons() {
        createChildrenButton();
        createSpendButton();
        createResultButton();
    }

    private void createChildrenButton() {
        childrenButton = view.findViewById(R.id.go_to_receipts_page_button);
        childrenButton.setOnClickListener(o -> {
            if (!isFragmentTag(FragmentTag.CHILDREN_FRAGMENT)) {
                AppFragmentManager.openFragmentInButtonsView(new ChildrenListPageFragment(), Animation.FROM_LEFT);
            }
                setButtonDark(childrenButton);
        });
    }

    private void createSpendButton() {
        spendButton = view.findViewById(R.id.go_to_expenses_page_button);
        spendButton.setOnClickListener(o -> {

            if (!isFragmentTag(FragmentTag.SPEND_FRAGMENT)) {
                if (linearLayout.getChildAt(0).getTag().toString().equals(FragmentTag.CHILDREN_FRAGMENT.toString())) {
                    AppFragmentManager.openFragmentInButtonsView(new SpendsListPageFragment(), Animation.FROM_RIGHT);
                } else if (isFragmentTag(FragmentTag.RESULT_FRAGMENT) || isFragmentTag(FragmentTag.SETTINGS_FRAGMENT)) {
                    AppFragmentManager.openFragmentInButtonsView(new SpendsListPageFragment(), Animation.FROM_LEFT);
                }
            }
            setButtonDark(spendButton);
        });
    }

    private void createResultButton() {
        resultButton = view.findViewById(R.id.go_to_result_page_button);
        resultButton.setOnClickListener(o -> {
            if (isFragmentTag(FragmentTag.SETTINGS_FRAGMENT)) {
                AppFragmentManager.openFragmentInButtonsView(new ResultPageFragment(), Animation.FROM_LEFT);
            } else if (!isFragmentTag(FragmentTag.RESULT_FRAGMENT)) {
                AppFragmentManager.openFragmentInButtonsView(new ResultPageFragment(), Animation.FROM_RIGHT);
            }
            setButtonDark(resultButton);
        });
    }

    private boolean isFragmentTag(Enum tag) {
        return linearLayout.getChildAt(0).getTag().toString().equals(tag.toString());
    }

    private void setButtonDark(Button button) {
        setButtonLight(childrenButton);
        setButtonLight(spendButton);
        setButtonLight(resultButton);
        button.setBackgroundResource(R.drawable.recycler_item_dark_blue);
    }

    private void setButtonLight(Button button) {
        button.setBackgroundResource(R.drawable.recycler_item_blue);
    }


}