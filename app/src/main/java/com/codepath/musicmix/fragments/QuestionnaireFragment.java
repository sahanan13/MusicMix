package com.codepath.musicmix.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.musicmix.MainActivity;
import com.codepath.musicmix.R;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class QuestionnaireFragment extends Fragment {

    public static final String TAG = "QuestionnaireFragment";
    MainActivity mainActivity;
    TextView tvTitle;
    TextView textView, textView2, textView3, textView4, textView5;;
    RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;
    String option1, option2, option3, option4, option5;
    Button btnSubmit;

    public QuestionnaireFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questionnaire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        tvTitle = view.findViewById(R.id.tvTitle);
        textView = view.findViewById(R.id.textView);
        radioGroup1 = view.findViewById(R.id.radioGroup1);
        textView2 = view.findViewById(R.id.textView2);
        radioGroup2 = view.findViewById(R.id.radioGroup2);
        textView3 = view.findViewById(R.id.textView3);
        radioGroup3 = view.findViewById(R.id.radioGroup3);
        textView4 = view.findViewById(R.id.textView4);
        radioGroup4 = view.findViewById(R.id.radioGroup4);
        textView5 = view.findViewById(R.id.textView5);
        radioGroup5 = view.findViewById(R.id.radioGroup5);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId1 = radioGroup1.getCheckedRadioButtonId();
                radioButton1 = view.findViewById(radioId1);
                int radioId2 = radioGroup2.getCheckedRadioButtonId();
                radioButton2 = view.findViewById(radioId2);
                int radioId3 = radioGroup3.getCheckedRadioButtonId();
                radioButton3 = view.findViewById(radioId3);
                int radioId4 = radioGroup4.getCheckedRadioButtonId();
                radioButton4 = view.findViewById(radioId4);
                int radioId5 = radioGroup5.getCheckedRadioButtonId();
                radioButton5 = view.findViewById(radioId5);
                option1 = (String) radioButton1.getText();
                option2 = (String) radioButton2.getText();
                option3 = (String) radioButton3.getText();
                option4 = (String) radioButton4.getText();
                option5 = (String) radioButton5.getText();
                Toast.makeText(getContext(), "Selected buttons: " + option1 + " " + option2 +
                        " " + option3 + " " + option4 + " " + option5, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Selected buttons: " + option1 + " " + option2 + " " + option3
                        + " " + option4 + " " + option5);
                radioGroup1.clearCheck();
                radioGroup2.clearCheck();
                radioGroup3.clearCheck();
                radioGroup4.clearCheck();
                radioGroup5.clearCheck();

            }
        });

    }
}