package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by lenovo on 10/22/2017.
 */

public class HouseholdFragment extends Fragment {
    private EditText mNoOfFamily,
                     mNoOfPeople;

    private RadioGroup m4Ps;

    private Button mSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_hold_layout, container, false);
        mNoOfFamily = (EditText) view.findViewById(R.id.et_house_hold_no_of_family);
        mNoOfPeople = (EditText) view.findViewById(R.id.et_house_hold_no_of_people);

        mSave = (Button) view.findViewById(R.id.btn_house_hold_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
