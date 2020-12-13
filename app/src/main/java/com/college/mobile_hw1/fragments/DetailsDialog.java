package com.college.mobile_hw1.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.college.mobile_hw1.R;
import com.college.mobile_hw1.interfaces.callbackes.DialogInputListener;

import java.util.Objects;

public class DetailsDialog extends DialogFragment {


    private View dialogView;
    private DialogInputListener listener;
    private EditText mainDialog_TXT_name;
    private ImageView mainDialog_IMG_boy, mainDialog_IMG_girl;
    private Button mainDialog_BTN_submit;
    private String name;
    private String avatar;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DialogInputListener)
            listener = (DialogInputListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement DialogInputListener");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dialogView = inflater.inflate(R.layout.user_details_fragment, container, false);

        init();

        submitListener();

        avatarsListeners();

        return dialogView;
    }

    private void submitListener() {
        mainDialog_BTN_submit.setOnClickListener((View v) -> {
            if (!mainDialog_TXT_name.getText().toString().equals("")) {
                name = mainDialog_TXT_name.getText().toString();
                if (avatar == null)
                    Toast.makeText(getActivity(), "Select Avatar", Toast.LENGTH_SHORT).show();
                else {
                    listener.detailsInput(name, avatar);

                    Objects.requireNonNull(getDialog()).dismiss();
                }
            } else
                Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();


        });
    }

    private void avatarsListeners() {

        mainDialog_IMG_boy.setOnClickListener((View v) -> {
            mainDialog_IMG_boy.setBackgroundColor(Color.BLUE);
            mainDialog_IMG_girl.setBackgroundColor(Color.DKGRAY);
            avatar = getResources().getResourceEntryName(R.drawable.player_boy);

        });

        mainDialog_IMG_girl.setOnClickListener((View v) -> {
            mainDialog_IMG_boy.setBackgroundColor(Color.DKGRAY);
            mainDialog_IMG_girl.setBackgroundColor(Color.BLUE);
            avatar = getResources().getResourceEntryName(R.drawable.player_girl);
        });

    }

    private void init() {
        mainDialog_TXT_name = dialogView.findViewById(R.id.mainDialog_TXT_name);
        mainDialog_BTN_submit = dialogView.findViewById(R.id.mainDialog_BTN_submit);
        mainDialog_IMG_boy = dialogView.findViewById(R.id.mainDialog_IMG_boy);
        mainDialog_IMG_girl = dialogView.findViewById(R.id.mainDialog_IMG_girl);
        avatar = null;
    }
}
